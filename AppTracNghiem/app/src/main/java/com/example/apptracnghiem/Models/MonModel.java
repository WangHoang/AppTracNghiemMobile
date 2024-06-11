package com.example.apptracnghiem.Models;

public class MonModel {
    private String monName, monImage, key;
    int setNum;

    public MonModel(String monName, String monImage, String key, int setNum) {
        this.monName = monName;
        this.monImage = monImage;
        this.key = key;
        this.setNum = setNum;

    }

    public MonModel() {
    }


    public String getMonName() {
        return monName;
    }

    public void setMonName(String monName) {
        this.monName = monName;
    }

    public String getMonImage() {
        return monImage;
    }

    public void setMonImage(String monImage) {
        this.monImage = monImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSetNum() {
        return setNum;
    }

    public void setSetNum(int setNum) {
        this.setNum = setNum;
    }
}
