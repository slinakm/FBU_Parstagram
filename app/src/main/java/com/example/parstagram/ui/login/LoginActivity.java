package com.example.parstagram.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.parstagram.MainActivity;
import com.example.parstagram.R;
import com.example.parstagram.databinding.ActivityLoginBinding;
import com.example.parstagram.ui.signup.SignupActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private ActivityLoginBinding binding;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ParseUser.getCurrentUser() != null) {
            goActivity(MainActivity.class);
        }

        loginViewModel =
        ViewModelProviders.of(this).get(LoginViewModel.class);

        // TODO: improve login page looks and add Instagram logo to Toolbar

        loginViewModel.getUsername().observe(this, new LoginActivity.textObserver(binding.etUsername));
        loginViewModel.getPassword().observe(this, new LoginActivity.textObserver(binding.etPassword));

        binding.btnLogin.setOnClickListener(new loginOnClickListener());
        binding.btnSignup.setOnClickListener(new signupOnClickListener());
    }

    private class textObserver implements Observer<String> {
        TextView tv;

        private textObserver(TextView tv) {
            this.tv = tv;
        }

        @Override
        public void onChanged(@Nullable String s) {
            tv.setText(s);
        }
    }

    private class signupOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "signupOnClickListener onClick: ");
            goActivity(SignupActivity.class);
        }
    }

    private class loginOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "loginOnClickListener onClick: ");

            loginUser(binding.etUsername.getText().toString(),
                    binding.etPassword.getText().toString());
        }
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
                            R.string.toast_login_err, Toast.LENGTH_SHORT).show();
                    return;
                }
                goActivity(MainActivity.class);
                // TODO: improve with material design
                Toast.makeText(LoginActivity.this,
                        R.string.toast_login_succ, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goActivity(Class c) {
        Intent i = new Intent(this, c);
        startActivity(i);
        finish();
    }

}
