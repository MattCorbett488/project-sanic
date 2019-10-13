package com.willowtree.matthewcorbett.project_sanic.api;

//public class ApiKeyInterceptor implements Interceptor {
//    private String apiKey;
//
//    public ApiKeyInterceptor(String apiKey) {
//        this.apiKey = apiKey;
//    }
//
//    @NotNull
//    @Override
//    public Response intercept(@NotNull Chain chain) throws IOException {
//        HttpUrl url = chain.request().url()
//                .newBuilder()
//                .addQueryParameter("api-key", apiKey)
//                .build();
//
//        Request request = chain.request().newBuilder().url(url).build();
//        return chain.proceed(request);
//    }
//}
