package edu.suda.ada;

import com.mongodb.MongoClient;
import edu.suda.ada.core.*;
import edu.suda.ada.core.cudr.AccountTemplate;
import edu.suda.ada.core.cudr.BlockTemplate;
import edu.suda.ada.core.cudr.TransactionTemplate;
import edu.suda.ada.core.cudr.impl.AccountTemplateMongoImpl;
import edu.suda.ada.core.cudr.impl.BlockTemplateMongoImpl;
import edu.suda.ada.core.cudr.impl.TransactionTemplateMongoImpl;
import edu.suda.ada.ethereum.EthereumBean;
import org.ethereum.config.SystemProperties;
import org.ethereum.core.BlockWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;

/**
 * Created by LiYang on 2016/11/22.
 */

@Configuration
@ComponentScan("edu.suda.ada")
public class AppConfig {
    @Bean
    public MongoTemplate mongoTemplate(){

        MappingMongoConverter converter =
                new MappingMongoConverter(mongoDbFactory(), new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(mongoDbFactory(), converter);
    }

    @Bean
    @Scope("singleton")
    public MongoDbFactory mongoDbFactory(){
        return new SimpleMongoDbFactory(mongoClient(), "test");
    }

    @Bean
    public MongoClient mongoClient(){
        return new MongoClient("127.0.0.1", 27017);
    }

    @Bean
    public AccountTemplate accountTemplate(){
        return new AccountTemplateMongoImpl(mongoTemplate());
    }

    @Bean
    public TransactionTemplate transactionTemplate(){
        return new TransactionTemplateMongoImpl(mongoTemplate());
    }

    @Bean
    public BlockTemplate blockTemplate(){
        return new BlockTemplateMongoImpl(mongoTemplate());
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
    public BlockContainer blockContainer(){
        return new BlockContainer(processor());
    }

    @Bean
    @Scope("singleton")
    public EthereumBean ethereumBean(){
        EthereumBean ethereumBean = new EthereumBean(blockContainer());
        ethereumBean.start();
        return ethereumBean;
    }
}
