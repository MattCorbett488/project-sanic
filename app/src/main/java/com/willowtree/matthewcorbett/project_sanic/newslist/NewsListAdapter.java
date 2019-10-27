package com.willowtree.matthewcorbett.project_sanic.newslist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.willowtree.matthewcorbett.project_sanic.R;
import com.willowtree.matthewcorbett.project_sanic.database.NewsStory;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListViewHolder> {

    private List<NewsStory> newsStories;

    public NewsListAdapter(List<NewsStory> newsStories) {
        this.newsStories = newsStories;
    }

    @NonNull
    @Override
    public NewsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);

        //Create the ViewHolder and return it
        return new NewsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsListViewHolder holder, int position) {
        NewsStory newsStory = newsStories.get(position);
        holder.bind(newsStory);
    }

    @Override
    public int getItemCount() {
        if (newsStories != null) {
            return newsStories.size();
        } else {
            return 0;
        }
    }
}
