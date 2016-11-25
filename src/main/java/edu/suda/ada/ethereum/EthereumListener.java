package edu.suda.ada.ethereum;

import edu.suda.ada.core.BlockContainer;
import org.ethereum.core.BlockSummary;
import org.ethereum.facade.Ethereum;
import org.ethereum.listener.EthereumListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class EthereumListener extends EthereumListenerAdapter {

    Ethereum ethereum;

    public static final int MAX_QUEUE_SIZE = Integer.MAX_VALUE;
    public static final int TIME_SLEEP_SECOND = 3;
    private BlockingQueue<BlockSummary> blockQueue = new LinkedBlockingDeque<>(MAX_QUEUE_SIZE);
    private boolean syncDone = false;

    private BlockContainer container;
    private Thread blockProcessingThread;
    private Runnable blockHander = () -> {
        processBlock();
    };

    public EthereumListener(Ethereum ethereum, BlockContainer container) {
        this.ethereum = ethereum;
        this.container = container;
        init();
    }

    @Override
    public void onBlock(BlockSummary blockSummary) {
        blockQueue.add(blockSummary);
    }

    private void init(){
        blockProcessingThread = new Thread(blockHander, "BlockProcessingThread");
        blockProcessingThread.start();
    }

    public void processBlock(){
        while (!Thread.interrupted()) {
            try {
                if (blockQueue.size() > 0){
                    container.add(blockQueue.take());
                }else {
                    Thread.sleep(TIME_SLEEP_SECOND * 1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
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
