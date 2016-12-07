package edu.suda.ada.api.impl.mongo;


import edu.suda.ada.api.BlockAPI;
import edu.suda.ada.core.MinerTopK;
import edu.suda.ada.core.SimpleBlock;
import edu.suda.ada.core.SimpleTransaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class BlockAPIMongoImpl implements BlockAPI {
    public static final String BLOCK_COLLECTION = "blocks";
    public static final String TRANSACTION_COLLECTION = "transactions";

    protected MongoTemplate mongoTemplate;

    public BlockAPIMongoImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

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
                                Criteria.where(key).lt(end))),
                SimpleBlock.class,
                BLOCK_COLLECTION
        );
    }

    public int getAggregateBySender(String address) {

        Aggregation agg = newAggregation(
                match(Criteria.where("miner").is(address)),
                group("miner").count().as("total"),
                project("miner").and("total").previousOperation(),
                sort(Sort.Direction.DESC, "total")

        );

        return mongoTemplate.aggregate(agg, BLOCK_COLLECTION, MinerTopK.class).getMappedResults().size();

    }

    public int getBlockMinedByMiner(String miner){
        return mongoTemplate.find(Query.query(Criteria.where("miner").is(miner)), SimpleBlock.class, BLOCK_COLLECTION).size();
    }
}
