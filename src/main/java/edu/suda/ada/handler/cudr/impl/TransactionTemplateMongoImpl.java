package edu.suda.ada.handler.cudr.impl;

import edu.suda.ada.handler.cudr.TransactionTemplate;
import edu.suda.ada.core.SimpleTransaction;
import org.springframework.data.mongodb.core.MongoTemplate;

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
    public void deleteTransaction(SimpleTransaction transaction) {

    }

    @Override
    public void updateTransaction(SimpleTransaction transaction) {

    }

    @Override
    public void saveTransaction(SimpleTransaction simpleTransaction) {
        mongoTemplate.save(simpleTransaction, TRANSACTION_COLLECTION);
    }

    @Override
    public void saveTransactions(List<SimpleTransaction> simpleTransactions) {
        mongoTemplate.insert(simpleTransactions, SimpleTransaction.class);
    }
}
