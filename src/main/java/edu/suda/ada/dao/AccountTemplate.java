package edu.suda.ada.dao;

/**
 * Created by LiYang on 2016/11/22.
 */
public interface AccountTemplate {
    boolean exist(String address);
    void delete(String address, boolean bulk);
    void addBalance(String address, double value, boolean bulk);
    void increaseNonce(String address, boolean bulk);
    void createAccount(String address, double balance, boolean isContract, String code);
    void commit();
    void startTracking();
}
