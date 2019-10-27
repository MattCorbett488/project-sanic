package com.willowtree.matthewcorbett.project_sanic.api;

import androidx.annotation.NonNull;

import com.willowtree.matthewcorbett.project_sanic.api.model.SearchResponse;
import com.willowtree.matthewcorbett.project_sanic.api.model.SearchResult;
import com.willowtree.matthewcorbett.project_sanic.database.NewsStory;

import java.util.ArrayList;
import java.util.List;

public class ApiMapper {

    public List<NewsStory> mapStories(@NonNull SearchResponse searchResponse) {
        if (searchResponse.getResults() == null) {
            return null;
        }
        List<NewsStory> stories = new ArrayList<>();

        for (SearchResult result : searchResponse.getResults()) {
            stories.add(mapStory(result));
        }
        return stories;
    }

    private NewsStory mapStory(SearchResult result) {
        NewsStory story = new NewsStory();

        story.setArticleAbstract(result.getArticleAbstract());
        story.setByline(result.getByline());
        story.setPublishedDate(result.getPublishedDate());
        story.setSection(result.getSection());
        story.setSubsection(result.getSubsection());

        story.setTitle(result.getTitle());

        return story;
    }
}
