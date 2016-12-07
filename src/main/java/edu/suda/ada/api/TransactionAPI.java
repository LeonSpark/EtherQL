package edu.suda.ada.api;

import edu.suda.ada.core.SimpleTransaction;
import edu.suda.ada.core.TopKAccount;

import java.util.List;

/**
 * Created by LiYang on 2016/11/22.
 */
public interface TransactionAPI {
    /**==================================================
     *        ethereum supported api
     ====================================================*/

    SimpleTransaction getTransactionByHash(String hash);
    List<SimpleTransaction> getTransactionsByBlockHash(String hash);
    List<SimpleTransaction> getTransactionByBlockNumber(long blockNumber);

    /**==================================================
     *       Entended api
     ====================================================*/
    List<SimpleTransaction> getTransactionsBySender(String sender);
    List<SimpleTransaction> getTransactionsByReceiver(String receiver);
    List<SimpleTransaction> getTransactionsRelatedWith(String address);
    List<SimpleTransaction> getTransactionsBetween(String addressA, String addressB);
    /**==================================================
     *       Range query api
     ====================================================*/
    List<SimpleTransaction> getTransactionsOrderedByValue(int offset, int limit, boolean asc);
    List<SimpleTransaction> getTransactionsByValue(double min, double max);

    List<SimpleTransaction> getTransactionsWithValueBetween(double min, double max, boolean asc);
    List<SimpleTransaction> getTransactionsBetweenBlocks(long start, long end);

    /**==================================================
     *       Topk api
     ====================================================*/
    List<TopKAccount> getTopKFrequentTradeAccounts(int topK);
    List<TopKAccount> getTopKValueTradeAccounts(int topK);
}
