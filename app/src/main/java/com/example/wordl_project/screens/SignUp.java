package com.example.wordl_project.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wordl_project.R;
import com.example.wordl_project.models.User;
import com.example.wordl_project.services.DatabaseService;
import com.example.wordl_project.utils.SharedPreferencesUtil;
import com.example.wordl_project.utils.Validator;

/// Activity for registering the user
/// This activity is used to register the user
/// It contains fields for the user to enter their information
/// It also contains a button to register the user
/// When the user is registered, they are redirected to the main activity
public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";

    private EditText etEmail, etPassword, etLName,etUsername;
    private Button btnRegister;
    private Button tvSignUp;

    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        /// set the layout for the activity
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseService = DatabaseService.getInstance();

        /// get the views
        etEmail = findViewById(R.id.inputEmail);
        etPassword = findViewById(R.id.inputPassword);
        etUsername = findViewById(R.id.inputUsername);
        btnRegister = findViewById(R.id.btnRegister);

        /// set the click listener
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnRegister.getId()) {
            Log.d(TAG, "onClick: Register button clicked");

            /// get the input from the user
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String username = etUsername.getText().toString();

            /// log the input
            Log.d(TAG, "onClick: Email: " + email);
            Log.d(TAG, "onClick: Password: " + password);
            Log.d(TAG, "onClick: First Name: " + username);

            /// Validate input
            Log.d(TAG, "onClick: Validating input...");
            if (!checkInput(email, password, username)) {
                /// stop if input is invalid
                return;
            }

            Log.d(TAG, "onClick: Registering user...");

            /// Register user
            registerUser(email, password, username);
        } else if (v.getId() == tvSignUp.getId()) {
            /// Navigate back to Login Activity
            finish();
        }
    }

    /// Check if the input is valid
    /// @return true if the input is valid, false otherwise
    /// @see Validator
    private boolean checkInput(String email, String password, String username) {
        if (!Validator.isEmailValid(email)) {
            Log.e(TAG, "checkInput: Invalid email address");
            /// show error message to user
            etEmail.setError("Invalid email address");
            /// set focus to email field
            etEmail.requestFocus();
            return false;
        }

        if (!Validator.isPasswordValid(password)) {
            Log.e(TAG, "checkInput: Password must be at least 6 characters long");
            /// show error message to user
            etPassword.setError("Password must be at least 6 characters long");
            /// set focus to password field
            etPassword.requestFocus();
            return false;
        }

        if (!Validator.isNameValid(username)) {
            Log.e(TAG, "checkInput: First name must be at least 3 characters long");
            /// show error message to user
            etUsername.setError("First name must be at least 3 characters long");
            /// set focus to first name field
            etUsername.requestFocus();
            return false;
        }

        Log.d(TAG, "checkInput: Input is valid");
        return true;
    }

    /// Register the user
    private void registerUser(String email, String password, String userName) {
        Log.d(TAG, "registerUser: Registering user...");

        String uid = databaseService.generateUserId();

        /// create a new user object
        User user = new User(
                uid,
                userName,
                password,
                email,
                "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"
                , 0,
                0,
                0,
                0,
                false);

        databaseService.checkIfEmailExists(email, new DatabaseService.DatabaseCallback<>() {
            @Override
            public void onCompleted(Boolean exists) {
                if (exists) {
                    Log.e(TAG, "onCompleted: Email already exists");
                    /// show error message to user
                    Toast.makeText(SignUp.this, "Email already exists", Toast.LENGTH_SHORT).show();
                } else {
                    /// proceed to create the user
                    createUserInDatabase(user);
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to check if email exists", e);
                /// show error message to user
                Toast.makeText(SignUp.this, "Failed to register user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUserInDatabase(User user) {
        databaseService.createNewUser(user, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Log.d(TAG, "createUserInDatabase: User created successfully");
                /// save the user to shared preferences
                SharedPreferencesUtil.saveUser(SignUp.this, user);
                Log.d(TAG, "createUserInDatabase: Redirecting to MainActivity");
                /// Redirect to MainActivity and clear back stack to prevent user from going back to register screen
                Intent mainIntent = new Intent(SignUp.this, MainActivity.class);
                /// clear the back stack (clear history) and start the MainActivity
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "createUserInDatabase: Failed to create user", e);
                /// show error message to user
                Toast.makeText(SignUp.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                /// sign out the user if failed to register
                SharedPreferencesUtil.signOutUser(SignUp.this);
            }
        });
    }
}