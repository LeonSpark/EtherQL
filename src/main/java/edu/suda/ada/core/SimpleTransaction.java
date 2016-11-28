package edu.suda.ada.core;

import org.ethereum.core.Transaction;
import org.ethereum.core.TransactionExecutionSummary;
import org.ethereum.jsonrpc.TypeConverter;
import org.ethereum.util.ByteUtil;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "transactions")
public class SimpleTransaction extends SimpleBasicTransaction {
    private long gasUsed;
    private long gasRefund;
    private long gasLeftover;
    private String result;

    private List<SimpleInternalTransaction> internalTransactions;

    public SimpleTransaction(TransactionExecutionSummary summary){
        setTransactionExecutionSummary(summary);
    }

    public SimpleTransaction(Transaction tx){
        setTransaction(tx);
    }

    public SimpleTransaction(){}

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

    public long getGasLeftover() {
        return gasLeftover;
    }

    public void setGasLeftover(long gasLeftover) {
        this.gasLeftover = gasLeftover;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<SimpleInternalTransaction> getInternalTransactions() {
        return internalTransactions;
    }

    public void setInternalTransactions(List<SimpleInternalTransaction> internalTransactions) {
        this.internalTransactions = internalTransactions;
    }

    public void setTransactionExecutionSummary(TransactionExecutionSummary summary){
        setTransaction(summary.getTransaction());

        this.gasUsed = summary.getGasUsed().longValue();
        this.gasRefund = summary.getGasRefund().longValue();
        this.gasLeftover = summary.getGasLeftover().longValue();
        this.result = ByteUtil.toHexString(summary.getResult());
        internalTransactions.addAll(summary.getInternalTransactions()
                .stream().map(SimpleInternalTransaction::new).collect(Collectors.toList()));
    }
}