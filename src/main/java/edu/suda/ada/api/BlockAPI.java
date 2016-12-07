package edu.suda.ada.api;

import edu.suda.ada.core.SimpleBlock;

import java.util.List;

/**
 * Created by LiYang on 2016/11/22.
 */
public interface BlockAPI {

    /**==================================================
     *        ethereum supported api
     ====================================================*/
    SimpleBlock getBlockByHash(String hash);
    SimpleBlock getBlockByNumber(long blockNumber);

    /**==================================================
     *       Entended api
     ====================================================*/
    SimpleBlock getBlockByTransaction(String txHash);

    /**==================================================
     *       Range query api
     ====================================================*/
    List<SimpleBlock> getBlocksByRange(int start, int end);
    List<SimpleBlock> getBlocksByTimestamp(long start, long end);
    int getBlockMinedByMiner(String miner);
}