package edu.suda.ada.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import edu.suda.ada.core.*;
import edu.suda.ada.handler.AccountProcessor;
import edu.suda.ada.handler.BlockProcessor;
import edu.suda.ada.handler.Processor;
import edu.suda.ada.handler.TransactionProcessor;
import edu.suda.ada.dao.AccountTemplate;
import edu.suda.ada.dao.BlockTemplate;
import edu.suda.ada.dao.TransactionTemplate;
import edu.suda.ada.dao.impl.mongo.AccountTemplateMongoImpl;
import edu.suda.ada.dao.impl.mongo.BlockTemplateMongoImpl;
import edu.suda.ada.dao.impl.mongo.TransactionTemplateMongoImpl;
import edu.suda.ada.ethereum.EthereumBean;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * Created by LiYang on 2016/11/22.
 */

@Configuration
@ComponentScan("edu.suda.ada")
public class AppConfig {

    private EthersqlConfig config = EthersqlConfig.getDefaultConfig();

//    @Bean
//    @Lazy
//    @Scope("prototype")
//    public MongoTemplate mongoTemplate(){
//
//        MappingMongoConverter converter =
//                new MappingMongoConverter(mongoDbFactory(), new MongoMappingContext());
//        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
//        return new MongoTemplate(mongoDbFactory(), converter);
//    }

    @Bean
    @Lazy
    public MongoDbFactory mongoDbFactory(){
        return new SimpleMongoDbFactory(mongoClient(), config.getMongoDatabase());
    }

    @Bean
    @Lazy
    public MongoClient mongoClient(){
        return new MongoClient(config.getMongoHost(), config.getMongoPort());
    }

    @Bean
    public AccountTemplate accountTemplate(){
        MappingMongoConverter converter =
                new MappingMongoConverter(mongoDbFactory(), new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        String defaultDb = config.getDefaultDB();

        switch (defaultDb){
            case "mongo" : return new AccountTemplateMongoImpl(new MongoTemplate(mongoDbFactory(), converter));
            case "mysql" : return new AccountTemplateMongoImpl(new MongoTemplate(mongoDbFactory(), converter));
            default: throw new RuntimeException("db should be mysql or mongo");
        }
    }

    @Bean
    public TransactionTemplate transactionTemplate(){
        MappingMongoConverter converter =
                new MappingMongoConverter(mongoDbFactory(), new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        String defaultDb = config.getDefaultDB();
        switch (defaultDb){
            case "mongo" : return new TransactionTemplateMongoImpl(new MongoTemplate(mongoDbFactory(), converter));
            case "mysql" : return new TransactionTemplateMongoImpl(new MongoTemplate(mongoDbFactory(), converter));
            default: throw new RuntimeException("db should be mysql or mongo");
        }
    }

    @Bean
    public BlockTemplate blockTemplate(){
        MappingMongoConverter converter =
                new MappingMongoConverter(mongoDbFactory(), new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        String defaultDb = config.getDefaultDB();
        switch (defaultDb){
            case "mongo" : return new BlockTemplateMongoImpl(new MongoTemplate(mongoDbFactory(), converter));
            case "mysql" : return new BlockTemplateMongoImpl(new MongoTemplate(mongoDbFactory(), converter));
            default: throw new RuntimeException("db should be mysql or mongo");
        }
    }

    @Bean
    public BlockProcessor blockProcessor(){
        return new BlockProcessor(blockTemplate());
    }

    @Bean
    public TransactionProcessor transactionProcessor(){
        return new TransactionProcessor(transactionTemplate());
    }

    @Bean
    public AccountProcessor accountProcessor(){
        return new AccountProcessor(accountTemplate());
    }

    @Bean
    public Processor processor(){
        Processor blockProcessor = blockProcessor();
        Processor transactionProcessor =transactionProcessor();
        Processor accountProcessor = accountProcessor();
        transactionProcessor.setSuccessor(accountProcessor);
        blockProcessor.setSuccessor(transactionProcessor);
        return blockProcessor;
    }

    @Bean
    @Scope("singleton")
    public BlockCache blockCache(){
        return new BlockCache(processor());
    }

    @Bean
    @Scope("singleton")
    public EthereumBean ethereumBean(){
        EthereumBean ethereumBean = new EthereumBean(blockCache());
        ethereumBean.start();
        return ethereumBean;
    }

    @Bean
    public Initializer initializer(){
        return new Initializer(accountTemplate());
    }
}
