package com.example.mc_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mc_project.R;
import com.example.mc_project.listener.CovidListener;

import java.util.List;

public class CovidAdapter extends RecyclerView.Adapter<CovidAdapter.CovidDataHolder> {

    private Context context;
    private List<String> zonesList;
    private CovidListener covidListener;

    public CovidAdapter(Context context, List<String> zonesList, CovidListener covidListener){
        this.context = context;
        this.zonesList = zonesList;
        this.covidListener = covidListener;
    }

    @NonNull
    @Override
    public CovidDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_covid_layout, parent, false);
        return new CovidDataHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CovidDataHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return zonesList.size();
    }

    public class CovidDataHolder extends RecyclerView.ViewHolder{

        private TextView zoneName;
        private Button create;

        public CovidDataHolder(View itemView) {
            super(itemView);
            zoneName = itemView.findViewById(R.id.zone_name);
            create = itemView.findViewById(R.id.create_geofence);
            create.setOnClickListener(v -> covidListener.onClickCreate(getLayoutPosition()));
        }

        public void bind(int position){
            zoneName.setText(zonesList.get(position));
        }
    }

}
