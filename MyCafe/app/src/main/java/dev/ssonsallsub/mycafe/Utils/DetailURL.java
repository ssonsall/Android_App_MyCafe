package dev.ssonsallsub.mycafe.utils;

import java.util.ArrayList;

public class DetailURL {
    private ArrayList<String> urlList;

    private DetailURL(){
        urlList = new ArrayList<>();
    }
    private static DetailURL instance = new DetailURL();
    public static DetailURL getInstance(){
        return instance;
    }

    public ArrayList<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(ArrayList<String> urlList) {
        this.urlList = urlList;
    }
}
