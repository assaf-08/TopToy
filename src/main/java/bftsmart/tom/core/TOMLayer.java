/**
 * Copyright (c) 2007-2013 Alysson Bessani, Eduardo Alchieri, Paulo Sousa, and
 * the authors indicated in the @author tags
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package bftsmart.tom.core;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignedObject;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

import bftsmart.clientsmanagement.ClientsManager;
import bftsmart.clientsmanagement.RequestList;
import bftsmart.communication.ServerCommunicationSystem;
import bftsmart.communication.client.RequestReceiver;
import bftsmart.consensus.Decision;
import bftsmart.consensus.Consensus;
import bftsmart.consensus.Epoch;
import bftsmart.consensus.roles.Acceptor;
import bftsmart.reconfiguration.ServerViewController;
import bftsmart.statemanagement.StateManager;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.core.messages.TOMMessage;
import bftsmart.tom.core.messages.TOMMessageType;
import bftsmart.tom.core.messages.ForwardedMessage;
import bftsmart.tom.leaderchange.RequestsTimer;
import bftsmart.tom.server.Recoverable;
import bftsmart.tom.server.RequestVerifier;
import bftsmart.tom.util.BatchBuilder;
import bftsmart.tom.util.BatchReader;
import org.apache.log4j.Logger;
//import bftsmart.tom.util.Logger;

/**
 * This class implements the state machine replication protocol described in
 * Joao Sousa's 'From Byzantine Consensus to BFT state machine replication: a latency-optimal transformation' (May 2012)
 * 
 * The synchronization phase described in the paper is implemented in the Synchronizer class
 */
public final class TOMLayer extends Thread implements RequestReceiver {
    private final static Logger logger = Logger.getLogger(TOMLayer.class);
    private boolean doWork = true;
    //other components used by the TOMLayer (they are never changed)
    public ExecutionManager execManager; // Execution manager
    public Acceptor acceptor; // Acceptor role of the PaW algorithm
    private ServerCommunicationSystem communication; // Communication system between replicas
    //private OutOfContextMessageThread ot; // Thread which manages messages that do not belong to the current das
    private DeliveryThread dt; // Thread which delivers total ordered messages to the appication
    public StateManager stateManager = null; // object which deals with the state transfer protocol

    /**
     * Manage timers for pending requests
     */
    public RequestsTimer requestsTimer;
    /**
     * Store requests received but still not ordered
     */
    public ClientsManager clientsManager;
    /**
     * The id of the das being executed (or -1 if there is none)
     */
    private int inExecution = -1;
    private int lastExecuted = -1;

    public MessageDigest md;
    private Signature engine;

    private ReentrantLock hashLock = new ReentrantLock();

    //the next two are used to generate non-deterministic data in a deterministic way (by the leader)
    public BatchBuilder bb = new BatchBuilder(System.nanoTime());

    /* The locks and conditions used to wait upon creating a propose */
    private ReentrantLock leaderLock = new ReentrantLock();
    private Condition iAmLeader = leaderLock.newCondition();
    private ReentrantLock messagesLock = new ReentrantLock();
    private Condition haveMessages = messagesLock.newCondition();
    private ReentrantLock proposeLock = new ReentrantLock();
    private Condition canPropose = proposeLock.newCondition();

    private PrivateKey prk;
    public ServerViewController controller;

    private RequestVerifier verifier;
            
    private Synchronizer syncher;

    /**
     * Creates a new instance of TOMulticastLayer
     *
     * @param manager Execution manager
     * @param receiver Object that receives requests from clients
     * @param recoverer
     * @param a Acceptor role of the PaW algorithm
     * @param cs Communication system between replicas
     * @param controller Reconfiguration Manager
     * @param verifier
     */
    public TOMLayer(ExecutionManager manager,
            ServiceReplica receiver,
            Recoverable recoverer,
            Acceptor a,
            ServerCommunicationSystem cs,
            ServerViewController controller,
            RequestVerifier verifier) {

        super("TOM Layer");

        this.execManager = manager;
        this.acceptor = a;
        this.communication = cs;
        this.controller = controller;

        //do not create a timer manager if the timeout is 0
        if (this.controller.getStaticConf().getRequestTimeout() == 0) {
            this.requestsTimer = null;
        } else {
            this.requestsTimer = new RequestsTimer(this, communication, this.controller); // Create requests timers manager (a thread)
        }

        try {
            this.md = MessageDigest.getInstance("MD5"); // TODO: shouldn't it be SHA?
        } catch (Exception e) {
//            e.printStackTrace(System.out);
            logger.error(e);
        }

        try {
            this.engine = Signature.getInstance("SHA1withRSA");
        } catch (Exception e) {
//            e.printStackTrace(System.err);
            logger.error(e);
        }

        this.prk = this.controller.getStaticConf().getRSAPrivateKey();
        this.dt = new DeliveryThread(this, receiver, recoverer, this.controller); // Create delivery thread
        this.dt.start();
        this.stateManager = recoverer.getStateManager();
        stateManager.init(this, dt);
        
        this.verifier = (verifier != null) ? verifier : ((request) -> true); // By default, never validate requests 
		
        // I have a verifier, now create clients manager
        this.clientsManager = new ClientsManager(this.controller, requestsTimer, this.verifier);

        this.syncher = new Synchronizer(this); // create synchronizer
    }

