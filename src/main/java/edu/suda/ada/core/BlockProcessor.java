package edu.suda.ada.core;

import edu.suda.ada.core.cudr.BlockTemplate;
import edu.suda.ada.entity.PlainBlock;
import org.ethereum.core.BlockSummary;

public class BlockProcessor extends Processor {

    private BlockTemplate blockTemplate;

    public BlockProcessor(BlockTemplate blockTemplate){
        this.blockTemplate = blockTemplate;
    }

    @Override
    public void processBlock(BlockSummary blockSummary) {
        PlainBlock block = new PlainBlock(blockSummary.getBlock());
        blockTemplate.saveBlock(block);

        successor.processBlock(blockSummary);
    }
}
