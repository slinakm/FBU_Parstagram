package com.example.parstagram.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parstagram.databinding.ItemPostBinding;
import com.example.parstagram.databinding.ItemProfilePostBinding;
import com.example.parstagram.models.Post;
import com.example.parstagram.ui.fragments.home.DetailsFragment;
import com.example.parstagram.ui.fragments.profile.ProfileFragment;
import com.parse.ParseFile;

import java.util.List;

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsAdapter.ViewHolder> {

    private static final String TAG = PostsAdapter.class.getSimpleName();
    private static Activity context;
    private static Fragment fragment;
    private List<Post> posts;

    public ProfilePostsAdapter(Activity context, Fragment fragment, List<Post> posts) {
        ProfilePostsAdapter.context = context;
        ProfilePostsAdapter.fragment = fragment;
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: ");
        ItemProfilePostBinding binding = ItemProfilePostBinding.inflate(context.getLayoutInflater(), parent, false);
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
        ItemProfilePostBinding binding;

        private ViewHolder(@NonNull ItemProfilePostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        private void bind(final Post post) {

            binding.ivPostImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fm = fragment.getChildFragmentManager();
                    DetailsFragment editNameDialogFragment = DetailsFragment.newInstance(post);
                    editNameDialogFragment.show(fm, "fragment_edit_name");
                }
            });


            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(binding.ivPostImage);
            }

            ParseFile profilePic = post.getUser().getParseFile(ProfileFragment.KEY_PROFILEPIC);
        }
    }
}

