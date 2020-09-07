package das.wrb;

import com.assafmanor.bbc.bbc.BBC;
import com.assafmanor.bbc.bbc.BBCBuilder;
import com.assafmanor.bbc.comm.communicationlayer.BBCCommServer;
import das.bbc.MetaDataAdapter;
import das.data.Data;

import das.ms.BFD;
import das.utils.TmoUtils;
import utils.config.yaml.ServerPublicDetails;
import utils.statistics.Statistics;

import static das.utils.TmoUtils.*;
import static java.lang.Math.max;
import static java.lang.String.format;
import proto.types.block.*;
import proto.types.meta.*;
import proto.types.wrb.*;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WRB {
    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(WRB.class);

    private static int id;
    private static int n;
    private static int f;
    private static WrbRpcs rpcs;
    private static BBC bbc;

    public WRB(int id, int workers, int n, int f, int tmo, ServerPublicDetails[] cluster,
               String serverCrt, String serverPrivKey, String caRoot) {
//        Logger rootLog = Logger.getLogger(BBCCommServer.class.getName());
//        rootLog.setLevel( Level.FINEST );
//        ConsoleHandler handler = new ConsoleHandler();
//        handler.setLevel(Level.FINEST);
//        rootLog.addHandler(handler);
        int bbcPort = 8181;
        bbc = new BBCBuilder(id,bbcPort,n,f).setNumberOfRounds(25).build();
        for(ServerPublicDetails details : cluster){
            bbc.addNodeToBroadcastList(details.getIp(),bbcPort);
        }
        WRB.f = f;
        WRB.n = n;
        WRB.id = id;

        new TmoUtils(n, workers, tmo);
        new Data(workers);
        new BFD(n, f, workers,20 * tmo);
        BFD.activateAll();
        WRB.rpcs = new WrbRpcs(id, workers, n, f, cluster, serverCrt, serverPrivKey, caRoot);
        logger.info(format("Initiated WRB: [id=%d; n=%d; f=%d; tmo=%d]", id, n, f, tmo));
    }

    static public void reconfigure() {

    }
    static public void start() {
        bbc.start();
        rpcs.start();
    }

    static public void shutdown() {
        rpcs.shutdown();
        bbc.shutdown();
        bbc.shutdown();
        logger.info(format("[#%d] shutting down wrb service", id));
    }

    static public void WRBBroadcast(BlockHeader h) {
        rpcs.wrbBroadcast(h);
    }

    static public void WRBSend(BlockHeader h, int[] recipients) {
        rpcs.wrbSend(h, recipients);
    }

    static public BlockHeader WRBDeliver(int worker, int cidSeries, int cid, int sender, int height, BlockHeader next)
            throws InterruptedException {
        Statistics.updateAll();
        Meta key = Meta.newBuilder()
                .setChannel(worker)
                .setCidSeries(cidSeries)
                .setCid(cid)
                .build();

        preDeliverLogic(key, worker, cidSeries, cid, sender);

//        BbcDecData dec = OBBC.propose(setFastBbcVote(key, worker, sender, cidSeries, cid, next), worker, height, sender);
        System.out.println(id+": proposing...Channel "+key.getChannel()+" Cid "+key.getCid()+" Cid Series "+key.getCidSeries());
        int dec = bbc.propose(1, MetaDataAdapter.metaToBBCMeta(key));
        System.out.println("Finish proposing...");
        if (dec == 0) {
            logger.debug(format("[#%d-C[%d]] bbc returned [%d] for [cidSeries=%d ; cid=%d]", id, worker, 0, cidSeries, cid));
            Statistics.updateNeg();
            return null;
        }
        Statistics.updatePos();

        logger.debug(format("[#%d-C[%d]] bbc returned [%d] for [cidSeries=%d ; cid=%d]", id, worker, 1, cidSeries, cid));

        return postDeliverLogic(key, worker, cidSeries, cid, sender, height);
    }

    static private void preDeliverLogic(Meta key, int worker, int cidSeries, int cid, int sender) throws InterruptedException {

        if (BFD.isSuspected(sender, worker)) {
            if (!Data.pending[worker].containsKey(key)) {
                logger.info(format("Suspect [%d:%d] skipping...", sender, worker));
                return;
            }
            BFD.unsuspect(sender, worker);
        }


        int realTmo = TmoUtils.getTmo(sender, worker);

        Statistics.updateTmo(realTmo);
        long startTime = System.currentTimeMillis();
        synchronized (Data.pending[worker]) {
            while (realTmo > 0 && !Data.pending[worker].containsKey(key)) {
                logger.debug(format("[#%d-C[%d]] going to sleep for at most [%d] ms for data msg [cidSeries=%d ; cid=%d]",
                        id, worker, realTmo, cidSeries, cid));
                Data.pending[worker].wait(realTmo);

                realTmo -= max(0, (System.currentTimeMillis() - startTime));
            }
        }


        long estimatedTime = System.currentTimeMillis() - startTime;
        updateTmo(sender, worker, (int) estimatedTime, Data.pending[worker].containsKey(key));
        logger.debug(format("[#%d-C[%d]] have waited [%d] ms for data msg [cidSeries=%d ; cid=%d]",
                id, worker, estimatedTime, cidSeries, cid));

        Statistics.updateActTmo((int) estimatedTime);
        Statistics.updateMaxTmo((int) estimatedTime);
        if (Data.pending[worker].containsKey(key)) {
            BFD.activate(worker);
        }
        if (BFD.isExceedsThreshold((int) estimatedTime)) {
            BFD.suspect(sender, worker, getTmo(sender, worker));
        }

    }

    static private BlockHeader postDeliverLogic(Meta key, int channel, int cidSeries, int cid, int sender, int height) throws InterruptedException {
        requestData(channel, cidSeries, cid, sender, height);
        if (!Data.pending[channel].containsKey(key)) {
            logger.error("header was not found [cidSeries=d ; cid=%d]");
        }
        return Data.pending[channel].get(key);
    }

    static private void requestData(int channel, int cidSeries, int cid, int sender, int height) throws InterruptedException {
        Meta key = Meta.newBuilder()
                .setChannel(channel)
                .setCidSeries(cidSeries)
                .setCid(cid)
                .build();
        if (Data.pending[channel].containsKey(key)) return;
        Meta meta = Meta.
                newBuilder().
                setCid(cid).
                setCidSeries(cidSeries).
                setChannel(channel).
                build();
        WrbReq req = WrbReq.newBuilder()
                .setMeta(meta)
                .setHeight(height)
                .setSender(id)
                .build();
        rpcs.broadcastReqMsg(req,channel, cidSeries, cid, sender);
        synchronized (Data.pending[channel]) {
            while (!Data.pending[channel].containsKey(key)) {
                Data.pending[channel].wait();
            }
        }

    }


}
