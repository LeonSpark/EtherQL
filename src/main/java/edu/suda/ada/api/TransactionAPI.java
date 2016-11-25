package edu.suda.ada.api;

import edu.suda.ada.entity.PlainTransaction;
import edu.suda.ada.entity.TopKAccount;

import java.util.List;

/**
 * Created by LiYang on 2016/11/22.
 */
public interface TransactionAPI {
    /**==================================================
     *        ethereum supported api
     ====================================================*/

    PlainTransaction getTransactionByHash(String hash);
    List<PlainTransaction> getTransactionsByBlockHash(String hash);
    List<PlainTransaction> getTransactionByBlockNumber(long blockNumber);

    /**==================================================
     *       Entended api
     ====================================================*/
    List<PlainTransaction> getTransactionsBySender(String sender);
    List<PlainTransaction> getTransactionsByReceiver(String receiver);
    List<PlainTransaction> getTransactionsRelatedWith(String address);
    List<PlainTransaction> getTransactionsBetween(String addressA, String addressB);
    /**==================================================
     *       Range query api
     ====================================================*/
    List<PlainTransaction> getTransactionsOrderedByValue(int offset, int limit, boolean asc);
    List<PlainTransaction> getTransactionsWithValueBetween(double min, double max, boolean asc);
    List<PlainTransaction> getTransactionsBetweenBlocks(long start, long end);

    /**==================================================
     *       Topk api
     ====================================================*/
    List<TopKAccount> getTopKFrequentTradeAccounts(int topK);
    List<TopKAccount> getTopKValueTradeAccounts(int topK);
}
