package com.example.projecteesa.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projecteesa.EditProfile;
import com.example.projecteesa.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;


public class ProfileFragment extends Fragment {

    FloatingActionButton fab;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView name,email;
    ImageView profile;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        fab=view.findViewById(R.id.edit_profile_fab);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        name=view.findViewById(R.id.name);
        email=view.findViewById(R.id.email);
        profile=view.findViewById(R.id.profile_image);
        Log.i("Hello:","Profile fragment");
        fab.setOnClickListener(v->
        {
            startActivity(new Intent(getContext(), EditProfile.class));
        });
        /*Query query= databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Log.i("Hello:","onDatachanged entered");
                for(DataSnapshot ds:snapshot.getChildren()) {
                    Log.i("Hello:","inside for loop");
                    String mname = ""+ds.child("name").getValue();
                    String memail = ""+ds.child("email").getValue();
                    String phoneNo = ""+ds.child("phoneNo").getValue();
                    Toast.makeText(getContext(), memail+mname, Toast.LENGTH_SHORT).show();
                    name.setText(mname);
                    email.setText(memail);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getActivity(), "Some Error Occured", Toast.LENGTH_SHORT).show();
            }
        });*/
        return view;
    }
}