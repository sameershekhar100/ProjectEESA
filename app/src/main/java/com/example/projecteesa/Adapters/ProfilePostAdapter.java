package com.example.projecteesa.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projecteesa.Posts.Post;
import com.example.projecteesa.R;
import com.example.projecteesa.utils.AccountsUtil;
import com.example.projecteesa.utils.ActivityProgressDialog;
import com.example.projecteesa.utils.MotionToastUtils;
import com.example.projecteesa.utils.TimeUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.PostHolder> {
    ArrayList<Post> posts;
    Context context;
    PostItemClicked listener;
    ArrayList<String> savedPosts;
    Post p;
    boolean b;
    String currentUserUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public ProfilePostAdapter(ArrayList<Post> posts,Context context,PostItemClicked listener) {
        this.posts = posts;
        this.context=context;
        this.listener=listener;
        if(AccountsUtil.fetchData()!=null) {
            savedPosts = AccountsUtil.fetchData().getSavedPost();
        }
    }

    @NonNull
    @NotNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_element,parent,false);
        PostHolder holder=new PostHolder(myView);
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> likes=posts.get(holder.getAdapterPosition()).getLikes();
                String postID=posts.get(holder.getAdapterPosition()).getPostId();
                String uid= FirebaseAuth.getInstance().getUid();
                if(likes.contains(uid))
                {
                    likes.remove(uid);
                    listener.onLikeClicked(likes,postID);
                    holder.likeBtn.setImageResource(R.drawable.ic_like_border);
                    if(likes.size()<2)
                    {
                        holder.likesTv.setText(likes.size()+" like");
                    }
                    else {
                        holder.likesTv.setText(likes.size() + " likes");
                    }
                }
                else
                {
                    likes.add(uid);
                    listener.onLikeClicked(likes,postID);
                    holder.likeBtn.setImageResource(R.drawable.ic_like);
                    if(likes.size()<2)
                    {
                        holder.likesTv.setText(likes.size()+" like");
                    }
                    else {
                        holder.likesTv.setText(likes.size() + " likes");
                    }
                }
            }
        });
        holder.bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert AccountsUtil.fetchData() != null;
                String path="AllPost/"+posts.get(holder.getAdapterPosition()).getPostId();
                if (savedPosts == null){
                    savedPosts = new ArrayList<>();
                    savedPosts.add(path);
                    holder.bookmarkBtn.setImageResource(R.drawable.ic_bookmark_black);
                    listener.onBookmarkClicked(savedPosts, AccountsUtil.getUID());
                    return;
                }
                if(savedPosts.contains(path))
                {
                    savedPosts.remove(path);
                    holder.bookmarkBtn.setImageResource(R.drawable.ic_bookmark_border);
                    listener.onBookmarkClicked(savedPosts,AccountsUtil.getUID());
                }
                else
                {
                    savedPosts.add(path);
                    holder.bookmarkBtn.setImageResource(R.drawable.ic_bookmark_black);
                    listener.onBookmarkClicked(savedPosts,AccountsUtil.getUID());
                }

            }
        });
        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postID=posts.get(holder.getAdapterPosition()).getPostId();

                listener.onCommentClicked(postID);
            }
        });

        holder.postMenuBtn.setOnClickListener(v -> {

            PopupMenu menu = new PopupMenu(context, holder.postMenuBtn);
            menu.inflate(R.menu.my_post_menu);

            menu.setOnMenuItemClickListener(item -> {
                AlertDialog.Builder alert =new AlertDialog.Builder(context);
                alert.setTitle("Delete").setMessage("Are you sure?")
                        .setNegativeButton("No",null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {



                        if (item.getItemId() == R.id.delete_post) {
                            ActivityProgressDialog dialog = new ActivityProgressDialog(context);
                            dialog.setCancelable(false);
                            dialog.setTitle("Deleting Post");
                            dialog.setMessage("Please wait while we delete your post");

                            dialog.showDialog();
                            String imgUrl = posts.get((holder.getAdapterPosition())).getImageUrl();
                            String postId = posts.get(holder.getAdapterPosition()).getPostId();
                            FirebaseFirestore.getInstance().collection("AllPost/" + postId + "/comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot snapshots : Objects.requireNonNull(task.getResult())) {
                                        FirebaseFirestore.getInstance().collection("AllPost/" + postId + "/comments").document(snapshots.getId()).delete();
                                    }
                                }
                            });
                            FirebaseFirestore.getInstance().collection("AllPost").document(postId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    dialog.hideDialog();
                                    if (task.isSuccessful()) {
                                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("posts/" + postId);
                                        StorageReference mPic = FirebaseStorage.getInstance().getReferenceFromUrl("" + imgUrl);
                                        mPic.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                MotionToastUtils.showSuccessToast(context, "Post deleted", "We have successfully deleted your post");
                                                posts.remove(holder.getAdapterPosition());
                                                notifyDataSetChanged();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull @NotNull Exception e) {
                                                MotionToastUtils.showErrorToast(context, "Error", e + "");
                                            }
                                        });

                                    } else {
                                        MotionToastUtils.showErrorToast(context, "Error", "Some error occurred while deleting your post");
                                    }
                                }
                            });
