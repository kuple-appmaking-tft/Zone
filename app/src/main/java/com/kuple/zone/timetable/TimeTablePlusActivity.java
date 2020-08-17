package com.kuple.zone.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Movie;
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
import com.google.gson.JsonArray;
import com.kuple.zone.R;
import com.kuple.zone.login.LoginActivity;
import com.smarteist.autoimageslider.IndicatorView.draw.drawer.type.ScaleDownDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimeTablePlusActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ArrayList<SeoulClass> arrayList = new ArrayList<>();

    Spinner searchcampusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                    String json = getJsonString();

                    Log.d("알림","test01");
                    try{

                        JSONObject jsonObject = new JSONObject(json);

                        JSONArray seoulArray = jsonObject.getJSONArray("seoul");
                        Log.d("알림", "test02");
                        JSONObject seoulObject = seoulArray.getJSONObject(0);
                        Log.d("알림", "test03");
                        JSONArray majorArray = seoulObject.getJSONArray("major");
                        Log.d("알림", "test04");
                        JSONObject majorObject = majorArray.getJSONObject(0);
                        Log.d("알림", "test05");
                        JSONArray majorsArray = majorObject.getJSONArray("majors");
                        Log.d("알림", "test06");
                        JSONObject majorsObject = majorsArray.getJSONObject(0);

                        JSONArray coursesArray = majorsObject.getJSONArray("courses");

                        for(int i = 0 ; i < coursesArray.length(); i++) {
                                JSONObject coursesObject = coursesArray.getJSONObject(0);
                                Log.d("알림", "test07");

                                SeoulClass seoulclass = new SeoulClass();
                                  Log.d("알림", "test08");
                                seoulclass.setClassNum(coursesObject.getString("classNum"));
                                seoulclass.setCode(coursesObject.getString("code"));
                                 Log.d("알림", "test09");
                                seoulclass.setName(coursesObject.getString("name"));
                                seoulclass.setProfessor(coursesObject.getString("professor"));
                                seoulclass.setSel(coursesObject.getString("sel"));
                                seoulclass.setTime(coursesObject.getString("time"));

                                arrayList.add(seoulclass);
                                Log.d("알림", "test10");

                           //  JSONArray courses = major.getJSONArray("2");
                          //      Log.d("알림", "test05");
                          //    JSONObject jsonArray = majors.getJSONObject("courses");
                          //  Log.d("알림", "test06");
                          // String courses = jsonObject.getJSONObject("seoul.major.majors")   .getString("courses");
                         //   JSONArray seoulArray = new JSONArray(jsonArray);
                         //   Log.d("알림", "test07");
                        }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                }


                else if (position == 1) {
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
            Log.d("알림", "test11");
            ((CustomViewHolder) holder).classNum_sejong.setText(arrayList.get(position).getClassNum());
            Log.d("알림", "test12");
            ((CustomViewHolder) holder).code_sejong.setText(arrayList.get(position).getCode());
            ((CustomViewHolder) holder).name_sejong.setText(arrayList.get(position).getName());
            ((CustomViewHolder) holder).professor_sejong.setText(arrayList.get(position).getProfessor());
            ((CustomViewHolder) holder).sel_sejong.setText(arrayList.get(position).getSel());
            ((CustomViewHolder) holder).time_sejong.setText(arrayList.get(position).getTime());

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




    private String getJsonString()
    {
        String json = "";
        Log.d("알림","example01");
        try {
            InputStream is = getAssets().open("total.json");
            Log.d("알림","example02");
            int fileSize = is.available();
            Log.d("알림","example03");
            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();
            Log.d("알림","example04");
            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return json;
    }

}

