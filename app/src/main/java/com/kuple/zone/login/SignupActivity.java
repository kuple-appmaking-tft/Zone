package com.kuple.zone.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kuple.zone.MainActivity;
import com.kuple.zone.R;
import com.kuple.zone.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignupActivity";
    // Views
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextPasswordCheck;
    EditText editTextNickname;
    Button buttonSignup;
    TextView signup_condition;

    Button buttonCancel;
    ProgressDialog progressDialog;

    // Firebase 정의
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initializig firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStore = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 profile 액티비티를 연다.
            startActivity(new Intent(getApplicationContext(), MainActivity.class)); //추가해 줄 ProfileActivity
        }
        //initializing views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordCheck = findViewById(R.id.editTextPasswordCheck);

        editTextNickname = findViewById(R.id.editTextNickname);
        buttonCancel = findViewById(R.id.buttonCancel);
        signup_condition = findViewById(R.id.signup_condition);

        buttonSignup = findViewById(R.id.buttonSignup);
        progressDialog = new ProgressDialog(this);

        //button click event
        buttonSignup.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        signup_condition.setOnClickListener(this);
    }

    //button click event
    @Override
    public void onClick(View view) {
        if (view == buttonSignup) {
            //TODO
            registerUser();
        }

        if (view == buttonCancel) {
            finish();
        }

        if(view == signup_condition){
            startActivity(new Intent(getApplicationContext(), AgreementActivity.class));
        }
    }

    //Firebse creating a new user
    private void registerUser() {
        //사용자가 입력하는 email, password를 가져온다.
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String passwordCheck = editTextPasswordCheck.getText().toString().trim();

        String nickname = editTextNickname.getText().toString().trim();

        //email과 password가 비었는지 아닌지를 체크 한다.
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(this, "닉네임을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(passwordCheck)) {
            Toast.makeText(this, "비밀번호 확인을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        signupFunc(email, password);
    }

    public void signupFunc(final String email, final String password) {

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.show();
                        if (task.isSuccessful()) {
                            checkProcess(email, password);
                        } else {
                            //에러발생시
                            Toast.makeText(SignupActivity.this, "회원가입에 실패했습6니다. \n\n - 이미 등록된 이메일  \n - 암호 최소 6자리 이상", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    public void checkProcess(final String email, final String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            firebaseUser = firebaseAuth.getCurrentUser();
                            final String uid = firebaseAuth.getUid();
                            UserModel userModel = new UserModel();
                            userModel.setUid(uid);
                            userModel.setUsermsg("...");
                            userModel.setUserEmail(editTextEmail.getText().toString());
                            userModel.setNickname(editTextNickname.getText().toString());
                            userModel.setFavoritList(new ArrayList<String>());

                            firebaseStore.collection("users")
                                    .document(firebaseUser.getUid())
                                    .set(userModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // 이메일 인증 확인 메일을 전송합니다.
                                            sendEmail();

                                            finish();
                                            startActivity(new Intent(getApplicationContext(), EmailCheckActivity.class));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public void sendEmail() {
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "이메일 인증 메일을 전송했습니다. \n가입한 이메일에서 확인해주세요!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignupActivity.this, "인증 메일 전송에 실패했습니다. \n쿠플존에 문의해주세요!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}