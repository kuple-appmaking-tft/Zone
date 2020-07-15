package com.kuple.zone.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.github.tlaabs.timetableview.TimetableView;
import com.kuple.zone.R;
import com.kuple.zone.timetable.TimeTablePlusActivity;

import java.util.HashMap;

public class TimetableFragment extends Fragment {

        Button PlusButton;




        public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {


            View view = inflater.inflate(R.layout.fragment_timetable, container, false);

            PlusButton = (Button)view.findViewById(R.id.btn_plus);
            PlusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), TimeTablePlusActivity.class);
                    startActivity(intent);
                }
            });
            return view;

        }



}