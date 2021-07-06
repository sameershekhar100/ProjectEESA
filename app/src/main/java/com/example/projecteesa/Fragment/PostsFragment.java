package com.example.projecteesa.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projecteesa.Adapters.PostAdapter;
import com.example.projecteesa.Posts.Post;
import com.example.projecteesa.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PostsFragment extends Fragment {

ArrayList<Post> posts;
RecyclerView recyclerView;
RecyclerView.LayoutManager manager;
FirebaseFirestore firestore;
CollectionReference postRefrence;
PostAdapter postAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_posts, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        firestore=FirebaseFirestore.getInstance();
        postRefrence=firestore.collection("AllPost");
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
                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                {
                    Post post=documentSnapshot.toObject(Post.class);
                    postList.add(post);
                }
                postAdapter=new PostAdapter(postList,getActivity());
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
}