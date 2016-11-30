package edu.suda.ada.dao.impl.mongo;

import edu.suda.ada.core.SimpleAccount;
import edu.suda.ada.dao.AccountTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountTemplateMongoImpl implements AccountTemplate {
    public static final String ACCOUNT_COLLECTION = "accounts";
    private MongoTemplate mongoTemplate;
    @Autowired
    private MongoDbFactory factory;
    private BulkOperations bulkOperations;

    private ExecutorService executorService;

    public AccountTemplateMongoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        executorService = Executors.newFixedThreadPool(5);
    }

    @Override
    public void delete(String address) {
        bulkOperations.remove(Query.query(Criteria.where("address").is(address)));
    }

    @Override
    public void addBalance(String address, double value) {
        Update update = new Update();
        update.inc("balance", value);
        update.set("isContract", false);
        update.set("code", "");
        bulkOperations.upsert(Query.query(Criteria.where("address").is(address)),
                    update);
    }

    @Override
    public void increaseNonce(String address) {
        Update update = new Update();
        update.inc("nonce", 1);
        Query query = Query.query(Criteria.where("address").is(address));
        bulkOperations.upsert(query, update);
    }

    @Override
    public void createAccount(String address, double balance, boolean isContract, String code) {
        SimpleAccount account = new SimpleAccount(address, balance, isContract, code);
        account.setNonce(0);
        bulkOperations.insert(account);
    }

    @Override
    public void commit() {
//        executorService.execute(task);
        bulkOperations.execute();
    }

    @Override
    public void startTracking() {
        mongoTemplate = new MongoTemplate(factory);
        bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, ACCOUNT_COLLECTION);
    }

    @Override
    public boolean isEmpty() {
        return mongoTemplate.exists(Query.query(Criteria.where("balance").gt(0)), "accounts");
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            bulkOperations.execute();
        }
    };
}
