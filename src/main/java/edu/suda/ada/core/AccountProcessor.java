package edu.suda.ada.core;

import edu.suda.ada.core.cudr.AccountTemplate;
import edu.suda.ada.entity.PlainBasicTransaction;
import edu.suda.ada.entity.PlainTransaction;
import org.ethereum.core.Block;
import org.ethereum.core.BlockSummary;
import org.ethereum.core.Transaction;
import org.ethereum.util.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class AccountProcessor extends Processor {

    private AccountTemplate accountTemplate;

    private Map<Query, Update> bulk = new HashMap<>();

    public AccountProcessor(AccountTemplate accountTemplate){
        this.accountTemplate = accountTemplate;
    }

    @Override
    public void processBlock(BlockSummary blockSummary) {
        blockSummary.getSummaries().stream().forEach(summary -> {
            Transaction tx = summary.getTransaction();
            addBulk(new PlainTransaction(tx));
            if (summary.getInternalTransactions().size() > 0)
            summary.getInternalTransactions().stream().map(PlainBasicTransaction::new).forEach(this::addBulk);
        });
//        rewardMiner(blockSummary.getBlock().getCoinbase(), );
//        accountTemplate.bulkUpdate(bulk);
    }

    public void addBulk(PlainBasicTransaction transaction){
        Update update = new Update();

        update.inc("balance", transaction.getValue());
        bulk.put(query(where("address").is(transaction.getTo())), update);
        Update update1 = new Update();
        update1.inc("balance", -(transaction.getValue() + transaction.getGasUsed()));
        bulk.put(query(where("address").is(transaction.getFrom())), update1);
    }

//    private void rewardMiner(byte[] coinbase){
//        String miner = ByteUtil.toHexString(coinbase);
//        Update update = new Update();
//        update.inc("balance", )
//        bulk.put(query(where("address").is(miner)),)
//    }
}
