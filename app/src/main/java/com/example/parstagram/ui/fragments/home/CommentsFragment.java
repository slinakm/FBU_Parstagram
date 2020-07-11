package com.example.parstagram.ui.fragments.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parstagram.R;
import com.example.parstagram.adapters.CommentsAdapter;
import com.example.parstagram.adapters.PostsAdapter;
import com.example.parstagram.databinding.FragmentCommentsBinding;
import com.example.parstagram.models.Comment;
import com.example.parstagram.models.Post;
import com.example.parstagram.ui.fragments.profile.ProfileFragment;
import com.example.parstagram.ui.fragments.profile.ProfileViewModel;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CommentsFragment extends Fragment {

    private static final String TAG = CommentsFragment.class.getSimpleName();
    private final List<Comment> comments = new ArrayList<>();

    private FragmentCommentsBinding binding;
    private Post post;

    private RecyclerView rvComments;
    private CommentsAdapter adapter;

    public static CommentsFragment newInstance(Post post) {

        Bundle args = new Bundle();
        args.putParcelable(Post.class.getSimpleName(), post);

        CommentsFragment fragment = new CommentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCommentsBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        post = (Post) getArguments().get(Post.class.getSimpleName());

        String username = post.getUser().getUsername();
        binding.tvUsername1.setText(username);
        binding.tvUsername2.setText(username);
        binding.tvRelTime.setText(post.getRelativeTime());

        if (post.isLiked()) {
            binding.ivLike.setActivated(true);
        } else {
            binding.ivLike.setActivated(false);
        }

        binding.tvDescription.setText(post.getDescription());

        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(getContext()).load(image.getUrl()).into(binding.ivPostImage);
        }

        ParseFile profilePic = post.getUser().getParseFile(ProfileFragment.KEY_PROFILEPIC);
        Log.d(TAG, "bind: " + profilePic);
        if (profilePic != null) {
            Glide.with(getContext()).
                    load(profilePic.getUrl()).
                    transform(new CircleCrop()).
                    into(binding.ivProfilePic);
        }

        adapter = new CommentsAdapter(getActivity(), this, comments);
        rvComments = binding.rvComments;
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));

        // Before setting up comments
        binding.rvComments.setVisibility(View.INVISIBLE);
        binding.tvNoComments.setVisibility(View.VISIBLE);


        queryComments();

        // Set OnClickListeners
        class imageOnClickListener implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getChildFragmentManager();
                DetailsFragment detailsFragment = DetailsFragment.newInstance(post);
                detailsFragment.show(fm, DetailsFragment.class.getSimpleName());
            }
        }
        binding.ivPostImage.setOnClickListener(new imageOnClickListener());

        class submitOnClickListener implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                final String content = binding.etComment.getText().toString();
                Comment comment = new Comment(post, ParseUser.getCurrentUser(), content);
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "done: error saving comment" + content, e);
                            Toast.makeText(getContext(), getString(R.string.error_saving_comment),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i(TAG, "done: success saving comment: " + content);
                            Toast.makeText(getContext(), getString(R.string.toast_save_succ),
                                    Toast.LENGTH_SHORT).show();
                            binding.etComment.setText("");

                            adapter.clear();
                            queryComments();
                        }
                    }
                });
            }
        }
        binding.btnSubmit.setOnClickListener(new submitOnClickListener());
    }

    private void queryComments() {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.orderByDescending(Comment.KEY_CREATEDAT);
        query.include(Comment.KEY_POST);
        query.whereEqualTo(Comment.KEY_POST, post);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> commentList, ParseException e) {

                if (e != null) {
                    Log.e(TAG, "queryComments: Issue getting comments", e);
                }

                for (Comment c : commentList) {
                    String name = "";
                    try {
                        name = c.getUser().fetchIfNeeded().getUsername();
                    } catch (ParseException ex) {
                        Log.v(TAG, ex.toString());
                        ex.printStackTrace();
                    }

                    Log.i(TAG, "Comment:" + c.getContent() + " user: "
                            + name);
                }

                Log.i(TAG, "done: number of comments = " + commentList.size());

                adapter.clear();
                adapter.addAll(commentList);

                if (comments.size() == 0) {
                    binding.rvComments.setVisibility(View.INVISIBLE);
                    binding.tvNoComments.setVisibility(View.VISIBLE);
                } else {
                    binding.rvComments.setVisibility(View.VISIBLE);
                    binding.tvNoComments.setVisibility(View.GONE);
                }
            }
        });
    }
}
