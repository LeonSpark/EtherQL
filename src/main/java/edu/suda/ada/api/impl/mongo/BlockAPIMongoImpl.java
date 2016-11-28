package edu.suda.ada.api.impl.mongo;


import edu.suda.ada.core.SimpleBlock;
import edu.suda.ada.core.SimpleTransaction;
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
    public SimpleBlock getBlockByHash(String hash) {
        return mongoTemplate.findOne(Query.query(Criteria.where("hash").is(hash)),
                SimpleBlock.class, BLOCK_COLLECTION);
    }

    @Override
    public SimpleBlock getBlockByNumber(long blockNumber) {
        return mongoTemplate.findOne(Query.query(Criteria.where("number").is(blockNumber)),
                SimpleBlock.class, BLOCK_COLLECTION);
    }

    @Override
    public SimpleBlock getBlockByTransaction(String txHash) {
        SimpleTransaction tx = mongoTemplate.findOne(
                Query.query(Criteria.where("hash").is(txHash)),
                SimpleTransaction.class,
                TRANSACTION_COLLECTION
        );

        return mongoTemplate.findOne(
                Query.query(Criteria.where("hash").is(tx.getBlockHash())),
                SimpleBlock.class,
                BLOCK_COLLECTION);
    }

    @Override
    public List<SimpleBlock> getBlocksByRange(int start, int end) {
       return getBlockRange(start, end, "number");
    }

    @Override
    public List<SimpleBlock> getBlocksByTimestamp(long start, long end) {
        return getBlockRange(start, end, "timestamp");
    }

    private List<SimpleBlock> getBlockRange(long start, long end, String key){
        return mongoTemplate.find(
                Query.query(new Criteria()
                            .andOperator(
                                Criteria.where(key).gte(start),
                                Criteria.where(key).lte(end))),
                SimpleBlock.class,
                BLOCK_COLLECTION
        );
    }

}
