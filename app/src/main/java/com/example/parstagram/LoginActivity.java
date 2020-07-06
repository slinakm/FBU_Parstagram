package com.example.parstagram;

import android.content.ContentProviderClient;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parstagram.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    static final String TAG = "LoginActivity";
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        // TODO: improve login page looks and add Instagram logo to Toolbar
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: ");

                loginUser(binding.etUsername.getText().toString(),
                        binding.etPassword.getText().toString());
            }
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "loginUser: " + username);

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    // TODO: State whether user has wrong username/password or not
                    Log.e(TAG, "loginUser: issue with login", e);
                    Toast.makeText(LoginActivity.this,
                            "Issue with login", Toast.LENGTH_SHORT).show();
                    return;
                }
                goMainActivity();
                // TODO: improve with material design
                Toast.makeText(LoginActivity.this,
                        "Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
