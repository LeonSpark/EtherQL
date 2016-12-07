package edu.suda.ada.ethereum;

import edu.suda.ada.core.BlockCache;
import org.ethereum.core.BlockSummary;
import org.ethereum.facade.Ethereum;
import org.ethereum.listener.EthereumListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;

public class EthereumListener extends EthereumListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger("listener");
    Ethereum ethereum;

    private static long counter = 0;
    private boolean syncDone = false;
    private BlockCache container;

    public EthereumListener(Ethereum ethereum, BlockCache container) {
        this.ethereum = ethereum;
        this.container = container;
    }

    @Override
    public void onBlock(BlockSummary blockSummary) {
        container.add(blockSummary);
        if (++counter % 100 == 0){
            logger.info("onBlock() received {} blocks,  Latest number : {}",
                    counter, blockSummary.getBlock().getNumber());
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
