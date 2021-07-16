package com.example.projecteesa.Posts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecteesa.Adapters.CommentAdapter;
import com.example.projecteesa.Adapters.RecyclerCommentAdapter;
import com.example.projecteesa.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {
    RecyclerView commentsList;
    RecyclerCommentAdapter commentAdapter;
    EditText write_cmt;
    Button post_cmt;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference commentsRef;
    DocumentReference username;
    String postID;

    //    ArrayList<Comment> myComment;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        commentsList = findViewById(R.id.comments_list);
        write_cmt = findViewById(R.id.write_comment);
        post_cmt = findViewById(R.id.postCmt);
        Intent intent = getIntent();
        postID = intent.getStringExtra("postID");
        commentsList.setLayoutManager(new LinearLayoutManager(this));
        commentsRef = firestore.collection("AllPost/" + postID + "/comments");

        fetchComments();
        username = firestore.document("Users/" + user.getUid());


        post_cmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });
    }

    void fetchComments() {
//        ArrayList<Comment> commentList = new ArrayList<>();
        Query query=commentsRef.orderBy("time", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions options=new FirestoreRecyclerOptions.Builder<Comment>().setQuery(query,Comment.class).build();
        commentAdapter=new RecyclerCommentAdapter(options,CommentActivity.this);
        commentsList.setAdapter(commentAdapter);
        commentAdapter.startListening();
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                    Comment comment = documentSnapshot.toObject(Comment.class);
//                    assert comment != null;
//                    commentList.add(new Comment(comment.getUserID(), comment.getMessage().toString(),comment.getTime(),comment.getImageURL()));
//                }
//                commentAdapter = new CommentAdapter(commentList,CommentActivity.this);
////        commentAdapter.update(commentList);
//                commentsList.setAdapter(commentAdapter);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull @NotNull Exception e) {
//                Toast.makeText(CommentActivity.this, e + "", Toast.LENGTH_SHORT).show();
//            }
//        });


    }

    void postComment() {

        String cmt = write_cmt.getText().toString();
        if(cmt.isEmpty()){
            Toast.makeText(this, "Enter yor comment", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("gandu<3","posst btn klicked");
        username.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.get("name").toString();
                String img=documentSnapshot.get("image").toString();

                long time = System.currentTimeMillis() ;
                Comment comment = new Comment(user.getUid(), cmt,time);
                commentsRef.document(user.getUid() + "+" + time).set(comment);
                Toast.makeText(CommentActivity.this, "posted", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(CommentActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        commentAdapter.startListening();
        write_cmt.setText("");

    }
}