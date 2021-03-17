package com.example.mc_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ViewFragment extends Fragment implements RecyclerAdapter.OnItemClickListener{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter recyclerAdapter;
    private TextView tview;
    private List<GeoFence> list;
    private Database database;
    private FloatingActionButton floatingActionButton;
    public ViewFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewfgm, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        tview = view.findViewById(R.id.nogfs);
        floatingActionButton = view.findViewById(R.id.floatingActionButtonAddGeofence);

        database = new Database(getContext());
        list = database.rdb.geoFenceDao().getAllGeoFences();
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
            recyclerAdapter.setOnItemClickListener(this::onItemClicked);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , MapsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onItemClicked(int position) {

        Intent intent = new Intent(getContext(), ViewGeofence.class);
        intent.putExtra("latitude", list.get(position).getLatitude());
        intent.putExtra("longitude", list.get(position).getLongitude());
        intent.putExtra("color", list.get(position).getColor());
        intent.putExtra("radius", list.get(position).getRadius());
        intent.putExtra("type", list.get(position).getType());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(recyclerAdapter == null){
            recyclerAdapter = new RecyclerAdapter(list);
            recyclerView.setAdapter(recyclerAdapter);
        }else {
            list = database.rdb.geoFenceDao().getAllGeoFences();
            recyclerAdapter.updateAdapter(list);
        }

    }
}

