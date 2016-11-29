package edu.suda.ada.handler;

import edu.suda.ada.core.SimpleBlock;
import edu.suda.ada.dao.BlockTemplate;
import org.ethereum.core.BlockSummary;

public class BlockProcessor extends Processor {

    private BlockTemplate blockTemplate;

    public BlockProcessor(BlockTemplate blockTemplate){
        this.blockTemplate = blockTemplate;
    }

    @Override
    public void processBlock(BlockSummary blockSummary) {
        SimpleBlock block = new SimpleBlock(blockSummary.getBlock());
        blockTemplate.saveBlock(block);

        successor.processBlock(blockSummary);
    }
}
