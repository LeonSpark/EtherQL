package edu.suda.ada.core;

import java.util.Map;

/**
 * Created by LiYang on 2016/11/23.
 */
public class KeyValueSource<Key,Value> implements Source<Key,Value>{

    protected Map<Key, Value> cache;

    public KeyValueSource(Map<Key,Value> withCache){
        this.cache = withCache;
    }

    @Override
    public boolean exist(Key key) {
        return cache.containsKey(key);
    }

    @Override
    public void put(Key key, Value value) {
        cache.put(key, value);
    }

    @Override
    public Value get(Key key) {
        return cache.get(key);
    }

    @Override
    public void delete(Key key) {
        cache.remove(key);
    }

    @Override
    public long size() {
        return cache.size();
    }

    public Map<Key,Value> getCache(){
        return cache;
    }
}
