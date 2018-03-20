package ca.csf.mobile1.tp2.application;

import android.app.Application;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;

public class Tp2Application extends Application {

    private ObjectMapper objectMapper;
    private OkHttpClient okHttpClient;

    public ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }
}
