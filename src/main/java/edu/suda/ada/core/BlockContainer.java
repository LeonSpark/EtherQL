package edu.suda.ada.core;

import edu.suda.ada.entity.BlockState;
import edu.suda.ada.facade.KeyValueSource;
import org.ethereum.core.Block;
import org.ethereum.core.BlockSummary;
import org.ethereum.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BlockContainer {
    private final Logger logger = LoggerFactory.getLogger("blockcontainer");
    private KeyValueSource<Long, List<BlockState>> index;
    private KeyValueSource<String, BlockSummary> data;
    private BlockState bestBlock;
    private Processor blockProcessor;

    public static int CACHE_SIZE = 6;

    public BlockContainer(Processor blockProcessor){
        index = new KeyValueSource<>(new ConcurrentHashMap<>());
        data = new KeyValueSource<>(new ConcurrentHashMap<>());
        this.blockProcessor = blockProcessor;
    }

    /**
     * add new block summery to blockContainer
     * @param summary
     */
    public void add(BlockSummary summary){
        logger.info("add new block to block container, block number: [{}]", summary.getBlock().getNumber());
        Block block = summary.getBlock();
        BlockState state = new BlockState.StateBuilder()
                .hash(ByteUtil.toHexString(block.getHash()))
                .parentHash(ByteUtil.toHexString(block.getParentHash()))
                .number(block.getNumber())
                .mainChain(true)
                .totalDifficulty(block.getCumulativeDifficulty())
                .build();

        data.put(state.getHash(), summary);

        if (!index.exist(block.getNumber())){
            index.put(block.getNumber(), Collections.singletonList(state));
            bestBlock = state;
        } else {
            bestBlock.setMainChain(false);
            rebuildBranch(state);
            index.get(block.getNumber()).add(state);
            bestBlock = state;
        }

        if (index.size() > CACHE_SIZE && needFlush()){
            flush();
        }
    }

    /**
     * rebuild the main branch
     * @param state newly add block state
     */
    private void rebuildBranch(BlockState state){
        logger.info("rebuild main branch");
        if (bestBlock.getNumber() == state.getNumber()){
            resetMainChain(state);
        }else {
            long level = bestBlock.getNumber() - 1;
            String mainChain = bestBlock.getParentHash();

             do {
                List<BlockState> ancestors = index.get(level);
                for (BlockState block : ancestors){
                    if (block.getHash().equals(mainChain)){
                        block.setMainChain(false);
                        mainChain = block.getParentHash();
                        bestBlock = block;
                    }
                }

            }while (level-- != state.getNumber());

            resetMainChain(state);
        }
    }

    private void resetMainChain(BlockState state){
        long number = bestBlock.getNumber() - 1;
        String mainChain = bestBlock.getParentHash();
        String current = state.getParentHash();

        while (true){
            List<BlockState> ancestors = index.get(number--);
            if (ancestors.size() > 1){
                for (BlockState block : ancestors){
                    if (block.getHash().equals(mainChain)){
                        block.setMainChain(false);
                        mainChain = block.getParentHash();
                    }else if (block.getHash().equals(current)){
                        block.setMainChain(true);
                        current = block.getParentHash();
                    }
                }
            }else {
                break;
            }
        }
    }

    /**
     * FIXME
     */
    private boolean needFlush(){
        return true;
    }

    private void flush(){
        logger.info("flushing to db");
        long number = bestBlock.getNumber();
        while (index.exist(number)){
            number--;
        }

        number += 1;

       List<BlockState> states = index.get(number);
        for (BlockState s : states){
            if (s.isMainChain()){
                blockProcessor.processBlock(data.get(s.getHash()));
            }

            data.delete(s.getHash());
        }
        index.delete(number);
    }
}
