package edu.suda.ada.dao.impl.mongo;

import com.mongodb.BasicDBObject;
import edu.suda.ada.core.SimpleAccount;
import edu.suda.ada.dao.AccountTemplate;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class AccountTemplateMongoImpl implements AccountTemplate {
    public static final String ACCOUNT_COLLECTION = "accounts";

    private MongoTemplate mongoTemplate;
    private BulkOperations bulkOperations;
    private boolean changed = false;

    public AccountTemplateMongoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean exist(String address) {
        return mongoTemplate.exists(Query.query(Criteria.where("address").is(address)), ACCOUNT_COLLECTION);
    }

    public void upsert(SimpleAccount simpleAccount) {
        BasicDBObject dbObject = new BasicDBObject();
        mongoTemplate.getConverter().write(simpleAccount, dbObject);
        mongoTemplate.upsert(Query.query(Criteria.where("address").is(simpleAccount.getAddress())),
                Update.fromDBObject(dbObject, "_id"),
                ACCOUNT_COLLECTION);
    }

    @Override
    public void delete(String address, boolean bulk) {
        if (bulk) {
            changed = true;
            bulkOperations.remove(Query.query(Criteria.where("address").is(address)));
        } else {
            mongoTemplate.remove(Query.query(Criteria.where("address").is(address)), ACCOUNT_COLLECTION);
        }
    }

    @Override
    public void addBalance(String address, double value, boolean bulk) {
        Update update = new Update();
        update.inc("balance", value);

        if (bulk) {
            changed = true;
            bulkOperations.updateOne(Query.query(Criteria.where("address").is(address)), update);
        } else {
            mongoTemplate.updateFirst(Query.query(Criteria.where("address").is(address)),
                    update, ACCOUNT_COLLECTION);
        }
    }

    @Override
    public void increaseNonce(String address, boolean bulk) {
        Update update = new Update();
        update.inc("nonce", 1);
        Query query = Query.query(Criteria.where("address").is(address));
        if (bulk) {
            changed = true;
            bulkOperations.updateOne(query, update);
        } else {
            mongoTemplate.updateFirst(query, update, ACCOUNT_COLLECTION);
        }
    }

    @Override
    public void createAccount(String address, double balance, boolean isContract, String code) {
        SimpleAccount account = new SimpleAccount(address, balance, isContract, code);
        account.setNonce(0);
        upsert(account);
    }

    @Override
    public void commit() {
        if (changed){
            bulkOperations.execute();
            changed = false;
        }
    }

    @Override
    public void startTracking() {
        bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, ACCOUNT_COLLECTION);
    }
}
