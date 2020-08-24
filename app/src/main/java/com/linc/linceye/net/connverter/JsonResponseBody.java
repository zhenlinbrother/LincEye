package com.linc.linceye.net.connverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public final class JsonResponseBody<T> implements Converter<ResponseBody, T> {

    @Override
    public T convert(ResponseBody value) throws IOException {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(value.string());
            return (T)jsonObject;
        } catch (JSONException e){
            return null;
        }

    }
}
