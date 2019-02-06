package wrb;

import config.Node;

import proto.Types.*;
import java.util.*;

public class ByzantineWrbNode extends WrbNode {
//    private boolean stopped = false;
    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ByzantineWrbNode.class);

    public ByzantineWrbNode(int channels, int id, String addr, int rmfPort, int f, int tmo, int tmoInterval, ArrayList<Node> nodes,
                            String bbcConfig, String serverCrt, String serverPrivKey, String caRoot) {
        super(channels, id, addr, rmfPort, f, tmo, tmoInterval, nodes, bbcConfig, serverCrt, serverPrivKey, caRoot);
    }

    public void selectiveBroadcast(Block msg, List<Integer> ids) {
//        Meta metaMsg = Meta.
//                newBuilder().
//                setSender(getID()).
//              //  setHeight(height).
//                setCid(cid).
//                setCidSeries(cidSeries).
//                setChannel(channel).
//                build();
//        Data.Builder dataMsg = Data.
//                newBuilder().
//                setData(ByteString.copyFrom(msg)).
//                setMeta(metaMsg);
//        Data dmsg = dataMsg.setSig(rmfDigSig.sign(dataMsg)).build();
        for (Map.Entry<Integer, WrbService.peer>  p: wrbService.peers.entrySet()) {
            if (ids.contains(p.getKey())) {
//                logger.debug("sending message " + Arrays.toString(msg) + " to " + p.getKey() + " with height of " + height);
                wrbService.sendDataMessage(p.getValue().stub, msg);
            }

        }
    }
    /*
        This should used outside the wrb protocol (Note that the wrb protocol does not handle such failures)
     */
    public void devidedBroadcast(List<Block> msgs, List<List<Integer>> ids) {
        for (int i = 0 ; i < msgs.size() ; i++) {
            selectiveBroadcast(msgs.get(i), ids.get(i));
        }
    }

}