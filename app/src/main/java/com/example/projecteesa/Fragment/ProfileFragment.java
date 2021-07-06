package com.example.projecteesa.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.projecteesa.LoginActivity;
import com.example.projecteesa.Posts.CreatePostActivity;
import com.example.projecteesa.ProfileSection.EditProfile;
import com.example.projecteesa.ProfileSection.Profile;
import com.example.projecteesa.R;
import com.example.projecteesa.utils.ActivityProgressDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileFragment extends Fragment {

    ImageButton fab;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference db = firestore.collection("Users");
    TextView name, email;
    String img = "";
    ImageView imageView;
    Button createPost;

    Profile profilex;
    static Profile profileData;

    private ActivityProgressDialog progressDialog;
    private Context mContext;

    public ProfileFragment() {
        // Required empty public constructor
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
        progressDialog = new ActivityProgressDialog(mContext);
        progressDialog.setCancelable(false);

        createPost=view.findViewById(R.id.add_post);
      
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        imageView = view.findViewById(R.id.profile_image);
        Log.i("Hello:", "Profile fragment");

        fetchData();
        createPost.setOnClickListener(v->{
            startActivity(new Intent(getContext(), CreatePostActivity.class));
        });

        fab.setOnClickListener(v ->
        {
            Intent intent = new Intent(getContext(), EditProfile.class);
            intent.putExtra("profile", profilex);
            startActivity(intent);
        });

        return view;
    }


    void fetchData() {
        progressDialog.setTitle("Fetching profile data");
        progressDialog.setMessage("Please wait while we fetch your profile data");
        progressDialog.showDialog();
        DocumentReference doc = db.document("" + firebaseUser.getUid());
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                progressDialog.hideDialog();
                if (documentSnapshot.exists()) {
                    Profile profile = documentSnapshot.toObject(Profile.class);
                    name.setText(profile.getName());
                    email.setText(profile.getBio());
                    img = profile.getImage();
                    if (img != null && !img.isEmpty())
                        Glide.with(getContext()).load(img).into(imageView);
                    profilex = profile;
                    profileData=profilex;
                }
            }

        });
    }
    public static Profile getProfileData()
    {
        return profileData;
    }

}