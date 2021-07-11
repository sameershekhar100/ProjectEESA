package com.example.projecteesa.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projecteesa.Adapters.PostAdapter;
import com.example.projecteesa.Adapters.PostItemClicked;
import com.example.projecteesa.MainActivity;
import com.example.projecteesa.Posts.Post;
import com.example.projecteesa.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements PostItemClicked {
    ArrayList<Post> posts;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    FirebaseFirestore firestore;
    CollectionReference postRefrence;
    PostAdapter postAdapter;
    DocumentReference userRefrence;
    private ShimmerFrameLayout shimmerLayout;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_posts, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        firestore=FirebaseFirestore.getInstance();
        postRefrence=firestore.collection("AllPost");
        String uid= FirebaseAuth.getInstance().getUid();
        userRefrence=firestore.collection("Users").document(uid);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        shimmerLayout.startShimmerAnimation();
        manager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        posts=fetchPosts();
        return  view;
    }

    private ArrayList<Post> fetchPosts() {
        ArrayList<Post> postList=new ArrayList<>();
        postRefrence.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                shimmerLayout.startShimmerAnimation();
                shimmerLayout.setVisibility(View.GONE);
                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                {
                    Post post=documentSnapshot.toObject(Post.class);
                    postList.add(post);
                }
                postAdapter=new PostAdapter(postList,getActivity(), HomeFragment.this);
                recyclerView.setAdapter(postAdapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getContext(), "??", Toast.LENGTH_SHORT).show();
            }
        });
        return postList;
    }

    @Override
    public void onLikeClicked(ArrayList<String> likes, String postID) {
        postRefrence.document(postID).update("likes",likes).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("Like updated","!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i("Failed: ","to update likes");
            }
        });
    }

    @Override
    public void onBookmarkClicked(ArrayList<String> savedPosts,String uid) {
        userRefrence.update("savedPost",savedPosts).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("SavedPost","post added!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i("SavedPost","Failed!");
            }
        });
    }
}