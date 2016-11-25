package edu.suda.ada.core.cudr.impl;

import com.mongodb.BasicDBObject;
import edu.suda.ada.core.cudr.AccountTemplate;
import edu.suda.ada.entity.PlainAccount;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Map;

public class AccountTemplateMongoImpl implements AccountTemplate {
    public static final String ACCOUNT_COLLECTION = "accounts";

    private MongoTemplate mongoTemplate;

    public AccountTemplateMongoImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean exist(PlainAccount account) {
        return mongoTemplate.exists(Query.query(Criteria.where("address").is(account.getAddress())), ACCOUNT_COLLECTION);
    }

    @Override
    public void upsert(PlainAccount plainAccount) {
        BasicDBObject dbObject = new BasicDBObject();
        mongoTemplate.getConverter().write(plainAccount, dbObject);
        mongoTemplate.upsert(Query.query(Criteria.where("address").is(plainAccount.getAddress())),
                Update.fromDBObject(dbObject,"_id"),
                ACCOUNT_COLLECTION);
    }

    @Override
    public void delete(PlainAccount account) {

    }

    @Override
    public void update(PlainAccount account) {

    }

    @Override
    public void bulkUpdate(Map<Query, Update> bulk) {
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, ACCOUNT_COLLECTION);

        bulk.forEach(bulkOperations::upsert);
        bulkOperations.execute();
    }
}
