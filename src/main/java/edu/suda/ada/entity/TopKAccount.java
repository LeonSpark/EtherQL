package edu.suda.ada.entity;

/**
 * Created by LiYang on 2016/11/11.
 */
public class TopKAccount {
    private String _id;
    private double value;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TopKAccount{" +
                "_id='" + _id + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
