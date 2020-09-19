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
import com.kuple.zone.Adapter.FeedAdapter;
import com.kuple.zone.Adapter.HorizentalAdapter;
import com.kuple.zone.Inteface.OnItemClick;
import com.kuple.zone.R;
import com.kuple.zone.board.CommonboardActivity;
import com.kuple.zone.board.WebViewActivity;
import com.kuple.zone.model.BoardInfo;
import com.kuple.zone.model.UserModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ArrayList<BoardInfo> modelFeedArrayList;
    private FeedAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    //private OnItemClick mCallback;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.feedKuple);
        String[] boardarray ={"쿠플광장","고민상담","쑥덕쑥덕","졸업생 게시판","쿠플툰","먹쿠먹쿠","강의평가","합격수기","취업광장","스터디게시판","꿀팁게시판"
                ,"부동산","구인구직","중고거래","분실물신고","총학생회"} ;
        for(String s:boardarray){
            RetrieveFireStore(s);
        }


//        mAdapter = new FeedAdapter(modelFeedArrayList);
//
//
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerView.setAdapter(mAdapter);


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

        final DocumentReference docRef = mStore.collection("users").document(firebaseUser.getUid());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("TAG", "Current data: " + snapshot.getData());
                    final UserModel userModel=snapshot.toObject(UserModel.class);
                    HorizentalAdapter adapter=new HorizentalAdapter(getContext(),userModel.getFavoritList());
                    adapter.setOnIemlClickListner(new ChildAdapter.OnItemClickListener() {
                        @Override
                        public void onitemClick(View v, int pos) {
                            Intent intent=new Intent(getContext(), CommonboardActivity.class);
                            intent.putExtra("BoardName",userModel.getFavoritList().get(pos));
                            startActivity(intent);
                        }
                    });
                    mRecyclerView.setAdapter(adapter);
                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });
        return view;
    }

    private void RetrieveFireStore(String collectionname) {

        mStore.collection(collectionname).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<BoardInfo> feed_list=new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        BoardInfo boardInfo=document.toObject(BoardInfo.class);
                        feed_list.add(boardInfo);
                    }
                    FeedAdapter feedAdapter=new FeedAdapter(feed_list,getContext());
                    mRecyclerView.setAdapter(feedAdapter);

                } else {

                }
            }
        });
    }
}