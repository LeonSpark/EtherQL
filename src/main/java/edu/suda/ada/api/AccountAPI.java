package edu.suda.ada.api;

import edu.suda.ada.entity.PlainAccount;

import java.util.List;

/**
 * Created by LiYang on 2016/11/22.
 */
public interface AccountAPI {
    /**==================================================
     *        Ethereum supported api
     ====================================================*/
    PlainAccount getAccountByAddress(String address);
    /**==================================================
     *        Range Query api
     ====================================================*/
    List<PlainAccount> getAccountsByBalanceAsc(int offset, int limit);
    List<PlainAccount> getAccountsByBalanceAsc(int limit);
    List<PlainAccount> getAccountsByBalanceDesc(int offset, int limit);
    List<PlainAccount> getAccountsByBalanceDesc(int limit);
    List<PlainAccount> getAccountsWithBalanceBetween(double min, double max);
    List<PlainAccount> getAccountsWithBalancegt(double min);
    List<PlainAccount> getAccountsWithBalancelt(double max);
}