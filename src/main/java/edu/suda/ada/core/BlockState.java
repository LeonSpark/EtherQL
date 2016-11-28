package edu.suda.ada.core;

import java.math.BigInteger;

/**
 * Created by LiYang on 2016/11/23.
 */
public class BlockState {

    private String hash;
    private boolean mainChain;
    private String parentHash;
    private long number;
    private BigInteger totalDifficulty;

    public String getHash() {
        return hash;
    }

    public boolean isMainChain() {
        return mainChain;
    }

    public void setMainChain(boolean mainChain){
        this.mainChain = mainChain;
    }

    public String getParentHash() {
        return parentHash;
    }

    public long getNumber() {
        return number;
    }

    public boolean parentOf(BlockState state){
        return state.getParentHash().equals(hash);
    }

    public BigInteger getTotalDifficulty() {
        return totalDifficulty;
    }

    private BlockState(StateBuilder builder){
        this.hash = builder.hash;
        this.parentHash = builder.parentHash;
        this.number = builder.number;
        this.mainChain = builder.mainChain;
        this.totalDifficulty = builder.totalDifficulty;
    }

    public static class StateBuilder{

        private String hash;
        private String parentHash;
        private boolean mainChain;
        private long number;
        private BigInteger totalDifficulty;

        public StateBuilder hash(String hash){
            this.hash = hash;
            return this;
        }

        public StateBuilder parentHash(String parentHash){
            this.parentHash = parentHash;
            return this;
        }

        public StateBuilder number(long number){
            this.number = number;
            return this;
        }

        public StateBuilder mainChain(boolean mainChain){
            this.mainChain = mainChain;
            return this;
        }

        public StateBuilder totalDifficulty(BigInteger totalDifficulty) {
            this.totalDifficulty = totalDifficulty;
            return this;
        }

        public BlockState build(){
            return new BlockState(this);
        }
    }
}
