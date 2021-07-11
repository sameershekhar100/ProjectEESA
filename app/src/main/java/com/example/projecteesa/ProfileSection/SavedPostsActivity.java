package com.example.projecteesa.ProfileSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.projecteesa.Adapters.PostAdapter;
import com.example.projecteesa.Adapters.PostItemClicked;
import com.example.projecteesa.Posts.Post;
import com.example.projecteesa.R;
import com.example.projecteesa.utils.AccountsUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SavedPostsActivity extends AppCompatActivity implements PostItemClicked {
    RecyclerView savedPostsRecycler;
    ArrayList<String> savedPostList;
    ArrayList<Post> postsList=new ArrayList<>();
    RecyclerView.LayoutManager manager;
    PostAdapter adapter;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_posts);
        savedPostsRecycler=findViewById(R.id.savedpost_recycler);
        savedPostList= AccountsUtil.fetchData().getSavedPost();
        manager= new LinearLayoutManager(this);
        firestore=FirebaseFirestore.getInstance();
        savedPostsRecycler.setLayoutManager(manager);
        new FetchAsync().doInBackground(savedPostList);
    }


    @Override
    public void onLikeClicked(ArrayList<String> likes, String postID) {
        firestore.collection("AllPost").document(postID).update("likes",likes).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("Like updated","!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i("Failed: ","to update likes");
            }
        });    }

    @Override
    public void onBookmarkClicked(ArrayList<String> savedPosts, String uid) {
        firestore.collection("Users").document(AccountsUtil.getUID()).update("savedPost",savedPosts).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("SavedPost","post added!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i("SavedPost","Failed!");
            }
        });    }
    public class FetchAsync extends AsyncTask<ArrayList<String>,Void,Void>{

        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        @Override
        protected Void doInBackground(ArrayList<String>... arrayLists) {
            ArrayList<String> list=arrayLists[0];
            adapter=new PostAdapter(postsList,SavedPostsActivity.this,SavedPostsActivity.this);
            savedPostsRecycler.setAdapter(adapter);
            for(String path:list)
            {
                firestore.document(path).get().addOnSuccessListener(documentSnapshot -> {
                    Post post=documentSnapshot.toObject(Post.class);
                    postsList.add(post);
                    adapter.setData(postsList);
                }).addOnFailureListener(e -> {
                    Log.i("Failure:","could not fetch");
                });
            }

            return null;
        }
    }
}