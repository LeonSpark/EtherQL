package edu.suda.ada.api;

import edu.suda.ada.entity.AccountWrapper;
import edu.suda.ada.entity.BlockWrapper;
import edu.suda.ada.entity.TopKAccount;
import edu.suda.ada.entity.TransactionWrapper;

import java.util.List;

/**
 * @author leon.
 */
public interface BlockchainTemplate {
    interface BlockTemplate {

        /**==================================================
         *        ethereum supported api
         ====================================================*/
        BlockWrapper getBlockByHash(String hash);
        BlockWrapper getBlockByNumber(long blockNumber);

        /**==================================================
         *       Entended api
         ====================================================*/
        BlockWrapper getBlockByTransaction(String txHash);

        void saveBlock(BlockWrapper blockWrapper);

        /**==================================================
         *       Range query api
         ====================================================*/
        List<BlockWrapper> getBlocksByRange(int start, int end);
        List<BlockWrapper> getBlocksByTimestamp(long start, long end);
    }

    interface TransactionTemplate{
        /**==================================================
         *        ethereum supported api
         ====================================================*/

        TransactionWrapper getTransactionByHash(String hash);
        List<TransactionWrapper> getTransactionsByBlockHash(String hash);
        List<TransactionWrapper> getTransactionByBlockNumber(long blockNumber);

        /**==================================================
         *       Entended api
         ====================================================*/
        List<TransactionWrapper> getTransactionsBySender(String sender);
        List<TransactionWrapper> getTransactionsByReceiver(String receiver);
        List<TransactionWrapper> getTransactionsRelatedWith(String address);
        List<TransactionWrapper> getTransactionsBetween(String addressA, String addressB);

        void saveTransaction(TransactionWrapper transactionWrapper);
        void saveTransactions(List<TransactionWrapper> transactionWrappers);
        /**==================================================
         *       Range query api
         ====================================================*/
        List<TransactionWrapper> getTransactionsOrderedByValue(int offset, int limit, boolean asc);
        List<TransactionWrapper> getTransactionsWithValueBetween(double min, double max, boolean asc);
        List<TransactionWrapper> getTransactionsBetweenBlocks(long start, long end);

        /**==================================================
         *       Topk api
         ====================================================*/
        List<TopKAccount> getTopKFrequentTradeAccounts(int topK);
        List<TopKAccount> getTopKValueTradeAccounts(int topK);
    }

    interface AccountTemplate {
        /**==================================================
         *        Ethereum supported api
         ====================================================*/
        AccountWrapper getAccountByAddress(String address);

        void upsertAccount(AccountWrapper accountWrapper);
        /**==================================================
         *        Range Query api
         ====================================================*/
        List<AccountWrapper> getAccountsByBalanceAsc(int offset, int limit);
        List<AccountWrapper> getAccountsByBalanceAsc(int limit);
        List<AccountWrapper> getAccountsByBalanceDesc(int offset, int limit);
        List<AccountWrapper> getAccountsByBalanceDesc(int limit);
        List<AccountWrapper> getAccountsWithBalanceBetween(double min, double max);
        List<AccountWrapper> getAccountsWithBalancegt(double min);
        List<AccountWrapper> getAccountsWithBalancelt(double max);
    }
}
