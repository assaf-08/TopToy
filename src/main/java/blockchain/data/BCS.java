package blockchain.data;

import blockchain.Blockchain;
import blockchain.Utils;
import proto.Types;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static blockchain.Utils.createBlockchain;
import static com.google.common.primitives.Ints.min;
import static com.google.common.primitives.Ints.reverse;

public class BCS {
    static private Blockchain[] bcs;
    final static Object lock = new Object();
    static int n;
    static int f;
    static int workers;
    public BCS(int id, int n, int f, int workers) {
        BCS.n = n;
        BCS.f = f;
        BCS.workers = workers;
        bcs = new Blockchain[workers];
        for (int i = 0 ; i < workers ; i++) {
            bcs[i] = createBlockchain(Utils.BCT.SGC, id, n,
                    Paths.get("blocks", String.valueOf(i)));
        }
    }

//    public static Blockchain getBC(int w) {
//        return bcs[w];
//    }

    public static boolean validateBlockHash(int w, Types.Block b) {
        return bcs[w].validateBlockHash(b);
    }

    public static boolean validateCurrentLeader(int w, int currLeader, int currF) {
        return bcs[w].validateCurrentLeader(currLeader, currF);
    }

    public static void addBlock(int w, Types.Block b) {
        bcs[w].addBlock(b);
    }

    public static void removeBlock(int w, int h) throws IOException {
        bcs[w].removeBlock(h);
    }

    public static void setBlocks(int w, List<Types.Block> blocks, int start) throws IOException {
        bcs[w].setBlocks(blocks, start);
    }

    public static void notifyOnNewDifiniteBlock(int w) {
        synchronized (bcs[w]) {
            bcs[w].notifyAll();
        }
    }

    public static Types.Block nbGetBlock(int w, int h) {
        return bcs[w].getBlock(h);
    }

    public static boolean contains(int w, int h) {
        return bcs[w].contains(h);
    }

    public static List<Types.Block> getBlocks(int w, int low, int high) {
        return bcs[w].getBlocks(low, high);
    }

    public static Types.Block bGetBlock(int w, int h) throws InterruptedException {
        synchronized (bcs[w]) {
            while (height() < h) {
                bcs[w].wait();
            }
            return bcs[w].getBlock(h);
        }
    }

    public static int lastIndex(int w) {
        return bcs[w].lastIndex();
    }


    public static int height(int w) {
        return lastIndex(w) - (f + 2);
    }

    public static int height() {
        int mh = height(0);
        for (int i = 0 ; i < workers ; i++) {
            mh = min(mh, height(i));
        }
        return mh;
    }

    public static void writeNextToDiskAsync(int w) {
        bcs[w].writeNextToDiskAsync();
    }

    public static boolean isValid(int w) {
        return bcs[w].isValid();
    }

    public static boolean isValid() {
        for (int i = 0 ; i < workers ; i++) {
            if (!isValid(i)) return false;
        }
        return true;
    }
}