//                            return true;
                                b=true;
                        }

                    }
                });
                    alert.show();
                return b;
            });
            menu.show();
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProfilePostAdapter.PostHolder holder, int position) {
        ArrayList<String> saved;
        Post post=posts.get(position);
        if (AccountsUtil.fetchData() !=null)
            saved= AccountsUtil.fetchData().getSavedPost();
        else
            saved = new ArrayList<>();
        Glide.with(context).load(post.getImageUrl()).into(holder.postImg);
        holder.captionHeader.setText(post.getName()+": ");
        holder.captionTv.setText(post.getCaption());
        if (post.getUserImg() != null && !post.getUserImg().isEmpty())
            Glide.with(context).load(post.getUserImg()).into(holder.ownerImg);
        else
            holder.ownerImg.setImageResource(R.drawable.user_profile_placeholder);
        holder.ownerNameTv.setText(post.getName());
        holder.likesTv.setText(post.getLikes().size()+" likes");
        holder.time.setText(TimeUtils.getTime(post.getTimestamp()));
        if(post.getLikes().contains(AccountsUtil.getUID()))
        {
            holder.likeBtn.setImageResource(R.drawable.ic_like);
        }
        else
        {
            holder.likeBtn.setImageResource(R.drawable.ic_like_border);
        }
        if(saved!=null && saved.contains("AllPost/"+post.getPostId()))
        {
            holder.bookmarkBtn.setImageResource(R.drawable.ic_bookmark_black);
        }
        else {
            holder.bookmarkBtn.setImageResource(R.drawable.ic_bookmark_border);
        }
        if (post.getUid().equals(currentUserUId))
            holder.postMenuBtn.setVisibility(View.VISIBLE);


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder{
        ImageView postImg;
        CircleImageView ownerImg;
        TextView ownerNameTv;
        TextView captionHeader;
        TextView captionTv;
        TextView likesTv;
        TextView time;
        ImageView likeBtn,commentBtn,bookmarkBtn;
        TextView postMenuBtn;
        public PostHolder(@NonNull @NotNull View itemView) {
            super(itemView);
/*
            postProfileHeader=itemView.findViewById(R.id.post_header_img);*/
            postImg=itemView.findViewById(R.id.post_image);
            ownerImg = itemView.findViewById(R.id.post_header_img);
            ownerNameTv = itemView.findViewById(R.id.post_header);
            captionTv = itemView.findViewById(R.id.post_caption);
            captionHeader = itemView.findViewById(R.id.caption_header);
            likesTv = itemView.findViewById(R.id.like_number_text);
            time= itemView.findViewById(R.id.post_time);
            likeBtn=itemView.findViewById(R.id.post_like_btn);
            commentBtn=itemView.findViewById(R.id.post_comment_btn);
            bookmarkBtn=itemView.findViewById(R.id.post_save_btn);
            postMenuBtn = itemView.findViewById(R.id.postMenuBtn);
        }
    }
}
