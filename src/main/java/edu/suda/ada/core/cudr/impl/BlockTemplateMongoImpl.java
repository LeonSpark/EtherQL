package edu.suda.ada.core.cudr.impl;

import com.mongodb.BasicDBObject;
import edu.suda.ada.api.BlockAPI;
import edu.suda.ada.api.impl.mongo.BlockAPIMongoImpl;
import edu.suda.ada.core.cudr.BlockTemplate;
import edu.suda.ada.entity.PlainBlock;
import edu.suda.ada.exception.InitializationException;
import edu.suda.ada.facade.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class BlockTemplateMongoImpl implements BlockTemplate{
    public  final String BLOCK_COLLECTION = "blocks";

    private MongoTemplate mongoTemplate;

    public BlockTemplateMongoImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean exist(PlainBlock block) {
       return mongoTemplate.exists(Query.query(Criteria.where("number").is(block.getNumber())), BLOCK_COLLECTION);
    }

    @Override
    public PlainBlock getBlock(Long number) {
        return mongoTemplate.findOne(Query.query(Criteria.where("number").is(number)), PlainBlock.class, BLOCK_COLLECTION);
    }

    @Override
    public void saveBlock(PlainBlock block) {
        mongoTemplate.save(block, BLOCK_COLLECTION);
    }

    @Override
    public void deleteBlock(PlainBlock block) {
        mongoTemplate.remove(Query.query(Criteria.where("number").is(block.getNumber())), BLOCK_COLLECTION);
    }

    @Override
    public void updateBlock(PlainBlock block) {
        BasicDBObject dbObject = new BasicDBObject();
        mongoTemplate.getConverter().write(block, dbObject);
        mongoTemplate.upsert(Query.query(Criteria.where("number").is(block.getNumber())),
                Update.fromDBObject(dbObject,"_id"),
                BLOCK_COLLECTION);
    }
}
