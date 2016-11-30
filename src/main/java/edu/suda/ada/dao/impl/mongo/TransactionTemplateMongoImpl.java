package edu.suda.ada.dao.impl.mongo;

import edu.suda.ada.dao.TransactionTemplate;
import edu.suda.ada.core.SimpleTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by LiYang on 2016/11/22.
 */
public class TransactionTemplateMongoImpl implements TransactionTemplate {
    public static final String TRANSACTION_COLLECTION = "transactions";

    private MongoTemplate mongoTemplate;
    private List<SimpleTransaction> txList = new ArrayList<>();
    @Autowired
    private MongoDbFactory factory;

    private ExecutorService executorService;
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            mongoTemplate = new MongoTemplate(factory);
            mongoTemplate.insert(txList, TRANSACTION_COLLECTION);
        }
    };

    public TransactionTemplateMongoImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
        executorService = Executors.newFixedThreadPool(5);
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
    public void saveTransactions(List<SimpleTransaction> simpleTransactions) {
        executorService.execute(task);
        txList.clear();
    }
}
