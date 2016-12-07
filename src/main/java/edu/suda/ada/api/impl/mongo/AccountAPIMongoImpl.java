package edu.suda.ada.api.impl.mongo;

import edu.suda.ada.api.AccountAPI;
import edu.suda.ada.core.SimpleAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class AccountAPIMongoImpl implements AccountAPI {

    public final String ACCOUNT_COLLECTION = "accounts";

    protected MongoTemplate mongoTemplate;

    public AccountAPIMongoImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public SimpleAccount getAccountByAddress(String address) {
        return mongoTemplate.findOne(
                Query.query(Criteria.where("address").is(address)),
                SimpleAccount.class, ACCOUNT_COLLECTION);
    }

    /**
     * get accounts according to balance
     * @param offset offset from the first record
     * @param limit number of records to retrieve
     * @param asc true for ascend and false for descent
     * @return list of accounts
     */
    private List<SimpleAccount> getAccountsByBalanceOrdered(int offset, int limit, boolean asc) {
        Sort.Order order;
        if (asc) {
            order = new Sort.Order(Sort.Direction.ASC, "balance");
        }else {
            order = new Sort.Order(Sort.Direction.DESC, "balance");
        }

        Query query = new Query().with(new Sort(order))
                .skip(offset).limit(limit);
        return execute(query);
    }

    private List<SimpleAccount> execute(Query query) {
        return mongoTemplate.find(query, SimpleAccount.class, ACCOUNT_COLLECTION);
    }

    @Override
    public List<SimpleAccount> getAccountsByBalanceAsc(int offset, int limit) {
        return getAccountsByBalanceOrdered(offset, limit, true);
    }

    @Override
    public List<SimpleAccount> getAccountsByBalanceAsc(int limit) {
        return getAccountsByBalanceAsc(0, limit);
    }

    @Override
    public List<SimpleAccount> getAccountsByBalanceDesc(int offset, int limit) {
        return getAccountsByBalanceOrdered(offset, limit, false);
    }

    @Override
    public List<SimpleAccount> getAccountsByBalanceDesc(int limit) {
        return getAccountsByBalanceDesc(0, limit);
    }

    @Override
    public List<SimpleAccount> getAccountsWithBalanceBetween(double min, double max) {
        Query query = new Query(Criteria.where("balance").gte(min)
                                .andOperator(Criteria.where("balance").lte(max)));
        return execute(query);
    }

    @Override
    public List<SimpleAccount> getAccountsWithBalancegt(double min) {
        Query query = new Query(Criteria.where("balance").gte(min));
        return execute(query);
    }
    @Override
    public List<SimpleAccount> getAccountsWithBalancelt(double max) {
        Query query = new Query(Criteria.where("balance").lte(max));
        return execute(query);
    }
}
