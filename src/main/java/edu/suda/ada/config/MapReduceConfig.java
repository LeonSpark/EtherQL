package edu.suda.ada.config;

import edu.suda.ada.io.ClasspathResourceLoader;
import edu.suda.ada.io.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author leon.
 */
public class MapReduceConfig {
    private final static String ANALYSIS_FREQUENCY_MAP = "analysis.frequency.map";
    private final static String ANALYSIS_FREQUENCY_REDUCE = "analysis.frequency.reduce";
    private final static String ANALYSIS_VALUE_MAP = "analysis.value.map";
    private final static String ANALYSIS_VALUE_REDUCE ="analysis.value.reduce";

    private Map<String, String> cachedFunctions = new HashMap<>();

    private final static String defaultPath = "mapreduce.properties";

    private ResourceLoader loader;

    private String path;
    private Properties properties;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public MapReduceConfig(){
        this(defaultPath);
    }

    public MapReduceConfig(String path) {
        this(new ClasspathResourceLoader(MapReduceConfig.class), path);
    }

    public MapReduceConfig(ResourceLoader resourceLoader, String path){
        this.loader = resourceLoader;
        this.path = path;
        properties = new Properties();
        init();
    }



    private void init(){
        try {
            properties.load(loader.getResource(path).getInputStream());
        } catch (IOException e) {
            LOG.error("error loading properties from {}, message: {}", path, e.getMessage());
            System.exit(-1);
        }
    }

    private String load(String path) {
        InputStream is;
        BufferedReader reader;

        StringBuffer buffer = new StringBuffer();
        String temp;
        try {
            is = loader.getResource(path).getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            while ((temp = reader.readLine())!=null){
                buffer.append(temp);
            }

        } catch (IOException e) {
            LOG.error("error reading from {}, message {}", path, e.getMessage());
            System.exit(-1);
        }

        return buffer.toString();
    }

    public String getFrequencyMap() {
        String mapFunction = cachedFunctions.get(ANALYSIS_FREQUENCY_MAP);
        if ( mapFunction == null){
            mapFunction = load(properties.getProperty(ANALYSIS_FREQUENCY_MAP));
            cachedFunctions.put(ANALYSIS_FREQUENCY_MAP, mapFunction);
        }
        return mapFunction;
    }

    public String getFrequencyReduce(){
        String reduceFunction = cachedFunctions.get(ANALYSIS_FREQUENCY_REDUCE);
        if ( reduceFunction == null){
            reduceFunction = load(properties.getProperty(ANALYSIS_FREQUENCY_REDUCE));
            cachedFunctions.put(ANALYSIS_FREQUENCY_REDUCE, reduceFunction);
        }
        return reduceFunction;
    }

    public String getValueMap(){
        String mapFunction = cachedFunctions.get(ANALYSIS_VALUE_MAP);
        if ( mapFunction == null){
            mapFunction = load(properties.getProperty(ANALYSIS_VALUE_MAP));
            cachedFunctions.put(ANALYSIS_VALUE_MAP, mapFunction);
        }
        return mapFunction;
    }

    public String getValueReduce(){
        String reduceFunction = cachedFunctions.get(ANALYSIS_VALUE_REDUCE);
        if ( reduceFunction == null){
            reduceFunction = load(properties.getProperty(ANALYSIS_VALUE_REDUCE));
            cachedFunctions.put(ANALYSIS_VALUE_REDUCE, reduceFunction);
        }
        return reduceFunction;
    }
}
