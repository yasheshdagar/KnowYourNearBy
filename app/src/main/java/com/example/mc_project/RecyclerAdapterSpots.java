package com.example.mc_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class RecyclerAdapterSpots extends RecyclerView.Adapter<RecyclerAdapterSpots.ViewHolder> {

    Context context;
    int resource;
    List<String> spotsList;

    public RecyclerAdapterSpots(Context context, int resource, List objects) {

        this.context=context;
        this.resource=resource;
        spotsList=objects;
    }

    @NonNull
    @Override
    public RecyclerAdapterSpots.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        RecyclerAdapterSpots.ViewHolder holder = new RecyclerAdapterSpots.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterSpots.ViewHolder holder, int position) {

        holder.textViewArea.setText(" " + spotsList.get(position));


    }

    @Override
    public int getItemCount() {
        return spotsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewArea;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewArea = itemView.findViewById(R.id.textViewArea);

        }
    }


}
