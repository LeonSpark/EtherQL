package edu.suda.ada.entity;

import java.util.Map;

public class Contract {
    private String address;
    private String code;
    private Map<String, String> data;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public void updateData(String key, String value) {
        data.put(key, value);
    }
}
