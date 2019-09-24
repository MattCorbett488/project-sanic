package com.willowtree.matthewcorbett.project_sanic;

import android.os.Bundle;
<<<<<<< HEAD
=======
import android.widget.TextView;
>>>>>>> eace2ad0c8e77a1c5941582ca182b08505e96d31

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
<<<<<<< HEAD
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String USERNAME_CHECKED = "USERNAME_CHECKED";
=======

    private TextView usernameTextView;
    private TextView passwordTextView;
    private TextView usernameSavedTextView;
    private TextView passwordSavedTextView;
    private TextView locationSharedTextView;
>>>>>>> eace2ad0c8e77a1c5941582ca182b08505e96d31

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

<<<<<<< HEAD
        boolean isUserNameChecked = getIntent().getBooleanExtra(USERNAME_CHECKED, false);
        String username = getIntent().getStringExtra(USERNAME);
        String password = getIntent().getStringExtra(PASSWORD);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.second_container, SecondFragment.newInstance(isUserNameChecked, username, password))
                    .commit();
        }
=======
        usernameTextView = findViewById(R.id.username);
>>>>>>> eace2ad0c8e77a1c5941582ca182b08505e96d31
    }
}
