package edu.suda.ada.api.impl.mysql;


import edu.suda.ada.api.BlockAPI;
import edu.suda.ada.core.SimpleBlock;

import java.util.List;

/**
 * @author leon.
 */

public class BlockAPIMysqlImpl implements BlockAPI {

    @Override
    public SimpleBlock getBlockByHash(String hash) {
        return null;
    }

    @Override
    public SimpleBlock getBlockByNumber(long blockNumber) {
        return null;
    }

    @Override
    public SimpleBlock getBlockByTransaction(String txHash) {
        return null;
    }

    @Override
    public List<SimpleBlock> getBlocksByRange(int start, int end) {
        return null;
    }

    @Override
    public List<SimpleBlock> getBlocksByTimestamp(long start, long end) {
        return null;
    }
}
