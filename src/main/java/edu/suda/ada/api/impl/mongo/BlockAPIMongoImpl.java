package edu.suda.ada.api.impl.mongo;


import edu.suda.ada.entity.PlainBlock;
import edu.suda.ada.entity.PlainTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import edu.suda.ada.api.BlockAPI;

import java.util.List;

public class BlockAPIMongoImpl implements BlockAPI {
    public static final String BLOCK_COLLECTION = "blocks";
    public static final String TRANSACTION_COLLECTION = "transactions";

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public PlainBlock getBlockByHash(String hash) {
        return mongoTemplate.findOne(Query.query(Criteria.where("hash").is(hash)),
                PlainBlock.class, BLOCK_COLLECTION);
    }

    @Override
    public PlainBlock getBlockByNumber(long blockNumber) {
        return mongoTemplate.findOne(Query.query(Criteria.where("number").is(blockNumber)),
                PlainBlock.class, BLOCK_COLLECTION);
    }

    @Override
    public PlainBlock getBlockByTransaction(String txHash) {
        PlainTransaction tx = mongoTemplate.findOne(
                Query.query(Criteria.where("hash").is(txHash)),
                PlainTransaction.class,
                TRANSACTION_COLLECTION
        );

        return mongoTemplate.findOne(
                Query.query(Criteria.where("hash").is(tx.getBlockHash())),
                PlainBlock.class,
                BLOCK_COLLECTION);
    }

    @Override
    public List<PlainBlock> getBlocksByRange(int start, int end) {
       return getBlockRange(start, end, "number");
    }

    @Override
    public List<PlainBlock> getBlocksByTimestamp(long start, long end) {
        return getBlockRange(start, end, "timestamp");
    }

    private List<PlainBlock> getBlockRange(long start, long end, String key){
        return mongoTemplate.find(
                Query.query(new Criteria()
                            .andOperator(
                                Criteria.where(key).gte(start),
                                Criteria.where(key).lte(end))),
                PlainBlock.class,
                BLOCK_COLLECTION
        );
    }

}
