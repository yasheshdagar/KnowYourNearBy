package com.example.mc_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter recyclerAdapter;
    private TextView tview;
    public ViewFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewfgm, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        tview = view.findViewById(R.id.nogfs);
        List<GeoFence> list = DatabaseActivity.rdb.geoFenceDao().getAllGeoFences();
        if(list.size()==0){
            tview.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            tview.setVisibility(View.INVISIBLE);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerAdapter = new RecyclerAdapter(list);
            recyclerView.setAdapter(recyclerAdapter);
        }
        return view;
    }
}

