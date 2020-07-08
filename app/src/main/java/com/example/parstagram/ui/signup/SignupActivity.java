package com.example.parstagram.ui.signup;

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
import com.example.parstagram.databinding.ActivitySignupBinding;
import com.example.parstagram.ui.login.LoginActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = SignupActivity.class.getSimpleName();

    private SignupViewModel signupViewModel;
    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signupViewModel =
                ViewModelProviders.of(this).get(SignupViewModel.class);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnSignup.setOnClickListener(new SignupOnClickListener());

        signupViewModel.getUsername().observe(this, new textObserver(binding.etUsername));
        signupViewModel.getPassword().observe(this, new textObserver(binding.etPassword));
        // TODO: set up so that passwords must match
        signupViewModel.getPasswordAgain().observe(this, new textObserver(binding.etPassword2));
        signupViewModel.getEmail().observe(this, new textObserver(binding.etEmail));
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

    private class SignupOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: signup button clicked");

            String username = binding.etUsername.getText().toString();
            String password = binding.etPassword.getText().toString();
            String password2 = binding.etPassword2.getText().toString();
            String email = binding.etEmail.getText().toString();

            if (username.isEmpty()
                    || password.isEmpty()
                    || password2.isEmpty()) {
                Toast.makeText(SignupActivity.this,
                        R.string.toast_required_fields_err, Toast.LENGTH_SHORT).show();
            } else if (!password2.equals(password)) {
                Toast.makeText(SignupActivity.this,
                        R.string.toast_match_password, Toast.LENGTH_SHORT).show();
            } else {
                signupUser(username, password, email);
            }
        }
    }

    private void signupUser(final String username, String password, String email) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        if (!email.isEmpty()) {
            user.setEmail(email);
        }

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i(TAG, "done: Signup " + username + " success!");
                    Toast.makeText(SignupActivity.this,
                            R.string.toast_success, Toast.LENGTH_SHORT).show();
                    goActivity(MainActivity.class);
                } else {
                    Log.e(TAG, "done: Signup error", e);
                    Toast.makeText(SignupActivity.this,
                            R.string.toast_signup_err, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    // TODO: State whether user has wrong username/password or not
                    Log.e(TAG, "loginUser: issue with login", e);
                    Toast.makeText(SignupActivity.this,
                            R.string.toast_login_err, Toast.LENGTH_SHORT).show();
                    return;
                }
                goActivity(MainActivity.class);
                // TODO: improve with material design
                Toast.makeText(SignupActivity.this,
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
