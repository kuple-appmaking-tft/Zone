package com.kuple.zone.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kuple.zone.MainActivity;
import com.kuple.zone.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //define view objects
    EditText editTextEmail;
    TextInputEditText editTextPassword;
    Button buttonSignin;
    TextView textviewSingin;
    TextView textviewFindPassword;
    ProgressDialog progressDialog;

    // 파이어베이스 객체 선언
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 파이어베이스 초기화
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // 이미 로그인이 된 경우
        if (firebaseUser != null) {
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            // 회원가입시 이메일 인증을 받지 않은경우
            if (firebaseUser.isEmailVerified() == false) {
                startActivity(new Intent(getApplicationContext(), EmailCheckActivity.class));
            } else {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }

        //initializing views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textviewSingin = findViewById(R.id.textViewSignin);
        textviewFindPassword = findViewById(R.id.textViewFindpassword);
        buttonSignin = findViewById(R.id.buttonSignup);
        progressDialog = new ProgressDialog(this);

        //button click event
        buttonSignin.setOnClickListener(this);
        textviewSingin.setOnClickListener(this);
        textviewFindPassword.setOnClickListener(this);
    }

    //firebase userLogin method
    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("로그인중입니다. 잠시 기다려 주세요...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser.isEmailVerified() == false) {
                                startActivity(new Intent(getApplicationContext(), EmailCheckActivity.class));
                            } else {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "아이디나 패스워드가 맞지않습니다", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignin) {
            userLogin();
        }
        if (view == textviewSingin) {
            startActivity(new Intent(this, SignupActivity.class));
        }
        if (view == textviewFindPassword) {
            startActivity(new Intent(this, FindActivity.class));
        }
    }
}
