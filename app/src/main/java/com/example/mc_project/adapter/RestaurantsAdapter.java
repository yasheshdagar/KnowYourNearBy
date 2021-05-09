package com.example.mc_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mc_project.MapsActivity;
import com.example.mc_project.R;
import com.example.mc_project.model.bean.RestaurantBE;

import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantsDataHolder> {

    private Context context;
    private List<RestaurantBE> restaurantsList;

    public RestaurantsAdapter(Context context, List<RestaurantBE> restaurantsList){
        this.context = context;
        this.restaurantsList = restaurantsList;
    }

    @NonNull
    @Override
    public RestaurantsAdapter.RestaurantsDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_restaurants_layout, parent, false);
        return new RestaurantsDataHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsDataHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return restaurantsList.size();
    }

    public class RestaurantsDataHolder extends RecyclerView.ViewHolder{

        private TextView restaurantName, city, state;

        Button geoFence, directions;

        public RestaurantsDataHolder(View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.name);
            city = itemView.findViewById(R.id.city);
            state = itemView.findViewById(R.id.state);
            geoFence = itemView.findViewById(R.id.geo_fence);
            directions = itemView.findViewById(R.id.directions);

            geoFence.setOnClickListener((v) -> {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("restaurants", true);
                intent.putExtra("lat", restaurantsList.get(getAdapterPosition()).getLocation().getLatitude());
                intent.putExtra("lng", restaurantsList.get(getAdapterPosition()).getLocation().getLongitude());
                context.startActivity(intent);
            });

            directions.setOnClickListener((v)->{
                String geoUri = "http://maps.google.com/maps?q=loc:"
                        + restaurantsList.get(getAdapterPosition()).getLocation().getLatitude()
                        + "," + restaurantsList.get(getAdapterPosition()).getLocation().getLongitude();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                context.startActivity(intent);
            });
        }

        public void bind(int position){

            String cityText = restaurantsList.get(position).getLocation().getCity();
            String stateText = restaurantsList.get(position).getLocation().getState();

            restaurantName.setText(restaurantsList.get(position).getRestaurantName());

            if(cityText == null) city.setText("NA");
            else city.setText(restaurantsList.get(position).getLocation().getCity());

            if(stateText == null) state.setText("NA");
            else state.setText(restaurantsList.get(position).getLocation().getState());
        }
    }
}
