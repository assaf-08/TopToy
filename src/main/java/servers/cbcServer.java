package servers;

import blockchain.blockchain;
import blockchain.basicBlockchain;
import config.Node;
import consensus.RBroadcast.RBrodcastService;

import proto.Types.*;
import rmf.RmfNode;

import java.util.ArrayList;

import static java.lang.String.format;

public class cbcServer extends bcServer {
    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(cbcServer.class);

    public cbcServer(String addr, int rmfPort, int id, int channel, int f, int tmo, int tmoInterval,
                     int maxTx, boolean fastMode, ArrayList<Node> cluster, RmfNode rmf, RBrodcastService panic, RBrodcastService sync) {
        super(addr, rmfPort, id, channel, f, tmo, tmoInterval, maxTx, fastMode, cluster, rmf, panic, sync);
    }

    public cbcServer(String addr, int rmfPort, int id, int channel, int f, int tmo, int tmoInterval,
                     int maxTx, boolean fastMode, ArrayList<Node> cluster,
                     String bbcConfig, String panicConfig, String syncConfig,  String serverCrt, String serverPrivKey, String caRoot) {
        super(addr, rmfPort, id, channel, f, tmo, tmoInterval, maxTx, fastMode, cluster,
                bbcConfig, panicConfig, syncConfig, serverCrt, serverPrivKey, caRoot);
    }

    Block leaderImpl() {
        if (!configuredFastMode) {
            return normalLeaderPhase();
        }
        if (currHeight == 1 || !fastMode) {
            normalLeaderPhase();
        }
        return fastModePhase();
    }

    Block normalLeaderPhase() {
        if (currLeader != getID()) {
            return null;
        }
        logger.debug(format("[#%d -C[%d]] prepare to disseminate a new block of [height=%d] [cidSeries=%d ; cid=%d]",
                getID(), channel, currHeight, cidSeries, cid));
        addTransactionsToCurrBlock();
        Block sealedBlock = currBlock.construct(getID(), currHeight, cidSeries, cid, channel, bc.getBlock(currHeight - 1).getHeader());
        rmfServer.broadcast(sealedBlock);
        return null;
    }

    Block fastModePhase() {
        if ((currLeader + 1) % n != getID()) {
            return null;
        }
        logger.debug(format("[#%d-C[%d]] prepare fast mode phase for [height=%d] [cidSeries=%d ; cid=%d]",
                getID(), channel, currHeight + 1, cidSeries, cid + 1));
        addTransactionsToCurrBlock();
        return currBlock.construct(getID(), currHeight + 1, cidSeries, cid + 1, channel, null);
    }

    @Override
    public blockchain initBC(int id, int channel) {
        return new basicBlockchain(id, channel);
    }

    @Override
    public blockchain getBC(int start, int end) {
        return new basicBlockchain(this.bc, start, end);
    }
}