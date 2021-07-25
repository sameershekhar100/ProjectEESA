package com.example.projecteesa.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.projecteesa.Adapters.PostAdapter;
import com.example.projecteesa.Adapters.PostItemClicked;
import com.example.projecteesa.Posts.CommentActivity;
import com.example.projecteesa.Posts.NumLikesActivity;
import com.example.projecteesa.Posts.Post;
import com.example.projecteesa.ProfileSection.UserProfileActivity;
import com.example.projecteesa.R;
import com.example.projecteesa.utils.Constants;
import com.example.projecteesa.utils.MotionToastUtils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment implements PostItemClicked {
    static ArrayList<Post> postList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    FirebaseFirestore firestore;
    CollectionReference postRefrence;
    PostAdapter postAdapter;
    DocumentReference userRefrence;
    private ShimmerFrameLayout shimmerLayout;
    private FirebaseRemoteConfig remoteConfig;
    private SwipeRefreshLayout refreshLayout;
    private static final String HOME_TAG="HomeFragment";
    //For pagination
    static DocumentSnapshot lastSnapshot;
    ProgressBar progressBar;
    NestedScrollView nestedScrollView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        remoteConfig = FirebaseRemoteConfig.getInstance();
        Log.i(HOME_TAG,"OnCreate");
        setup(view);
        if(postList.size()==0) {
            fetchPostsData();
        }
        else {
            postAdapter=new PostAdapter(postList,getContext(),HomeFragment.this);
            recyclerView.setAdapter(postAdapter);
            shimmerLayout.setVisibility(View.GONE);
        }
        setupPagination();
        return view;
    }

    private void setupPagination() {
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight())
                {
                    if(lastSnapshot!=null) {
                        fetchPosts();
                    }
                    Log.i(HOME_TAG,"last snapshot");
                }
            }
        });
    }

    private void fetchPostsData() {
        shimmerLayout.startShimmerAnimation();
        remoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(10)
                .build());

        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put(Constants.FETCH_POSTS_COUNT_KEY, 5);
        remoteConfig.setDefaultsAsync(defaults);
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            lastSnapshot=null;
                            postList=new ArrayList<>();
                            fetchPosts();
                        }
                    }
                });
    }

    private void setup(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        firestore = FirebaseFirestore.getInstance();
        postRefrence = firestore.collection("AllPost");
        String uid = FirebaseAuth.getInstance().getUid();
        userRefrence = firestore.collection("Users").document(uid);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        refreshLayout = view.findViewById(R.id.swipe_refresh);

        refreshLayout.setOnRefreshListener(this::fetchPostsData);
        //For pagination
        nestedScrollView=view.findViewById(R.id.nested_scroll_home);
        progressBar=view.findViewById(R.id.progressbar_home);
        progressBar.setVisibility(View.GONE);
        Log.i(HOME_TAG,"Setup view");
    }

    private void fetchPosts() {
        if(lastSnapshot==null)
        {
            Log.i(HOME_TAG,"first one");
            postRefrence.limit(remoteConfig.getLong(Constants.FETCH_POSTS_COUNT_KEY)).orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    shimmerLayout.startShimmerAnimation();
                    shimmerLayout.setVisibility(View.GONE);
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Post post = documentSnapshot.toObject(Post.class);
                        postList.add(post);
                    }
                    int lastSize=queryDocumentSnapshots.size()-1;
                    if(lastSize>0) {
                        lastSnapshot = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    }
                    else
                    {
                        lastSnapshot=null;
                    }
                    postAdapter = new PostAdapter(postList, getActivity(), HomeFragment.this);
                    recyclerView.setAdapter(postAdapter);
                    refreshLayout.setRefreshing(false);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    MotionToastUtils.showErrorToast(getContext(), "Error fetching posts", "Some error occured while fetching posts, try after some time.");
                    refreshLayout.setRefreshing(false);
                }
            });
        }
        else
        {
            Log.i(HOME_TAG,"Next Fetch");
            progressBar.setVisibility(View.VISIBLE);
            postRefrence.limit(remoteConfig.getLong(Constants.FETCH_POSTS_COUNT_KEY))
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(lastSnapshot).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    shimmerLayout.startShimmerAnimation();
                    shimmerLayout.setVisibility(View.GONE);
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Post post = documentSnapshot.toObject(Post.class);
                        postList.add(post);
                    }
                    int lastSize=queryDocumentSnapshots.size()-1;
                    if(lastSize>0) {
                        lastSnapshot = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    }
                    else
                    {
                        lastSnapshot=null;
                    }
                    postAdapter.setData(postList);
                    progressBar.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    MotionToastUtils.showErrorToast(getContext(), "Error fetching posts", "Some error occured while fetching posts, try after some time.");
                    refreshLayout.setRefreshing(false);
                }
            });
        }
    }

    @Override
    public void onLikeClicked(ArrayList<String> likes, String postID) {
        postRefrence.document(postID).update("likes", likes).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("Like updated", "!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i("Failed: ", "to update likes");
            }
        });
    }

    @Override
    public void onBookmarkClicked(ArrayList<String> savedPosts, String uid) {
        userRefrence.update("savedPost", savedPosts).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("SavedPost", "post added!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i("SavedPost", "Failed!");
            }
        });
    }

    @Override
    public void onOwnerProfileClicked(String uid) {
        Intent intent = new Intent(getContext(), UserProfileActivity.class);
        intent.putExtra(Constants.USER_UID_KEY, uid);
        startActivity(intent);
    }

    @Override
    public void onCommentClicked(String postID) {
        Intent intent = new Intent(getContext(), CommentActivity.class);
        intent.putExtra("postID", postID);
        startActivity(intent);
    }

    @Override
    public void onNumLikesClicked(String postID) {
        Intent intent=new Intent(getContext(), NumLikesActivity.class);
        intent.putExtra("ID",postID+"");
        startActivity(intent);
    }

}