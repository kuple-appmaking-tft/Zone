package com.kuple.zone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kuple.zone.commonboard.CommonboardActivity;
import com.kuple.zone.login.EmailCheckActivity;
import com.kuple.zone.login.MypageActivity;
import com.kuple.zone.photoboard.PhotoboardActivity;

public class MainActivity extends AppCompatActivity {

    // 파이어베이스 객체 선언
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 파이어베이스 초기화
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();



        findViewById(R.id.main_CommonBoard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CommonboardActivity.class));
            }
        });
        findViewById(R.id.main_PhotoBoard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PhotoboardActivity.class));
            }
        });

        findViewById(R.id.main_mypage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MypageActivity.class));
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        firebaseUser = firebaseAuth.getCurrentUser();

        // 이미 로그인이 된 경우
        if(firebaseUser == null){
            // 로그인이 안된 경우 이 액티비티를 종료합니다.
            finish();
        }
    }
}
