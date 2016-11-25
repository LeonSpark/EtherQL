package edu.suda.ada.api.impl.mongo;

import edu.suda.ada.api.AccountAPI;
import edu.suda.ada.entity.PlainAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class AccountAPIMongoImpl implements AccountAPI {

    public final String ACCOUNT_COLLECTION = "accounts";

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public PlainAccount getAccountByAddress(String address) {
        return mongoTemplate.findOne(
                Query.query(Criteria.where("address").is(address)),
                PlainAccount.class, ACCOUNT_COLLECTION);
    }

    /**
     * get accounts according to balance
     * @param offset offset from the first record
     * @param limit number of records to retrieve
     * @param asc true for ascend and false for descent
     * @return list of accounts
     */
    private List<PlainAccount> getAccountsByBalanceOrdered(int offset, int limit, boolean asc) {
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

    private List<PlainAccount> execute(Query query) {
        return mongoTemplate.find(query, PlainAccount.class, ACCOUNT_COLLECTION);
    }
    @Override
    public List<PlainAccount> getAccountsByBalanceAsc(int offset, int limit) {
        return getAccountsByBalanceOrdered(offset, limit, true);
    }

    @Override
    public List<PlainAccount> getAccountsByBalanceAsc(int limit) {
        return getAccountsByBalanceAsc(0, limit);
    }

    @Override
    public List<PlainAccount> getAccountsByBalanceDesc(int offset, int limit) {
        return getAccountsByBalanceOrdered(offset, limit, false);
    }

    @Override
    public List<PlainAccount> getAccountsByBalanceDesc(int limit) {
        return getAccountsByBalanceDesc(0, limit);
    }

    @Override
    public List<PlainAccount> getAccountsWithBalanceBetween(double min, double max) {
        Query query = new Query(Criteria.where("balance").gte(min)
                                .andOperator(Criteria.where("balance").lte(max)));
        return execute(query);
    }

    @Override
    public List<PlainAccount> getAccountsWithBalancegt(double min) {
        Query query = new Query(Criteria.where("balance").gte(min));
        return execute(query);
    }
    @Override
    public List<PlainAccount> getAccountsWithBalancelt(double max) {
        Query query = new Query(Criteria.where("balance").lte(max));
        return execute(query);
    }
}
