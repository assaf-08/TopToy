package blockchain;

import crypto.DigestMethod;

import proto.Types.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;

public abstract class blockchain {
    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(blockchain.class);
    private final List<Block> blocks = new ArrayList<>();
    private final int creatorID;

    public blockchain(int creatorID) {
        this.creatorID = creatorID;
        createGenesis();
    }

    public blockchain(blockchain orig, int start, int end) {
        this.creatorID = orig.creatorID;
        this.blocks.addAll(orig.getBlocks(start, end));
    }

    abstract block createNewBLock();

    abstract void createGenesis();

    boolean validateBlockCreator(Block b, int f) {
        if (blocks.size() >= f && blocks.subList(blocks.size() - f, blocks.size()).
        stream().
        map(bl -> bl.getHeader().getCreatorID()).
        collect(Collectors.toList()).
        contains(b.getHeader().getCreatorID())) {
            logger.debug(format("[#%d] invalid block", creatorID));
            return false;
        }
        return true;
    }

    public abstract boolean validateBlockData(Block b);

    void setBlocks(List<Block> Nblocks, int start) {
        for (int i = start ; i < start + Nblocks.size() ; i++) {
            if (blocks.size() <= i) {
                blocks.add(Nblocks.get(i - start));
            } else {
                blocks.set(i, Nblocks.get(i - start));
            }

        }
    }
    boolean validateBlockHash(Block b) {
        byte[] d = DigestMethod.hash(blocks.get(b.getHeader().getHeight() - 1).getHeader().toByteArray());
        return DigestMethod.validate(b.getHeader().getPrev().toByteArray(),
                Objects.requireNonNull(d));
    }

    void addBlock(Block b) {
        synchronized (blocks) {
            blocks.add(b);
        }
    }

    Block getBlock(int index) {
        synchronized (blocks) {
            return blocks.get(index);
        }
    }

    List<Block> getBlocks(int start, int end) {
        synchronized (blocks) {
            return blocks.subList(start, end);
        }
    }

    public int getHeight() {
        synchronized (blocks) {
            return blocks.size() - 1;
        }
    }

    void removeBlock(int index) {
        blocks.remove(index);
    }

}
