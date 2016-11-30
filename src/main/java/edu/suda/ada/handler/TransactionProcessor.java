package edu.suda.ada.handler;

import edu.suda.ada.core.SimpleTransaction;
import edu.suda.ada.dao.TransactionTemplate;
import org.ethereum.core.BlockSummary;
import org.ethereum.core.TransactionExecutionSummary;
import org.ethereum.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionProcessor extends Processor {
    private final Logger LOG = LoggerFactory.getLogger("processor");
    private TransactionTemplate transactionTemplate;
    private List<SimpleTransaction> txs = new ArrayList<>();


    public TransactionProcessor(TransactionTemplate transactionTemplate){
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void processBlock(BlockSummary blockSummary) {
        long start = System.currentTimeMillis();
        String blockHash = ByteUtil.toHexString(blockSummary.getBlock().getHash());

        if (containsTransaction(blockSummary)){
            blockSummary.getSummaries().forEach(summary -> handleTransaction(summary, blockHash));
        }
        long endTime = System.currentTimeMillis();
        transactionTemplate.saveTransactions(txs);
        txs.clear();
        long end = System.currentTimeMillis();
        try {
            Thread.sleep(50000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (end - start > 5){
            LOG.warn("Block number : {} Transaction processor takes : {} loop takes : {}",
                    blockSummary.getBlock().getNumber(), end - start, endTime - start);
        }
        successor.processBlock(blockSummary);
    }

    private boolean containsTransaction(BlockSummary blockSummary){
        return blockSummary.getSummaries() != null && blockSummary.getSummaries().size() > 0;
    }

    /**
     * TODO add log info
     * @param summary
     */
    private void handleTransaction(TransactionExecutionSummary summary, String blockHash){
        SimpleTransaction tx = new SimpleTransaction(summary);
        tx.setBlockHash(blockHash);
        txs.add(tx);
    }
}
