package com.deepspring.blueprint.utils;


import com.deepspring.blueprint.bean.NewsList;
import com.google.gson.Gson;

public class GsonUtil {
    public static NewsList parseJsonWithGson(final String requestText){
        Gson gson = new Gson();
        return gson.fromJson(requestText, NewsList.class);
    }
}
