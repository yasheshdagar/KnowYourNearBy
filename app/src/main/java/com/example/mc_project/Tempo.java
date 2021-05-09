package com.example.mc_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Tempo extends AppCompatActivity {

    private RecyclerView recyclerViewSpots;
    private Spinner spinnerState, spinnerCity;
    private ArrayAdapter arrayAdapterState, arrayAdapterCity;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List listCity;
    private String state;
    private ArrayList<String> arrayListAreas;
    private RecyclerAdapterSpots recyclerAdapterSpots;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempo);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        arrayListAreas = new ArrayList<>();

        spinnerState = findViewById(R.id.spinnerState);
        spinnerCity = findViewById(R.id.spinnerCity);
        recyclerViewSpots = findViewById(R.id.recyclerViewSpots);

        arrayAdapterState = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item);
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
        arrayAdapterCity = new ArrayAdapter<String>(Tempo.this, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterCity.add("Select City");
        recyclerAdapterSpots = new RecyclerAdapterSpots(this, R.layout.blackspot_item, arrayListAreas);



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

                    //if(!listCity.isEmpty())
                    //arrayAdapterCity.add(listCity);
                    spinnerCity.setAdapter(arrayAdapterCity);

                    Log.i("cityLisy", listCity.toString());

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

                arrayListAreas.clear();

                if(!city.equals("Select City")) {
                    databaseReference.child(state).child(city).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String areas[] = snapshot.getValue().toString().split(" : ");

                            for (int i = 0; i < areas.length; i++) {
                                arrayListAreas.add(areas[i]);
                                Log.i("areadr", arrayListAreas.toString());
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

        recyclerViewSpots.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewSpots.setLayoutManager(linearLayoutManager);
        recyclerViewSpots.setAdapter(recyclerAdapterSpots);



    }
}