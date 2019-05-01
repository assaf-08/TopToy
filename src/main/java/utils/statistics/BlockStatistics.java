package utils.statistics;

import proto.Types;

public class BlockStatistics {
    BlockHeaderStatistics headerStatistics;
    Types.BlockID bid;
    int dataSize;
    long proposedTime;

    public BlockStatistics setHeaderStatistics(BlockHeaderStatistics headerStatistics) {
        this.headerStatistics = headerStatistics;
        return this;
    }

    public BlockStatistics setBid(Types.BlockID bid) {
        this.bid = bid;
        return this;
    }

    public BlockStatistics setDataSize(int dataSize) {
        this.dataSize = dataSize;
        return this;
    }

    public BlockStatistics setProposedTime(long proposedTime) {
        this.proposedTime = proposedTime;
        return this;
    }

    public Types.BlockID getBid() {
        return bid;
    }

    public int getDataSize() {
        return dataSize;
    }

    public long getProposedTime() {
        return proposedTime;
    }

    public BlockHeaderStatistics getHeaderStatistics() {
        return headerStatistics;
    }
}