    /**
     * Computes an hash for a TOM message
     *
     * @param data Data from which to generate the hash
     * @return Hash for the specified TOM message
     */
    public final byte[] computeHash(byte[] data) {
        byte[] ret = null;
        hashLock.lock();
        ret = md.digest(data);
        hashLock.unlock();

        return ret;
    }

    public SignedObject sign(Serializable obj) {
        try {
            return new SignedObject(obj, prk, engine);
        } catch (Exception e) {
//            e.printStackTrace(System.err);
            logger.error(e);
            return null;
        }
    }

    /**
     * Verifies the signature of a signed object
     *
     * @param so Signed object to be verified
     * @param sender Replica id that supposedly signed this object
     * @return True if the signature is valid, false otherwise
     */
    public boolean verifySignature(SignedObject so, int sender) {
        try {
            return so.verify(controller.getStaticConf().getRSAPublicKey(sender), engine);
        } catch (Exception e) {
            logger.error("", e);
        }
        return false;
    }

    /**
     * Retrieve Communication system between replicas
     *
     * @return Communication system between replicas
     */
    public ServerCommunicationSystem getCommunication() {
        return this.communication;
    }

    public void imAmTheLeader() {
        leaderLock.lock();
        iAmLeader.signal();
        leaderLock.unlock();
    }

    /**
     * Sets which das was the last to be executed
     *
     * @param last ID of the das which was last to be executed
     */
    public void setLastExec(int last) {
        this.lastExecuted = last;
    }

    /**
     * Gets the ID of the das which was established as the last executed
     *
     * @return ID of the das which was established as the last executed
     */
    public int getLastExec() {
        return this.lastExecuted;
    }

    /**
     * Sets which das is being executed at the moment
     *
     * @param inEx ID of the das being executed at the moment
     */
    public void setInExec(int inEx) {
        proposeLock.lock();
        logger.info("(TOMLayer.setInExec) modifying inExec from " + this.inExecution + " to " + inEx);
        this.inExecution = inEx;
        if (inEx == -1 && !isRetrievingState()) {
            canPropose.signalAll();
        }
        proposeLock.unlock();
    }

    /**
     * This method blocks until the PaW algorithm is finished
     */
    public void waitForPaxosToFinish() {
        proposeLock.lock();
        canPropose.awaitUninterruptibly();
        proposeLock.unlock();
    }

    /**
     * Gets the ID of the das currently beign executed
     *
     * @return ID of the das currently beign executed (if no das ir
     * executing, -1 is returned)
     */
    public int getInExec() {
        return this.inExecution;
    }

    /**
     * This method is invoked by the communication system to deliver a request.
     * It assumes that the communication system delivers the message in FIFO
     * order.
     *
     * @param msg The request being received
     */
    @Override
    public void requestReceived(TOMMessage msg) {
        
        if (!doWork) return;
        
        // check if this request is valid and add it to the client' pending requests list
        boolean readOnly = (msg.getReqType() == TOMMessageType.UNORDERED_REQUEST
                || msg.getReqType() == TOMMessageType.UNORDERED_HASHED_REQUEST);
        if (readOnly) {
            logger.info("(TOMLayer.requestReceived) Received read-only TOMMessage from client " + msg.getSender() + " with sequence number " + msg.getSequence() + " for session " + msg.getSession());

            dt.deliverUnordered(msg, syncher.getLCManager().getLastReg());
        } else {
            logger.info("(TOMLayer.requestReceived) Received TOMMessage from client " + msg.getSender() + " with sequence number " + msg.getSequence() + " for session " + msg.getSession());

            if (clientsManager.requestReceived(msg, true, communication)) {
                haveMessages();
            } else {
                logger.info("(TOMLayer.requestReceived) the received TOMMessage " + msg + " was discarded.");
            }
        }
    }

