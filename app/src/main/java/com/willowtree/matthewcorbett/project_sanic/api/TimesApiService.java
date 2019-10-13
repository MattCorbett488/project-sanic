package com.willowtree.matthewcorbett.project_sanic.api;

import androidx.annotation.NonNull;

import com.willowtree.matthewcorbett.project_sanic.api.model.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TimesApiService {
    String BASE_URL = "https://api.nytimes.com/svc/topstories/v2/";
    //TODO: Fill this out
    String API_KEY = "YOUR_KEY_HERE";

    @GET("home.json")
    Call<SearchResponse> search(@Query("api-key") String apiKey);

    @GET("{section}.json")
    Call<SearchResponse> searchSection(@Query("api-key") String apiKey, @NonNull @Path("section") String section);

    /*
    These two methods assume that the ApiKeyInterceptor is hooked up to our OkHttpClient
     */
    @GET("home.json")
    Call<SearchResponse> searchWithInterceptor();

    @GET("{section}.json")
    Call<SearchResponse> searchWithInterceptor(@NonNull @Path("section") String section);
}
