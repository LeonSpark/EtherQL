package edu.suda.ada.dao;

import edu.suda.ada.dao.impl.mongo.AccountTemplateMongoImpl;
import edu.suda.ada.dao.impl.mongo.BlockTemplateMongoImpl;
import edu.suda.ada.dao.impl.mongo.TransactionTemplateMongoImpl;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * Created by LiYang on 2016/12/1.
 */
public class MongoTemplateFactory implements TemplateFactory {
    private MongoDbFactory factory;
    private MappingMongoConverter converter;
    public MongoTemplateFactory(MongoDbFactory factory){
        this.factory = factory;
        converter = new MappingMongoConverter(new DefaultDbRefResolver(factory), new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }

    @Override
    public BlockTemplate getBlockTemplate() {
       return new BlockTemplateMongoImpl(new MongoTemplate(factory, converter));
    }

    @Override
    public AccountTemplate getAccountTemplate() {
        return new AccountTemplateMongoImpl(new MongoTemplate(factory, converter));
    }

    @Override
    public TransactionTemplate getTransactionTemplate() {
        return new TransactionTemplateMongoImpl(new MongoTemplate(factory, converter));
    }
}
