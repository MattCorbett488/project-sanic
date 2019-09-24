package com.willowtree.matthewcorbett.project_sanic;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ExampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_example);

        //A boolean representing whether to show a username
        boolean shouldShowUsername = false;

        //Get our fragment manager so that we can add/remove/replace Fragments
        getSupportFragmentManager()
                //Start our Fragment Transaction for adding a new Fragment
                .beginTransaction()
                //Add a Fragment telling it the container ID and the Fragment to use
                .add(R.id.fragment_container, ExampleFragment.newInstance(shouldShowUsername))
                //Add this Fragment to our back stack so that, if we add ANOTHER fragment on top of it,
                //hitting the back button will go back to this one
                .addToBackStack(/* name */ null)
                //Commit our changes for executing this Fragment transaction
                .commit();
    }
}
