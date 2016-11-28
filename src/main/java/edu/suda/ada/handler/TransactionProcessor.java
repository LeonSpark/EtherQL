package edu.suda.ada.handler;

import edu.suda.ada.core.SimpleLog;
import edu.suda.ada.core.SimpleTransaction;
import edu.suda.ada.handler.cudr.LogTemplate;
import edu.suda.ada.handler.cudr.TransactionTemplate;
import org.ethereum.core.BlockSummary;
import org.ethereum.core.TransactionExecutionSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TransactionProcessor extends Processor {

    private TransactionTemplate transactionTemplate;
    private LogTemplate logTemplate;

    private List<SimpleTransaction> txs = new ArrayList<>();
    private List<SimpleLog> logs = new ArrayList<>();

    public TransactionProcessor(TransactionTemplate transactionTemplate){
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void processBlock(BlockSummary blockSummary) {
        if (containsTransaction(blockSummary)){
            blockSummary.getSummaries().forEach(this::handleTransaction);
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
    private void handleTransaction(TransactionExecutionSummary summary){
        txs.add(new SimpleTransaction(summary));
        logs.addAll(summary.getLogs().stream().map(SimpleLog::new).collect(Collectors.toList()));

        transactionTemplate.saveTransactions(txs);
    }
}
