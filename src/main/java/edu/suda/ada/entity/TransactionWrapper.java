package edu.suda.ada.entity;

import org.ethereum.core.Transaction;
import org.ethereum.jsonrpc.TypeConverter;
import org.ethereum.util.ByteUtil;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transactions")
public class TransactionWrapper {
    private String hash;
    private double nonce;
    private String blockHash;
    private int transactionIndex;
    private String from;
    private String to;
    private double value;
    private long gasPrice;
    private long gasLimit;
    private String data;
    public TransactionWrapper(Transaction tx){
        setTransaction(tx);
    }

    public TransactionWrapper(){}

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public int getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(int transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getNonce() {
        return nonce;
    }

    public void setNonce(double nonce) {
        this.nonce = nonce;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(int gasPrice) {
        this.gasPrice = gasPrice;
    }

    public long getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(int gasLimit) {
        this.gasLimit = gasLimit;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setTransaction(org.ethereum.core.Transaction tx){
        this.hash = TypeConverter.toJsonHex(tx.getHash());
        this.nonce = ByteUtil.bytesToBigInteger(tx.getNonce()).doubleValue();
        this.from = TypeConverter.toJsonHex(tx.getSender());
        this.to = tx.getReceiveAddress() == null ? "":TypeConverter.toJsonHex(tx.getReceiveAddress());
        this.value = ByteUtil.bytesToBigInteger(tx.getValue()).doubleValue();
        this.gasPrice = ByteUtil.byteArrayToLong(tx.getGasPrice());
        this.gasLimit = ByteUtil.byteArrayToLong(tx.getGasLimit());
        this.data = tx.getData() == null ? "":TypeConverter.toJsonHex(tx.getData());
    }

    @Override
    public String toString() {
        return "TransactionWrapper{" +
                "hash='" + hash + '\'' +
                ", nonce=" + nonce +
                ", blockHash='" + blockHash + '\'' +
                ", transactionIndex=" + transactionIndex +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", value=" + value +
                ", gasPrice=" + gasPrice +
                ", gasLimit=" + gasLimit +
                ", data='" + data + '\'' +
                '}';
    }
}