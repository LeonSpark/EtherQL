package edu.suda.ada.api.impl.mysql;


import edu.suda.ada.api.BlockchainTemplate.BlockTemplate;
import edu.suda.ada.entity.BlockWrapper;

import java.util.List;

/**
 * @author leon.
 */

public class BlockTemplateMysqlImpl implements BlockTemplate {

    @Override
    public BlockWrapper getBlockByHash(String hash) {
        return null;
    }

    @Override
    public BlockWrapper getBlockByNumber(long blockNumber) {
        return null;
    }

    @Override
    public BlockWrapper getBlockByTransaction(String txHash) {
        return null;
    }

    @Override
    public void saveBlock(BlockWrapper blockWrapper) {

    }

    @Override
    public List<BlockWrapper> getBlocksByRange(int start, int end) {
        return null;
    }

    @Override
    public List<BlockWrapper> getBlocksByTimestamp(long start, long end) {
        return null;
    }
}
