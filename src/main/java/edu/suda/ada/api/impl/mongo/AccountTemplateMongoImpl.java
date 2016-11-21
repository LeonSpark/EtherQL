package edu.suda.ada.api.impl.mongo;

import edu.suda.ada.api.BlockchainTemplate.AccountTemplate;
import edu.suda.ada.entity.AccountWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class AccountTemplateMongoImpl implements AccountTemplate {

    public final String ACCOUNT_COLLECTION = "accounts";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public AccountWrapper getAccountByAddress(String address) {
        return mongoTemplate.findOne(
                Query.query(Criteria.where("address").is(address)),
                AccountWrapper.class, ACCOUNT_COLLECTION);
    }

    @Override
    public void upsertAccount(AccountWrapper accountWrapper) {
        mongoTemplate.remove(
                Query.query(Criteria.where("address").is(accountWrapper.getAddress())),
                ACCOUNT_COLLECTION);
        mongoTemplate.save(accountWrapper, ACCOUNT_COLLECTION);
    }

    /**
     * get accounts according to balance
     * @param offset offset from the first record
     * @param limit number of records to retrieve
     * @param asc true for ascend and false for descent
     * @return list of accounts
     */
    private List<AccountWrapper> getAccountsByBalanceOrdered(int offset, int limit, boolean asc) {
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

    private List<AccountWrapper> execute(Query query) {
        return mongoTemplate.find(query, AccountWrapper.class, ACCOUNT_COLLECTION);
    }
    @Override
    public List<AccountWrapper> getAccountsByBalanceAsc(int offset, int limit) {
        return getAccountsByBalanceOrdered(offset, limit, true);
    }

    @Override
    public List<AccountWrapper> getAccountsByBalanceAsc(int limit) {
        return getAccountsByBalanceAsc(0, limit);
    }

    @Override
    public List<AccountWrapper> getAccountsByBalanceDesc(int offset, int limit) {
        return getAccountsByBalanceOrdered(offset, limit, false);
    }

    @Override
    public List<AccountWrapper> getAccountsByBalanceDesc(int limit) {
        return getAccountsByBalanceDesc(0, limit);
    }

    @Override
    public List<AccountWrapper> getAccountsWithBalanceBetween(double min, double max) {
        Query query = new Query(Criteria.where("balance").gte(min)
                                .andOperator(Criteria.where("balance").lte(max)));
        return execute(query);
    }

    @Override
    public List<AccountWrapper> getAccountsWithBalancegt(double min) {
        Query query = new Query(Criteria.where("balance").gte(min));
        return execute(query);
    }
    @Override
    public List<AccountWrapper> getAccountsWithBalancelt(double max) {
        Query query = new Query(Criteria.where("balance").lte(max));
        return execute(query);
    }
}
