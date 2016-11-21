package edu.suda.ada.api.impl.mongo;


import edu.suda.ada.entity.BlockWrapper;
import edu.suda.ada.entity.TransactionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import edu.suda.ada.api.BlockchainTemplate.BlockTemplate;

import java.util.List;

public class BlockTemplateMongoImpl implements BlockTemplate{
    public static final String BLOCK_COLLECTION = "blocks";
    public static final String TRANSACTION_COLLECTION = "transactions";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public BlockWrapper getBlockByHash(String hash) {
        return mongoTemplate.findOne(Query.query(Criteria.where("hash").is(hash)),
                BlockWrapper.class, BLOCK_COLLECTION);
    }

    @Override
    public BlockWrapper getBlockByNumber(long blockNumber) {
        return mongoTemplate.findOne(Query.query(Criteria.where("number").is(blockNumber)),
                BlockWrapper.class, BLOCK_COLLECTION);
    }

    @Override
    public BlockWrapper getBlockByTransaction(String txHash) {
        TransactionWrapper tx = mongoTemplate.findOne(
                Query.query(Criteria.where("hash").is(txHash)),
                TransactionWrapper.class,
                TRANSACTION_COLLECTION
        );

        return mongoTemplate.findOne(
                Query.query(Criteria.where("hash").is(tx.getBlockHash())),
                BlockWrapper.class,
                BLOCK_COLLECTION);
    }

    @Override
    public void saveBlock(BlockWrapper blockWrapper) {
        mongoTemplate.save(blockWrapper, BLOCK_COLLECTION);
    }


    @Override
    public List<BlockWrapper> getBlocksByRange(int start, int end) {
       return getBlockRange(start, end, "number");
    }

    @Override
    public List<BlockWrapper> getBlocksByTimestamp(long start, long end) {
        return getBlockRange(start, end, "timestamp");
    }

    private List<BlockWrapper> getBlockRange(long start, long end, String key){
        return mongoTemplate.find(
                Query.query(new Criteria()
                            .andOperator(
                                Criteria.where(key).gte(start),
                                Criteria.where(key).lte(end))),
                BlockWrapper.class,
                BLOCK_COLLECTION
        );
    }

}
