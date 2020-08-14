package com.kuple.zone.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.kuple.zone.R;

public class HomeFragment extends Fragment implements View.OnClickListener{
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(getView().getId()){
            case R.id.btnBus:
            case R.id.btnMeal:
            case R.id.btnTimeTable:
        }
    }
}