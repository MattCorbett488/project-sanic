package com.willowtree.matthewcorbett.project_sanic.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
A model representation of our NYT Search
The results field contains the list of articles
 */
public class SearchResponse {

    private String status;
    private String copyright;
    private String section;
    @SerializedName(value = "last_updated")
    private String lastUpdated;
    @SerializedName(value = "num_results")
    private int numResults;
    private List<SearchResult> results;

    public String getStatus() {
        return status;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getSection() {
        return section;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public int getNumResults() {
        return numResults;
    }

    public List<SearchResult> getResults() {
        return results;
    }
}
