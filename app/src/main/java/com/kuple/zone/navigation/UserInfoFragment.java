package com.kuple.zone.navigation;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kuple.zone.Adapter.CommonAdapter;
import com.kuple.zone.Adapter.ReplyActAdapter;
import com.kuple.zone.Adapter.ReplyAdapter;
import com.kuple.zone.Inteface.OnItemClick;
import com.kuple.zone.R;
import com.kuple.zone.board.CommonboardActivity;
import com.kuple.zone.board.DetailActivity;
import com.kuple.zone.login.LoginActivity;
import com.kuple.zone.model.BoardInfo;
import com.kuple.zone.model.ReplyActModel;
import com.kuple.zone.model.ReplyInfo;
import com.kuple.zone.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserInfoFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "UserFragment";

    private UserModel userModel;

    // Recycler Adapter
    private CommonAdapter mBoardAdapter;
    private ReplyActAdapter mReplyAdapter;

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore mFirestore;

    // loading ui
    private ProgressDialog loadingbar;


    // firebase database reference
    private DatabaseReference mDatabase;

    // Views
    private RecyclerView recyclerView;
    private LinearLayout layoutReply;
    private LinearLayout layoutBoard;
    private LinearLayout layoutScrap;

    //view objects
    private Button buttonLogout;
    private Button buttonUpdate;

    // delete user info all
    private Button textivewDelete;

    // take user info
    private TextView textviewNickname;
    private TextView textviewName;
    private TextView textviewMajor;
    private TextView textviewCollege;
    private TextView textviewCode;
    private TextView textviewState;
    private TextView textviewUpdate;
    private TextView textviewBoardCount;
    private TextView textviewReplyCount;
    private TextView textviewScrapCount;

    @Override
    public void onResume() {
        super.onResume();

        takeUserInfo(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadingbar = new ProgressDialog(getContext());//로딩바

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

    }

    public void takeUserInfo(@Nullable final View v) {
        mFirestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = mFirestore.collection("users").document(firebaseUser.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userModel = documentSnapshot.toObject(UserModel.class);
                if (userModel == null) {
                    Toast.makeText(v.getContext(), "유저 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if(userModel.getBoardInfoList() !=null){
                        showMyActBoardList(userModel);
                        textviewBoardCount.setText("" + userModel.getBoardInfoList().size());
                    }else if(userModel.getReplyList() != null){
                        showMyActReplyList(userModel);
                        textviewReplyCount.setText("" + userModel.getReplyList().size());
                    }

                    if(userModel.getNickname() == null){
                        textviewNickname.setText("닉네임");
                    }else{
                        textviewNickname.setText(userModel.getNickname());
                    }

                    // Student Info
                    if(userModel.getStudentModel() == null){
                        //Toast.makeText(v.getContext(), "학사 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();

                        /*textviewName.setText("이름");
                        textviewCollege.setText("대학");
                        textviewState.setText("외부인");
                        textviewCode.setText("학번");
                        textviewMajor.setText("전공");
                        textviewUpdate.setText("학적사항 업데이트 관련 텍스트\n학적사항 업데이트 관련 텍스트");*/
                    }else{
                        textviewName.setText(userModel.getStudentModel().getName());
                        textviewCollege.setText(userModel.getStudentModel().getCollege());
                        textviewState.setText(userModel.getStudentModel().getAcademicInfo());
                        textviewCode.setText(userModel.getStudentModel().getCode());
                        textviewMajor.setText(userModel.getStudentModel().getMajor());
                    }
                }



            }
        });
    }

    public void initializeViewGroup(View v) {
        //initializing views
        //button
        buttonLogout = v.findViewById(R.id.buttonLogout);
        textivewDelete = v.findViewById(R.id.textviewDelete);
        buttonUpdate = v.findViewById(R.id.update_button);

        //views
        recyclerView = v.findViewById(R.id.recyclerView);
        layoutReply = v.findViewById(R.id.layout_reply);
        layoutBoard = v.findViewById(R.id.layout_board);
        layoutScrap = v.findViewById(R.id.layout_scrab);

        //textview
        textviewNickname = v.findViewById(R.id.textviewNickname);
        textviewCollege = v.findViewById(R.id.textviewCollege);
        textviewCode = v.findViewById(R.id.textviewCode);
        textviewName = v.findViewById(R.id.textviewName);
        textviewMajor = v.findViewById(R.id.textviewMajor);
        textviewState = v.findViewById(R.id.textviewState);
        textviewUpdate = v.findViewById(R.id.update_text);
        textviewBoardCount = v.findViewById(R.id.textview_board_count);
        textviewReplyCount = v.findViewById(R.id.textview_reply_count);
        textviewScrapCount = v.findViewById(R.id.textview_scrap_count);

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
        if (view == textivewDelete) {
            final AlertDialog.Builder alert_confirm = new AlertDialog.Builder(view.getContext());
            alert_confirm.setMessage("! 쿠플존에서 탈퇴하시겠습니까?\n\n쿠플존에서 탈퇴하면 대화 내용, 친구 시간표가 모두 삭제됩니다. 탈퇴하시겠습니까?, 탈퇴시 정보는 1개월간 유지됩니다.").setCancelable(false).setNegativeButton("취소", new DialogInterface.OnClickListener() {
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
                                    if (task.isSuccessful()) {
                                        Toast.makeText(view.getContext(), "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                        getActivity().finish();
                                        startActivity(new Intent(view.getContext(), LoginActivity.class));
                                    } else {
                                        reCredential(view);
                                    }
                                }
                            });
                }
            });
            alert_confirm.show();
        }

        if(view == buttonUpdate){
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            LayoutInflater inflater = getLayoutInflater();
            View inflateView = inflater.inflate(R.layout.dialog_student_info, null);
            builder.setView(inflateView);
            final Button submit = (Button) inflateView.findViewById(R.id.buttonSubmit);
            final Button cancel = (Button) inflateView.findViewById(R.id.buttonCancel);
            final EditText email = (EditText) inflateView.findViewById(R.id.edittextEmailAddress);
            final EditText password = (EditText) inflateView.findViewById(R.id.edittextPassword);

            final AlertDialog portal_alert = builder.create();
            submit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String strEmail = email.getText().toString();
                    String strPassword = password.getText().toString();
                    Toast.makeText(getContext(), strEmail+"/"+strPassword,Toast.LENGTH_LONG).show();
                    buttonUpdate.setText("업데이트 완료!");
                    buttonUpdate.setTextColor(Color.parseColor("#9e9e9e"));
                    buttonUpdate.setBackgroundColor(Color.parseColor("#eaeaea"));

                    // 업데이트 내역을 보여줍니다.
                    textviewUpdate.setText("이름, 대학, 전공, 학번, 학적사항이 업데이트 되었습니다.");

                    portal_alert.dismiss();
                }
            });


            cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    portal_alert.dismiss();
                }
            });


            portal_alert.show();
        }
    }

    // 로그인한지 오래되거나 재인증이 필요한 경우 탈퇴가 이루어지지 않습니다.
    private void reCredential(final View view) {
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
                                        if (task.isSuccessful()) {
                                            firebaseAuth.getCurrentUser().delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(view.getContext(), "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                                                getActivity().finish();
                                                                startActivity(new Intent(view.getContext(), LoginActivity.class));
                                                            } else {
                                                            }

                                                        }
                                                    });
                                        } else {
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

        // 버튼 이벤트
        buttonLogout.setOnClickListener(this);
        textivewDelete.setOnClickListener(this);
        buttonUpdate.setOnClickListener(this);

        layoutBoard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(userModel.getBoardInfoList() != null){
                    showMyActBoardList(userModel);
                    textviewBoardCount.setText("" + userModel.getBoardInfoList().size());
                }
                return false;
            }
        });

        layoutReply.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(userModel.getReplyList() != null){
                    showMyActReplyList(userModel);
                    textviewReplyCount.setText("" + userModel.getReplyList().size());
                }
                return false;
            }
        });

        layoutScrap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                return false;
            }
        });


        // Inflate the layout for this fragment
        return fragmentView;
    }

    private void showMyActBoardList(UserModel userModel){

        loadingbar.setMessage("유저 정보를 불러옵니다.");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        final ArrayList<BoardInfo> mBoardList = new ArrayList<>();

        for (BoardInfo boardInfo : userModel.getBoardInfoList()) {
            if (boardInfo.getDeleted_at().equals("0")) {
                mBoardList.add(boardInfo);
            } else {
                Log.d(TAG, "삭제됬었음");
            }
        }
        OnItemClick onItemClick = new OnItemClick() {
            @Override
            public void onClick(String value) {
            }
        };

        mBoardAdapter = new CommonAdapter(userModel.getBoardInfoList(), getActivity(), firebaseUser, onItemClick, "");
        mBoardAdapter.setOnIemlClickListner(new CommonAdapter.OnItemClickListener() {//Detail 액티비티로 이동
            @Override
            public void onitemClick(View v, int pos) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("DocumentId", mBoardList.get(pos).getDocumentId());
                intent.putExtra("BoardName", mBoardList.get(pos).getBoardName());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mBoardAdapter);
        loadingbar.dismiss();

    }

    private void showMyActReplyList(UserModel userModel) {
        loadingbar.show();

        List<ReplyActModel> replyInfoList = new ArrayList<>();

        for (ReplyActModel replyActModel : userModel.getReplyList()) {

            assert replyActModel != null;
            if (replyActModel.getDeleted_at().equals("0")) {
                replyInfoList.add(replyActModel);
            }
        }

        OnItemClick onItemClick = new OnItemClick() {
            @Override
            public void onClick(String value) {
            }
        };

        mReplyAdapter = new ReplyActAdapter(userModel, replyInfoList, getContext());
        recyclerView.setAdapter(mReplyAdapter);

        loadingbar.dismiss();


    }
}