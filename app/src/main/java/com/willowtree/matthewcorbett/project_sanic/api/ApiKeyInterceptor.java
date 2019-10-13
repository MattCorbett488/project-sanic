package com.willowtree.matthewcorbett.project_sanic.api;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiKeyInterceptor implements Interceptor {
    private String apiKey;

    public ApiKeyInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        HttpUrl url = chain.request().url()
                .newBuilder()
                .addQueryParameter("api-key", apiKey)
                .build();

        Request request = chain.request().newBuilder().url(url).build();
        return chain.proceed(request);
    }
}
