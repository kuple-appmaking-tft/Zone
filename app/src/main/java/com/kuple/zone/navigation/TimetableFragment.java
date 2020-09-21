package com.kuple.zone.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kuple.zone.R;
import com.kuple.zone.model.Timetable;
import com.kuple.zone.timetable.TimeTablePlusActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class TimetableFragment extends Fragment {

    Button PlusButton;
    TimetableView timetable;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser user= mAuth.getCurrentUser();
    ArrayList<Schedule> item = new ArrayList<>();
    ImageView refresh;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_timetable, container, false);

        PlusButton = (Button) view.findViewById(R.id.btn_plus);
        PlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TimeTablePlusActivity.class);
                startActivity(intent);
            }
        });
        timetable=view.findViewById(R.id.timetable);
        refresh=view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetreiveFireStore();
            }
        });

        RetreiveFireStore();
        return view;

    }
    private void RetreiveFireStore() {
        db.collection("users")
                .document(user.getUid())
                .collection("TimeTable")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Schedule schedule=document.toObject(Schedule.class);
                                ArrayList<Schedule> item = new ArrayList<>();
                                item.add(schedule);
                                timetable.add(item);
                            }
                        } else {

                        }
                        ;
                    }
                });
    }


}