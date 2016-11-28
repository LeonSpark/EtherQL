package edu.suda.ada.api;

import edu.suda.ada.core.SimpleAccount;

import java.util.List;

/**
 * Created by LiYang on 2016/11/22.
 */
public interface AccountAPI {
    /**==================================================
     *        Ethereum supported api
     ====================================================*/
    SimpleAccount getAccountByAddress(String address);
    /**==================================================
     *        Range Query api
     ====================================================*/
    List<SimpleAccount> getAccountsByBalanceAsc(int offset, int limit);
    List<SimpleAccount> getAccountsByBalanceAsc(int limit);
    List<SimpleAccount> getAccountsByBalanceDesc(int offset, int limit);
    List<SimpleAccount> getAccountsByBalanceDesc(int limit);
    List<SimpleAccount> getAccountsWithBalanceBetween(double min, double max);
    List<SimpleAccount> getAccountsWithBalancegt(double min);
    List<SimpleAccount> getAccountsWithBalancelt(double max);
}