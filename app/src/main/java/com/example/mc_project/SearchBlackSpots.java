package com.example.mc_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.mc_project.R;
import com.example.mc_project.RecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchBlackSpots extends Fragment {

    private RecyclerView recyclerViewSpots;
    private Spinner spinnerState, spinnerCity;
    private ArrayAdapter arrayAdapterState, arrayAdapterCity;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List listCity;
    private String state;
    private ArrayList<String> arrayListAreas;
    private RecyclerAdapterSpots recyclerAdapterSpots;

    public SearchBlackSpots() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_black_spots, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        arrayListAreas = new ArrayList<>();

        spinnerState = view.findViewById(R.id.spinnerState);
        spinnerCity = view.findViewById(R.id.spinnerCity);
        recyclerViewSpots = view.findViewById(R.id.recyclerViewSpots);

        arrayAdapterState = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterState.add("Select State");
        arrayAdapterState.add("Andhra Pradesh");
        arrayAdapterState.add("Bihar");
        arrayAdapterState.add("Chhatishgarh");
        arrayAdapterState.add("Gujarat");
        arrayAdapterState.add("Haryana");
        arrayAdapterState.add("Karnataka");
        arrayAdapterState.add("Kerala");
        arrayAdapterState.add("Maharashtra");
        arrayAdapterState.add("Madhya Pradesh");
        arrayAdapterState.add("Rajasthan");
        arrayAdapterState.add("Tamil Nadu");
        arrayAdapterState.add("Uttar Pradesh");
        arrayAdapterState.add("West Bengal");
        spinnerState.setAdapter(arrayAdapterState);

        listCity = new ArrayList<String>();
        arrayAdapterCity = new ArrayAdapter<String>(getContext(),  android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterCity.add("Select City");
        recyclerAdapterSpots = new RecyclerAdapterSpots(getContext(), R.layout.blackspot_item, arrayListAreas);

        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                listCity.clear();
                arrayAdapterCity.clear();
                arrayAdapterCity.add("Select City");
                arrayListAreas.clear();



                state = spinnerState.getItemAtPosition(i).toString();
                //state = "Haryana";
                if(!state.equals("Select State")) {
                    databaseReference.child(state).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                Log.i("keys1", "" + dataSnapshot.getKey());
                                listCity.add(dataSnapshot.getKey());
                                arrayAdapterCity.add(dataSnapshot.getKey().toString());

                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    if(!listCity.isEmpty())
                        arrayAdapterCity.add(listCity);
                    spinnerCity.setAdapter(arrayAdapterCity);

                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String city = spinnerCity.getItemAtPosition(i).toString();

                if(!city.equals("Select City")) {
                    databaseReference.child(state).child(city).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String areas[] = snapshot.getValue().toString().split(" : ");

                            for (int i = 0; i < areas.length; i++) {
                                arrayListAreas.add(areas[i]);
                                recyclerAdapterSpots.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        recyclerViewSpots.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewSpots.setLayoutManager(linearLayoutManager);
        recyclerViewSpots.setAdapter(recyclerAdapterSpots);



    }
}

