package com.example.parstagram.ui.fragments.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.parstagram.BitmapManipulation;
import com.example.parstagram.EndlessRecyclerViewScrollListener;
import com.example.parstagram.adapters.PostsAdapter;
import com.example.parstagram.models.Post;
import com.example.parstagram.databinding.FragmentHomeBinding;
import com.example.parstagram.ui.fragments.camera.CameraFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final int MAXIMUM_POSTS = 20;

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private RecyclerView rvPosts;
    private PostsAdapter adapter;
    private final List<Post> allPosts = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = binding.rvPosts;
        adapter = new PostsAdapter(getActivity(), this, allPosts);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(layoutManager);
        rvPosts.setOnScrollListener(new postsOnScrollListener(layoutManager));

        binding.swipeContainer.setOnRefreshListener(new
                swipeRefreshListener(binding.swipeContainer));
        queryPosts();
    }

    private class postsOnScrollListener extends EndlessRecyclerViewScrollListener {
        public postsOnScrollListener(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            adapter.clear();
            queryPosts();
        }
    }

    private class swipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        SwipeRefreshLayout swipeContainer;

        private swipeRefreshListener(SwipeRefreshLayout swipeContainer) {
            this.swipeContainer = swipeContainer;
        }
        @Override
        public void onRefresh() {
            queryPosts();
            swipeContainer.setRefreshing(false);
        }
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.orderByDescending(Post.KEY_CREATEDAT);
        query.include(Post.KEY_USER);
        query.setLimit(MAXIMUM_POSTS);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "queryPosts: Issue getting posts", e);
                }

                for (Post p: posts) {
                    Log.i(TAG, "Post:" + p.getDescription()
                            + ", User: " + p.getUser().getUsername());
                }

                Log.i(TAG, "done: number of posts = " + posts.size());

                adapter.clear();
                adapter.addAll(posts);
            }
        });

    }
}