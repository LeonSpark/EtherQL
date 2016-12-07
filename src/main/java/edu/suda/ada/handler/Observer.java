package edu.suda.ada.handler;

import org.ethereum.core.BlockSummary;

/**
 * Created by LiYang on 2016/12/1.
 */
public interface Observer {
    void update(BlockSummary blockSummary);
}
