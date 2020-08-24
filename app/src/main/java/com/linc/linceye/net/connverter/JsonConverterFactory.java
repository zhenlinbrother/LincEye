package com.linc.linceye.net.connverter;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class JsonConverterFactory extends Converter.Factory {

    public static JsonConverterFactory create(){
        return new JsonConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new JsonResponseBody<JSONObject>();
    }
}
