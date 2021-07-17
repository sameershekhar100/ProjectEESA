package com.example.projecteesa.Adapters;

import android.app.SearchManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projecteesa.Fragment.SearchFragment;
import com.example.projecteesa.ProfileSection.Profile;
import com.example.projecteesa.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class SearchAdapter extends FirestoreRecyclerAdapter<Profile, SearchAdapter.ProfileHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    ProfileItemClicked listener;
    public SearchAdapter(@NonNull @NotNull FirestoreRecyclerOptions<Profile> options, Context context, ProfileItemClicked listener) {
        super(options);
        this.context=context;
        this.listener=listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull SearchAdapter.ProfileHolder holder, int position, @NonNull @NotNull Profile model) {
        SearchFragment.noDataTv.setVisibility(View.GONE);
        holder.name.setText(model.getName());
        int passingYear = model.getPassingYear();
        String statusText = "";
        Date date = new Date();
        int currentYear = date.getYear()+1900;
        if(currentYear>passingYear) statusText += "Alumni ";
        else statusText += "Student ";
        statusText += model.getBranch() + " "+ passingYear;
        holder.branch.setText(statusText);
        if (model.getUserImg() == null || model.getUserImg().isEmpty())
            holder.profile.setImageResource(R.drawable.user_profile_placeholder);
        else
        Glide.with(context).load(model.getUserImg()).into(holder.profile);
    }

    @NonNull
    @NotNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_element,parent,false);
        SearchAdapter.ProfileHolder holder=new SearchAdapter.ProfileHolder(view);
        holder.card.setOnClickListener(v->
        {
            String uid=getSnapshots().getSnapshot(holder.getAdapterPosition()).getId();
            listener.profileClicked(uid);
        });
        return holder;
    }

    public class ProfileHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name,branch;
        CardView card;
        public ProfileHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            profile=itemView.findViewById(R.id.searched_image);
            name=itemView.findViewById(R.id.searched_name);
            branch=itemView.findViewById(R.id.searched_branch);
            card=itemView.findViewById(R.id.searched_profile_card);
        }
    }
}
