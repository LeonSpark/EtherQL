package edu.suda.ada.handler;

import edu.suda.ada.core.SimpleLog;
import edu.suda.ada.core.SimpleTransaction;
import edu.suda.ada.dao.LogTemplate;
import edu.suda.ada.dao.TransactionTemplate;
import org.ethereum.core.BlockSummary;
import org.ethereum.core.TransactionExecutionSummary;
import org.ethereum.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionProcessor extends Processor {
    private final Logger LOG = LoggerFactory.getLogger("processor");

    private TransactionTemplate transactionTemplate;
    private LogTemplate logTemplate;

    private List<SimpleTransaction> txs = new ArrayList<>();
    private List<SimpleLog> logs = new ArrayList<>();

    public TransactionProcessor(TransactionTemplate transactionTemplate){
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void processBlock(BlockSummary blockSummary) {
        String blockHash = ByteUtil.toHexString(blockSummary.getBlock().getHash());
        if (containsTransaction(blockSummary)){
            blockSummary.getSummaries().forEach(summary -> handleTransaction(summary, blockHash));
        }

        successor.processBlock(blockSummary);
    }

    private boolean containsTransaction(BlockSummary blockSummary){
        return Objects.nonNull(blockSummary.getSummaries()) && blockSummary.getSummaries().size() > 0;
    }

    /**
     * TODO add log info
     * @param summary
     */
    private void handleTransaction(TransactionExecutionSummary summary, String blockHash){
        SimpleTransaction tx = new SimpleTransaction(summary);
        LOG.info("[Fee : {}]  [gasUsed : {}]  [gasLeftOver: {}]  [gasLimit : {}]" +
                        "  [gasPrice : {}]   [ gasRefund:{}]",
                summary.getFee(), summary.getGasUsed().longValue(),
                summary.getGasLeftover(), summary.getGasLimit(),
                summary.getGasPrice(), summary.getGasRefund());
        tx.setBlockHash(blockHash);
        txs.add(tx);

        transactionTemplate.saveTransactions(txs);
    }
}
