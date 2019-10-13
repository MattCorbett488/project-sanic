package com.willowtree.matthewcorbett.project_sanic.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {
    private String section;
    private String subsection;
    private String title;
    @SerializedName(value = "abstract")
    private String articleAbstract;

    private String url;
    @SerializedName(value = "short_url")
    private String shortUrl;

    private String byline;
    @SerializedName(value = "item_type")
    private String itemType;

    @SerializedName(value = "updated_date")
    private String updatedDate;
    @SerializedName(value = "created_date")
    private String createdDate;
    @SerializedName(value = "published_date")
    private String publishedDate;

    @SerializedName("material_type_facet")
    private String materialTypeFacet;
    private String kicker;

    @SerializedName(value = "des_facet")
    private List<String> desFacet;
    @SerializedName(value = "org_facet")
    private List<String> orgFacet;
    @SerializedName(value = "geo_facet")
    private List<String> geoFacet;

    public String getSection() {
        return section;
    }

    public String getSubsection() {
        return subsection;
    }

    public String getTitle() {
        return title;
    }

    public String getArticleAbstract() {
        return articleAbstract;
    }

    public String getUrl() {
        return url;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getByline() {
        return byline;
    }

    public String getItemType() {
        return itemType;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getMaterialTypeFacet() {
        return materialTypeFacet;
    }

    public String getKicker() {
        return kicker;
    }

    public List<String> getDesFacet() {
        return desFacet;
    }

    public List<String> getOrgFacet() {
        return orgFacet;
    }

    public List<String> getGeoFacet() {
        return geoFacet;
    }
}