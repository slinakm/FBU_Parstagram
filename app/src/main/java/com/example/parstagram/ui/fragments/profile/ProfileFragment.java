package com.example.parstagram.ui.fragments.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.parstagram.BitmapManipulation;
import com.example.parstagram.R;
import com.example.parstagram.ui.fragments.camera.CameraFragment;
import com.example.parstagram.ui.login.LoginActivity;
import com.example.parstagram.databinding.FragmentProfileBinding;
import com.parse.ParseUser;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private ProfileViewModel notificationsViewModel;
    private FragmentProfileBinding binding;
    private File photoFile;
    private final String photoFileName = "photo.jpg";


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
        ParseUser user = ParseUser.getCurrentUser();

        binding.btnLogout.setOnClickListener(new logoutOnClickListener());
//        Glide.with(this).load().into(binding.ivBigProfilePic);
        binding.tvUsername.setText(user.getUsername());
    }

    /**
     *   onClickListener and helper methods to launch camera
     * */

    private class cameraOnClickListener implements View.OnClickListener {
        public void onClick(View view) {
            Log.i(TAG, "onClick: camera button was clicked by user");
            photoFile =
                    BitmapManipulation.launchCamera(ProfileFragment.this.getActivity(), ProfileFragment.this,
                            photoFileName, TAG);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: here!");
        if (requestCode == BitmapManipulation.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d(TAG, "onActivityResult: picture taken");
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                Bitmap resizedBitmap = BitmapManipulation.scaleToFitWidth(takenImage,
                        (int) getResources().getDimension((R.dimen.resized_post_image)));

                photoFile =
                        BitmapManipulation.writeResizedBitmap(getContext(), photoFileName,
                                resizedBitmap, "resized", TAG);

                Glide.with(getContext()).
                        load(photoFile).
                        into(binding.ivBigProfilePic);
            } else {
                Toast.makeText(getContext(), getString(R.string.toast_camera_err),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     *   Logout button
     * */
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