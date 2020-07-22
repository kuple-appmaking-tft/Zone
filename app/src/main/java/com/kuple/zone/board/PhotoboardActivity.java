package com.kuple.zone.board;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kuple.zone.Adapter.PhotoboardAdapter;
import com.kuple.zone.MainActivity;
import com.kuple.zone.R;
import com.kuple.zone.model.BoardInfo;

import java.util.ArrayList;
import java.util.List;

public class PhotoboardActivity extends AppCompatActivity {
    private RecyclerView mMainRecyclerView;
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();
    private PhotoboardAdapter mainAdapter;
    private List<BoardInfo> mPostingInfoList;
    private List<String> mDocumentIdList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String mBoardName;
    private ImageView mSerch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoboard);
        mBoardName=getIntent().getStringExtra("BoardName");
        findViewById(R.id.float_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PhotoboardActivity.this, WriteActivity.class);
                intent.putExtra("BoardName",mBoardName);
                startActivityForResult(intent,99);
            }
        });
        mMainRecyclerView=findViewById(R.id.main_recycler_view);
        mMainRecyclerView.setHasFixedSize(true);
        swipeRefreshLayout=findViewById(R.id.main_SwipeRefreshLayout);
        retreive_Testing(mBoardName);
        deepLinkSwitcher();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        String getString=getIntent().getStringExtra("Refresh");
        try {//업로드 하자마자 자동 refresh
            if(getString.equals("success")){
                mainAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){

        }
        mSerch=findViewById(R.id.photo_serch);
        mSerch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PhotoboardActivity.this, SearchActivity.class);
                intent.putExtra("BoardName",mBoardName);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 99&&requestCode==99) {//write 액티비티실행후 나온 결과 받아오기.
            Log.d("양성열","리절트 함수 실행");
            retreive_Testing(mBoardName);//업로드 화면 끝났을때
        }
//        else if(resultCode == RESULT_CANCELED) {
//            Log.d("양성열","실패");
//        }
    }
    public void retreive_Testing(final String mBoardName){
        mPostingInfoList=new ArrayList<>();

        mStore.collection(mBoardName)
                .orderBy("date", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //        for (QueryDocumentSnapshot document : task.getResult()) {
                        //Log.d("yangseongyeal",document.getId());
                        BoardInfo postingInfo = document.toObject(BoardInfo.class);
                        mPostingInfoList.add(postingInfo);
                    }
                    mainAdapter = new PhotoboardAdapter(mPostingInfoList, PhotoboardActivity.this);
                    mainAdapter.setOnIemlClickListner(new PhotoboardAdapter.OnItemClickListener() {//클릭됬을때
                        @Override
                        public void onitemClick(View v, int pos) {
                            Intent intent=new Intent(PhotoboardActivity.this,DetailActivity.class);
                            intent.putExtra("DocumentId",mDocumentIdList.get(pos));
                            intent.putExtra("BoardName",mBoardName);
                            startActivity(intent);
                        }
                    });
                    mMainRecyclerView.setAdapter(mainAdapter);
                } else {

                }
            }
        });
    }

    private void deepLinkSwitcher() {//딥링크받음
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null) {
            String intentData = intent.getData().toString();
            String appstring="https://www.photopostiongyang.com/";
            String documentId=intentData.substring(34,intentData.length());

            if (intentData != null && !intentData.isEmpty()) {//파이어베이스에서 만든 링크는 잘 들어옴.
                /*
                Here
                SCHEME = https
                HOST = www.pexels.com
                PATH PATTERN = /@md-emran-hossain-emran-11822
                * */
                Intent documentIdIntent=new Intent(PhotoboardActivity.this,DetailActivity.class);
                documentIdIntent.putExtra("DocumentId",documentId);
                startActivity(documentIdIntent);
            }
        }
    }//end function

}
