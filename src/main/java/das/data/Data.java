package das.data;

import crypto.blockDigSig;
import proto.Types;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static blockchain.Utils.validateBlockHash;
import static java.lang.String.format;

public class Data {
    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Data.class);
    public enum RBTypes {
        FORK,
        SYNC,
        NOT_MAPPED

    };

    public static ConcurrentHashMap<Types.Meta, BbcDecData>[] bbcDec;
    public static ConcurrentHashMap<Types.Meta, Types.BlockHeader>[] pending;
//    public static ConcurrentHashMap<Types.Meta, Types.BlockHeader>[] received;
    public static ConcurrentHashMap<Types.Meta, VoteData>[] votes;
    public static Queue<Types.ForkProof>[] forksRBData;
    public static HashMap<Integer, List<Types.subChainVersion>>[] syncRBData;
    public static ConcurrentHashMap<Types.Meta, List<Integer>>[] preConsVote;
    public static ConcurrentHashMap<Types.Meta, VoteData>[] fvData;
    public static ArrayList<Types.Meta>[] preConsDone;
    static public Object[] preConsNotifyer;

    public Data(int channels) {
        bbcDec = new ConcurrentHashMap[channels];
        pending = new ConcurrentHashMap[channels];
//        received = new ConcurrentHashMap[channels];
        votes = new ConcurrentHashMap[channels];
        forksRBData = new Queue[channels];
        syncRBData = new HashMap[channels];
        fvData = new ConcurrentHashMap[channels];
        preConsVote = new ConcurrentHashMap[channels];
        preConsDone = new ArrayList[channels];
        this.preConsNotifyer = new Object[channels];
        for (int i = 0 ; i < channels ; i++) {
            bbcDec[i] = new ConcurrentHashMap<>();
            pending[i] = new ConcurrentHashMap<>();
//            received[i] = new ConcurrentHashMap<>();
            votes[i] = new ConcurrentHashMap<>();
            forksRBData[i] = new LinkedList<>();
            syncRBData[i] = new HashMap<>();
            fvData[i] = new ConcurrentHashMap<>();
            preConsVote[i] = new ConcurrentHashMap<>();
            preConsDone[i] = new ArrayList<>();
            preConsNotifyer[i] = new Object();
        }
    }

    static public void evacuateOldData(int channel, Types.Meta key) {
        bbcDec[channel].remove(key);
        pending[channel].remove(key);
        votes[channel].remove(key);
        preConsVote[channel].remove(key);
        preConsDone[channel].remove(key);
        fvData[channel].remove(key);
    }

    static public void evacuateAllOldData(int channel, Types.Meta mKey) {
        int cidSeries = mKey.getCidSeries();
        int cid = mKey.getCid();
        for (Types.Meta key : bbcDec[channel].keySet()) {
            if (key.getCidSeries() < cidSeries)  {
                bbcDec[channel].remove(key);
                continue;
            }
            if (key.getCidSeries() == cidSeries && key.getCid() < cid) bbcDec[channel].remove(key);
        }
        for (Types.Meta key : pending[channel].keySet()) {
            if (key.getCidSeries() < cidSeries)  {
                pending[channel].remove(key);
                continue;
            }
            if (key.getCidSeries() == cidSeries && key.getCid() < cid) pending[channel].remove(key);
        }
        for (Types.Meta key : votes[channel].keySet()) {
            if (key.getCidSeries() < cidSeries)  {
                votes[channel].remove(key);
                continue;
            }
            if (key.getCidSeries() == cidSeries && key.getCid() < cid) votes[channel].remove(key);
        }

        for (Types.Meta key : preConsVote[channel].keySet()) {
            if (key.getCidSeries() < cidSeries)  {
                preConsVote[channel].remove(key);
                continue;
            }
            if (key.getCidSeries() == cidSeries && key.getCid() < cid) preConsVote[channel].remove(key);
        }

        for (Types.Meta key : fvData[channel].keySet()) {
            if (key.getCidSeries() < cidSeries)  {
                fvData[channel].remove(key);
                continue;
            }
            if (key.getCidSeries() == cidSeries && key.getCid() < cid) fvData[channel].remove(key);
        }
        synchronized (preConsNotifyer[channel]) {
            for (Types.Meta key : preConsDone[channel]) {
                if (key.getCidSeries() < cidSeries)  {
                    preConsDone[channel].remove(key);
                    continue;
                }
                if (key.getCidSeries() == cidSeries && key.getCid() < cid) preConsDone[channel].remove(key);
            }
        }


    }

    static public RBTypes getRBType(int type) {
        switch (type) {
            case 0: return RBTypes.FORK;
            case 1: return RBTypes.SYNC;
        }
        return RBTypes.NOT_MAPPED;
    }


    static public boolean validateForkProof(Types.ForkProof p) {
        logger.debug(format("starts validating fp"));
        Types.Block curr = p.getCurr();
        Types.Block prev = p.getPrev();
        if (!blockDigSig.verifyBlock(curr)) {
            logger.debug(format("invalid fork proof #3"));
            return false;
        }
        if (!blockDigSig.verifyBlock(prev)) {
            logger.debug(format("invalid fork proof #4"));
            return false;
        }

        logger.debug(format("panic for fork is valid [fp=%d]", p.getCurr().getHeader().getHeight()));
        return true;
    }

    static public boolean validateSubChainVersion(Types.subChainVersion v, int f) {
        if (v.getVCount() == 0) return true;
        ArrayList<Integer> leaders = new ArrayList<>();
        leaders.add(v.getV(0).getHeader().getM().getSender());
        for (int i = 1 ; i < v.getVList().size() ; i++ ) {
            Types.Block pb = v.getV(i);
            if (!blockDigSig.verifyBlock(pb)) {
                logger.debug(format("invalid sub chain version, block [height=%d] digital signature is invalid " +
                        "[sender=%d]", pb.getHeader().getHeight(),  v.getSender()));
                return false;
            }
            if (!validateBlockHash(v.getV(i - 1), pb)) {
                logger.debug(format("invalid sub chain version, block hash is invalid [height=%d]",
                        pb.getHeader().getHeight()));
                return false;
            }
            if (leaders.size() > f) {
                if (leaders.contains(pb.getHeader().getM().getSender())) {
                    logger.debug(format("invalid invalid sub chain version, block creator is invalid [height=%d]",
                             pb.getHeader().getHeight()));
                    return false;
                }
            }
        }
        return true;
    }
}