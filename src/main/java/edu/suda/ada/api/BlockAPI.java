package edu.suda.ada.api;

import edu.suda.ada.entity.PlainBlock;

import java.util.List;

/**
 * Created by LiYang on 2016/11/22.
 */
public interface BlockAPI {

    /**==================================================
     *        ethereum supported api
     ====================================================*/
    PlainBlock getBlockByHash(String hash);
    PlainBlock getBlockByNumber(long blockNumber);

    /**==================================================
     *       Entended api
     ====================================================*/
    PlainBlock getBlockByTransaction(String txHash);

    /**==================================================
     *       Range query api
     ====================================================*/
    List<PlainBlock> getBlocksByRange(int start, int end);
    List<PlainBlock> getBlocksByTimestamp(long start, long end);
}