package com.willowtree.matthewcorbett.project_sanic.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NewsStory.class}, version = 1)
public abstract class NewsStoryDatabase extends RoomDatabase {
    private static volatile NewsStoryDatabase INSTANCE;

    public NewsStoryDao newsStoryDao;

    public static NewsStoryDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context,
                    NewsStoryDatabase.class,
                    "news_story_database")
                    .build();
        }
        return INSTANCE;
    }
}
