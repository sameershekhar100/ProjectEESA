package com.example.projecteesa.Posts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projecteesa.Adapters.ProfileItemClicked;
import com.example.projecteesa.Adapters.RecyclerCommentAdapter;
import com.example.projecteesa.R;
import com.example.projecteesa.utils.AccountsUtil;
import com.example.projecteesa.utils.TimeUtils;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {
    RecyclerView commentsList;
    RecyclerCommentAdapter commentAdapter;
    EditText write_cmt;
    Button post_cmt;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference commentsRef;
    DocumentReference postref;
    DocumentReference username;
    String postID;
    ImageView postImg, postProfileHeader, likeBtn, bookmarkBtn;
    TextView caption, likes, captionHeader, postHeader, postTime;
    CardView mainCard;
    LinearLayout postHeaderLayout;
    Post post;
    ArrayList<String> savedPosts;
    String postPath;
    ArrayList<String> likeslist;
    //    ArrayList<Comment> myComment;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        postProfileHeader = findViewById(R.id.post_header_img1);
        postHeader = findViewById(R.id.post_header1);
        postImg = findViewById(R.id.post_image1);
        likeBtn = findViewById(R.id.post_like_btn1);
        likes = findViewById(R.id.like_number_text1);
        captionHeader = findViewById(R.id.caption_header1);
        caption = findViewById(R.id.post_caption1);
        mainCard = findViewById(R.id.mainCard1);
        bookmarkBtn = findViewById(R.id.post_save_btn1);
        postHeaderLayout = findViewById(R.id.post_header_layout1);

        commentsList = findViewById(R.id.comments_list);
        write_cmt = findViewById(R.id.write_comment);
        post_cmt = findViewById(R.id.postCmt);
        Intent intent = getIntent();
        postID = intent.getStringExtra("postID");
        postPath = "AllPost/" + postID;
        postref = firestore.document("AllPost/" + postID);
        commentsList.setLayoutManager(new LinearLayoutManager(this));
        commentsRef = firestore.collection("AllPost/" + postID + "/comments");

        fetchComments();

        postref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                post = documentSnapshot.toObject(Post.class);
//                Log.d("captioonnnnnn",post.getCaption()+"");
                ArrayList<String> like = post.getLikes();
                if (like.size() < 2) {
                    likes.setText(like.size() + " like");
                } else {
                    likes.setText(like.size() + " likes");
                }
                caption.setText(post.getCaption().toString());
                captionHeader.setText(post.getName() + ": ");
                postHeader.setText(post.getName() + "");
                Glide.with(getApplicationContext()).load(post.getImageURL()).into(postImg);

                if (post.getUserProfile() != null && !(post.getUserProfile().isEmpty()))
                    Glide.with(getApplicationContext()).load(post.getUserProfile()).into(postProfileHeader);
                else
                    postProfileHeader.setImageResource(R.drawable.user_profile_placeholder);
                if (like.contains(user.getUid())) {
                    likeBtn.setImageResource(R.drawable.ic_like);
                }
            }
        });
        savedPosts = AccountsUtil.fetchData().getSavedPost();

        if (savedPosts.contains(postPath)) {
            bookmarkBtn.setImageResource(R.drawable.ic_bookmark_black);
        }
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> likeslist = post.getLikes();
                String uid = FirebaseAuth.getInstance().getUid();
                if (likeslist.contains(uid)) {
                    likeslist.remove(uid);
                    likeBtn.setImageResource(R.drawable.ic_like_border);
                    if (likeslist.size() < 2) {
                        likes.setText(likeslist.size() + " like");
                    } else {
                        likes.setText(likeslist.size() + " likes");
                    }
                } else {
                    likeslist.add(uid);
                    likeBtn.setImageResource(R.drawable.ic_like);
                    if (likeslist.size() < 2) {
                        likes.setText(likeslist.size() + " like");
                    } else {
                        likes.setText(likeslist.size() + " likes");
                    }
                }
                postref.update("likes", likeslist);
            }
        });
        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AccountsUtil.fetchData() != null) {
                    savedPosts = AccountsUtil.fetchData().getSavedPost();
                }
                String path = "AllPost/" + postID;
                if (savedPosts == null) {
                    savedPosts = new ArrayList<>();
                    savedPosts.add(path);
                    bookmarkBtn.setImageResource(R.drawable.ic_bookmark_black);
                }
                if (savedPosts.contains(path)) {
                    savedPosts.remove(path);
                    bookmarkBtn.setImageResource(R.drawable.ic_bookmark_border);
                } else {
                    savedPosts.add(path);
                    bookmarkBtn.setImageResource(R.drawable.ic_bookmark_black);
                }
                firestore.document("Users/" + user.getUid()).update("savedPost", savedPosts);

            }
        });


//        postTime.setText(TimeUtils.getTime(post.getTimestamp()));


        postHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add intent to user profile
            }
        });


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
        Query query = commentsRef.orderBy("time", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions options = new FirestoreRecyclerOptions.Builder<Comment>().setQuery(query, Comment.class).build();
        commentAdapter = new RecyclerCommentAdapter(options, CommentActivity.this);
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
        if (cmt.isEmpty()) {
            Toast.makeText(this, "Enter yor comment", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("gandu<3", "posst btn klicked");
        username.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                long time = System.currentTimeMillis();
                Comment comment = new Comment(user.getUid(), cmt, time);
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
// TODO add on click listener for comment users