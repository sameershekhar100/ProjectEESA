package com.example.projecteesa.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projecteesa.Posts.Comment;
import com.example.projecteesa.R;
import com.example.projecteesa.utils.TimeUtils;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    ArrayList<Comment> arrayList=new ArrayList<>();
    Context context;
    public CommentAdapter(ArrayList<Comment> arrayList,Context context) {
        this.arrayList = arrayList;
        this.context=context;
    }

    @NonNull
    @NotNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_view,parent,false);
        CommentHolder cHolder=new CommentHolder(myView);
        return cHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentAdapter.CommentHolder holder, int position) {
        Comment msg =arrayList.get(position);
        holder.cMsg.setText(msg.getMessage());
        holder.userName.setText(msg.getUserID());

        Timestamp timestamp=new Timestamp(msg.getTime());
        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat sdf2=new SimpleDateFormat("dd/MM/yyyy");

        Date date=new Date(timestamp.getTime());
        String time=sdf.format(date);
        String time2=sdf2.format(date);
        holder.cTime.setText(TimeUtils.getTime(msg.getTime()));
        Glide.with(context).load(msg.getImageURL()).into(holder.comment_pic);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

//    public void update(ArrayList<Comment> arrayList){
//        this.arrayList.clear();
//        this.arrayList.addAll(arrayList);
//        notifyDataSetChanged();
//    }

    class CommentHolder extends RecyclerView.ViewHolder {
        CircleImageView comment_pic;
        TextView userName;
        TextView cMsg,cTime;

        public CommentHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            comment_pic = itemView.findViewById(R.id.cimg);
            userName = itemView.findViewById(R.id.cuser);
            cMsg = itemView.findViewById(R.id.cMsg);
            cTime=itemView.findViewById(R.id.time);
        }
    }
}
