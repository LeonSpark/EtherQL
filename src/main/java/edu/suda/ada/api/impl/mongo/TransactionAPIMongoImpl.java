package edu.suda.ada.api.impl.mongo;

import edu.suda.ada.api.TransactionAPI;
import edu.suda.ada.config.EthersqlConfig;
import edu.suda.ada.core.SimpleTransaction;
import edu.suda.ada.core.TopKAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionAPIMongoImpl implements TransactionAPI {
    public final String TRANSACTION_COLLECTION = "transactions";
    public final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected MongoTemplate mongoTemplate;

    private EthersqlConfig config = EthersqlConfig.getDefaultConfig();

    @Override
    public SimpleTransaction getTransactionByHash(String hash) {

        return mongoTemplate.findOne(Query.query(Criteria.where("hash").is(hash)),
                SimpleTransaction.class, TRANSACTION_COLLECTION);
    }

    @Override
    public List<SimpleTransaction> getTransactionsByBlockHash(String hash) {
        return mongoTemplate.find(Query.query(Criteria.where("blockHash").is(hash)),
                SimpleTransaction.class, TRANSACTION_COLLECTION);
    }

    @Override
    public List<SimpleTransaction> getTransactionByBlockNumber(long blockNumber) {
        if (blockNumber < 0) {
            return null;
        }
        return mongoTemplate.find(Query.query(Criteria.where("blockHash").is(blockNumber)),
                SimpleTransaction.class, TRANSACTION_COLLECTION);
    }

    @Override
    public List<SimpleTransaction> getTransactionsBySender(String sender) {
        Query query = Query.query(Criteria.where("from").is(sender));
        return validateAndExecute(sender, query);
    }

    @Override
    public List<SimpleTransaction> getTransactionsByReceiver(String receiver) {
        Query query = Query.query(Criteria.where("to").is(receiver));
        return validateAndExecute(receiver, query);
    }

    @Override
    public List<SimpleTransaction> getTransactionsRelatedWith(String address) {
        Query query = Query.query(Criteria.where("from").is(address)
                .orOperator(Criteria.where("to").is(address)));
        return validateAndExecute(address, query);
    }

    @Override
    public List<SimpleTransaction> getTransactionsBetween(String addressA, String addressB) {

        Query query = Query.query(Criteria.where("from").is(addressA)
                .andOperator(Criteria.where("to").is(addressB))
                .orOperator(Criteria.where("from").is(addressB))
                .andOperator(Criteria.where("to").is(addressA)));
        return execute(query);

    }

    @Override
    public List<SimpleTransaction> getTransactionsOrderedByValue(int offset, int limit, boolean asc) {
        Query query = order(asc);
        query.skip(offset).limit(limit);

        return execute(query);
    }

    @Override
    public List<SimpleTransaction> getTransactionsWithValueBetween(double min, double max, boolean asc) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("value").gt(min), Criteria.where("value").lt(max));
        query.addCriteria(criteria);
        query.with(new Sort(new Sort.Order(asc ? Sort.Direction.ASC : Sort.Direction.DESC, "value")));
        return execute(query);
    }

    private Query order(boolean asc) {
        Query query = new Query();

        query.with(new Sort(new Sort.Order(asc?Sort.Direction.ASC : Sort.Direction.DESC, "value")));

        return query;
    }

    @Override
    //TODO
    public List<SimpleTransaction> getTransactionsBetweenBlocks(long start, long end) {
        return null;
    }

    private boolean validateHash(String hash) {
        boolean result = hash != null && hash.startsWith("0x");
        if (!result) LOG.error("not a valid address");
        return result;
    }

    private List<SimpleTransaction> execute(Query query) {
        return mongoTemplate.find(query, SimpleTransaction.class, TRANSACTION_COLLECTION);
    }

    private List<SimpleTransaction> validateAndExecute(String address, Query query) {
        if (!validateHash(address)) return null;
        return execute(query);
    }

    @Override
    public List<TopKAccount> getTopKFrequentTradeAccounts(int topK) {

        return getRange(topK,
                config.getFrequencyMap(),
                config.getFrequencyReduce());
    }

    @Override
    public List<TopKAccount> getTopKValueTradeAccounts(int topK) {
        return getRange(topK, config.getValueMap(),
                config.getValueReduce());
    }


    private List<TopKAccount> getRange(int topK, String mapFunction, String reduceFunction) {

        MapReduceResults<TopKAccount> reduceResults =
                mongoTemplate.mapReduce(
                TRANSACTION_COLLECTION, mapFunction,
                reduceFunction,
                TopKAccount.class);
        List<TopKAccount> list = new ArrayList<>();


        for (TopKAccount topKAccount : reduceResults) {
            list.add(topKAccount);
        }

        Collections.sort(list, (o1, o2) -> {
            double diff = o1.getValue() - o2.getValue();
            if (diff > 0) return -1;
            else if (diff == 0) return 0;
            else return 1;
        });

        return list.subList(0, topK);
    }
}
