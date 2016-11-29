package edu.suda.ada.dao;

import edu.suda.ada.core.SimpleTransaction;

import java.util.List;

/**
 * Created by LiYang on 2016/11/22.
 */
public interface TransactionTemplate {
    void saveTransaction(SimpleTransaction transaction);
    void saveTransactions(List<SimpleTransaction> transactions);
    void deleteTransaction(SimpleTransaction transaction);
    void updateTransaction(SimpleTransaction transaction);
}
