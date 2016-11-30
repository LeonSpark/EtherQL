package edu.suda.ada.handler;

import edu.suda.ada.dao.AccountTemplate;
import org.ethereum.core.BlockSummary;
import org.ethereum.core.Genesis;
import org.ethereum.core.Transaction;
import org.ethereum.core.TransactionExecutionSummary;
import org.ethereum.util.ByteUtil;
import org.ethereum.vm.DataWord;
import org.ethereum.vm.program.InternalTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AccountProcessor extends Processor {
    private final Logger LOG = LoggerFactory.getLogger("processor");
    private AccountTemplate accountTemplate;

    public AccountProcessor(AccountTemplate accountTemplate){
        this.accountTemplate = accountTemplate;
    }

    @Override
    public void processBlock(BlockSummary blockSummary) {

        long startTime = System.currentTimeMillis();
        accountTemplate.startTracking();

        long start = System.currentTimeMillis();
        rewardMinderAndUncles(blockSummary.getRewards());
        blockSummary.getSummaries().forEach(this::processTransaction);
        long end = System.currentTimeMillis();

        accountTemplate.commit();
        long endTime = System.currentTimeMillis();
        if (endTime - startTime > 5){
            LOG.warn("Block number {}, Account Processor takes : {}  loop takes : {}",
                    blockSummary.getBlock().getNumber(), endTime - startTime, end - start);
        }
    }

    /**
     * Reward the miner and uncles
     * @param rewards
     */
    private void rewardMinderAndUncles(Map<byte[], BigInteger> rewards){
        for (byte[] addr : rewards.keySet()){
            String address = ByteUtil.toHexString(addr);
            accountTemplate.addBalance(address, rewards.get(addr).doubleValue());
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
            accountTemplate.createAccount(
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
        accountTemplate.increaseNonce(ByteUtil.toHexString(address));
    }

    private void spendGas(String sender, double fee) {
        accountTemplate.addBalance(sender, -fee);
    }

    /**
     * transfer the amount of value to the receiver
     * @param tx transaction
     */
    private void transfer(Transaction tx){
        //if the value is greater than 0
        if (ByteUtil.bytesToBigInteger(tx.getValue()).compareTo(new BigInteger("0")) > 0){
            accountTemplate.addBalance(ByteUtil.toHexString(tx.getSender()),
                    ByteUtil.bytesToBigInteger(tx.getValue()).negate().doubleValue());

            String receiver;

            if (!tx.isContractCreation()){
                receiver = ByteUtil.toHexString(tx.getReceiveAddress());
            }else {
                receiver = ByteUtil.toHexString(tx.getContractAddress());
            }

            double value = ByteUtil.bytesToBigInteger(tx.getValue()).doubleValue();
            accountTemplate.addBalance(receiver, value);
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

        //subtract the gas and value from sender's account
        accountTemplate.addBalance(sender, -(value.doubleValue() + gas));

        String receiver = ByteUtil.toHexString(transaction.getReceiveAddress());
        accountTemplate.addBalance(receiver, value.doubleValue());
    }

    private void deleteAccounts(List<DataWord> deletions){
        for (DataWord account : deletions){
            accountTemplate.delete(account.asString());
        }
    }
}
