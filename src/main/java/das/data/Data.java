package das.data;

import blockchain.data.BCS;
import crypto.BlockDigSig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static blockchain.Utils.validateBlockHash;
import static java.lang.String.format;
import proto.types.meta.*;
import proto.types.block.*;
import proto.types.forkproof.*;
import proto.types.version.*;

public class Data {
    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Data.class);
    public enum RBTypes {
        FORK,
        SYNC,
        BBC,
        START,
        NOT_MAPPED

    };

    public static ConcurrentHashMap<Meta, BbcDecData>[] bbcFastDec;
    public static ConcurrentHashMap<Meta, BbcDecData>[] bbcRegDec;
    public static ConcurrentHashMap<Meta, BlockHeader>[] pending;
    public static ConcurrentHashMap<Meta, VoteData>[] bbcVotes;
    public static Queue<ForkProof>[] forksRBData;
    public static HashMap<Integer, List<SubChainVersion>>[] syncRBData;
    public static ConcurrentHashMap<Meta, List<Integer>>[] preConsVote;
    public static ConcurrentHashMap<Meta, VoteData>[] fvData;
    public static ArrayList<Meta>[] preConsDone;

    public Data(int channels) {
        bbcFastDec = new ConcurrentHashMap[channels];
        bbcRegDec = new ConcurrentHashMap[channels];
        pending = new ConcurrentHashMap[channels];
        bbcVotes = new ConcurrentHashMap[channels];
        forksRBData = new Queue[channels];
        syncRBData = new HashMap[channels];
        fvData = new ConcurrentHashMap[channels];
        preConsVote = new ConcurrentHashMap[channels];
        preConsDone = new ArrayList[channels];
        for (int i = 0 ; i < channels ; i++) {
            bbcFastDec[i] = new ConcurrentHashMap<>();
            bbcRegDec[i] = new ConcurrentHashMap<>();
            pending[i] = new ConcurrentHashMap<>();
            bbcVotes[i] = new ConcurrentHashMap<>();
            forksRBData[i] = new LinkedList<>();
            syncRBData[i] = new HashMap<>();
            fvData[i] = new ConcurrentHashMap<>();
            preConsVote[i] = new ConcurrentHashMap<>();
            preConsDone[i] = new ArrayList<>();
        }
    }

    static public void evacuateOldData(int channel, Meta key) {
        logger.debug(format("evacuating all data for [worker=%d ; cidSeries=%d ; cid=%d]",channel, key.getCidSeries(),
                key.getCid()));
        bbcFastDec[channel].remove(key);
        bbcRegDec[channel].remove(key);
        bbcVotes[channel].remove(key);
        pending[channel].remove(key);
        preConsVote[channel].remove(key);
        fvData[channel].remove(key);
        synchronized (preConsDone[channel]) {
            preConsDone[channel].remove(key);
        }

    }
    static public void addToPendings(BlockHeader request, Meta key) {
        int worker = key.getChannel();
        int height = request.getHeight();
        if (!BlockDigSig.verifyHeader(request.getBid().getPid(), request)) {
            logger.debug(format("invalid pgb message [w=%d ; cidSeries=%d ; cid=%d ; sender=%d, height=%d]",
                    request.getM().getChannel(), request.getM().getCidSeries(), request.getM().getCid()
                    , request.getBid().getPid(), request.getHeight()));

            return;
        }
        if (BCS.contains(worker, height)) return;
        synchronized (pending[worker]) {
            pending[worker].putIfAbsent(key, request);
            pending[worker].notify();
        }
    }


    static public void evacuateAllOldData(int channel, Meta mKey) {
        int cidSeries = mKey.getCidSeries();
        int cid = mKey.getCid();
        logger.debug(format("evacuating all data for [worker=%d ; cidSeries=%d ; cid=%d]",channel, cidSeries, cid));
        for (Meta key : bbcFastDec[channel].keySet()) {
            if (key.getCidSeries() < cidSeries)  {
                bbcFastDec[channel].remove(key);
                continue;
            }
            if (key.getCidSeries() == cidSeries && key.getCid() < cid) bbcFastDec[channel].remove(key);
        }

        for (Meta key : bbcRegDec[channel].keySet()) {
            if (key.getCidSeries() < cidSeries)  {
                bbcRegDec[channel].remove(key);
                continue;
            }
            if (key.getCidSeries() == cidSeries && key.getCid() < cid) bbcRegDec[channel].remove(key);
        }

        for (Meta key : bbcVotes[channel].keySet()) {
            if (key.getCidSeries() < cidSeries)  {
                bbcVotes[channel].remove(key);
                continue;
            }
            if (key.getCidSeries() == cidSeries && key.getCid() < cid) bbcVotes[channel].remove(key);
        }

        for (Meta key : pending[channel].keySet()) {
            if (key.getCidSeries() < cidSeries)  {
                pending[channel].remove(key);
                continue;
            }
            if (key.getCidSeries() == cidSeries && key.getCid() < cid) pending[channel].remove(key);
        }


        for (Meta key : preConsVote[channel].keySet()) {
            if (key.getCidSeries() < cidSeries)  {
                preConsVote[channel].remove(key);
                continue;
            }
            if (key.getCidSeries() == cidSeries && key.getCid() < cid) preConsVote[channel].remove(key);
        }

        for (Meta key : fvData[channel].keySet()) {
            if (key.getCidSeries() < cidSeries)  {
                fvData[channel].remove(key);
                continue;
            }
            if (key.getCidSeries() == cidSeries && key.getCid() < cid) fvData[channel].remove(key);
        }
        synchronized (preConsDone[channel]) {
            for (Iterator<Meta> itKey = preConsDone[channel].iterator() ; itKey.hasNext() ; ) {
                Meta key = itKey.next();
                if (key.getCidSeries() < cidSeries)  {
                    itKey.remove();
                    continue;
                }
                if (key.getCidSeries() == cidSeries && key.getCid() < cid) itKey.remove();
            }
        }


    }

    static public RBTypes getRBType(int type) {
        switch (type) {
            case 0: return RBTypes.FORK;
            case 1: return RBTypes.SYNC;
            case 2: return RBTypes.BBC;
            case 3: return RBTypes.START;
        }
        return RBTypes.NOT_MAPPED;
    }


    static public boolean validateForkProof(ForkProof p) {
        logger.debug(format("starts validating fp"));
        Block curr = p.getCurr();
        Block prev = p.getPrev();
        if (!BlockDigSig.verifyBlock(curr)) {
            logger.debug(format("invalid fork proof #3"));
            return false;
        }
        if (!BlockDigSig.verifyBlock(prev)) {
            logger.debug(format("invalid fork proof #4"));
            return false;
        }

        logger.debug(format("panic for fork is valid [fp=%d]", p.getCurr().getHeader().getHeight()));
        return true;
    }

    static public boolean validateSubChainVersion(SubChainVersion v, int f) {
        if (v.getVCount() == 0) return true;
        ArrayList<Integer> leaders = new ArrayList<>();
        leaders.add(v.getV(0).getHeader().getBid().getPid());
        for (int i = 1 ; i < v.getVList().size() ; i++ ) {
            Block pb = v.getV(i);
            if (!BlockDigSig.verifyBlock(pb)) {
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
                if (leaders.contains(pb.getHeader().getBid().getPid())) {
                    logger.debug(format("invalid invalid sub chain version, block creator is invalid [height=%d]",
                             pb.getHeader().getHeight()));
                    return false;
                }
            }
        }
        return true;
    }
}
