package com.willowtree.matthewcorbett.project_sanic;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private TextView usernameTextView;
    private TextView passwordTextView;
    private TextView usernameSavedTextView;
    private TextView passwordSavedTextView;
    private TextView locationSharedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        usernameTextView = findViewById(R.id.username);
    }
}
