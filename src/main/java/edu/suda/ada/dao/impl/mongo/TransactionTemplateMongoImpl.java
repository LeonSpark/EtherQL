package edu.suda.ada.dao.impl.mongo;

import edu.suda.ada.core.SimpleTransaction;
import edu.suda.ada.dao.TransactionTemplate;
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
        mongoTemplate.insert(simpleTransaction, TRANSACTION_COLLECTION);
    }

    @Override
    public int saveTransactions(List<SimpleTransaction> simpleTransactions) {
        mongoTemplate.insertAll(simpleTransactions);
        return simpleTransactions.size();
    }
}
