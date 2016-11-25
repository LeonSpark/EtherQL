package edu.suda.ada.entity;

import org.ethereum.core.Transaction;
import org.ethereum.core.TransactionExecutionSummary;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "transactions")
public class PlainTransaction extends PlainBasicTransaction {
    private long gasUsed;
    private long gasRefund;
    private List<PlainBasicTransaction> interalTransactions;

    public PlainTransaction(TransactionExecutionSummary summary){
        setTransactionExecutionSummary(summary);
    }

    public PlainTransaction(Transaction tx){
        setTransaction(tx);
    }

    public PlainTransaction(){}

    public long getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(long gasUsed) {
        this.gasUsed = gasUsed;
    }

    public long getGasRefund() {
        return gasRefund;
    }

    public void setGasRefund(long gasRefund) {
        this.gasRefund = gasRefund;
    }

    public void setTransactionExecutionSummary(TransactionExecutionSummary summary){
        setTransaction(summary.getTransaction());
        this.gasUsed = summary.getGasUsed().longValue();
        this.gasRefund = summary.getGasRefund().longValue();
        interalTransactions.addAll(summary.getInternalTransactions()
                .stream().map(PlainTransaction::new).collect(Collectors.toList()));
    }
}