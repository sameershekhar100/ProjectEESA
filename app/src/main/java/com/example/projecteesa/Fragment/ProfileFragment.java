package com.example.projecteesa.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projecteesa.Adapters.PostItemClicked;
import com.example.projecteesa.Adapters.ProfilePostAdapter;
import com.example.projecteesa.Posts.CommentActivity;
import com.example.projecteesa.Posts.NumLikesActivity;
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
import java.util.Collections;
import java.util.Comparator;
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
    private String myPostHeaderText = "My Posts";
    //For pagination
    static ArrayList<Post> myPostList = new ArrayList<>();
    static DocumentSnapshot lastsnapshot=null;
    NestedScrollView nestedScrollView;
    ProgressBar progressBar;



    public ProfileFragment() {
        // Required empty public constructor
    }

    public ProfileFragment(String uid) {
        userUid = uid;
        myPostList=new ArrayList<>();
        lastsnapshot=null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        setupView(view);
//        getPosts();
        Log.i("Hello:", "Profile fragment");

        if(myPostList.size()>0 && myPostList.get(0).getUid().equals(currentUserUid))
        {
            profilePostAdapter = new ProfilePostAdapter(myPostList, getContext(), ProfileFragment.this);
            myPosts.setAdapter(profilePostAdapter);
            fetchData();
        }
        else
        {
            myPostList=new ArrayList<>();
            lastsnapshot=null;
            fetchMyPosts();
            fetchData();
        }


        setListeners();

        return view;
    }

    private void setupView(View view) {
        mContext = getContext();
        fab = view.findViewById(R.id.edit_profile_fab);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        currentUserUid = firebaseUser.getUid();
        if (userUid.isEmpty()) userUid = currentUserUid;
        doc = db.document("" + userUid);
        progressDialog = new ActivityProgressDialog(mContext);
        progressDialog.setCancelable(false);

        name = view.findViewById(R.id.name);
        statusTv = view.findViewById(R.id.statusTv);
        imageView = view.findViewById(R.id.profile_image);
        myPosts = view.findViewById(R.id.myPosts);
        myPostHeaderTitle = view.findViewById(R.id.header_title);
        layoutManager = new LinearLayoutManager(getContext());
        bioTv = view.findViewById(R.id.bioTv);
        emailBtn = view.findViewById(R.id.emailBtn);
        linkedinBtn = view.findViewById(R.id.linkedinBtn);
        myPosts.setLayoutManager(layoutManager);
        if (!userUid.equals(currentUserUid)) fab.setVisibility(View.GONE);
        //For pagination
        nestedScrollView=view.findViewById(R.id.profile_scroll);
        progressBar=view.findViewById(R.id.profile_progress);

    }

    private void setListeners() {
        linkedinBtn.setOnClickListener(v -> {
            if (profilex != null) {
                String linkedinProfileUrl = profilex.getLinkedinUrl();
                if (linkedinProfileUrl != null && !(linkedinProfileUrl.isEmpty())) {
                    Intent linkedinIntent = new Intent(Intent.ACTION_VIEW);
                    if (!linkedinProfileUrl.contains("https://"))
                        linkedinProfileUrl = "https://" + linkedinProfileUrl;
                    linkedinIntent.setData(Uri.parse(linkedinProfileUrl));
                    startActivity(linkedinIntent);
                } else
                    MotionToastUtils.showInfoToast(getContext(), "Linkedin profile unavailable", "User does not have linkedin profile");
            }
        });
        emailBtn.setOnClickListener(v -> {
            String email = profilex.getEmail();
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            startActivity(emailIntent);
        });
        fab.setOnClickListener(v ->
        {
            Intent intent = new Intent(getContext(), EditProfile.class);
            intent.putExtra("profile", profilex);
            startActivity(intent);
        });
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight())
                {
                    if(lastsnapshot!=null)
                    {
                        fetchMyPosts();
                    }
                }
            }
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
                    if (!currentUserUid.equals(userUid))
                        myPostHeaderText = profile.getName().split(" ")[0] + " Posts";
                    myPostHeaderTitle.setText(myPostHeaderText.toUpperCase());
                    myPostHeaderTitle.setVisibility(View.VISIBLE);
                    bioTv.setText(profile.getBio());
                    img = profile.getUserImg();
                    int passingYear = profile.getPassingYear();
                    if (passingYear != 0) {
                        String statusText = "";
                        Date date = new Date();
                        int currentYear = date.getYear() + 1900;
                        if (currentYear > passingYear) statusText += "Alumni ";
                        else statusText += "Student ";
                        statusText += profile.getBranch() + " " + passingYear;
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

    }

    void fetchMyPosts() {
        if(lastsnapshot==null)
        {
            firestore.collection("AllPost")
                    .whereEqualTo("uid",userUid)
                    .limit(2)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.isEmpty()) {
                        myPostHeaderTitle.setVisibility(View.GONE);
                        return;
                    }
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Post post = documentSnapshot.toObject(Post.class);
                        myPostList.add(post);
                    }
                    if (myPostList.isEmpty()) {
                        myPostHeaderTitle.setVisibility(View.GONE);
                        return;
                    }
                    int lastSize=queryDocumentSnapshots.size()-1;
                    if(lastSize>0) {
                        lastsnapshot = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    }
                    else
                    {
                        lastsnapshot=null;
                    }
                    profilePostAdapter = new ProfilePostAdapter(myPostList, getContext(), ProfileFragment.this);
                    myPosts.setAdapter(profilePostAdapter);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.i("FirestoreException", e.toString());
                }
            });

        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            Log.i("Hello:","next fetch");
            firestore.collection("AllPost")
                    .whereEqualTo("uid",userUid)
                    .limit(2)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(lastsnapshot)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.isEmpty() && myPostList.isEmpty()) {
                        myPostHeaderTitle.setVisibility(View.GONE);
                        return;
                    }
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Post post = documentSnapshot.toObject(Post.class);
                        myPostList.add(post);
                    }
                    if (myPostList.isEmpty()) {
                        myPostHeaderTitle.setVisibility(View.GONE);
                        return;
                    }
                    int lastSize=queryDocumentSnapshots.size()-1;
                    if(lastSize>0) {
                        lastsnapshot = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    }
                    else
                    {
                        lastsnapshot=null;
                    }
                    profilePostAdapter.setData(myPostList);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.i("FirestoreException", e.toString());
                }
            });
        }
    }

    public static Profile getProfileData() {
        return profileData;
    }

    @Override
    public void onLikeClicked(ArrayList<String> likes, String postID) {
        firestore.collection("AllPost")
                .document(postID)
                .update("likes", likes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
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
        doc.update("savedPost", savedPosts).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        //Not to be deleted.
    }

    @Override
    public void onCommentClicked(String postID) {
        Intent intent = new Intent(getContext(), CommentActivity.class);
        intent.putExtra("postID", postID);
        startActivity(intent);
    }

    @Override
    public void onNumLikesClicked(String postID) {
        Intent intent = new Intent(getContext(), NumLikesActivity.class);
        intent.putExtra("ID", postID);
        startActivity(intent);
    }
}