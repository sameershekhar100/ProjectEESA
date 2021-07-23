package com.example.projecteesa.Posts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecteesa.Adapters.AllLikesAdapter;
import com.example.projecteesa.Adapters.likeItemClicked;
import com.example.projecteesa.Fragment.ProfileFragment;
import com.example.projecteesa.ProfileSection.Profile;
import com.example.projecteesa.ProfileSection.UserProfileActivity;
import com.example.projecteesa.R;
import com.example.projecteesa.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NumLikesActivity extends AppCompatActivity implements likeItemClicked {
    RecyclerView recyclerView;
    Toolbar toolbar;
    String postId;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ArrayList<String> users = new ArrayList<>();
    DocumentReference post;
    CollectionReference uid;
    TextView nonZeroLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_likes);
        getSupportActionBar().hide();
        recyclerView = findViewById(R.id.likesList);
//        toolbar = (Toolbar)findViewById(R.id.toolbar1);
        Intent intent = getIntent();
        postId = intent.getStringExtra("ID");
        recyclerView.setLayoutManager(new LinearLayoutManager(NumLikesActivity.this));
        post = firestore.document("AllPost/" + postId);
        uid = firestore.collection("Users/");
        nonZeroLike=findViewById(R.id.zero_like);
        post.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Post post = documentSnapshot.toObject(Post.class);
                assert post != null;
                users = post.getLikes();
                if(users.size()==0){
                    nonZeroLike.setVisibility(View.VISIBLE);
                    return;
                }
                AllLikesAdapter adapter = new AllLikesAdapter(users, getApplicationContext(),NumLikesActivity.this);
                recyclerView.setAdapter(adapter);
            }

        });


    }

    @Override
    public void onLikeUserClicked(String uid) {
        Intent intent=new Intent(getApplicationContext(), UserProfileActivity.class);
        intent.putExtra(Constants.USER_UID_KEY, uid);
        startActivity(intent);
    }
}
