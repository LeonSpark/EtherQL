package edu.suda.ada.config;

import edu.suda.ada.core.SimpleAccount;
import edu.suda.ada.dao.AccountTemplate;
import org.ethereum.config.SystemProperties;
import org.ethereum.core.Genesis;
import org.ethereum.core.Transaction;
import org.ethereum.util.ByteUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;

public class Initializer {
    private MongoTemplate mongoTemplate;
    private SystemProperties systemProperties = SystemProperties.getDefault();
    private List<SimpleAccount> accounts = new ArrayList<>();

    public Initializer(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        if (!this.mongoTemplate.exists(Query.query(Criteria.where("balance").gt(0)), "accounts")) {
            init();
        }
    }

    private void init() {
        Genesis.getInstance(systemProperties).getTransactionsList().forEach(this::processTransaction);
    }

    private void processTransaction(Transaction tx) {
        SimpleAccount account = new SimpleAccount();
        account.setBalance(ByteUtil.bytesToBigInteger(tx.getValue()).doubleValue());
        account.setAddress(ByteUtil.toHexString(tx.getReceiveAddress()));
        account.setCode("");
        account.setNonce(0);
        accounts.add(account);
        if (accounts.size() >= 1000) {
            flush();
        }
    }

    private void flush() {
        mongoTemplate.insertAll(accounts);
        accounts.clear();
    }
}
