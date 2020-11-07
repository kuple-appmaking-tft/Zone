package com.kuple.zone.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.irshulx.Editor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.StructuredQuery;
import com.kuple.zone.Adapter.FeedAdapter;
import com.kuple.zone.MainActivity;
import com.kuple.zone.R;
import com.kuple.zone.board.DetailActivity;
import com.kuple.zone.board.MealWebViewActivity;
import com.kuple.zone.board.WebViewActivity;
import com.kuple.zone.model.BoardInfo;
import com.kuple.zone.model.RoundImageView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerview;
    private CircleImageView mCircleview;
    private RoundImageView roundImageView;
    private ArrayList<BoardInfo> modelFeedArrayList;
    private FeedAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private SwipeRefreshLayout refresh;

    ArrayList<BoardInfo> feed_list = new ArrayList<>();


    //private OnItemClick mCallback;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerview = (RecyclerView) view.findViewById(R.id.feedKuple);
        mCircleview = (CircleImageView) view.findViewById(R.id.img_profile);
        refresh=view.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(),"새로고침",Toast.LENGTH_SHORT).show();
                getActivity().finish();
                getContext().startActivity(new Intent(getContext(), MainActivity.class));
                refresh.setRefreshing(false);

            }
        });
        String[] boardarray = {"쿠플광장", "고민상담", "쑥덕쑥덕", "졸업생 게시판", "쿠플툰", "먹쿠먹쿠", "강의평가", "합격수기", "취업광장", "스터디게시판", "꿀팁게시판"
                , "부동산", "구인구직", "중고거래", "분실물신고", "총학생회"};

        for (int i = 0; i < 16; i++) {
            RetrieveFireStore(boardarray[i], mRecyclerview);
        }

        //Log.d("반복문끝",String.valueOf(feed_list.size()));

//                    feedAdapter.setOnIemlClickListner(new FeedAdapter.OnItemClickListener() {
//                        @Override
//                        public void onitemClick(View v, int pos) {
//                            BoardInfo data=feed_list.get(pos);
//                            Intent intent=new Intent(getActivity(), DetailActivity.class);
//                            intent.putExtra("BoardName",data.getBoardTitle());
//                            intent.putExtra("DocumentId",data.getDocumentId());
//                            startActivity(intent);
//
//                        }
//                    });
        Log.e("fragment", "HomeFragment");
        ImageView btnLib = (ImageView) view.findViewById(R.id.btnLib);
        btnLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                startActivity(intent);
            }
        });
        ImageView btnMeal = (ImageView) view.findViewById(R.id.btnMeal);
        btnMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MealWebViewActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void RetrieveFireStore(final String collectionName, final RecyclerView recyclerView) {

            mStore.collection(collectionName).orderBy("viewcount").startAt(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        BoardInfo boardInfo = document.toObject(BoardInfo.class);
//                        Log.d("정보",boardInfo.getNickname());
                        feed_list.add(boardInfo);
                        Log.d("불러온정보", collectionName);
                    }
                    FeedAdapter feedAdapter = new FeedAdapter(feed_list, getContext(),getActivity());

                    feedAdapter.setOnIemlClickListner(new FeedAdapter.OnItemClickListener() {
                        @Override
                        public void onitemClick(View v, int pos) {
                            Intent intent=new Intent(getActivity(),DetailActivity.class);
                            Log.d("불러온정보2", collectionName);
                            intent.putExtra("BoardName",feed_list.get(pos).getBoardName());
                            intent.putExtra("DocumentId",feed_list.get(pos).getDocumentId());
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(feedAdapter);
                } else {
                }
            }
            //
        });
    }
}