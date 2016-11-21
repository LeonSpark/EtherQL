package edu.suda.ada;

import edu.suda.ada.api.impl.mongo.AccountTemplateMongoImpl;
import edu.suda.ada.api.impl.mongo.BlockTemplateMongoImpl;
import edu.suda.ada.api.impl.mongo.TransactionTemplateMongoImpl;
import edu.suda.ada.api.impl.mysql.BlockTemplateMysqlImpl;
import edu.suda.ada.io.ClasspathResourceLoader;
import edu.suda.ada.io.ResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class Config {
    private ResourceLoader loader;

    @PostConstruct
    public void init(){
        loader = new ClasspathResourceLoader(Config.class);
//        loader.getResource();
    }


    @Bean
    public AccountTemplateMongoImpl accountTemplateMongoImpl(){
        return new AccountTemplateMongoImpl();
    }

    @Bean
    public BlockTemplateMongoImpl blockTemplateMongoImpl(){
        return new BlockTemplateMongoImpl();
    }

    @Bean
    public TransactionTemplateMongoImpl transactionTemplateMongoImpl(){
        return new TransactionTemplateMongoImpl();
    }

    @Bean
    public BlockTemplateMysqlImpl blockTemplateMysqlImpl(){
        return new BlockTemplateMysqlImpl();
    }
}
