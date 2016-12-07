package edu.suda.ada.core;

/**
 * Created by LiYang on 2016/12/4.
 */
public class MinerTopK {
    private String miner;
    private long count;

    public MinerTopK(String miner, long count) {
        this.miner = miner;
        this.count = count;
    }

    public String getMiner() {
        return miner;
    }

    public void setMiner(String miner) {
        this.miner = miner;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