    /**
     * Creates a value to be proposed to the acceptors. Invoked if this replica
     * is the leader
     *
     * @param dec Object that will eventually hold the decided value
     * @return A value to be proposed to the acceptors
     */
    public byte[] createPropose(Decision dec) {
        // Retrieve a set of pending requests from the clients manager
        RequestList pendingRequests = clientsManager.getPendingRequests();

        int numberOfMessages = pendingRequests.size(); // number of messages retrieved
        int numberOfNonces = this.controller.getStaticConf().getNumberOfNonces(); // ammount of nonces to be generated

        //for benchmarking
        if (dec.getConsensusId() > -1) { // if this is from the leader change, it doesnt matter
            dec.firstMessageProposed = pendingRequests.getFirst();
            dec.firstMessageProposed.consensusStartTime = System.nanoTime();
        }
        dec.batchSize = numberOfMessages;

        logger.info("(TOMLayer.run) creating a PROPOSE with " + numberOfMessages + " msgs");

        return bb.makeBatch(pendingRequests, numberOfNonces, System.currentTimeMillis(), controller);
    }

    /**
     * This is the main code for this thread. It basically waits until this
     * replica becomes the leader, and when so, proposes a value to the other
     * acceptors
     */
    @Override
    public void run() {
        logger.info("Running."); // TODO: can't this be outside of the loop?
        while (doWork) {

            // blocks until this replica learns to be the leader for the current epoch of the current das
            leaderLock.lock();
            logger.info("Next leader for CID=" + (getLastExec() + 1) + ": " + execManager.getCurrentLeader());

            //******* EDUARDO BEGIN **************//
            if (execManager.getCurrentLeader() != this.controller.getStaticConf().getProcessId()) {
                iAmLeader.awaitUninterruptibly();
                //waitForPaxosToFinish();
            }
            //******* EDUARDO END **************//
            leaderLock.unlock();
            
            if (!doWork) break;

            // blocks until the current das finishes
            proposeLock.lock();

            if (getInExec() != -1) { //there is some das running
                logger.info("(TOMLayer.run) Waiting for das " + getInExec() + " termination.");
                canPropose.awaitUninterruptibly();
            }
            proposeLock.unlock();
            
            if (!doWork) break;

            logger.info("(TOMLayer.run) I'm the leader.");

            // blocks until there are requests to be processed/ordered
            messagesLock.lock();
            if (!clientsManager.havePendingRequests()) {
                haveMessages.awaitUninterruptibly();
            }
            messagesLock.unlock();
            
            if (!doWork) break;
            
            logger.info("(TOMLayer.run) There are messages to be ordered.");

            logger.info("(TOMLayer.run) I can try to propose.");

            if ((execManager.getCurrentLeader() == this.controller.getStaticConf().getProcessId()) && //I'm the leader
                    (clientsManager.havePendingRequests()) && //there are messages to be ordered
                    (getInExec() == -1)) { //there is no das in execution

                // Sets the current das
                int execId = getLastExec() + 1;
                setInExec(execId);

                Decision dec = execManager.getConsensus(execId).getDecision();

                // Bypass protocol if service is not replicated
                if (controller.getCurrentViewN() == 1) {

                    logger.info("(TOMLayer.run) Only one replica, bypassing das.");

                    byte[] value = createPropose(dec);

                    Consensus consensus = execManager.getConsensus(dec.getConsensusId());
                    Epoch epoch = consensus.getEpoch(0, controller);
                    epoch.propValue = value;
                    epoch.propValueHash = computeHash(value);
                    epoch.getConsensus().addWritten(value);
                    epoch.deserializedPropValue = checkProposedValue(value, true);
                    epoch.getConsensus().getDecision().firstMessageProposed = epoch.deserializedPropValue[0];
                    dec.setDecisionEpoch(epoch);

                    //logger.info("ESTOU AQUI!");
                    dt.delivery(dec);
                    continue;

                }
                execManager.getProposer().startConsensus(execId,
                        createPropose(dec));
            }
        }
        logger.info("TOMLayer stopped.");
    }

