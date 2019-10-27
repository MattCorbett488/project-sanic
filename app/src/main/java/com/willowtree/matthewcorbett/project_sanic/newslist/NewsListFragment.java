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

import com.willowtree.matthewcorbett.project_sanic.NewsRepository;
import com.willowtree.matthewcorbett.project_sanic.R;
import com.willowtree.matthewcorbett.project_sanic.api.ApiKeyInterceptor;
import com.willowtree.matthewcorbett.project_sanic.api.TimesApiService;
import com.willowtree.matthewcorbett.project_sanic.database.NewsStory;
import com.willowtree.matthewcorbett.project_sanic.database.NewsStoryDao;
import com.willowtree.matthewcorbett.project_sanic.database.NewsStoryDatabase;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsListFragment extends Fragment {

    private Disposable fetchNewsDisposable;

    private RecyclerView newsList;
    private NewsRepository newsRepository;

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

        TimesApiService apiService = retrofit.create(TimesApiService.class);

        NewsStoryDao dao = NewsStoryDatabase.getDatabase(getContext().getApplicationContext()).newsStoryDao;
        newsRepository = new NewsRepository(apiService, dao);

        fetchNewsDisposable = fetchStories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<NewsStory>>() {
                    @Override
                    public void accept(List<NewsStory> newsStories) {
                        setUpNewsList(newsStories);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        onFetchNewsError(throwable);
                    }
                });
    }

    private void setUpNewsList(List<NewsStory> stories) {
        NewsListAdapter adapter = new NewsListAdapter(stories);
        newsList.setAdapter(adapter);
    }

    private void onFetchNewsError(Throwable t) {
        String message = t.getLocalizedMessage() != null ? t.getLocalizedMessage() : "No message";
        Log.e("FETCH_NEWS_FAILURE", message);
    }

    /*
    This is just a helper method to let us do network activity off the main thread.  It uses
    a library called RxJava 2 - you don't need to worry about it for now!
     */
    private Single<List<NewsStory>> fetchStories() {
        return Single.defer(new Callable<SingleSource<List<NewsStory>>>() {
            @Override
            public SingleSource<List<NewsStory>> call() {
                List<NewsStory> newsStories = newsRepository.fetchNewsStories();
                if (newsStories == null) {
                    return Single.error(new RuntimeException("No stories"));
                }
                return Single.just(newsStories);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fetchNewsDisposable != null && !fetchNewsDisposable.isDisposed()) {
            fetchNewsDisposable.dispose();
            fetchNewsDisposable = null;
        }
    }
}
