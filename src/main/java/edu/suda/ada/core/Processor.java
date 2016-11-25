package edu.suda.ada.core;

import org.ethereum.core.BlockSummary;

/**
 * Created by LiYang on 2016/11/22.
 */
public abstract class Processor {
    protected  Processor successor;

    public void setSuccessor(Processor successor){
        this.successor = successor;
    }

    public abstract void processBlock(BlockSummary blockSummary);
}
