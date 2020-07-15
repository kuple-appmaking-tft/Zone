package com.kuple.zone.timetable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.tlaabs.timetableview.TimetableView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kuple.zone.R;
import com.kuple.zone.login.LoginActivity;

public class TimeTablePlusActivity extends AppCompatActivity {

    Spinner searchcampusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_plus);

        searchcampusSpinner = findViewById(R.id.search_campus);
        ArrayAdapter searchcampusAdapter = ArrayAdapter.createFromResource(this,R.array.search_campus, android.R.layout.simple_spinner_dropdown_item);
        searchcampusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchcampusSpinner.setAdapter(searchcampusAdapter);

        searchcampusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

}