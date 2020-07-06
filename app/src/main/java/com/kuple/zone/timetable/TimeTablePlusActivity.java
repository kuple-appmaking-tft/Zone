package com.kuple.zone.timetable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.github.tlaabs.timetableview.TimetableView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kuple.zone.R;
import com.kuple.zone.login.LoginActivity;

public class TimeTablePlusActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_plus);

    }
}