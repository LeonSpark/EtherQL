package edu.suda.ada.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import edu.suda.ada.exception.InitializationException;
import edu.suda.ada.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

public class EthersqlConfig {
    private static EthersqlConfig ethersqlConfig = new EthersqlConfig();
    private final Logger logger = LoggerFactory.getLogger("config");
    private Config config;
    private ClassLoader classLoader;

    public static EthersqlConfig getDefaultConfig(){
        return ethersqlConfig;
    }
    private String mongoHost = null;
    private int mongoPort = 0;
    private String mongoDatabase = null;
    private String frequencyMap = null;
    private String frequencyReduce = null;
    private String valueMap = null;
    private String valueReduce = null;


    public EthersqlConfig(Config config){
        Config javaSystemProperties = ConfigFactory.load("no-such-resource-only-system-props");
        logger.info("loading java system properties");
        Config userConfig = ConfigFactory.parseResources("ethersql.conf");
        logger.info("loading user defined ethersql.conf");
        this.classLoader = getClass().getClassLoader();
        Config defaultConfig = ConfigFactory.load(classLoader, "ethersql.conf");

        this.config = javaSystemProperties
                .withFallback(defaultConfig)
                .withFallback(config.withFallback(userConfig));
        validateConfig();
    }

    public EthersqlConfig(String configLocation){
        this(ConfigFactory.parseResources(configLocation));
    }

    public EthersqlConfig(File configFile){
        this(ConfigFactory.parseFile(configFile));
    }

    public EthersqlConfig(){
        this(ConfigFactory.empty());
    }

    public Config getConfig(){
        return config;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface ValidateMe{}

    private void validateConfig(){
        for (Method method : getClass().getMethods()){
            if (method.isAnnotationPresent(ValidateMe.class)){
                try {
                    method.invoke(this);
                } catch (Exception e) {
                    throw new InitializationException("Error validating config" + method, e);
                }
            }
        }
    }

    @ValidateMe
    public String getDefaultDB(){
        return config.getString("default.db");
    }

    public String getMongoHost() {
        return mongoHost == null ? config.getString("mongo.host") : mongoHost ;
    }

    public void setMongoHost(String mongoHost) {
        this.mongoHost = mongoHost;
    }

    public int getMongoPort() {
        return mongoPort == 0 ? config.getInt("mongo.port") : mongoPort;
    }

    public void setMongoPort(int mongoPort) {
        this.mongoPort = mongoPort;
    }

    public String getMongoDatabase() {
        return mongoDatabase == null ? config.getString("mongo.db") : mongoDatabase;
    }

    public void setMongoDatabase(String mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    private String load(String location){
        InputStreamReader isr = new InputStreamReader(classLoader.getResourceAsStream(location));
        StringBuffer content = new StringBuffer();
        char[] buffer = new char[4096];
        try {
            while (isr.read(buffer) > 0){
                content.append(buffer);
            }
            return content.toString();
        } catch (IOException e) {
            throw new ResourceNotFoundException("Resource not found : " + location, e);
        }finally {
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ValidateMe
    public String getFrequencyMap(){
        if (frequencyMap != null) return frequencyMap;
        String location = config.getString("analysis.frequency.map");
        logger.info(location);
        return frequencyMap = load(location);
    }

    @ValidateMe
    public String getFrequencyReduce(){
        if (frequencyReduce != null) return frequencyReduce;
        String location = config.getString("analysis.frequency.reduce");
        return frequencyReduce = load(location);
    }

    @ValidateMe
    public String getValueMap(){
        if (valueMap != null) return valueMap;
        String location = config.getString("analysis.value.map");
        return valueMap = load(location);
    }
    @ValidateMe
    public String getValueReduce(){
        if (valueReduce != null) return valueReduce;
        String location = config.getString("analysis.value.reduce");
        return valueReduce = load(location);
    }
}
