package edu.suda.ada.api.impl.mysql;


import edu.suda.ada.api.BlockAPI;
import edu.suda.ada.entity.PlainBlock;

import java.util.List;

/**
 * @author leon.
 */

public class BlockAPIMysqlImpl implements BlockAPI {

    @Override
    public PlainBlock getBlockByHash(String hash) {
        return null;
    }

    @Override
    public PlainBlock getBlockByNumber(long blockNumber) {
        return null;
    }

    @Override
    public PlainBlock getBlockByTransaction(String txHash) {
        return null;
    }

    @Override
    public List<PlainBlock> getBlocksByRange(int start, int end) {
        return null;
    }

    @Override
    public List<PlainBlock> getBlocksByTimestamp(long start, long end) {
        return null;
    }
}
