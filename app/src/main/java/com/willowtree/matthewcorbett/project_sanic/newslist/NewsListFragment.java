package com.willowtree.matthewcorbett.project_sanic.newslist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.willowtree.matthewcorbett.project_sanic.R;
import com.willowtree.matthewcorbett.project_sanic.api.ApiKeyInterceptor;
import com.willowtree.matthewcorbett.project_sanic.api.TimesApiService;
import com.willowtree.matthewcorbett.project_sanic.api.model.SearchResponse;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsListFragment extends Fragment {

    private RecyclerView newsList;
    private TimesApiService apiService;

    public static NewsListFragment newInstance() {
        return new NewsListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newsList = view.findViewById(R.id.news_list);

        //TODO: Set LayoutManager for newsList RecyclerView
        newsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        //Create our HTTP client
        OkHttpClient client = new OkHttpClient.Builder()
                //We're adding an interceptor that will add our API key to every call we make
                .addInterceptor(new ApiKeyInterceptor(TimesApiService.API_KEY))
                .build();

        //Create our Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TimesApiService.BASE_URL)
                .client(client)
                //This GSON Converter Factory will use GSON to convert our JSON responses to/from our data models
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(TimesApiService.class);

        //Make our search call without passing the API key since we're using the interceptor
        apiService.searchWithInterceptor().enqueue(searchCallback);
    }

    private Callback<SearchResponse> searchCallback = new Callback<SearchResponse>() {
        @Override
        public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
            //Check if our response was successful and has a response body; punch out otherwise
            if (!response.isSuccessful() || response.body() == null) {
                return;
            }
            //Get our response body and punch out if the response doesn't have any results
            SearchResponse searchResponse = response.body();
            if (searchResponse.getResults() == null) {
                return;
            }

            //Create our adapter, passing in our search results
            NewsListAdapter adapter = new NewsListAdapter(searchResponse.getResults());

            //Set the adapter to our RecyclerView
            newsList.setAdapter(adapter);
        }

        @Override
        public void onFailure(Call<SearchResponse> call, Throwable t) {
            String message = t.getLocalizedMessage() != null ? t.getLocalizedMessage() : "No message";
            Log.d("API Error", message);
        }
    };
}
