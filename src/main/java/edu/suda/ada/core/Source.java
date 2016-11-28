package edu.suda.ada.core;

/**
 * Created by LiYang on 2016/11/23.
 */
public interface Source<K,V> {
    boolean exist(K key);
    void put(K key, V value);
    V get(K key);
    void delete(K key);
    long size();
}
