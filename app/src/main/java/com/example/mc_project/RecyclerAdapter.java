package com.example.mc_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.security.SecureRandom;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    List<GeoFence> gfs;
    public RecyclerAdapter(List<GeoFence>gf){
        this.gfs = gf;
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        holder.Name.setText(String.valueOf(gfs.get(position).getName()));
        String rds = String.valueOf(gfs.get(position).getRadius());
        rds+=" Km";
        holder.Radius.setText(rds);
        holder.img.setImageResource(R.drawable.geofencesimg);
    }

    @Override
    public int getItemCount() {
        return gfs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name,Radius;
        ImageView img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.Name);
            Radius = itemView.findViewById(R.id.radius);
            img = itemView.findViewById(R.id.imageid);
        }
    }
}
