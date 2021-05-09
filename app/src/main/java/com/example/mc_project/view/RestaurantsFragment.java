package com.example.mc_project.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mc_project.R;
import com.example.mc_project.adapter.RestaurantsAdapter;
import com.example.mc_project.api.ApiUrl;
import com.example.mc_project.model.bean.RestaurantBE;
import com.example.mc_project.model.response.RestaurantsNearByResponse;
import com.example.mc_project.presenter.RestaurantsPresenter;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsFragment extends Fragment {

    private LinearLayoutManager layoutManager;

    private RestaurantsAdapter restaurantsAdapter;

    private RecyclerView recyclerView;

    private List<RestaurantBE> restaurantList;

    private RestaurantsPresenter restaurantsPresenter;

    private ProgressDialog progressDialog;

    private TextView emptyView;

    private final int LIMIT = 50;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(getActivity().getClass().getName(), "[OnCreateView] - Fragment inflated");
        return inflater.inflate(R.layout.fragment_restaurants_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(this.getActivity().getClass().getName(), "[OnViewCreated] - Fragment view created");

        recyclerView = view.findViewById(R.id.restaurants_recycler);
        emptyView = view.findViewById(R.id.empty_view);

        layoutManager = new LinearLayoutManager(getActivity());
        restaurantList = new ArrayList<>();
        restaurantsAdapter = new RestaurantsAdapter(getActivity(), restaurantList);
        restaurantsPresenter = new RestaurantsPresenter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(restaurantsAdapter);

        getRestaurants();
    }

    private void getRestaurants() {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(Boolean.FALSE);
        }

        progressDialog.show();
        restaurantsPresenter.getRestaurants(ApiUrl.CATEGORY_ID, "gurgaon", LIMIT);
    }

    public void onSuccessRestaurants(RestaurantsNearByResponse response) {

        Log.i(this.getActivity().getClass().getName(), "[onSuccessRestaurants] - Response received successfully");

        progressDialog.dismiss();

        if (response.getRestaurantResponse().getRestaurantList().size() > 0) {
            restaurantList.addAll(response.getRestaurantResponse().getRestaurantList());
            restaurantsAdapter.notifyDataSetChanged();
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    public void onErrorSuggestion() {

        Log.i(this.getActivity().getClass().getName(), "[onErrorRestaurants] - Error received");

        progressDialog.dismiss();
    }
}
