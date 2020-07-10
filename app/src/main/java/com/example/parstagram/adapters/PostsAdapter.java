package com.example.parstagram.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parstagram.R;
import com.example.parstagram.databinding.ItemPostBinding;
import com.example.parstagram.models.Post;
import com.example.parstagram.ui.fragments.home.DetailsFragment;
import com.example.parstagram.ui.fragments.home.UserProfileFragment;
import com.example.parstagram.ui.fragments.profile.ProfileFragment;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private static final String TAG = PostsAdapter.class.getSimpleName();
    private static Activity context;
    private static Fragment fragment;
    private List<Post> posts;

    public PostsAdapter(Activity context, Fragment fragment, List<Post> posts) {
        PostsAdapter.context = context;
        PostsAdapter.fragment = fragment;
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: ");
        ItemPostBinding binding = ItemPostBinding.inflate(context.getLayoutInflater(), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: ");
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: ");
        return posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        ItemPostBinding binding;

        private ViewHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        private void bind(final Post post) {

            // Set up images and text
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
                Glide.with(context).load(image.getUrl()).into(binding.ivPostImage);
            }

            ParseFile profilePic = post.getUser().getParseFile(ProfileFragment.KEY_PROFILEPIC);
            Log.d(TAG, "bind: " + profilePic);
            if (profilePic != null) {
                Glide.with(context).
                        load(profilePic.getUrl()).
                        transform(new CircleCrop()).
                        into(binding.ivProfilePic);
            }


            // Set up OnClickListeners
            class userOnClickListener implements View.OnClickListener {
                @Override
                public void onClick(View view) {
                    FragmentManager fm = fragment.getParentFragmentManager();
                    UserProfileFragment userProfileFragment =
                            UserProfileFragment.newInstance(post.getUser());

                    FragmentTransaction fts = fm.beginTransaction();
                    // Replace the content of the container
                    fts.replace(R.id.nav_host_fragment,userProfileFragment);
                    // Append this transaction to the backstack
                    fts.addToBackStack(UserProfileFragment.class.getSimpleName());
                    // Commit the changes
                    fts.commit();
                }
            }

            binding.tvUsername1.setOnClickListener(new userOnClickListener());
            binding.ivProfilePic.setOnClickListener(new userOnClickListener());

            binding.ivPostImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fm = fragment.getChildFragmentManager();
                    DetailsFragment detailsFragment = DetailsFragment.newInstance(post);
                    detailsFragment.show(fm, DetailsFragment.class.getSimpleName());
                }
            });

            class likeOnClickListener implements View.OnClickListener {
                @Override
                public void onClick(View view) {
                    final boolean isActivated = !binding.ivLike.isActivated();

                    if (isActivated) {
                        likePost(post);
                    } else {
                        unlikePost(post);
                    }
                }
            }
            binding.ivLike.setOnClickListener(new likeOnClickListener());

            class savedOnClickListener implements View.OnClickListener {
                @Override
                public void onClick(View view) {
                    final boolean isActivated = !binding.ivSave.isActivated();
                    binding.ivSave.setActivated(isActivated);
                }
            }
            binding.ivSave.setOnClickListener(new savedOnClickListener());

        }

        private void likePost(Post post) {
            Log.d(TAG, "likePost: number before adding new like "
                    + post.getLikes());
            binding.ivLike.setActivated(true);
            post.addLike();
        }

        private void unlikePost(Post post) {
            Log.d(TAG, "likePost: number before removing like " + post.getLikes());
            binding.ivLike.setActivated(false);
            post.unlike();
        }
    }


}
