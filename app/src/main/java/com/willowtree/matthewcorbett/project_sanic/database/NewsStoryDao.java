package com.willowtree.matthewcorbett.project_sanic.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsStoryDao {

    @Query("SELECT * FROM news_stories")
    List<NewsStory> getAllStories();

    @Query("SELECT * FROM news_stories WHERE title LIKE :title LIMIT 1")
    NewsStory findStoryByTitle(String title);

    @Insert
    void insertAllStories(List<NewsStory> stories);

    @Delete
    void deleteStory(NewsStory story);
}
