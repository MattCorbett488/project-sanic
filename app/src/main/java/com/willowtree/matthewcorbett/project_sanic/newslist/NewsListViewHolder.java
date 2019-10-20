package com.willowtree.matthewcorbett.project_sanic.newslist;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.willowtree.matthewcorbett.project_sanic.R;
import com.willowtree.matthewcorbett.project_sanic.api.model.SearchResponse;
import com.willowtree.matthewcorbett.project_sanic.api.model.SearchResult;

public class NewsListViewHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView author;
    private TextView section;
    private TextView subsection;
    private TextView articleAbstract;

    public NewsListViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        author = itemView.findViewById(R.id.author);
        section = itemView.findViewById(R.id.section);
        subsection = itemView.findViewById(R.id.subsection);
        articleAbstract = itemView.findViewById(R.id.article_abstract);
    }

    public void bind(SearchResult searchResult) {
        title.setText(searchResult.getTitle());
        author.setText(searchResult.getByline());
        section.setText(searchResult.getSection());
        subsection.setText(searchResult.getSubsection());
        articleAbstract.setText(searchResult.getArticleAbstract());
    }
}
