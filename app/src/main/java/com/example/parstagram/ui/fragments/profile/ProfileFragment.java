package com.example.parstagram.ui.fragments.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.parstagram.ui.login.LoginActivity;
import com.example.parstagram.databinding.FragmentProfileBinding;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private ProfileViewModel notificationsViewModel;
    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnLogin.setOnClickListener(new logoutOnClickListener());
    }

    private class logoutOnClickListener implements View.OnClickListener {
        public void onClick(View view) {
            Log.i(TAG, "onClick: submit button was clicked by user");

            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();

            Intent i = new Intent(ProfileFragment.this.getContext(), LoginActivity.class);
            getContext().startActivity(i);
        }
    }
}