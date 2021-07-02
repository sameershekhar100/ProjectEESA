package com.example.projecteesa.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projecteesa.Adapters.HomeAdapter;
import com.example.projecteesa.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=view.findViewById(R.id.recyclerView_home);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        ArrayList<String> list=getData();
        HomeAdapter adapter=new HomeAdapter(list);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private ArrayList<String> getData() {
        ArrayList<String> list=new ArrayList<>();
        for(int i=0;i<15;i++)
        {
            list.add("Text: "+i);
        }
        return list;
    }
}