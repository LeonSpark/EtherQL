package edu.suda.ada.entity;

import org.ethereum.core.Block;
import org.ethereum.core.Transaction;
import org.ethereum.util.ByteUtil;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import static org.ethereum.jsonrpc.TypeConverter.*;

@Document(collection = "blocks")
public class BlockWrapper {
    private long number;
    private String hash;
    private String parentHash;
    private BigInteger nonce;
    private String transactionsRoot;
    private String stateRoot;
    private String receiptsRoot;
    private String miner;
    private int difficulty;
    private String extraData;
    private long gasLimit;
    private long gasUsed;
    private long timestamp;
    private List<String> transactions;

    public BlockWrapper(Block block) {
        setBlock(block);
    }

    public BlockWrapper(){}

    public long getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getParentHash() {
        return parentHash;
    }

    public void setParentHash(String parentHash) {
        this.parentHash = parentHash;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public String getTransactionsRoot() {
        return transactionsRoot;
    }

    public void setTransactionsRoot(String transactionsRoot) {
        this.transactionsRoot = transactionsRoot;
    }

    public String getStateRoot() {
        return stateRoot;
    }

    public void setStateRoot(String stateRoot) {
        this.stateRoot = stateRoot;
    }

    public String getReceiptsRoot() {
        return receiptsRoot;
    }

    public void setReceiptsRoot(String receiptsRoot) {
        this.receiptsRoot = receiptsRoot;
    }

    public String getMiner() {
        return miner;
    }

    public void setMiner(String miner) {
        this.miner = miner;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public long getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(int gasLimit) {
        this.gasLimit = gasLimit;
    }

    public long getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(int gasUsed) {
        this.gasUsed = gasUsed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

    public void setBlock(org.ethereum.core.Block block) {
        this.number = block.getNumber();
        this.hash = toJsonHex(block.getHash());
        this.parentHash = toJsonHex(block.getParentHash());
        this.nonce = ByteUtil.bytesToBigInteger(block.getNonce());
        this.transactionsRoot = toJsonHex(block.getTxTrieRoot());
        this.stateRoot = toJsonHex(block.getStateRoot());
        this.receiptsRoot = toJsonHex(block.getReceiptsRoot());
        this.miner = toJsonHex(block.getCoinbase());
        this.difficulty = ByteUtil.byteArrayToInt(block.getDifficulty());
        this.extraData = toJsonHex(block.getExtraData() == null ? new byte[]{}:block.getExtraData());
        this.gasLimit = ByteUtil.byteArrayToLong(block.getGasLimit());
        this.gasUsed = block.getGasUsed();
        this.timestamp = block.getTimestamp();
        List<String> transactions = new ArrayList<>();
        for (Transaction tx : block.getTransactionsList()) {
            transactions.add(toJsonHex(tx.getHash()));
        }
        this.transactions = transactions;
    }
}
