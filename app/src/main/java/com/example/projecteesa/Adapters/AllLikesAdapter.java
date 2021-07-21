package com.example.projecteesa.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projecteesa.ProfileSection.Profile;
import com.example.projecteesa.R;
import com.example.projecteesa.utils.AccountsUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllLikesAdapter extends RecyclerView.Adapter<AllLikesAdapter.numLikeHolder> {
ArrayList<String> name;
Context mContext;
FirebaseFirestore firestore=FirebaseFirestore.getInstance();
CollectionReference reference=firestore.collection("Users/");
    likeItemClicked listener;
    public AllLikesAdapter(ArrayList<String> name, Context mContext,likeItemClicked listener) {
        this.name = name;
        this.mContext = mContext;
        this.listener=listener;
    }

    @NonNull
    @NotNull
    @Override
    public numLikeHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.liked_user_view,parent,false);
        numLikeHolder cHolder=new numLikeHolder(myView);
        cHolder.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid=name.get(cHolder.getAdapterPosition());
                listener.onLikeUserClicked(uid);
            }
        });
        cHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid=name.get(cHolder.getAdapterPosition());
                listener.onLikeUserClicked(uid);
            }
        });
        return cHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AllLikesAdapter.numLikeHolder holder, int position) {
        String uid=name.get(position);
        reference.document(uid+"").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Profile p=documentSnapshot.toObject(Profile.class);
                holder.user.setText(p.getName());
                holder.userBio.setText(p.getBio());
                if (p.getUserImg() == null || p.getUserImg().isEmpty())
                    holder.imageView.setImageResource(R.drawable.user_profile_placeholder);
                else
                Glide.with(mContext).load(p.getUserImg()).into(holder.imageView);
            }
        });


    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class numLikeHolder extends RecyclerView.ViewHolder{
        CircleImageView imageView;
        TextView user;
        TextView userBio;
        public numLikeHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.cimg1);
            user=itemView.findViewById(R.id.cuser1);
            userBio=itemView.findViewById(R.id.cBio1);
        }
    }
}
