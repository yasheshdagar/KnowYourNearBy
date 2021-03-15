package com.example.mc_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddDataFragment extends Fragment {

    private EditText Iname,Ilatitude,Ilongitude,Iradius,Icolor,IType;
    private Button btnsave;
    public AddDataFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addfgm, container, false);
        Iname = view.findViewById(R.id.name);
        Ilatitude = view.findViewById(R.id.latitude);
        Ilongitude = view.findViewById(R.id.longitude);
        Iradius = view.findViewById(R.id.radius);
        Icolor = view.findViewById(R.id.color);
        IType = view.findViewById(R.id.Type);
        btnsave = view.findViewById(R.id.save);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Iname.getText().toString();
                double latitude = Double.parseDouble(Ilatitude.getText().toString());
                double longitude = Double.parseDouble(Ilongitude.getText().toString());
                float radius = Float.parseFloat(Iradius.getText().toString());
                int color = Integer.parseInt(Icolor.getText().toString());
                String type = IType.getText().toString();

                GeoFence gf = new GeoFence();
                gf.setId(0);
                gf.setName(name);
                gf.setLatitude(latitude);
                gf.setLongitude(longitude);
                gf.setRadius(radius);
                gf.setColor(color);
                gf.setType(type);

                MainActivity.rdb.geoFenceDao().addGeoFence(gf);
                Toast.makeText(getActivity(),"Data Saved !!",Toast.LENGTH_SHORT).show();
                Iname.setText("");
                Ilatitude.setText("");
                Ilongitude.setText("");
                Iradius.setText("");
                Icolor.setText("");
                IType.setText("");
            }
        });
        return view;
    }
}
