package edu.suda.ada.dao.impl.mongo;

import edu.suda.ada.core.SimpleAccount;
import edu.suda.ada.dao.AccountTemplate;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Map;

public class AccountTemplateMongoImpl implements AccountTemplate {
    public static final String ACCOUNT_COLLECTION = "accounts";
    private MongoOperations mongoTemplate;

    public AccountTemplateMongoImpl(MongoTemplate template){
        this.mongoTemplate = template;
    }

    @Override
    public void delete(List<String> addresses) {
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, ACCOUNT_COLLECTION);

        for (String address : addresses){
            bulkOperations.remove(Query.query(Criteria.where("address").is(address)));
        }

        bulkOperations.execute();
    }

    @Override
    public void addBalance(String address, double value) {
        Update update = new Update();
        update.inc("balance", value);
        update.set("isContract", false);
        update.set("code", "");
        mongoTemplate.upsert(Query.query(Criteria.where("address").is(address)),
                    update, ACCOUNT_COLLECTION);
    }

    @Override
    public void increaseNonce(String address) {
        Update update = new Update().inc("nonce", 1);
        Query query = Query.query(Criteria.where("address").is(address));
        mongoTemplate.upsert(query, update, ACCOUNT_COLLECTION);
    }

    @Override
    public void createAccount(String address, double balance, boolean isContract, String code) {
        SimpleAccount account = new SimpleAccount(address, balance, isContract, code);
        account.setNonce(0);
        mongoTemplate.insert(account, ACCOUNT_COLLECTION);
    }

    @Override
    public int commit(Map<String, Double> changes) {
        BulkOperations bulk = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, ACCOUNT_COLLECTION);
        for (String address : changes.keySet()){
            Update update = new Update();
            update.inc("balance", changes.get(address));
            update.set("isContract", false);
            bulk.upsert(Query.query(Criteria.where("address").is(address)),
                    update);
        }
        bulk.execute();
        return 0;
    }
}