    /**
     * Called by the current das instance, to notify the TOM layer that
     * a value was decided
     *
     * @param dec The decision of the das
     */
    public void decided(Decision dec) {
        
        dec.setRegency(syncher.getLCManager().getLastReg());
        dec.setLeader(execManager.getCurrentLeader());
        
        this.dt.delivery(dec); // Sends the decision to the delivery thread
    }

    /**
     * Verify if the value being proposed for a epoch is valid. It verifies the
     * client signature of all batch requests.
     *
     * TODO: verify timestamps and nonces
     *
     * @param proposedValue the value being proposed
     * @return Valid messages contained in the proposed value
     */
    public TOMMessage[] checkProposedValue(byte[] proposedValue, boolean addToClientManager) {
        
        try{
            
            logger.info("(TOMLayer.isProposedValueValid) starting");

            BatchReader batchReader = new BatchReader(proposedValue,
                this.controller.getStaticConf().getUseSignatures() == 1);

            TOMMessage[] requests = null;

            //deserialize the message
            //TODO: verify Timestamps and Nonces
            requests = batchReader.deserialiseRequests(this.controller);
            
            //enforce the "external validity" property, i.e, verify if the
            //requests are valid in accordance to the application semantics
            //and not an erroneous requests sent by a Byzantine leader.
            for (TOMMessage r : requests) {
                if (controller.getStaticConf().isBFT() &&!verifier.isValidRequest(r)) return null;
            }
            

            if (addToClientManager) {
                for (int i = 0; i < requests.length; i++) {
					//notifies the client manager that this request was received and get
                    //the result of its validation
                    if (!clientsManager.requestReceived(requests[i], false)) {
                        clientsManager.getClientsLock().unlock();
                        logger.info("(TOMLayer.isProposedValueValid) finished, return=false");
                        logger.info("failure in deserialize batch");
                        return null;
                    }
                }
            }

            logger.info("(TOMLayer.isProposedValueValid) finished, return=true");

            return requests;
        
        } catch (Exception e) {
            logger.info("(TOMLayer.isProposedValueValid) finished, return=false");
            logger.error("", e);
            if (Thread.holdsLock(clientsManager.getClientsLock())) clientsManager.getClientsLock().unlock();

            return null;
        }
    }

    public void forwardRequestToLeader(TOMMessage request) {
        int leaderId = execManager.getCurrentLeader();
        if (this.controller.isCurrentViewMember(leaderId)) {
            logger.info("(TOMLayer.forwardRequestToLeader) forwarding " + request + " to " + leaderId);
            communication.send(new int[]{leaderId},
                    new ForwardedMessage(this.controller.getStaticConf().getProcessId(), request));
        }
    }

    public boolean isRetrievingState() {
        //lockTimer.lock();
        boolean result = stateManager != null && stateManager.isRetrievingState();
        //lockTimer.unlock();

        return result;
    }

    public boolean isChangingLeader() {
        
        return !requestsTimer.isEnabled();

    }
    
    public void setNoExec() {
        logger.info("(TOMLayer.setNoExec) modifying inExec from " + this.inExecution + " to " + -1);

        proposeLock.lock();
        this.inExecution = -1;
        //ot.addUpdate();
        canPropose.signalAll();
        proposeLock.unlock();
    }

    public void processOutOfContext() {
        for (int nextConsensus = getLastExec() + 1;
                execManager.receivedOutOfContextPropose(nextConsensus);
                nextConsensus = getLastExec() + 1) {
            execManager.processOutOfContextPropose(execManager.getConsensus(nextConsensus));
        }
    }

    public StateManager getStateManager() {
        return stateManager;
    }

    public Synchronizer getSynchronizer() {
        return syncher;
    }
   
    private void haveMessages() {
        messagesLock.lock();
        haveMessages.signal();
        messagesLock.unlock();
    }
    
    public DeliveryThread getDeliveryThread() {
        return dt;
    }
    
    public void shutdown() {
        this.doWork = false;
        imAmTheLeader();
        haveMessages();
        setNoExec();

        if (this.requestsTimer != null) this.requestsTimer.shutdown();
        if (this.clientsManager != null) {
            this.clientsManager.clear();
            this.clientsManager.getPendingRequests().clear();
        }
        if (this.dt != null) this.dt.shutdown();
        if (this.communication != null) this.communication.shutdown();
 
    }
}
