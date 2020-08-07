package com.kuple.zone.navigation;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kuple.zone.Adapter.NotificationAdapter;
import com.kuple.zone.R;
import com.kuple.zone.board.DetailActivity;
import com.kuple.zone.model.NotiInfo;

import java.util.ArrayList;
import java.util.List;

public class AlarmFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    private RecyclerView mRecyclerView;
    private NotificationAdapter mNotificationAdapter;
    private List<NotiInfo> list ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.notifications,container,false);
        mRecyclerView=view.findViewById(R.id.notifications_RecyclerView);
        mStore.collection("users").document(firebaseUser.getUid()).collection("notification")
                .orderBy("date", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list= new ArrayList<NotiInfo>();
                if(task.getResult()!=null){
                    for(QueryDocumentSnapshot data:task.getResult()){
                        NotiInfo notiInfo=data.toObject(NotiInfo.class);
                        list.add(notiInfo);
                    }
                    mNotificationAdapter=new NotificationAdapter(list);
                    mNotificationAdapter.setOnIemlClickListner(new NotificationAdapter.OnItemClickListener() {
                        @Override
                        public void onitemClick(View v, int pos) {
                            Log.d("클릭","내가쓴글:"+String.valueOf(pos));
                            Intent intent=new Intent(getActivity(), DetailActivity.class);
                            intent.putExtra("DocumentId",list.get(pos).getDocumentId());
                            intent.putExtra("BoardName",list.get(pos).getBoardName());
                            startActivity(intent);
                        }
                    });
                    mRecyclerView.setAdapter(mNotificationAdapter);
                }
            }
        });



        return view;
    }
}
