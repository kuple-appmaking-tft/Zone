<<<<<<< HEAD:app/src/main/java/com/kuple/zone/navigation/UserFragment.java
package com.kuple.zone.navigation;
=======
package com.kuple.zone.login;
>>>>>>> origin/seo:app/src/main/java/com/kuple/zone/login/MypageActivity.java

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
<<<<<<< HEAD:app/src/main/java/com/kuple/zone/navigation/UserFragment.java
import com.kuple.zone.R;
import com.kuple.zone.login.LoginActivity;
=======
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.kuple.zone.R;
import com.google.firebase.database.FirebaseDatabase;
import com.kuple.zone.model.UserModel;
>>>>>>> origin/seo:app/src/main/java/com/kuple/zone/login/MypageActivity.java

public class UserFragment extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "UserFragment";

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore mFirestore;

    // firebase database reference
    private DatabaseReference mDatabase;

    //view objects
    private Button buttonLogout;

    // delete user info all
    private TextView textivewDelete;

    // take user info
    private TextView textviewNickname;
    private TextView textviewId;
    private TextView textviewName;
    private TextView textviewGroup;
    private TextView textviewCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user);

        initializeViewGroup();

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        // 유저 정보를 뷰에 보여줍니다.
        takeUserInfo();

        //logout button event
        buttonLogout.setOnClickListener(this);
        textivewDelete.setOnClickListener(this);
    }

    public void takeUserInfo(){
        //유저가 있다면, null이 아니면 계속 진행
        firebaseAuth.getCurrentUser().reload();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        mFirestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = mFirestore.collection("users").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserModel userModel = documentSnapshot.toObject(UserModel.class);
                if (userModel == null) {
                    Toast.makeText(MypageActivity.this, "유저 정보를 가져오지 못했습니다.", Toast.LENGTH_LONG).show();
                } else {
                    textviewId.setText("아이디: "+userModel.getUserEmail());
                    textviewName.setText("이름: "+userModel.getNickname());
                    textviewGroup.setText("학적: "+userModel.getPhoneNumber());
                }
            }
        });
    }

    public void initializeViewGroup(){
        //initializing views
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        textivewDelete = (TextView) findViewById(R.id.textviewDelete);
        textviewNickname = (TextView) findViewById(R.id.textviewNickname);
        textviewId = (TextView) findViewById(R.id.textviewId);
        textviewName = (TextView) findViewById(R.id.textviewName);
        textviewGroup = (TextView) findViewById(R.id.textviewGroup);
        textviewCode = (TextView) findViewById(R.id.textviewCode);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        // 회원탈퇴를 클릭하면 회원정보를 삭제한다.
        // 회원 탈퇴를 하더라도 회원의 정보를 Database에서는 3개월간 저장해야 하기에 DB에서는 삭제하지 않습니다.
        // Admin에서 회원의 DB 정보를 삭제해야 합니다.
        // 회원 가입 후 바로 유저의 삭제는 이루어지지 않습니다. (로그인 내역이 있어야 합니다)
        if(view == textivewDelete) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(UserFragment.this);
            alert_confirm.setMessage("정말 계정을 삭제 할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(UserFragment.this, "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        }
                                    });
                        }
                    }
            );
<<<<<<< HEAD:app/src/main/java/com/kuple/zone/navigation/UserFragment.java
            alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(UserFragment.this, "취소", Toast.LENGTH_LONG).show();
                }
            });
            alert_confirm.show();
=======
>>>>>>> origin/seo:app/src/main/java/com/kuple/zone/login/MypageActivity.java
        }
    }
}