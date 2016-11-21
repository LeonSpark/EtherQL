package edu.suda.ada;

import edu.suda.ada.api.BlockchainTemplate.*;
import edu.suda.ada.api.impl.mongo.AccountTemplateMongoImpl;
import edu.suda.ada.api.impl.mongo.BlockTemplateMongoImpl;
import edu.suda.ada.api.impl.mongo.TransactionTemplateMongoImpl;
import edu.suda.ada.ethereum.EthereumBean;
import org.ethereum.config.SystemProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.sql.DataSource;
import java.util.concurrent.Executors;

/**
 * @author leon.
 */
public class Starter implements BeanPostProcessor{
    /**
     * Ethereum 文件存储位置
     */
    private String dataLocation;

    private DataSource dataSource;

    private BlockTemplate blockTemplate;

    private AccountTemplate accountTemplate;

    private TransactionTemplate transactionTemplate;

    private SystemProperties systemProperties = SystemProperties.getDefault();


    public BlockTemplate getBlockTemplate() {
        return blockTemplate;
    }

    public void setBlockTemplate(BlockTemplate blockTemplate) {
        this.blockTemplate = blockTemplate;
    }

    public AccountTemplate getAccountTemplate() {
        return accountTemplate;
    }

    public void setAccountTemplate(AccountTemplate accountTemplate) {
        this.accountTemplate = accountTemplate;
    }

    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {

        if (blockTemplate == null) blockTemplate = new BlockTemplateMongoImpl();
        if (accountTemplate == null) accountTemplate = new AccountTemplateMongoImpl();
        if (transactionTemplate == null) transactionTemplate = new TransactionTemplateMongoImpl();
        if (dataLocation != null)
            systemProperties.setDataBaseDir(dataLocation);
        EthereumBean ethereumBean = new EthereumBean(blockTemplate, accountTemplate, transactionTemplate);
        Executors.newSingleThreadExecutor().submit(ethereumBean::start);
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return null;
    }
}
