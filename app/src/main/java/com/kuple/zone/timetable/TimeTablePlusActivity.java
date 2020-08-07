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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kuple.zone.R;
import com.kuple.zone.login.LoginActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TimeTablePlusActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<SejongClass> sejongClasses = new ArrayList<>();
    private List<String> Sejong = new ArrayList<>();
    private FirebaseDatabase database;

    Spinner searchcampusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        setContentView(R.layout.activity_time_table_plus);
        recyclerView = findViewById(R.id.class_view);
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
                    database.getReference().child("classes/seoul/general/0/classes/0/courses").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            sejongClasses.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                SejongClass sejongClass = snapshot.getValue(SejongClass.class);
                                sejongClasses.add(sejongClass);
                            }
                            classRecyclerViewAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else if (position == 1) {

                    database.getReference().child("classes/sejong/general/0/classes/0/courses").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            sejongClasses.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                SejongClass sejongClass = snapshot.getValue(SejongClass.class);
                                sejongClasses.add(sejongClass);
                            }
                            classRecyclerViewAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

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
            ((CustomViewHolder) holder).classNum_sejong.setText(sejongClasses.get(position).classNum);
            ((CustomViewHolder) holder).code_sejong.setText(sejongClasses.get(position).code);
            ((CustomViewHolder) holder).name_sejong.setText(sejongClasses.get(position).name);
            ((CustomViewHolder) holder).professor_sejong.setText(sejongClasses.get(position).professor);
            ((CustomViewHolder) holder).sel_sejong.setText(sejongClasses.get(position).sel);
            ((CustomViewHolder) holder).time_sejong.setText(sejongClasses.get(position).time);

        }

        @Override
        public int getItemCount() {
            return sejongClasses.size();
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
