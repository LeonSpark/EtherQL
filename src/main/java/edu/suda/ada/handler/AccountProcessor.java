package edu.suda.ada.handler;

import edu.suda.ada.handler.cudr.AccountTemplate;
import org.ethereum.core.BlockSummary;
import org.ethereum.core.Transaction;
import org.ethereum.core.TransactionExecutionSummary;
import org.ethereum.util.ByteUtil;
import org.ethereum.vm.DataWord;
import org.ethereum.vm.program.InternalTransaction;
import org.springframework.data.mongodb.core.query.Update;

import javax.print.DocFlavor;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class AccountProcessor extends Processor {

    private AccountTemplate accountTemplate;

    public AccountProcessor(AccountTemplate accountTemplate){
        this.accountTemplate = accountTemplate;
    }

    @Override
    public void processBlock(BlockSummary blockSummary) {
        accountTemplate.startTracking();
        rewardMinderAndUncles(blockSummary.getRewards());

        blockSummary.getSummaries().forEach(this::processTransaction);
        accountTemplate.commit();
    }

    /**
     * Reward the miner and uncles
     * @param rewards
     */
    private void rewardMinderAndUncles(Map<byte[], BigInteger> rewards){
        for (byte[] addr : rewards.keySet()){
            String address = ByteUtil.toHexString(addr);
            createNewOrUpdate(address, rewards.get(addr).doubleValue());
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
        accountTemplate.increaseNonce(ByteUtil.toHexString(address), true);
    }

    private void spendGas(String sender, double fee) {
        accountTemplate.addBalance(sender, -fee, true);
    }

    /**
     * transfer the amount of value to the receiver
     * @param tx transaction
     */
    private void transfer(Transaction tx){
        //if the value is greater than 0
        if (ByteUtil.bytesToBigInteger(tx.getValue()).compareTo(new BigInteger("0")) > 0){
            accountTemplate.addBalance(ByteUtil.toHexString(tx.getSender()),
                    ByteUtil.bytesToBigInteger(tx.getValue()).negate().doubleValue(), true);

            String receiver = ByteUtil.toHexString(tx.getReceiveAddress());
            double value = ByteUtil.bytesToBigInteger(tx.getValue()).doubleValue();
            createNewOrUpdate(receiver, value);
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
        accountTemplate.addBalance(sender, -(value.doubleValue() + gas), true);

        String receiver = ByteUtil.toHexString(transaction.getReceiveAddress());
        createNewOrUpdate(receiver, value.doubleValue());
    }

    private void deleteAccounts(List<DataWord> deletions){
        for (DataWord account : deletions){
            accountTemplate.delete(account.asString(), true);
        }
    }

    /**
     * create a new account with the initial balance if the account is not exist.
     * update it's balance otherwise
     * @param address address of the account that needs to be modified.
     * @param balance balance in this modification
     */
    private void createNewOrUpdate(final String address, double balance){
        if (accountTemplate.exist(address)){
            accountTemplate.addBalance(address, balance, true);
        }else {
            accountTemplate.createAccount(address, balance, false, null);
        }
    }
}
