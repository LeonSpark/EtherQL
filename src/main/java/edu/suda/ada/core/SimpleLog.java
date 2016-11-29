package edu.suda.ada.core;

import org.ethereum.util.ByteUtil;
import org.ethereum.vm.DataWord;
import org.ethereum.vm.LogInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiYang on 2016/11/28.
 */
public class SimpleLog {
    private String address;
    private List<String> topics = new ArrayList<>();
    private String data;

    public SimpleLog(LogInfo logInfo) {
        this.address = ByteUtil.toHexString(logInfo.getAddress());
        this.data = ByteUtil.toHexString(logInfo.getData());
        logInfo.getTopics().forEach(this::addTopic);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void addTopic(DataWord topic){
        topics.add(topic.asString());
    }
}
