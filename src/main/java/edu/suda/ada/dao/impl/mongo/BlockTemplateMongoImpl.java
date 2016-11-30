package edu.suda.ada.dao.impl.mongo;

import com.mongodb.BasicDBObject;
import edu.suda.ada.core.SimpleBlock;
import edu.suda.ada.dao.BlockTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


public class BlockTemplateMongoImpl implements BlockTemplate{
    public  final String BLOCK_COLLECTION = "blocks";

    private MongoTemplate mongoTemplate;

    public BlockTemplateMongoImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean exist(SimpleBlock block) {
       return mongoTemplate.exists(Query.query(Criteria.where("number").is(block.getNumber())), BLOCK_COLLECTION);
    }

    @Override
    public SimpleBlock getBlock(Long number) {
        return mongoTemplate.findOne(Query.query(Criteria.where("number").is(number)), SimpleBlock.class, BLOCK_COLLECTION);
    }

    @Override
    public void saveBlock(SimpleBlock block) {
        mongoTemplate.insert(block, BLOCK_COLLECTION);
    }

    @Override
    public void deleteBlock(SimpleBlock block) {
        mongoTemplate.remove(Query.query(Criteria.where("number").is(block.getNumber())), BLOCK_COLLECTION);
    }

    @Override
    public void updateBlock(SimpleBlock block) {
        BasicDBObject dbObject = new BasicDBObject();
        mongoTemplate.getConverter().write(block, dbObject);
        mongoTemplate.upsert(Query.query(Criteria.where("number").is(block.getNumber())),
                Update.fromDBObject(dbObject,"_id"),
                BLOCK_COLLECTION);
    }
}
