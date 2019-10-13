package com.willowtree.matthewcorbett.project_sanic;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.willowtree.matthewcorbett.project_sanic.api.TimesApiService;


public class NewsFragment extends Fragment {

    //TODO: Create the TimesApiService using Retrofit
    private TimesApiService apiService;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO: Flesh out this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO: Create Retrofit using Retrofit.Builder()

        //TODO: Initialize TimesApiService here
    }
}
