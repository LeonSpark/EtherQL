package edu.suda.ada.dao;

/**
 * Created by LiYang on 2016/11/22.
 */
public interface AccountTemplate {
    void delete(String address);
    void addBalance(String address, double value);
    void increaseNonce(String address);
    void createAccount(String address, double balance, boolean isContract, String code);
    void commit();
    void startTracking();
    boolean isEmpty();
}
