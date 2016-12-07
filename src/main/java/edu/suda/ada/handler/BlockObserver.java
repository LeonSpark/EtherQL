package edu.suda.ada.handler;

import edu.suda.ada.core.SimpleBlock;
import edu.suda.ada.dao.BlockTemplate;
import edu.suda.ada.dao.TemplateFactory;
import org.ethereum.core.BlockSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by LiYang on 2016/12/1.
 */
public class BlockObserver extends AbstractObserver {
    private final Logger logger = LoggerFactory.getLogger("processor");
    private TemplateFactory factory;

    public BlockObserver(TemplateFactory factory){
        this.factory = factory;
        executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public void update(BlockSummary blockSummary) {
        BlockTemplate blockTemplate = factory.getBlockTemplate();
        long start = System.currentTimeMillis();
        SimpleBlock block = new SimpleBlock(blockSummary.getBlock());

        results.add(executorService.submit(() -> blockTemplate.saveBlock(block)));

        //if any task in the executor pool is done then return, else sleep for 100 MS
        if (results.size() >= 10){
            waitForAvailable(results);
        }
        long end = System.currentTimeMillis();
        if (end - start > 50){
            logger.warn("Block number {}, Block Processor takes : {}",
                    blockSummary.getBlock().getNumber(), end - start);
        }
    }
}
