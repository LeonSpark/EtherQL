package edu.suda.ada.config;

import edu.suda.ada.dao.AccountTemplate;
import org.ethereum.config.SystemProperties;
import org.ethereum.core.Genesis;
import org.ethereum.core.Transaction;
import org.ethereum.util.ByteUtil;

/**
 * Created by LiYang on 2016/12/1.
 */
public class Initializer {
    private AccountTemplate accountTemplate;
    private SystemProperties systemProperties = SystemProperties.getDefault();
    public Initializer(AccountTemplate accountTemplate){
        this.accountTemplate = accountTemplate;
        if (accountTemplate.isEmpty()){
            init();
        }
    }

    private void init(){
        Genesis.getInstance(systemProperties).getTransactionsList().forEach(this::processTransaction);
    }

    private void processTransaction(Transaction tx){
        accountTemplate.addBalance(ByteUtil.toHexString(tx.getReceiveAddress()), ByteUtil.bytesToBigInteger(tx.getValue()).doubleValue());
    }
}
