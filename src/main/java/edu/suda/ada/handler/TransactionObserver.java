package edu.suda.ada.handler;

import edu.suda.ada.core.SimpleTransaction;
import edu.suda.ada.dao.TemplateFactory;
import edu.suda.ada.dao.TransactionTemplate;
import org.ethereum.core.BlockSummary;
import org.ethereum.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by LiYang on 2016/12/1.
 */
public class TransactionObserver extends AbstractObserver {
    private final Logger logger = LoggerFactory.getLogger("processor");
    private TransactionTemplate transactionTemplate;
    private TemplateFactory templateFactory;
    private ExecutorService executorService;

    public TransactionObserver(TemplateFactory factory){
        this.templateFactory = factory;
        executorService = Executors.newFixedThreadPool(30);
    }

    @Override
    public void update(BlockSummary blockSummary) {
        transactionTemplate = templateFactory.getTransactionTemplate();
        List<SimpleTransaction> txs = new ArrayList<>();
        long start = System.currentTimeMillis();
        String blockHash = ByteUtil.toHexString(blockSummary.getBlock().getHash());

        if (containsTransaction(blockSummary)){
            blockSummary.getSummaries().forEach(summary -> {
                SimpleTransaction tx = new SimpleTransaction(summary);
                tx.setBlockHash(blockHash);
                txs.add(tx);
            });
        }

        long endTime = System.currentTimeMillis();
        results.add(executorService.submit(() -> transactionTemplate.saveTransactions(txs)));

        //if any task in the executor pool is done then return, else sleep for 100 MS
         if (results.size() >= 30){
            waitForAvailable(results);
         }

        long end = System.currentTimeMillis();

        if (end - start > 50){
            logger.warn("Block number : {} Transaction processor takes : {} loop takes : {}",
                    blockSummary.getBlock().getNumber(), end - start, endTime - start);
        }
    }

    private boolean containsTransaction(BlockSummary blockSummary){
        return blockSummary.getSummaries() != null && blockSummary.getSummaries().size() > 0;
    }

}
