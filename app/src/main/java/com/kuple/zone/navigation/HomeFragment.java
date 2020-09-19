package com.kuple.zone.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.kuple.zone.Adapter.ChildAdapter;
import com.kuple.zone.Adapter.FeedAdapter;
import com.kuple.zone.Adapter.HorizentalAdapter;
import com.kuple.zone.R;
import com.kuple.zone.board.CommonboardActivity;
import com.kuple.zone.board.WebViewActivity;
import com.kuple.zone.model.BoardInfo;
import com.kuple.zone.model.UserModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<BoardInfo> modelFeedArrayList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.feedKuple);
        mAdapter = new FeedAdapter(modelFeedArrayList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        Log.e("fragment", "HomeFragment");
        ImageView btnMeal = (ImageView) view.findViewById(R.id.btnMeal);
        btnMeal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),WebViewActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        //3초후에 해당 adapter를 갱신하고 동글뱅이를 닫아준다.setRefreshing(false);
        //핸들러를 사용하는 이유는 일반쓰레드는 메인쓰레드가 가진 UI에 접근할 수 없기 때문에 핸들러를 이용해서
        //메시지큐에 메시지를 전달하고 루퍼를 이용하여 순서대로 UI에 접근한다.

        //반대로 메인쓰레드에서 일반 쓰레드에 접근하기 위해서는 루퍼를 만들어야 한다.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //해당 어댑터를 서버와 통신한 값이 나오면 됨
                FeedAdapter mAdapter = new FeedAdapter (HomeFragment.this, android.R.layout.list_content);;
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },3000);
    }

}