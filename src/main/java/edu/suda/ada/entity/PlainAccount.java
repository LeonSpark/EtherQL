package edu.suda.ada.entity;

import org.ethereum.core.AccountState;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

import static org.ethereum.jsonrpc.TypeConverter.toJsonHex;

@Document(collection = "accounts")
public class PlainAccount {
    private String address;
    private double balance;
    private long nonce;
    private String stateRoot;
    private String codeHash;
    private boolean isEoA;

    public PlainAccount(AccountState state){
        setAccount(state);
    }

    public PlainAccount(){}

    public boolean isEoA() {
        return isEoA;
    }

    public void setEoA(boolean eoA) {
        isEoA = eoA;
    }

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

    public String getStateRoot() {
        return stateRoot;
    }

    public void setStateRoot(String stateRoot) {
        this.stateRoot = stateRoot;
    }

    public String getCodeHash() {
        return codeHash;
    }

    public void setCodeHash(String codeHash) {
        this.codeHash = codeHash;
    }

    public void setAccount(AccountState account){
        this.nonce = account.getNonce().longValue();
        this.balance = account.getBalance().doubleValue();
        this.codeHash = toJsonHex(account.getCodeHash());
        this.stateRoot = toJsonHex(account.getStateRoot());
    }

    @Override
    public String toString() {
        return "PlainAccount{" +
                "address='" + address + '\'' +
                ", balance=" + balance +
                ", nonce=" + nonce +
                ", stateRoot='" + stateRoot + '\'' +
                ", codeHash='" + codeHash + '\'' +
                ", isEoA=" + isEoA +
                '}';
    }
}
