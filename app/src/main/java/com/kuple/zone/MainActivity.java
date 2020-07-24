package com.kuple.zone;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.kuple.zone.login.LoginActivity;
import com.kuple.zone.navigation.AlarmFragment;
import com.kuple.zone.navigation.BoardFragment;
import com.kuple.zone.navigation.DetailViewFragment;
import com.kuple.zone.navigation.TimetableFragment;
import com.kuple.zone.navigation.UserInfoFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private BoardFragment fragmentBoard;
    private AlarmFragment fragmentAlarm;
    private DetailViewFragment fragmentHome;
    private TimetableFragment fragmentTimetable;
    private UserInfoFragment fragmentUser;
    private FragmentTransaction transaction;
    private BottomNavigationView bottomNavigationView;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();

        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }


        fragmentManager = getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentBoard = new BoardFragment();
        fragmentAlarm = new AlarmFragment();
        fragmentHome = new DetailViewFragment();
        fragmentUser = new UserInfoFragment();
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

        if (num == R.id.action_home) {
            transaction.replace(R.id.main_content, fragmentHome).commitAllowingStateLoss();
        } else if (num == R.id.action_board) {
            transaction.replace(R.id.main_content, fragmentBoard).commitAllowingStateLoss();
        } else if (num == R.id.action_alarm) {
            transaction.replace(R.id.main_content, fragmentAlarm).commitAllowingStateLoss();
        } else if (num == R.id.action_account) {
            transaction.replace(R.id.main_content, fragmentUser).commitAllowingStateLoss();
        } else if (num == R.id.action_timetable) {
            transaction.replace(R.id.main_content, fragmentTimetable).commitAllowingStateLoss();
        }
    }
}