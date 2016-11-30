package edu.suda.ada.handler;

import edu.suda.ada.core.SimpleBlock;
import edu.suda.ada.dao.BlockTemplate;
import org.ethereum.core.BlockSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockProcessor extends Processor {
    private final Logger logger = LoggerFactory.getLogger("processor");

    private BlockTemplate blockTemplate;

    public BlockProcessor(BlockTemplate blockTemplate){
        this.blockTemplate = blockTemplate;
    }

    @Override
    public void processBlock(BlockSummary blockSummary) {
        long start = System.currentTimeMillis();
        SimpleBlock block = new SimpleBlock(blockSummary.getBlock());
        blockTemplate.saveBlock(block);
        long end = System.currentTimeMillis();
        if (end - start > 50){
            logger.warn("Block number {}, Block Processor takes : {}  loop takes : {}",
                    blockSummary.getBlock().getNumber(), end - start , end - start);
        }
        successor.processBlock(blockSummary);
    }
}
