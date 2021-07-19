package com.example.projecteesa.Posts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projecteesa.MainActivity;
import com.example.projecteesa.utils.AccountsUtil;
import com.example.projecteesa.ProfileSection.Profile;
import com.example.projecteesa.R;
import com.example.projecteesa.utils.ActivityProgressDialog;
import com.example.projecteesa.utils.MotionToastUtils;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class CreatePostActivity extends AppCompatActivity {
    private Context mContext = this;
    TextView emptyImg;
    Button post;
    ImageView postImg;
    EditText captionEdit;
    Uri selected;
    String caption;
    Uri downloadUrl;
    String pID;
    StorageReference storageReference;
    FirebaseFirestore firestore;
    String userID= FirebaseAuth.getInstance().getUid();
    CollectionReference postsCollection,UserCollection;
    private Toolbar toolbar;
    private static final int IMG_CODE=1;
    private ActivityProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        setView();
        getSupportActionBar().hide();
        toolbar = findViewById(R.id.toolbar);
        TextView titleTv = toolbar.findViewById(R.id.titleTv);
        titleTv.setText("Create a new post");
        progressDialog = new ActivityProgressDialog(mContext);
        progressDialog.setCancelable(false);
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
        progressDialog.setTitle("Creating new post");
        progressDialog.setMessage("Please wait while we create your new post");
        progressDialog.showDialog();
        if(caption==null)
        {
            caption="";
        }
        pID=userID+System.currentTimeMillis();
        StorageReference photoRef=storageReference.child(pID);
        byte[] data;
        try {
            //compression of selected image to 10% of the actual size
            Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selected);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
            data = baos.toByteArray();
            photoRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                    progressDialog.hideDialog();
                    Toast.makeText(CreatePostActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void addDataToFirestore() {
        String url=downloadUrl.toString();
        long time=System.currentTimeMillis();
        ArrayList<String> likes=new ArrayList<>();
        AccountsUtil util=new AccountsUtil();
        Profile item=util.fetchData();
        String userProfileImg=item.getUserImg();
        String name= AccountsUtil.fetchData().getName();
        String postID=userID+time;
        Post post=new Post(postID,userID,name,userProfileImg,caption,url,time,likes);
        postsCollection.document(postID).set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.hideDialog();
                MotionToastUtils.showSuccessToast(mContext, "Success", "Your new post is now live");
                Intent homeIntent = new Intent(mContext, MainActivity.class);
                startActivity(homeIntent);
                finishAffinity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                MotionToastUtils.showErrorToast(mContext, "Error occured", "Some error occurred while created your post");
                progressDialog.hideDialog();
            }
        });

    }

}