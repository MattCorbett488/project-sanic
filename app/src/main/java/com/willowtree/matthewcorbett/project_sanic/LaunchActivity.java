package com.willowtree.matthewcorbett.project_sanic;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.willowtree.matthewcorbett.project_sanic.newslist.NewsListFragment;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, NewsListFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}
