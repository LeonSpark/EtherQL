package edu.suda.ada.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by LiYang on 2016/11/22.
 */
public interface AccountTemplate {
    void delete(List<String> toDeletes);
    void addBalance(String address, double value);
    void increaseNonce(String address);
    void createAccount(String address, double balance, boolean isContract, String code);
    int commit(Map<String, Double> changes);
}
