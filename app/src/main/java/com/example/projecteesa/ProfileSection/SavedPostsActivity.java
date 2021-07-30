package com.example.projecteesa.ProfileSection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecteesa.Adapters.PostAdapter;
import com.example.projecteesa.Adapters.PostItemClicked;
import com.example.projecteesa.Posts.CommentActivity;
import com.example.projecteesa.Posts.NumLikesActivity;
import com.example.projecteesa.Posts.Post;
import com.example.projecteesa.R;
import com.example.projecteesa.utils.AccountsUtil;
import com.example.projecteesa.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SavedPostsActivity extends AppCompatActivity implements PostItemClicked {
    RecyclerView savedPostsRecycler;
    ArrayList<String> savedPostList;
    ArrayList<Post> postsList = new ArrayList<>();
    LinearLayoutManager manager;
    PostAdapter adapter;
    FirebaseFirestore firestore;
    static boolean b = false;
    ProgressBar p;
    int curr, total, scrolledOut;
    private Toolbar toolbar;
    int i = 0, j = 1;
    private TextView noSavedPostsTv;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_posts);
        getSupportActionBar().hide();
        savedPostsRecycler = findViewById(R.id.savedpost_recycler);
        toolbar = findViewById(R.id.toolbar);
        TextView titleTv = toolbar.findViewById(R.id.titleTv);
        p = findViewById(R.id.progress_saved);
        titleTv.setText("Saved Posts");
        savedPostList = AccountsUtil.fetchData().getSavedPost();
        noSavedPostsTv = findViewById(R.id.no_post_tv);
        if (savedPostList == null || savedPostList.size() == 0)
            noSavedPostsTv.setVisibility(View.VISIBLE);
        manager = new LinearLayoutManager(this);
        firestore = FirebaseFirestore.getInstance();
        savedPostsRecycler.setLayoutManager(manager);
        new FetchAsync().doInBackground(savedPostList);
    }


    @Override
    public void onLikeClicked(ArrayList<String> likes, String postID) {
        firestore.collection("AllPost").document(postID).update("likes", likes).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        firestore.collection("Users").document(AccountsUtil.getUID()).update("savedPost", savedPosts).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra(Constants.USER_UID_KEY, uid);
        startActivity(intent);
    }

    @Override
    public void onCommentClicked(String postID) {
        Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
        intent.putExtra("postID", postID + "");
        startActivity(intent);
    }

    @Override
    public void onNumLikesClicked(String postID) {
        Intent intent = new Intent(getApplicationContext(), NumLikesActivity.class);
        intent.putExtra("ID", postID + "");
        startActivity(intent);
    }

    public class FetchAsync extends AsyncTask<ArrayList<String>, Void, Void> {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        @Override
        protected Void doInBackground(ArrayList<String>... arrayLists) {
            ArrayList<String> list = arrayLists[0];
            adapter = new PostAdapter(postsList, SavedPostsActivity.this, SavedPostsActivity.this);
            savedPostsRecycler.setAdapter(adapter);
            for( String path :list){
                firestore.document(path).get().addOnSuccessListener(documentSnapshot -> {
                    Post post = documentSnapshot.toObject(Post.class);
                    if(post!=null) {
                        postsList.add(post);
                        adapter.setData(postsList);
                    }
                }).addOnFailureListener(e -> {
                    Log.i("Failure:", "could not fetch");
                });
            }
            return null;
        }
    }
}

//    void setupPagination(){
//
//        savedPostsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
//                    b=true;
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                curr=manager.getChildCount();
//                total=manager.getItemCount();
//                scrolledOut=manager.findFirstVisibleItemPosition();
//
//                if(b&&(curr+scrolledOut==total)){
//                    b=false;
//                    Log.i("Failure:", b+" "+j+"");
//                   j=0;
//
//
//                    getDataAgain();
//                }
//            }
//        });
//    }
//    void getDataAgain(){
//        p.setVisibility(View.VISIBLE);
//        for(;i<savedPostList.size();i++,j++)
//        {
//            if (j > 3) {
//                break;
//            }
//            String path=savedPostList.get(i);
//            firestore.document(path).get().addOnSuccessListener(documentSnapshot -> {
//                Post post = documentSnapshot.toObject(Post.class);
//                if (post != null) {
//                    postsList.add(post);
//                    adapter.setData(postsList);
//                    adapter.notifyDataSetChanged();
//                    p.setVisibility(View.GONE);
//                }
//                if(i==savedPostList.size()) {
//                    p.setVisibility(View.GONE);
//                    Log.d("taggg", i + "  " + savedPostList.size() + "");
//                }
//            }).addOnFailureListener(e -> {
//                Log.i("Failure:", "could not fetch");
//                p.setVisibility(View.GONE);
//            });
//
//        }
//        if(i==savedPostList.size()){
//            p.setVisibility(View.INVISIBLE);
//        }
//    }
//}