package edu.suda.ada.core;


import org.ethereum.util.ByteUtil;
import org.ethereum.vm.program.InternalTransaction;

/**
 * Created by LiYang on 2016/11/28.
 */
public class SimpleInternalTransaction extends SimpleBasicTransaction {
    private String parentHash;
    private int deep;
    private int index;
    private boolean rejected = false;
    private String note;

    public SimpleInternalTransaction(InternalTransaction internalTransaction) {
        setTransaction(internalTransaction);
        this.parentHash = ByteUtil.toHexString(internalTransaction.getParentHash());
        this.deep = internalTransaction.getDeep();
        this.index = internalTransaction.getIndex();
        this.rejected = internalTransaction.isRejected();
        this.note = internalTransaction.getNote();
    }

    public String getParentHash() {
        return parentHash;
    }

    public void setParentHash(String parentHash) {
        this.parentHash = parentHash;
    }

    public int getDeep() {
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
