package com.example.projecteesa.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projecteesa.Posts.Comment;
import com.example.projecteesa.ProfileSection.Profile;
import com.example.projecteesa.R;
import com.example.projecteesa.utils.TimeUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerCommentAdapter extends FirestoreRecyclerAdapter<Comment, RecyclerCommentAdapter.CommentHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    private final CommentClickListener mListener;

    public RecyclerCommentAdapter(@NonNull @NotNull FirestoreRecyclerOptions<Comment> options, Context context, CommentClickListener mListener) {
        super(options);
        this.context = context;
        this.mListener = mListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull RecyclerCommentAdapter.CommentHolder holder, int position, @NonNull @NotNull Comment model) {
        FirebaseFirestore.getInstance().document("Users/" + model.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Profile profile = documentSnapshot.toObject(Profile.class);
                holder.userName.setText(profile.getName());
                Glide.with(context).load(profile.getUserImg()).into(holder.comment_pic);
            }
        });
        holder.cMsg.setText(model.getMessage());
//        holder.userName.setText(model.getUserID());
        holder.cTime.setText(TimeUtils.getTime(model.getTime()));

        holder.profileHeader.setOnClickListener(v -> {
            mListener.onProfileClicked(model.getUid());
        });

    }

    public interface CommentClickListener {
        void onProfileClicked(String uid);
    }

    @NonNull
    @NotNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_view, parent, false);
        RecyclerCommentAdapter.CommentHolder cHolder = new RecyclerCommentAdapter.CommentHolder(myView);
        return cHolder;
    }

    class CommentHolder extends RecyclerView.ViewHolder {

        CircleImageView comment_pic;
        TextView userName;
        TextView cMsg, cTime;
        RelativeLayout profileHeader;

        public CommentHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            comment_pic = itemView.findViewById(R.id.cimg);
            userName = itemView.findViewById(R.id.cuser);
            cMsg = itemView.findViewById(R.id.cMsg);
            cTime = itemView.findViewById(R.id.time);
            profileHeader = itemView.findViewById(R.id.l1);
        }
    }
}
