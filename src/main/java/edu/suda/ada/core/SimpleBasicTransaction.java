package edu.suda.ada.core;

import org.ethereum.core.Transaction;
import org.ethereum.jsonrpc.TypeConverter;
import org.ethereum.util.ByteUtil;
import org.ethereum.vm.program.InternalTransaction;

/**
 * Created by LiYang on 2016/11/22.
 */
public class SimpleBasicTransaction {
    private String hash;
    private long nonce;
    private String blockHash;
    private String from;
    private String to;
    private double value;
    private long gasPrice;
    private long gasLimit;
    private String data;

    public SimpleBasicTransaction() {}

    public SimpleBasicTransaction(Transaction transaction){
        setTransaction(transaction);
    }

    public void setTransaction(Transaction tx){
        this.hash = TypeConverter.toJsonHex(tx.getHash());
        this.nonce = ByteUtil.byteArrayToLong(tx.getNonce());
        this.from = TypeConverter.toJsonHex(tx.getSender());
        this.to = tx.getReceiveAddress() == null ? "":TypeConverter.toJsonHex(tx.getReceiveAddress());
        this.value = ByteUtil.bytesToBigInteger(tx.getValue()).doubleValue();
        this.gasPrice = ByteUtil.byteArrayToLong(tx.getGasPrice());
        this.gasLimit = ByteUtil.byteArrayToLong(tx.getGasLimit());
        this.data = tx.getData() == null ? "":TypeConverter.toJsonHex(tx.getData());
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(long gasPrice) {
        this.gasPrice = gasPrice;
    }

    public long getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(long gasLimit) {
        this.gasLimit = gasLimit;
    }

    public long getGasUsed(){
        return gasLimit * gasPrice;
    }
}
