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
    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment들
    private DetailViewFragment menu1Fragment = new DetailViewFragment();
    private BoardFragment menu2Fragment = new BoardFragment();
    private TimetableFragment menu3Fragment = new TimetableFragment();
    private AlarmFragment menu4Fragment = new AlarmFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, menu1Fragment).commitAllowingStateLoss();

        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.action_home: {
                        transaction.replace(R.id.main_content, menu1Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.action_board: {
                        transaction.replace(R.id.main_content, menu2Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.action_timetable: {
                        transaction.replace(R.id.main_content, menu3Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.action_alarm: {
                        transaction.replace(R.id.main_content, menu4Fragment).commitAllowingStateLoss();
                        break;
                    }
                }

                return true;
            }
        });
    }
}