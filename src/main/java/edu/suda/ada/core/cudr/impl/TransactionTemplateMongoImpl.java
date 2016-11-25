package edu.suda.ada.core.cudr.impl;

import edu.suda.ada.core.cudr.TransactionTemplate;
import edu.suda.ada.entity.PlainTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by LiYang on 2016/11/22.
 */
public class TransactionTemplateMongoImpl implements TransactionTemplate {
    public static final String TRANSACTION_COLLECTION = "transactions";

    private MongoTemplate mongoTemplate;

    public TransactionTemplateMongoImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void deleteTransaction(PlainTransaction transaction) {

    }

    @Override
    public void updateTransaction(PlainTransaction transaction) {

    }

    @Override
    public void saveTransaction(PlainTransaction plainTransaction) {
        mongoTemplate.save(plainTransaction, TRANSACTION_COLLECTION);
    }

    @Override
    public void saveTransactions(List<PlainTransaction> plainTransactions) {
        mongoTemplate.insert(plainTransactions, PlainTransaction.class);
    }
}
