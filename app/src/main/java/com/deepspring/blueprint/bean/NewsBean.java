package com.deepspring.blueprint.bean;


import com.google.gson.annotations.SerializedName;

public class NewsBean {
    @SerializedName("ctime")
    public String time;

    public String title;

    public String description;

    public String picUrl;

    public String url;
}
