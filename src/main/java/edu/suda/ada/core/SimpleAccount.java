package edu.suda.ada.core;

import org.ethereum.core.AccountState;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

import static org.ethereum.jsonrpc.TypeConverter.toJsonHex;

@Document(collection = "accounts")
public class SimpleAccount {
    private String address;
    private double balance;
    private long nonce;
    private boolean isContract;
    private String code;

    public boolean isContract() {
        return isContract;
    }

    public void setContract(boolean contract) {
        isContract = contract;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public SimpleAccount(AccountState state){
        setAccount(state);
    }

    public SimpleAccount(String address, double balance, boolean isContract, String code){
        this.address = address;
        this.balance = balance;
        this.isContract = isContract;
        this.code = code;
        this.nonce = 0;
    }

    public SimpleAccount(){}


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public void setAccount(AccountState account){
        this.nonce = account.getNonce().longValue();
        this.balance = account.getBalance().doubleValue();

    }

    @Override
    public String toString() {
        return "SimpleAccount{" +
                "address='" + address + '\'' +
                ", balance=" + balance +
                ", nonce=" + nonce +
                ", isContract=" + isContract +
                ", code='" + code + '\'' +
                '}';
    }
}
