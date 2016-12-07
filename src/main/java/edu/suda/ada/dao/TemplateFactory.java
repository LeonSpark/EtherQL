package edu.suda.ada.dao;

/**
 * Created by LiYang on 2016/12/1.
 */
public interface TemplateFactory {
    BlockTemplate getBlockTemplate();
    AccountTemplate getAccountTemplate();
    TransactionTemplate getTransactionTemplate();
}
