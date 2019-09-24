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

public class LaunchActivity extends AppCompatActivity {
    public static final int USERNAME_MAX_LENGTH = 16;
    public static final int PASSWORD_MAX_LENGTH = 8;

    private TextInputLayout usernameInput;
    private TextInputLayout passwordInput;
    private RadioGroup fontSizeGroup;
    private CheckBox saveUsernameCheckbox;
    private CheckBox savePasswordCheckbox;
    private Switch shareLocationSwitch;
    private Button submitButton;

    private String preferredFontSize = "Small Font";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        /*
        Get references to all of our views
        We do this by calling findViewById(R.id.<id name from layout XML>)
         */
        usernameInput = findViewById(R.id.username_text_input);
        passwordInput = findViewById(R.id.password_text_input);

        fontSizeGroup = findViewById(R.id.font_size_group);

        saveUsernameCheckbox = findViewById(R.id.save_username_checkbox);
        savePasswordCheckbox = findViewById(R.id.save_password_checkbox);

        shareLocationSwitch = findViewById(R.id.share_location_switch);

        submitButton = findViewById(R.id.submit_button);

        //Add a TextWatcher for text input validation on our username entry
        if (usernameInput.getEditText() != null) {
            usernameInput.getEditText().addTextChangedListener(usernameTextWatcher);
        }
        //Add a TextWatcher for text input validation on our password entry
        if (passwordInput.getEditText() != null) {
            passwordInput.getEditText().addTextChangedListener(passwordTextWatcher);
        }

        fontSizeGroup.setOnCheckedChangeListener(fontSizeCheckListener);

        //Set our click listener for whenever our button is clicked
        submitButton.setOnClickListener(submitListener);
    }

    private RadioGroup.OnCheckedChangeListener fontSizeCheckListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            //Find which ID is now checked and update our preferredFontSize string accordingly
            switch (checkedId) {
                case R.id.small_font_radio_button:
                    preferredFontSize = "Small Font";
                    break;
                case R.id.medium_font_radio_button:
                    preferredFontSize = "Medium Font";
                    break;
                case R.id.large_font_radio_button:
                    preferredFontSize = "Large Font";
                    break;
            }
        }
    };

    //This TextWatcher will let us validate our username text input
    private TextWatcher usernameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //Don't care about this method
        }

        //The CharSequence s is the text that's been entered so far in our text entry
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //If our text entry is longer than we allow, then set the error
            //Otherwise, unset it (setting the error null will cause it to go away)
            if (s.toString().length() > USERNAME_MAX_LENGTH) {
                usernameInput.setError("Username too long");
            } else {
                usernameInput.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //Also don't care about this method
        }
    };

    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //Don't care about this method
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().length() > PASSWORD_MAX_LENGTH) {
                passwordInput.setError("Password too long");
            } else {
                passwordInput.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //Also don't care about this method
        }
    };

    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isSaveUsernameChecked = saveUsernameCheckbox.isChecked();
            boolean isSavePasswordChecked = savePasswordCheckbox.isChecked();

            boolean isShareLocationEnabled = shareLocationSwitch.isChecked();

            String username = "";
            String password = "";

            if (usernameInput.getEditText() != null) {
                username = usernameInput.getEditText().getText().toString();
            }

            if (passwordInput.getEditText() != null) {
                password = passwordInput.getEditText().getText().toString();
            }

            String output = String.format("Username: %s, Password: %s, Font Size: %s, Save Username: %s, Save Password: %s, Share Location: %s",
                    username, password, preferredFontSize,
                    String.valueOf(isSaveUsernameChecked), String.valueOf(isSavePasswordChecked),
                    String.valueOf(isShareLocationEnabled));

            Toast.makeText(LaunchActivity.this, output, Toast.LENGTH_SHORT).show();
        }
    };
}
