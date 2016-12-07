package edu.suda.ada.config;

import com.mongodb.MongoClient;
import edu.suda.ada.api.BlockAPI;
import edu.suda.ada.api.impl.mongo.BlockAPIMongoImpl;
import edu.suda.ada.core.BlockCache;
import edu.suda.ada.dao.MongoTemplateFactory;
import edu.suda.ada.ethereum.EthereumBean;
import edu.suda.ada.handler.AccountObserver;
import edu.suda.ada.handler.BlockObserver;
import edu.suda.ada.handler.TransactionObserver;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
@ComponentScan("edu.suda.ada")
public class AppConfig {

    private EthersqlConfig config = EthersqlConfig.getDefaultConfig();

    @Bean
    @Lazy
    public MongoDbFactory mongoDbFactory() {
        return new SimpleMongoDbFactory(mongoClient(), config.getMongoDatabase());
    }

    @Bean
    @Lazy
    public MongoClient mongoClient() {
        return new MongoClient(config.getMongoHost(), config.getMongoPort());
    }

    @Bean
    public MongoTemplateFactory mongoTemplateFactory() {
        return new MongoTemplateFactory(mongoDbFactory());
    }

    @Bean
    @Scope("prototype")
    public BlockObserver blockObserver() {
        return new BlockObserver(mongoTemplateFactory());
    }

    @Bean
    @Scope("prototype")
    public TransactionObserver transactionObserver() {
        return new TransactionObserver(mongoTemplateFactory());
    }

    @Bean
    @Scope("prototype")
    public AccountObserver accountObserver() {
        return new AccountObserver(mongoTemplateFactory());
    }

    @Bean
    @Scope("prototype")
    public BlockAPI blockAPI() {
        return new BlockAPIMongoImpl(new MongoTemplate(mongoDbFactory()));
    }

    @Bean
    @Scope("singleton")
    public BlockCache blockCache() {
        BlockCache blockCache = new BlockCache();

        blockCache.addObserver(transactionObserver());
        blockCache.addObserver(accountObserver());
        blockCache.addObserver(blockObserver());
        return blockCache;
    }

    @Bean
    @Scope("singleton")
    public EthereumBean ethereumBean() {
        EthereumBean ethereumBean = new EthereumBean(blockCache());
        ethereumBean.start();
        return ethereumBean;
    }

    @Bean
    public Initializer initializer() {
        return new Initializer(new MongoTemplate(mongoDbFactory()));
    }
}
