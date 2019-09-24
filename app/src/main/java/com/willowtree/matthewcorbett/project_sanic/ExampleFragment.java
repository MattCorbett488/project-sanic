package com.willowtree.matthewcorbett.project_sanic;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ExampleFragment extends Fragment {
    private static final String SHOW_USERNAME = "SHOW_USERNAME";

    private boolean shouldShowUsername;

    public static ExampleFragment newInstance(boolean shouldShowUsername) {
        //Create our new Fragment
        ExampleFragment exampleFragment = new ExampleFragment();

        //Create a new Bundle for our arguments
        Bundle arguments = new Bundle();

        //Put our argument in there using our SHOW_USERNAME key with the argument shouldShowUsername
        //for the value
        arguments.putBoolean(SHOW_USERNAME, shouldShowUsername);

        //Set the Bundle to be our Fragment's arguments
        exampleFragment.setArguments(arguments);

        return exampleFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_example, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //If we have arguments, let's do some stuff with them
        if (getArguments() != null) {
            /*Set our shouldShowUsername variable to whatever our argument is
            The first parameter to getBoolean is the key and the second parameter is a
            default value in case there is no argument with that key
             */
            shouldShowUsername = getArguments().getBoolean(SHOW_USERNAME, false);
        }
    }
}
