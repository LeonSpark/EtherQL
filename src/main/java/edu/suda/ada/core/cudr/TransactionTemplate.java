package edu.suda.ada.core.cudr;

import edu.suda.ada.entity.PlainTransaction;

import java.util.List;

/**
 * Created by LiYang on 2016/11/22.
 */
public interface TransactionTemplate {
    void saveTransaction(PlainTransaction transaction);
    void saveTransactions(List<PlainTransaction> transactions);
    void deleteTransaction(PlainTransaction transaction);
    void updateTransaction(PlainTransaction transaction);
}
