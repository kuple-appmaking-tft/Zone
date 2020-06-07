package com.kuple.zone.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kuple.zone.MainActivity;
import com.kuple.zone.MypageActivity;
import com.kuple.zone.R;

public class EmailCheckActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "EmailCheckActivity";

    Button buttonAuthCheck;
    TextView textviewResend;
    Button emailCheckLogout;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_check);

        buttonAuthCheck= (Button) findViewById(R.id.buttonAuthCheck);
        textviewResend= (TextView) findViewById(R.id.textviewResend);
        emailCheckLogout= (Button) findViewById(R.id.emailCheckLogout);

        buttonAuthCheck.setOnClickListener(this);
        textviewResend.setOnClickListener(this);
        emailCheckLogout.setOnClickListener(this);
    }

    // 유저의 이메일 인증 유무를 확인합니다.
    public void firebaseAuthCheck(){

        firebaseAuth.getInstance().getCurrentUser().reload();
        firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        // 이미 로그인이 된 경우
        if(firebaseUser != null){
            // 회원가입시 이메일 인증을 받지 않은경우
            if(firebaseUser.isEmailVerified() == false){
                Toast.makeText(EmailCheckActivity.this, "메일 인증이 이루어지지 않았습니다. \n다시 한번 확인해주세요!", Toast.LENGTH_SHORT).show();
            }else{
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }
    }

    public void sendEmail(){
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EmailCheckActivity.this, "이메일 인증 메일을 전송했습니다. \n가입한 이메일에서 확인해주세요!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(EmailCheckActivity.this, "인증 메일 전송에 실패했습니다. \n1분 뒤에 다시 시도해주세요!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == emailCheckLogout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        if(view == buttonAuthCheck) {
            firebaseAuthCheck();
        }

        if(view == textviewResend){
            sendEmail();
        }
    }
}
