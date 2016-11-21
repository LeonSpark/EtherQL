package edu.suda.ada.ethereum;

import edu.suda.ada.api.BlockchainTemplate.AccountTemplate;
import edu.suda.ada.api.BlockchainTemplate.BlockTemplate;
import edu.suda.ada.api.BlockchainTemplate.TransactionTemplate;
import edu.suda.ada.entity.AccountWrapper;
import edu.suda.ada.entity.BlockWrapper;
import edu.suda.ada.entity.TransactionWrapper;
import org.ethereum.core.*;
import org.ethereum.facade.Ethereum;
import org.ethereum.listener.EthereumListenerAdapter;
import org.ethereum.util.BIUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EthereumListener extends EthereumListenerAdapter {

    Ethereum ethereum;

    private BlockTemplate blockTemplate;
    private AccountTemplate accountTemplate;
    private TransactionTemplate transactionTemplate;

    private boolean syncDone = false;
    public EthereumListener(Ethereum ethereum, BlockTemplate bt, AccountTemplate at, TransactionTemplate tt) {
        this.ethereum = ethereum;
        this.accountTemplate = at;
        this.transactionTemplate = tt;
        this.blockTemplate = bt;
    }

    @Override
    public void onBlock(BlockSummary blockSummary) {

        BlockWrapper block = new BlockWrapper(blockSummary.getBlock());

        blockTemplate.saveBlock(block);

        List<TransactionWrapper> txList = blockSummary.getReceipts().stream()
                .map(receipt -> new TransactionWrapper(receipt.getTransaction())).collect(Collectors.toList());

        transactionTemplate.saveTransactions(txList);

        List<AccountWrapper> accountWrappers = new ArrayList<>();
        List<TransactionExecutionSummary> summaries = blockSummary.getSummaries();

        for (TransactionExecutionSummary summary : summaries){
            Transaction tx = summary.getTransaction();
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

    /**
     * Just small method to estimate total power off all miners on the net
     * @param block
     */
    private void calcNetHashRate(Block block){

        if ( block.getNumber() > 1000){

            long avgTime = 1;
            long cumTimeDiff = 0;
            Block currBlock = block;
            for (int i=0; i < 1000; ++i){

                Block parent = ethereum.getBlockchain().getBlockByHash(currBlock.getParentHash());
                long diff = currBlock.getTimestamp() - parent.getTimestamp();
                cumTimeDiff += Math.abs(diff);
                currBlock = parent;
            }

            avgTime = cumTimeDiff / 1000;

            BigInteger netHashRate = block.getDifficultyBI().divide(BIUtil.toBI(avgTime));
            double hashRate = netHashRate.divide(new BigInteger("1000000000")).doubleValue();

            System.out.println("Net hash rate: " + hashRate + " GH/s");
        }

    }

}
