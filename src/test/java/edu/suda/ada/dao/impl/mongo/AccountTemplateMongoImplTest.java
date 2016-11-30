package edu.suda.ada.dao.impl.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Created by LiYang on 2016/11/30.
 */

public class AccountTemplateMongoImplTest {
    private MongoTemplate mongoTemplate;

    @Before
    public void setup(){
        MongoDbFactory factory = new SimpleMongoDbFactory(new MongoClient("127.0.0.1", 27017), "test");
        mongoTemplate = new MongoTemplate(factory);
    }

    @Test
    public void addBalance() throws Exception {
        Update update = new Update();
        update.inc("balance", 100);
        BulkOperations bulk = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, "test");
        mongoTemplate.upsert(Query.query(Criteria.where("address").is("aaa")),
               update, "test");
    }

}