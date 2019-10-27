package com.willowtree.matthewcorbett.project_sanic;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.willowtree.matthewcorbett.project_sanic.api.ApiMapper;
import com.willowtree.matthewcorbett.project_sanic.api.TimesApiService;
import com.willowtree.matthewcorbett.project_sanic.api.model.SearchResponse;
import com.willowtree.matthewcorbett.project_sanic.database.NewsStory;
import com.willowtree.matthewcorbett.project_sanic.database.NewsStoryDao;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class NewsRepository {
    private ApiMapper apiMapper;
    private TimesApiService newsApi;
    private NewsStoryDao newsStoryDao;

    public NewsRepository(TimesApiService newsApi, NewsStoryDao newsStoryDao) {
        this.newsApi = newsApi;
        this.newsStoryDao = newsStoryDao;
        apiMapper = new ApiMapper();
    }

    @Nullable
    @WorkerThread
    public List<NewsStory> fetchNewsStories() {
        List<NewsStory> stories = newsStoryDao.getAllStories();

        if (stories != null) {
            return stories;
        }

        try {
            Response<SearchResponse> response = newsApi.search(TimesApiService.API_KEY).execute();
            if (response.isSuccessful() && response.body() != null) {
                return apiMapper.mapStories(response.body());
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
