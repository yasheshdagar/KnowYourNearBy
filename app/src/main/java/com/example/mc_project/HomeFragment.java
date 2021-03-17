package com.example.mc_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment implements View.OnClickListener{
    private Button btnadd,btnview;
    public HomeFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homefgm, container, false);
        btnadd = view.findViewById(R.id.buttonAdd);
        btnview = view.findViewById(R.id.buttonView);
        btnadd.setOnClickListener(this);
        btnview.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonAdd:
                DatabaseActivity.fmng.beginTransaction().replace(R.id.Container,new AddDataFragment(),null).addToBackStack(null).commit();
                //DatabaseActivity.fmng.beginTransaction().replace(R.id.Container,new AddDataFragment(),null).addToBackStack(null).commit();
                break;
            case R.id.buttonView:
                DatabaseActivity.fmng.beginTransaction().replace(R.id.Container,new AddDataFragment(),null).addToBackStack(null).commit();
               // DatabaseActivity.fmng.beginTransaction().replace(R.id.Container,new ViewFragment(),null).addToBackStack(null).commit();
                break;

        }

    }
}
