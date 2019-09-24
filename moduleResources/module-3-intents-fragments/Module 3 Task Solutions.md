### Intents

To build our Intent in our Launch/Main Activity, we can add the following OnClickListener:

```java
private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //We're getting the status of the checkboxes
            boolean isSaveUsernameChecked = saveUsernameCheckbox.isChecked();
            boolean isSavePasswordChecked = savePasswordCheckbox.isChecked();

            boolean isShareLocationEnabled = shareLocationSwitch.isChecked();

            //Getting the username and password from our text entry fields
            String username = "";
            String password = "";

            if (usernameInput.getEditText() != null) {
                username = usernameInput.getEditText().getText().toString();
            }

            if (passwordInput.getEditText() != null) {
                password = passwordInput.getEditText().getText().toString();
            }

            //Build the Intent
            Intent intent = new Intent(LaunchActivity.this, SecondActivity.class);
            
            //Add all the extra data - you may need to define these keys
            intent.putExtra(USERNAME_KEY, username);
            intent.putExtra(PASSWORD_KEY, password);
            intent.putExtra(SAVE_USERNAME_KEY, isSaveUsernameChecked);
            intent.putExtra(SAVE_PASSSORD_KEY, isSavePasswordChecked);

            //Start our new activity
            startActivity(intent);
        }
    };
```


### Fragments Part 1

To replace our LaunchActivity (or whatever you named the landing Activity), let's give the Activity a layout with a fragment container. We can just copy the one from `ExampleActivity`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        xmlns:app="http://schemas.android.com/apk/res-auto">

    <!--
This FrameLayout (another layout type specifically designed to just hold one item inside)
will be our container for our fragments
-->
    <FrameLayout
            android:id="@+id/fragment_container"
            android:layout_width="0dp"
            android:layout_height="0dp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

</android.support.constraint.ConstraintLayout>
```

And our Activity will look something like this:

```java
public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launch);

        //Get our fragment manager so that we can add/remove/replace Fragments
        getSupportFragmentManager()
                //Start our Fragment Transaction for adding a new Fragment
                .beginTransaction()
                //Add a Fragment telling it the container ID and the Fragment to use
                .add(R.id.fragment_container, LaunchFragment.newInstance())
                //Add this Fragment to our back stack so that, if we add ANOTHER fragment on top of it,
                //hitting the back button will go back to this one
                .addToBackStack(/* name */ null)
                //Commit our changes for executing this Fragment transaction
                .commit();
    }
}
```

### Fragments Part 2

To navigate from our `LaunchFragment` to our `ForgotPasswordFragment`, let's either add a new OnClickListener or replace our current one.

```java
    private View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    //Replace the current Fragment with our ForgotPasswordFragment
                    .replace(R.id.fragment_container, ForgotPasswordFragment.newInstance())
                    //Add this Fragment to our back stack so that, if we add ANOTHER fragment on top of it,
                    //hitting the back button will go back to this one
                    .addToBackStack(/* name */ null)
                    //Commit our changes for executing this Fragment transaction
                    .commit();
        }
    };
```

If we make a new OnClickListener, then we need to make sure we set that listener on our button.

For our `ForgotPasswordFragment`, a completed layout might look like this:

```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    <!-- TODO: Turn hardcoded text into string resources-->
    <!-- TODO: Extract margins to dimens rather than hardcoded dp-->

    <TextView
            android:id="@+id/forgot_password_title"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="32dp"
            android:text="Forgot Password"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

    <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/new_password_text_input"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="16dp"
            android:layout_marginTop="16dp"
            android:layout_marginEnd="16dp"
            android:hint="New Password"
            app:hintEnabled="true"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/forgot_password_title"
            app:passwordToggleEnabled="true">

        <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:inputType="textPassword" />
    </com.google.android.material.textfield.TextInputLayout>

    <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/confirm_password_text_input"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="16dp"
            android:layout_marginTop="16dp"
            android:layout_marginEnd="16dp"
            android:hint="Confirm Password"
            app:hintEnabled="true"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/new_password_text_input"
            app:passwordToggleEnabled="true">

        <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:inputType="textPassword" />

    </com.google.android.material.textfield.TextInputLayout>

    <Button
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="16dp"
            android:layout_marginEnd="16dp"
            android:layout_marginTop="32dp"
            android:text="Update Password"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/confirm_password_text_input" />
</android.support.constraint.ConstraintLayout>
```

and, in the Fragment code:

```java
public class ForgotPasswordFragment extends Fragment {

    private TextInputLayout newPasswordInputLayout;
    private TextInputLayout confirmPasswordInputLayout;
    private Button updatePasswordButton;
    
    
    public static ForgotPasswordFragment newInstance() {
        return new ForgotPasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        newPasswordInputLayout = view.findViewById(R.id.new_password_text_input);
        confirmPasswordInputLayout = view.findViewById(R.id.confirm_password_text_input);
        updatePasswordButton = view.findViewById(R.id.update_password_button);
        
        updatePasswordButton.setOnClickListener(updateListener);
    }
    
    private View.OnClickListener updateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String newPassword = "", confirmPassword = "";

            if (newPasswordInputLayout != null) {
                newPassword = newPasswordInputLayout.getEditText().getText().toString();
            }
            if (confirmPasswordInputLayout != null) {
                confirmPassword = confirmPasswordInputLayout.getEditText().getText().toString();
            }
            
            Toast.makeText(getContext(), 
                    "New Password: " + newPassword + ", Confirm Password: " + confirmPassword, 
                    Toast.LENGTH_SHORT).show();
        }
    };
}
```