package com.kuple.zone.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kuple.zone.Adapter.ChildAdapter;
import com.kuple.zone.Adapter.CommonAdapter;
import com.kuple.zone.Adapter.FeedAdapter;
import com.kuple.zone.Adapter.HorizentalAdapter;
import com.kuple.zone.Inteface.OnItemClick;
import com.kuple.zone.R;
import com.kuple.zone.board.CommonboardActivity;
import com.kuple.zone.board.DetailActivity;
import com.kuple.zone.board.WebViewActivity;
import com.kuple.zone.model.BoardInfo;
import com.kuple.zone.model.UserModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerView0, mRecyclerView1, mRecyclerView2, mRecyclerView3, mRecyclerView4, mRecyclerView5, mRecyclerView6, mRecyclerView7, mRecyclerView8, mRecyclerView9, mRecyclerView10, mRecyclerView11, mRecyclerView12, mRecyclerView13, mRecyclerView14,
            mRecyclerView15;
    private ArrayList<BoardInfo> modelFeedArrayList;
    private FeedAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    ArrayList<BoardInfo> feed_list = new ArrayList<>();

    //private OnItemClick mCallback;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView0 = (RecyclerView) view.findViewById(R.id.feedKuple0);



        String[] boardarray = {"쿠플광장", "고민상담", "쑥덕쑥덕", "졸업생 게시판", "쿠플툰", "먹쿠먹쿠", "강의평가", "합격수기", "취업광장", "스터디게시판", "꿀팁게시판"
                , "부동산", "구인구직", "중고거래", "분실물신고", "총학생회"};

        for (int i = 0; i < 16; i++) {


            RetrieveFireStore(boardarray[i], mRecyclerView0);


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
        ImageView btnMeal = (ImageView) view.findViewById(R.id.btnMeal);
        btnMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    private void RetrieveFireStore(final String collectionname, final RecyclerView recyclerView) {

        mStore.collection(collectionname).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        BoardInfo boardInfo = document.toObject(BoardInfo.class);
//                        Log.d("정보",boardInfo.getNickname());
                        feed_list.add(boardInfo);
                        Log.d("불러온정보", collectionname);
                    }
                    FeedAdapter feedAdapter = new FeedAdapter(feed_list, getContext());
                    feedAdapter.setOnIemlClickListner(new FeedAdapter.OnItemClickListener() {
                        @Override
                        public void onitemClick(View v, int pos) {
                            Intent intent=new Intent(getActivity(),DetailActivity.class);
                            Log.d("불러온정보2", collectionname);
                            intent.putExtra("BoardName",feed_list.get(pos).getBoardName());
                            intent.putExtra("DocumentId",feed_list.get(pos).getDocumentId());
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(feedAdapter);
                } else {

                }
            }
        });
    }
}