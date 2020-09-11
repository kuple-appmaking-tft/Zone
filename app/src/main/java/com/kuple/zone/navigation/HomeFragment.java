package com.kuple.zone.navigation;

import android.content.Intent;
import android.os.Bundle;
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

public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerView;
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
}