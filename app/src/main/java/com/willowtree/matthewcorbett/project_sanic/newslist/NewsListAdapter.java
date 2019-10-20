package com.willowtree.matthewcorbett.project_sanic.newslist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.willowtree.matthewcorbett.project_sanic.R;
import com.willowtree.matthewcorbett.project_sanic.api.model.SearchResult;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListViewHolder> {

    private List<SearchResult> searchResults;

    public NewsListAdapter(List<SearchResult> searchResults) {
        this.searchResults = searchResults;
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
        SearchResult searchResult = searchResults.get(position);
        holder.bind(searchResult);
        OnLongCl
    }

    @Override
    public int getItemCount() {
        if (searchResults != null) {
            return searchResults.size();
        } else {
            return 0;
        }
    }
}
