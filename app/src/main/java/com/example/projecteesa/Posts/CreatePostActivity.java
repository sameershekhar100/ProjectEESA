package com.example.projecteesa.Posts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projecteesa.AccountsUtil;
import com.example.projecteesa.Fragment.ProfileFragment;
import com.example.projecteesa.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

public class CreatePostActivity extends AppCompatActivity {
    TextView emptyImg;
    Button post;
    ImageView postImg;
    EditText captionEdit;
    Uri selected;
    String caption;
    Uri downloadUrl;
    StorageReference storageReference;
    FirebaseFirestore firestore;
    String userID= FirebaseAuth.getInstance().getUid();
    CollectionReference postsCollection,UserCollection;
  
    private static final int IMG_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        setView();
        postImg.setOnClickListener(v->{
            Intent post=new Intent();
            post.setType("image/*");
            post.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(post,"Select Picture"),IMG_CODE);
        });
        post.setOnClickListener(v->{
            if(selected!=null) {
                caption = captionEdit.getText().toString().trim();
                createPost(caption);
            }
        });

    }
    private void setView() {
        emptyImg=findViewById(R.id.empty_post);
        post=findViewById(R.id.create_post_btn);
        postImg=findViewById(R.id.create_post_img);
        captionEdit=findViewById(R.id.create_post_caption);
        storageReference= FirebaseStorage.getInstance().getReference().child("posts");
        firestore=FirebaseFirestore.getInstance();
        postsCollection=firestore.collection("AllPost");
        UserCollection=firestore.collection("Users/"+userID+"/MyPosts");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_CODE && resultCode == RESULT_OK)
        {
            assert data!=null;
            selected=data.getData();
            try {
                Bitmap currentImage= MediaStore.Images.Media.getBitmap
                        (this.getContentResolver(),selected);
                postImg.setImageBitmap(currentImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createPost(String caption) {
        if(caption==null)
        {
            caption="";
        }
        StorageReference photoRef=storageReference.child(selected.getLastPathSegment());
        photoRef.putFile(selected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> taskresult=taskSnapshot.getStorage().getDownloadUrl();
                while (!taskresult.isSuccessful());
                downloadUrl=taskresult.getResult();
                addDataToFirestore();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(CreatePostActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void addDataToFirestore() {
        String url=downloadUrl.toString();
        long time=System.currentTimeMillis();
        ArrayList<String> likes=new ArrayList<>();
        String name= ProfileFragment.getProfileData().getName();
        Post post=new Post(userID,name,caption,url,time,likes);
        postsCollection.document(userID+time).set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CreatePostActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(CreatePostActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        UserCollection.document(userID+time).set(post);

    }

}