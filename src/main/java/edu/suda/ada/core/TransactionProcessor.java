package edu.suda.ada.core;

import edu.suda.ada.core.cudr.TransactionTemplate;
import edu.suda.ada.entity.PlainTransaction;
import org.ethereum.core.BlockSummary;
import org.ethereum.core.TransactionExecutionSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionProcessor extends Processor {

    private TransactionTemplate transactionTemplate;


    public TransactionProcessor(TransactionTemplate transactionTemplate){
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void processBlock(BlockSummary blockSummary) {

        List<PlainTransaction> transactions = blockSummary.getSummaries()
                .stream().map(PlainTransaction::new).collect(Collectors.toList());
        transactionTemplate.saveTransactions(transactions);

        successor.processBlock(blockSummary);
    }
}
