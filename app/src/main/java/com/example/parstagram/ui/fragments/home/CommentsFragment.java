package com.example.parstagram.ui.fragments.home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.parstagram.databinding.FragmentCommentsBinding;
import com.example.parstagram.models.Post;
import com.parse.ParseUser;

public class CommentsFragment extends Fragment {

    private FragmentCommentsBinding binding;
    private Post post;

    public static CommentsFragment newInstance(Post post) {

        Bundle args = new Bundle();
        args.putParcelable(Post.class.getSimpleName(), post);

        CommentsFragment fragment = new CommentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentCommentsBinding.inflate(getLayoutInflater());

        binding.ivPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getChildFragmentManager();
                DetailsFragment detailsFragment = DetailsFragment.newInstance(post);
                detailsFragment.show(fm, DetailsFragment.class.getSimpleName());
            }
        });
    }
}
