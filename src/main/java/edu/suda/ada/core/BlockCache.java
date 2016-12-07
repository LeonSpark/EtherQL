package edu.suda.ada.core;

import edu.suda.ada.handler.AbstractObserver;
import edu.suda.ada.handler.Observer;
import org.ethereum.core.Block;
import org.ethereum.core.BlockSummary;
import org.ethereum.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

public class BlockCache {
    private final Logger logger = LoggerFactory.getLogger("cache");

    private KeyValueSource<Long, List<BlockState>> index;
    private KeyValueSource<String, BlockSummary> data;
    private BlockingQueue<BlockSummary> blockQueue = new LinkedBlockingDeque(Integer.MAX_VALUE);
    private BlockingQueue<BlockSummary> toFlush = new LinkedBlockingDeque(Integer.MAX_VALUE);
    private BlockState bestBlock;
    private List<AbstractObserver> observers = new ArrayList<>();
    public static int FLUSH_SIZE_LIMIT = 12;

    private Thread blockCacheService;
    private Runnable cacheBlockTask = () -> cacheBlock();
    private Thread blockProcessService;
    private Runnable blockProcessTask = () -> processBlock();


    public BlockCache(){
        index = new KeyValueSource<>(new ConcurrentHashMap<>());
        data = new KeyValueSource<>(new ConcurrentHashMap<>());
        init();
    }

    public void addObserver(AbstractObserver observer){
        observers.add(observer);
    }

    public void deleteObserver(Observer observer){
        observers.remove(observer);
    }

    private void notifyObserver(BlockSummary blockSummary){
        for (AbstractObserver observer : observers){
           observer.update(blockSummary);
        }
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
        blockQueue.add(summary);
        
        if (blockQueue.size() > 100){
            logger.warn("BlockQueue size : {}", blockQueue.size());
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
                    logger.info("Adding best block to block cache, number: {} ", state.getNumber());

                    index.put(block.getNumber(), Collections.singletonList(state));
                } else {
                    logger.warn("Forking occurs in block number : {}", state.getNumber());

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
                notifyObserver(block);
                long end = System.currentTimeMillis();

                logger.warn("process block : {}, takes : {}  (flush queue size: {})",
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

        logger.info("Add to flushQueue, current size: {} ", toFlush.size());
    }

}
