package edu.suda.ada;

import com.mongodb.MongoClient;
import org.junit.Assert;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Query;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by LiYang on 2016/11/30.
 */
public class MongoTemplateTest {


    static MongoDbFactory factory = new SimpleMongoDbFactory(new MongoClient("127.0.0.1", 27017), "performance");

    public void testPerformance()  {
        ExecutorService executor = Executors.newFixedThreadPool(500);
        for (int i = 0 ; i < 50; i++){
            executor.execute(new InsertTask());
        }
        executor.shutdown();
        while (!executor.isTerminated()){

        }

        MongoTemplate mongoTemplate = new MongoTemplate(factory);

        Assert.assertTrue(mongoTemplate.count(new Query(), "block") == 100000);

    }


    public static class InsertTask implements Runnable {

        @Override
        public void run() {
            MongoOperations operations = new MongoTemplate(factory);
            for (int i = 0 ; i < 1000; i++){

                operations.insert(new Block(), "block");
            }
        }
    }
}
