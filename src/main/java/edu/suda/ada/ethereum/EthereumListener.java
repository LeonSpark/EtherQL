package edu.suda.ada.ethereum;

import edu.suda.ada.core.BlockCache;
import org.ethereum.core.BlockSummary;
import org.ethereum.facade.Ethereum;
import org.ethereum.listener.EthereumListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class EthereumListener extends EthereumListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger("listener");
    Ethereum ethereum;

    public static final int MAX_QUEUE_SIZE = Integer.MAX_VALUE;
    public static final int TIME_SLEEP_MILLISECONDS = 3000;
    private BlockingQueue<BlockSummary> blockQueue = new LinkedBlockingDeque<>(MAX_QUEUE_SIZE);
    private boolean syncDone = false;

    private BlockCache container;
    private Thread blockProcessingThread;
    private Runnable blockHandler = () -> processBlock();

    public EthereumListener(Ethereum ethereum, BlockCache container) {
        this.ethereum = ethereum;
        this.container = container;
        init();
    }

    @Override
    public void onBlock(BlockSummary blockSummary) {
        logger.info("Receiving new block   [ number: {} ]", blockSummary.getBlock().getNumber());

        blockQueue.add(blockSummary);
    }

    private void init(){
        blockProcessingThread = new Thread(blockHandler, "BlockProcessingThread");
        blockProcessingThread.start();
    }

    public void processBlock(){
        while (!Thread.interrupted()) {
            try {
                if (blockQueue.size() > 0){
                    container.add(blockQueue.take());
                }else {
                    Thread.sleep(TIME_SLEEP_MILLISECONDS);
                }
            } catch (InterruptedException e) {
                logger.error("BlockProcessingThread interrupted. message: [{}]", e.getMessage());
            }
        }
    }
    /**
     *  Mark the fact that you are touching
     *  the head of the chain
     */
    @Override
    public void onSyncDone() {
        syncDone = true;
    }
}
