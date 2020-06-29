package com.kuple.zone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.auth.User;
import com.kuple.zone.navigation.*;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private BoardFragment fragmentBoard;
    private AlarmFragment fragmentAlarm;
    private DetailViewFragment fragmentHome;
    private TimetableFragment fragmentTimetable;
    private UserFragment fragmentUser;
    private FragmentTransaction transaction;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentBoard = new BoardFragment();
        fragmentAlarm = new AlarmFragment();
        fragmentHome = new DetailViewFragment();
        fragmentUser = new UserFragment();
        fragmentTimetable = new TimetableFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, fragmentHome).commitAllowingStateLoss();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                clickHandler(item.getItemId());
                return true;
            }
        });

    }

    public void clickHandler(int num) {
        transaction = fragmentManager.beginTransaction();

        switch (num) {
            case R.id.action_home:
                transaction.replace(R.id.main_content, fragmentHome).commitAllowingStateLoss();
                break;
            case R.id.action_board:
                transaction.replace(R.id.main_content, fragmentBoard).commitAllowingStateLoss();
                break;
            case R.id.action_alarm:
                transaction.replace(R.id.main_content, fragmentAlarm).commitAllowingStateLoss();
                break;
            case R.id.action_account:
                transaction.replace(R.id.main_content, fragmentUser).commitAllowingStateLoss();
                break;
            case R.id.action_timetable:
                transaction.replace(R.id.main_content, fragmentTimetable).commitAllowingStateLoss();
                break;
        }
    }
}