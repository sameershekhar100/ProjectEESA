package com.example.projecteesa.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projecteesa.Adapters.PostItemClicked;
import com.example.projecteesa.Adapters.ProfilePostAdapter;
import com.example.projecteesa.Posts.CommentActivity;
import com.example.projecteesa.Posts.CreatePostActivity;
import com.example.projecteesa.Posts.Post;
import com.example.projecteesa.ProfileSection.EditProfile;
import com.example.projecteesa.ProfileSection.Profile;
import com.example.projecteesa.R;
import com.example.projecteesa.utils.ActivityProgressDialog;
import com.example.projecteesa.utils.MotionToastUtils;
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
import java.util.Date;


public class ProfileFragment extends Fragment implements PostItemClicked {

    ImageButton fab;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference db = firestore.collection("Users");
    DocumentReference doc;
    TextView name, statusTv, myPostHeaderTitle;
    String img = "";
    ImageView imageView;
    RecyclerView myPosts;
    RecyclerView.LayoutManager layoutManager;
    ProfilePostAdapter profilePostAdapter;
    Profile profilex;
    static Profile profileData;

    private ActivityProgressDialog progressDialog;
    private Context mContext;
    private TextView bioTv;
    private ImageButton linkedinBtn;
    private ImageButton emailBtn;
    private String userUid = "";
    private String currentUserUid = "";

    public ProfileFragment() {
        // Required empty public constructor
    }

    public ProfileFragment(String uid){
        userUid = uid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mContext = getContext();
        fab = view.findViewById(R.id.edit_profile_fab);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        currentUserUid =  firebaseUser.getUid();
        if (userUid.isEmpty()) userUid = currentUserUid;
        doc = db.document("" + userUid);
        progressDialog = new ActivityProgressDialog(mContext);
        progressDialog.setCancelable(false);


        name = view.findViewById(R.id.name);
        statusTv = view.findViewById(R.id.statusTv);
        imageView = view.findViewById(R.id.profile_image);
        myPosts=view.findViewById(R.id.myPosts);
        myPostHeaderTitle = view.findViewById(R.id.header_title);
        layoutManager=new LinearLayoutManager(getContext());
        bioTv = view.findViewById(R.id.bioTv);
        emailBtn = view.findViewById(R.id.emailBtn);
        linkedinBtn = view.findViewById(R.id.linkedinBtn);
        myPosts.setLayoutManager(layoutManager);
        if(!userUid.equals(currentUserUid)) fab.setVisibility(View.GONE);

//        getPosts();
        Log.i("Hello:", "Profile fragment");

        fetchData();

        fab.setOnClickListener(v ->
        {
            Intent intent = new Intent(getContext(), EditProfile.class);
            intent.putExtra("profile", profilex);
            startActivity(intent);
        });

        setListeners();

        return view;
    }

    private void setListeners() {
        linkedinBtn.setOnClickListener(v -> {
            if (profilex != null){
                String linkedinProfileUrl = profilex.getLinkedinUrl();
                if (linkedinProfileUrl != null && !(linkedinProfileUrl.isEmpty())){
                    Intent linkedinIntent = new Intent(Intent.ACTION_VIEW);
                    if (!linkedinProfileUrl.contains("https://"))
                        linkedinProfileUrl = "https://"+linkedinProfileUrl;
                    linkedinIntent.setData(Uri.parse(linkedinProfileUrl));
                    startActivity(linkedinIntent);
                }else
                    MotionToastUtils.showInfoToast(getContext(), "Linkedin profile unavailable", "User does not have linkedin profile");
            }
        });
        emailBtn.setOnClickListener(v -> {
            String email = firebaseUser.getEmail();
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            startActivity(emailIntent);
        });
    }


    void fetchData() {
        progressDialog.setTitle("Fetching profile data");
        progressDialog.setMessage("Please wait while we fetch your profile data");
        progressDialog.showDialog();

        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                progressDialog.hideDialog();
                if (documentSnapshot.exists()) {
                    Profile profile = documentSnapshot.toObject(Profile.class);
                    name.setText(profile.getName());
                    bioTv.setText(profile.getBio());
                    img = profile.getImage();
                    int passingYear = profile.getPassingYear();
                    if(passingYear != 0){
                            String statusText = "";
                            Date date = new Date();
                            int currentYear = date.getYear()+1900;
                            if(currentYear>passingYear) statusText += "Alumni ";
                            else statusText += "Student ";
                            statusText += passingYear;
                            statusTv.setText(statusText);
                    }
                    if (img != null && !img.isEmpty())
                        Glide.with(getContext()).load(img).into(imageView);
                        profilex = profile;
                    if (userUid.equals(currentUserUid))
                        profileData = profilex;
                }
            }
        });
        progressDialog.hideDialog();
        fetchMyPosts();

    }

    void fetchMyPosts(){
        ArrayList<Post> myPostList=new ArrayList<>();
        db.document(userUid).collection("MyPosts").
                orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()){
                    myPostHeaderTitle.setVisibility(View.GONE);
                    return;
                }
                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                {
                    Post post=documentSnapshot.toObject(Post.class);
                    myPostList.add(post);
                }
                Log.e("abc",myPostList.size()+"");
                profilePostAdapter=new ProfilePostAdapter(myPostList,getContext(),ProfileFragment.this);
                myPosts.setAdapter(profilePostAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getContext(), "oh yeah I fucked up :(", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public static Profile getProfileData()
    {
        return profileData;
    }

    @Override
    public void onLikeClicked(ArrayList<String> likes, String postID) {
        firestore.collection("AllPost")
                .document(postID)
                .update("likes",likes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void onBookmarkClicked(ArrayList<String> savedPosts, String uid) {
        doc.update("savedPost",savedPosts).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    @Override
    public void onOwnerProfileClicked(String uid) {
        //Not to be deleted.
    }

    @Override
    public void onCommentClicked(String postID) {
        Intent intent=new Intent(getContext(), CommentActivity.class);
        intent.putExtra("postID",postID);
        startActivity(intent);
        Toast.makeText(getContext(), "working", Toast.LENGTH_SHORT).show();
    }
}