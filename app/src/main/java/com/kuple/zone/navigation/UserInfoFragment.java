package com.kuple.zone.navigation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kuple.zone.R;
import com.kuple.zone.login.LoginActivity;
import com.kuple.zone.model.UserModel;

public class UserInfoFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "UserFragment";

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore mFirestore;

    // firebase database reference
    private DatabaseReference mDatabase;

    //view objects
    private Button buttonLogout;

    // delete user info all
    private Button textivewDelete;

    // take user info
    private TextView textviewNickname;
    private TextView textviewId;
    private TextView textviewName;
    private TextView textviewGroup;
    private TextView textviewCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public void takeUserInfo(final View fragmentView){
        mFirestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = mFirestore.collection("users").document(firebaseUser.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserModel userModel = documentSnapshot.toObject(UserModel.class);
                if (userModel == null) {
                    Toast.makeText(fragmentView.getContext(), "유저 정보를 가져오지 못했습니다.", Toast.LENGTH_LONG).show();
                } else {
                    textviewNickname.setText("닉네임: "+userModel.getNickname());
                    textviewId.setText("아이디: "+userModel.getUserEmail());
                    textviewName.setText("이름: "+userModel.getNickname());
                    textviewGroup.setText("학적: "+userModel.getPhoneNumber());
                }
            }
        });
    }

    public void initializeViewGroup(View v){
        //initializing views
        buttonLogout = (Button) v.findViewById(R.id.buttonLogout);
        textivewDelete = (Button) v.findViewById(R.id.textviewDelete);
        textviewNickname = (TextView) v.findViewById(R.id.textviewNickname);
        textviewId = (TextView) v.findViewById(R.id.textviewId);
        textviewName = (TextView) v.findViewById(R.id.textviewName);
        textviewGroup = (TextView) v.findViewById(R.id.textviewGroup);
        textviewCode = (TextView) v.findViewById(R.id.textviewCode);
    }

    @Override
    public void onClick(final View view) {
        if (view == buttonLogout) {
            firebaseAuth.signOut();
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        // 회원탈퇴를 클릭하면 회원정보를 삭제한다.
        // 회원 탈퇴를 하더라도 회원의 정보를 Database에서는 3개월간 저장해야 하기에 DB에서는 삭제하지 않습니다.
        // Admin에서 회원의 DB 정보를 삭제해야 합니다.
        // 회원 가입 후 바로 유저의 삭제는 이루어지지 않습니다. (로그인 내역이 있어야 합니다)
        if(view == textivewDelete) {
            final AlertDialog.Builder alert_confirm = new AlertDialog.Builder(view.getContext());
            alert_confirm.setMessage("정말 계정을 삭제 할까요?").setCancelable(false).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            }).setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            firebaseAuth.getCurrentUser().delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(view.getContext(), "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                                getActivity().finish();
                                                startActivity(new Intent(view.getContext(), LoginActivity.class));
                                            }else{
                                                reCredential(view);
                                            }

                                        }
                                    });
                        }
            });
            alert_confirm.show();
        }
    }

    private void reCredential(final View view)
    {
        // 로그인한지 오래되거나 재인증이 필요한 경우 탈퇴가 이루어지지 않습니다.
        final EditText edittext = new EditText(view.getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("계정의 비밀번호를 입력해주세요.");
        builder.setView(edittext);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(firebaseUser.getEmail(), edittext.toString());
                        firebaseUser.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            firebaseAuth.getCurrentUser().delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(view.getContext(), "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                                                getActivity().finish();
                                                                startActivity(new Intent(view.getContext(), LoginActivity.class));
                                                            }else{
                                                                reCredential(view);
                                                            }

                                                        }
                                                    });
                                        }else{
                                            Toast.makeText(view.getContext(), "계정 삭제에 실패했습니다.\n서비스 센터에 문의해주세요!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_user_info, container, false);
        initializeViewGroup(fragmentView);

        // 유저 정보를 뷰에 보여줍니다.
        takeUserInfo(fragmentView);

        //logout button event
        buttonLogout.setOnClickListener(this);
        textivewDelete.setOnClickListener(this);

        // Inflate the layout for this fragment
        return fragmentView;
    }
}