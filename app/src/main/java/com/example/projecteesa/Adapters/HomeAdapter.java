package com.example.projecteesa.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecteesa.Fragment.HomeFragment;
import com.example.projecteesa.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    ArrayList<String> list;

    public HomeAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_element,parent,false);
        HomeHolder homeHolder=new HomeHolder(view);
        return homeHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeAdapter.HomeHolder holder, int position) {
        holder.text.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeHolder extends RecyclerView.ViewHolder {
        TextView text;
        public HomeHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            text=itemView.findViewById(R.id.home_text);
        }
    }
}
