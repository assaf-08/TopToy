package blockchain;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import config.Config;
import config.Node;
import crypto.DigestMethod;
import proto.Types;
import rmf.RmfNode;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class asyncBcServer extends bcServer {
    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(asyncBcServer.class);

    public asyncBcServer(String addr, int rmfPort,  int id) {
        super(addr, rmfPort,  id);
    }

    byte[] leaderImpl() throws InterruptedException {
        Random rand = new Random();
        int x = rand.nextInt(1500) + 1;
        logger.info(format("[#%d] sleeps for %d ms",getID(), x));
        Thread.sleep(x);
        if (!configuredFastMode) {
            return normalLeaderPhase();
        }
        if (currHeight == 1 || !fastMode) {
            normalLeaderPhase();
        }
        return fastModePhase();
    }

    byte[] normalLeaderPhase() {
        if (currLeader != getID()) {
            return null;
        }
        logger.info(format("[#%d] prepare to disseminate a new block of [height=%d]", getID(), currHeight));
        addTransactionsToCurrBlock();
        Types.Block sealedBlock = currBlock.construct(getID(), currHeight, DigestMethod.hash(bc.getBlock(currHeight - 1).getHeader().toByteArray()));
        rmfServer.broadcast(cidSeries, cid, sealedBlock.toByteArray(), currHeight);
        return null;
    }

    byte[] fastModePhase() {
        if ((currLeader + 1) % n != getID()) {
            return null;
        }
        addTransactionsToCurrBlock();
        return currBlock.construct(getID(), currHeight,
                DigestMethod.hash(bc.getBlock(currHeight - 1).getHeader().toByteArray())).toByteArray();
    }

    @Override
    blockchain initBC(int id) {
        return new basicBlockchain(id);
    }

    @Override
    blockchain getBC(int start, int end) {
        return new basicBlockchain(this.bc, start, end);
    }
}
