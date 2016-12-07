package edu.suda.ada.handler;

import edu.suda.ada.dao.AccountTemplate;
import edu.suda.ada.dao.TemplateFactory;
import org.ethereum.core.BlockSummary;
import org.ethereum.core.Transaction;
import org.ethereum.core.TransactionExecutionSummary;
import org.ethereum.util.ByteUtil;
import org.ethereum.vm.DataWord;
import org.ethereum.vm.program.InternalTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class AccountObserver extends AbstractObserver {
    private final Logger logger = LoggerFactory.getLogger("processor");
    private TemplateFactory templateFactory;

    private AccountTemplate defaultAccountTemplate;
    private Map<String, Double> changes = new HashMap<>();

    public AccountObserver(TemplateFactory factory){
        this.templateFactory = factory;
        this.defaultAccountTemplate = factory.getAccountTemplate();
        executorService = Executors.newFixedThreadPool(60);
    }

    @Override
    public synchronized void update(BlockSummary blockSummary) {
        AccountTemplate accountTemplate = templateFactory.getAccountTemplate();

        long startTime = System.currentTimeMillis();
        rewardMinderAndUncles(blockSummary.getRewards());
        blockSummary.getSummaries().forEach(this::processTransaction);
        long end = System.currentTimeMillis();

        Map<String, Double> changeCopy = new HashMap<>();
        for (String key : changes.keySet()){
            changeCopy.put(key, changes.get(key));
        }

        results.add(executorService.submit(() -> accountTemplate.commit(changeCopy)));

        //if any task in the executor pool is done then return, else sleep for 100 MS
        if (results.size() >= 60){
            waitForAvailable(results);
        }

        changes.clear();
        long endTime = System.currentTimeMillis();
        if (endTime - startTime > 50){
            logger.warn("Block number {}, Account Processor takes : {}  loop takes : {}",
                    blockSummary.getBlock().getNumber(), endTime - startTime, end - startTime);
        }
    }

    /**
     * Reward the miner and uncles
     * @param rewards
     */
    private void rewardMinderAndUncles(Map<byte[], BigInteger> rewards){
        for (byte[] addr : rewards.keySet()){
            String address = ByteUtil.toHexString(addr);
            changes.put(address, rewards.get(addr).doubleValue());
        }
    }

    /**
     * handle individual transaction
     * @param summary transaction execution summary
     */
    private final void processTransaction(TransactionExecutionSummary summary){
        Transaction tx = summary.getTransaction();

        //1. increase the nonce of the sender
        increaseNonce(tx.getSender());

        //2. substract the fee
        spendGas(ByteUtil.toHexString(tx.getSender()), summary.getFee().doubleValue());

        //3. if this is a contract creation transaction, create an new contract account
        if (tx.isContractCreation()){
            defaultAccountTemplate.createAccount(
                    ByteUtil.toHexString(tx.getContractAddress()),
                    0,
                    true,
                    ByteUtil.toHexString(tx.getData()));
        }

        //4. transfer the value no matter this is a message call or contract creation transaction
        transfer(tx);

        //5. handle internal transactions
        if (summary.getInternalTransactions() != null && summary.getInternalTransactions().size() > 0)
            summary.getInternalTransactions().forEach(this::handleInternalTransaction);

        //6. delete the suicide accounts.
        deleteAccounts(summary.getDeletedAccounts());
    }

    /**
     * increase the nonce of the sender by 1
     * @param address
     */
    private void increaseNonce(byte[] address){
        defaultAccountTemplate.increaseNonce(ByteUtil.toHexString(address));
    }

    private void spendGas(String sender, double fee) {
        changes.put(sender, changes.getOrDefault(sender, 0.0) - fee);
    }

    /**
     * transfer the amount of value to the receiver
     * @param tx transaction
     */
    private void transfer(Transaction tx){
        //if the value is greater than 0
        if (ByteUtil.bytesToBigInteger(tx.getValue()).compareTo(new BigInteger("0")) > 0){
            String sender = ByteUtil.toHexString(tx.getSender());
            double value = ByteUtil.bytesToBigInteger(tx.getValue()).doubleValue();

            changes.put(sender, changes.getOrDefault(sender, 0.0) - value);
            String receiver;

            if (!tx.isContractCreation()){
                receiver = ByteUtil.toHexString(tx.getReceiveAddress());
            }else {
                receiver = ByteUtil.toHexString(tx.getContractAddress());
            }
            changes.put(receiver, changes.getOrDefault(receiver, 0.0) + value);
        }
    }

    /**
     * add internal transactions
     * @param transaction internal transaction
     */
    private void handleInternalTransaction(InternalTransaction transaction){
        String sender = ByteUtil.toHexString(transaction.getSender());
        BigInteger value = ByteUtil.bytesToBigInteger(transaction.getValue());
        long gas = ByteUtil.byteArrayToLong(transaction.getGasLimit()) * ByteUtil.byteArrayToLong(transaction.getGasPrice());

        changes.put(sender, changes.getOrDefault(sender, 0.0) - gas - value.doubleValue());
        String receiver = ByteUtil.toHexString(transaction.getReceiveAddress());
        changes.put(receiver, changes.getOrDefault(receiver, 0.0) + value.doubleValue());
    }

    private void deleteAccounts(List<DataWord> deletions){
        if (deletions == null || deletions.size() == 0) return;

        List<String> deleteAccounts = deletions.stream().map(DataWord::asString).collect(Collectors.toList());
        defaultAccountTemplate.delete(deleteAccounts);
    }
}
