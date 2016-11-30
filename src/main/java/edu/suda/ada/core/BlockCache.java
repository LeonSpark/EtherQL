package edu.suda.ada.core;

import edu.suda.ada.handler.Processor;
import org.apache.commons.logging.Log;
import org.ethereum.core.Block;
import org.ethereum.core.BlockSummary;
import org.ethereum.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class BlockCache {
    private final Logger LOG = LoggerFactory.getLogger("cache");

    private KeyValueSource<Long, List<BlockState>> index;
    private KeyValueSource<String, BlockSummary> data;
    private BlockingQueue<BlockSummary> blockQueue = new LinkedBlockingDeque(Integer.MAX_VALUE);
    private BlockingQueue<BlockSummary> toFlush = new LinkedBlockingDeque(Integer.MAX_VALUE);
    private BlockState bestBlock;
    private Processor blockProcessor;

    public static int FLUSH_SIZE_LIMIT = 5;
    private Thread blockCacheService;
    private Runnable cacheBlockTask = () -> cacheBlock();
    private Thread blockProcessService;
    private Runnable blockProcessTask = () -> processBlock();

    public BlockCache(Processor blockProcessor){
        index = new KeyValueSource<>(new ConcurrentHashMap<>());
        data = new KeyValueSource<>(new ConcurrentHashMap<>());
        this.blockProcessor = blockProcessor;
        init();
    }

    private void init(){
        blockCacheService = new Thread(cacheBlockTask, "cacheBlocksThread");
        blockCacheService.start();

        blockProcessService = new Thread(blockProcessTask, "flushBlockThread");
        blockProcessService.start();
    }

    /**
     * add new block summery to blockContainer
     * @param summary
     */
    public void add(BlockSummary summary){
        try {
            blockQueue.put(summary);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void cacheBlock(){

       while (!Thread.interrupted()){
            try {
                BlockSummary summary = blockQueue.take();
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
                    LOG.info("Adding best block to block cache, number: {} ", state.getNumber());

                    index.put(block.getNumber(), Collections.singletonList(state));
                } else {
                    LOG.warn("Forking occurs in block number : {}", state.getNumber());

                    bestBlock.setMainChain(false);
                    rebuildBranch(state);
                    index.get(block.getNumber()).add(state);
                }

                bestBlock = state;
                if (index.size() > FLUSH_SIZE_LIMIT){
                    toFlush();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processBlock(){
        while (!Thread.interrupted()){
            try {

                BlockSummary block = toFlush.take();
                long start = System.currentTimeMillis();
                blockProcessor.processBlock(block);
                long end = System.currentTimeMillis();

                LOG.warn("process block : {}, takes : {}  (flush queue size: {})",
                        block.getBlock().getNumber(),  end - start, toFlush.size());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * rebuild the main branch
     * @param state newly add block state
     */
    private void rebuildBranch(BlockState state){
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

    /**
     * reset the main chain when a fork takes place
     * @param state
     */
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
     * Add block to flushQueue
     */
    private void toFlush(){
        long number = bestBlock.getNumber() - FLUSH_SIZE_LIMIT;

        if (index.exist(number)){
            List<BlockState> states = index.get(number);

            for (BlockState s : states){
                if (s.isMainChain()){
                    toFlush.add(data.get(s.getHash()));
                }
                data.delete(s.getHash());
            }
            index.delete(number);
        }

        LOG.info("Add to flushQueue, current size: {} ", toFlush.size());
    }

}
