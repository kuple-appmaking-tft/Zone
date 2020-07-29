package com.kuple.zone.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.transition.Transition;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kuple.zone.R;
import com.kuple.zone.login.LoginActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimeTablePlusActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ArrayList<SejongClass> arrayList = new ArrayList<>();
    private FirebaseDatabase database;

    Spinner searchcampusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        setContentView(R.layout.activity_time_table_plus);
        recyclerView = (RecyclerView) findViewById(R.id.class_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ClassRecyclerViewAdapter classRecyclerViewAdapter = new ClassRecyclerViewAdapter();
        recyclerView.setAdapter(classRecyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration); //리사이클러뷰 구분선


        searchcampusSpinner = findViewById(R.id.search_campus);
        ArrayAdapter searchcampusAdapter = ArrayAdapter.createFromResource(this, R.array.search_campus, android.R.layout.simple_spinner_dropdown_item);
        searchcampusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchcampusSpinner.setAdapter(searchcampusAdapter);

        searchcampusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // DocumentReference docRef = db.collection("seoul").document("example");

                    db.collection("seoul").document("50eabaf0-cf08-11ea-842c-e7219ecd0044")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        arrayList.clear();
                                        DocumentSnapshot document = task.getResult();

                                        List list = (List) document.getData().get("majors.major.courses");
                                        for(int i = 0; i < list.size(); i++) {
                                            //  (HashMap<String, Object> 형태
                                            HashMap map = (HashMap) list.get(i);
                                            SejongClass sejongClass = new SejongClass();
                                            sejongClass.classNum = map.get("classNum").toString();
                                            sejongClass.code = map.get("code").toString();
                                            sejongClass.name = map.get("name").toString();
                                            sejongClass.professor = map.get("professor").toString();
                                            sejongClass.sel = map.get("sel").toString();
                                            sejongClass.time = map.get("time").toString();
                                            Log.d("TAG","Success");
                                            arrayList.add(sejongClass);
                                        }
                                        classRecyclerViewAdapter.notifyDataSetChanged();
                                    } else { Log.d("TAG", "SSibal", task.getException());}

                                }
                            });


                  /*
                   db.collection("sejong")
                         .get()
                         .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                             @Override
                             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                 if (task.isSuccessful()) {
                                     arrayList.clear();
                                     for (QueryDocumentSnapshot document : task.getResult()) {
                                         SejongClass sejongclass = document.toObject(SejongClass.class);

                                         arrayList.add(sejongclass);

                                     }
                                     classRecyclerViewAdapter.notifyDataSetChanged();
                                 } else {
                                 }
                             }
                         });
                         */

                } else if (position == 1) {/*

                    database.getReference().child("seoul/example").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d("TAG","Success");
                            arrayList.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                SejongClass sejongClass = snapshot.getValue(SejongClass.class);
                                arrayList.add(sejongClass);
                            }
                            classRecyclerViewAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/

                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    class ClassRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_sejong, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((CustomViewHolder) holder).classNum_sejong.setText(arrayList.get(position).classNum);
            ((CustomViewHolder) holder).code_sejong.setText(arrayList.get(position).code);
            ((CustomViewHolder) holder).name_sejong.setText(arrayList.get(position).name);
            ((CustomViewHolder) holder).professor_sejong.setText(arrayList.get(position).professor);
            ((CustomViewHolder) holder).sel_sejong.setText(arrayList.get(position).sel);
            ((CustomViewHolder) holder).time_sejong.setText(arrayList.get(position).time);

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            TextView classNum_sejong;
            TextView code_sejong;
            TextView name_sejong;
            TextView professor_sejong;
            TextView sel_sejong;
            TextView time_sejong;

            public CustomViewHolder(View view) {
                super(view);
                classNum_sejong = (TextView) view.findViewById(R.id.classNum_sejong);
                code_sejong = (TextView) view.findViewById(R.id.code_sejong);
                name_sejong = (TextView) view.findViewById(R.id.name_sejong);
                professor_sejong = (TextView) view.findViewById(R.id.professor_sejong);
                sel_sejong = (TextView) view.findViewById(R.id.sel_sejong);
                time_sejong = (TextView) view.findViewById(R.id.time_sejong);

            }
        }
    }


}

