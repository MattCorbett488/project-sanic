package com.willowtree.matthewcorbett.project_sanic;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.willowtree.matthewcorbett.project_sanic.api.ApiKeyInterceptor;
import com.willowtree.matthewcorbett.project_sanic.api.TimesApiService;
import com.willowtree.matthewcorbett.project_sanic.api.model.SearchResponse;
import com.willowtree.matthewcorbett.project_sanic.api.model.SearchResult;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NewsFragment extends Fragment {

    private TimesApiService apiService;

    private TextView title;
    private TextView author;
    private TextView section;
    private TextView subsection;
    private TextView articleAbstract;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        //Bind our views
        title = view.findViewById(R.id.title);
        author = view.findViewById(R.id.author);
        section = view.findViewById(R.id.section);
        subsection = view.findViewById(R.id.subsection);
        articleAbstract = view.findViewById(R.id.article_abstract);

        //Make our search call and pass the API key
        //apiService.search(TimesApiService.API_KEY).enqueue(searchCallback);

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

            //Get the first item from our results
            SearchResult firstResult = searchResponse.getResults().get(0);

            //Set all our text fields based on the search response data
            title.setText(firstResult.getTitle());
            author.setText(firstResult.getByline());
            section.setText(firstResult.getSection());
            subsection.setText(firstResult.getSubsection());
            articleAbstract.setText(firstResult.getArticleAbstract());

        }

        @Override
        public void onFailure(Call<SearchResponse> call, Throwable t) {
            String message = t.getLocalizedMessage() != null ? t.getLocalizedMessage() : "No message";
            Log.d("API Error", message);
        }
    };
}
