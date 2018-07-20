package rmf;

import com.google.protobuf.ByteString;
import config.Node;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import proto.Data;
import proto.Meta;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.String.format;

public class RmfNode extends Node{
    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(RmfNode.class);
    private boolean stopped = false;
    private RmfService rmfService;
    private Server rmfServer;
    int height;
    int cid = 0;
    public RmfNode(int id, String addr, int port, int f, int tmoInterval, int tmo, ArrayList<Node> nodes, String bbcConfig) {
        super(addr, port, id);
        rmfService = new RmfService(id, f, tmoInterval, tmo, nodes, bbcConfig);
        startGrpcServer();
//        height = 0;
    }

    private void startGrpcServer() {
        try {
            rmfServer = ServerBuilder.
                    forPort(getPort()).
                    addService(rmfService).
                    build().
                    start();
        } catch (IOException e) {
            logger.error("", e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (stopped) return;
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                logger.warn("*** shutting down gRPC server since JVM is shutting down");
                RmfNode.this.stop();
                logger.warn("*** server shut down");
            }
        });
    }

    public void stop() {
        stopped = true;
        rmfService.shutdown();
        rmfServer.shutdown();
    }

    public void blockUntilShutdown() throws InterruptedException {
        rmfService.shutdown();
        if (rmfServer != null) {
            rmfServer.awaitTermination();
        }

    }

    // This should be called only after all servers are running (as this object contains also the client logic)
    public void start() {
        rmfService.start();
    }

    public void broadcast(byte[] msg, int height) {
        logger.info(format("[#%d] broadcast data message with [height=%d]", getID(), height));
        Meta metaMsg = Meta.
                newBuilder().
                setSender(getID()).
                setHeight(height).
                setCid(cid).
                build();
        Data dataMsg = Data.
                newBuilder().
                setData(ByteString.copyFrom(msg)).
                setMeta(metaMsg).
                build();
        rmfService.rmfBroadcast(dataMsg);
    }

    public byte[]




    deliver(int height, int sender) {
        byte[] ret = rmfService.deliver(height, sender, cid);
        cid++;
        return ret;
    }


}
