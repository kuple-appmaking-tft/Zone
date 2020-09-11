package com.kuple.zone.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.TimetableView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.kuple.zone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TimeTablePlusActivity extends AppCompatActivity {
    private Schedule schedule; // 스케줄추가

    public static final int RESULT_OK_ADD = 1;
    ArrayList<SeoulClass> arrayList = new ArrayList<>();

    Spinner searchcampusSpinner;

//    Button timetable_plusbtn = (Button) findViewById(R.id.timetable_plusbtn) ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_plus);

        RecyclerView recyclerView = findViewById(R.id.class_view);
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
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                if (position == 0) {

                    String json = getJsonString();

                    try{



                        //major 추가
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray seoulArray = jsonObject.getJSONArray("seoul");
                        JSONObject seoulObject = seoulArray.getJSONObject(0);
                        JSONArray majorArray = seoulObject.getJSONArray("major");
                        for (int b = 0; b < majorArray.length(); b++) {
                            JSONObject majorObject = majorArray.getJSONObject(b);
                            JSONArray majorsArray = majorObject.getJSONArray("majors");
                            for (int c = 0; c < majorsArray.length(); c++) {
                                JSONObject majorsObject = majorsArray.getJSONObject(c);
                                JSONArray coursesArray = majorsObject.getJSONArray("courses");
                                for (int i = 0; i < coursesArray.length(); i++) {
                                    JSONObject coursesObject = coursesArray.getJSONObject(i);
                                    SeoulClass seoulclass = new SeoulClass();
                                    seoulclass.setClassNum(coursesObject.getString("classNum"));
                                    seoulclass.setCode(coursesObject.getString("code"));
                                    seoulclass.setName(coursesObject.getString("name"));
                                    seoulclass.setProfessor(coursesObject.getString("professor"));
                                    seoulclass.setSel(coursesObject.getString("sel"));
                                    seoulclass.setTime(coursesObject.getString("time"));
                                    arrayList.add(seoulclass);
                                }
                                // classRecyclerViewAdapter.notifyDataSetChanged();
                            }

                        }

                        //general 추가
                        JSONObject seoulObject_general = seoulArray.getJSONObject(1);
                        JSONArray generalArray = seoulObject_general.getJSONArray("general");
                        for (int b = 0; b < generalArray.length(); b++) {
                            JSONObject majorObject = generalArray.getJSONObject(b);
                            JSONArray majorsArray = majorObject.getJSONArray("classes");
                            for (int c = 0; c < majorsArray.length(); c++) {
                                JSONObject majorsObject = majorsArray.getJSONObject(c);
                                JSONArray coursesArray = majorsObject.getJSONArray("courses");
                                for (int i = 0; i < coursesArray.length(); i++) {
                                    JSONObject coursesObject = coursesArray.getJSONObject(i);
                                    SeoulClass seoulclass = new SeoulClass();
                                    seoulclass.setClassNum(coursesObject.getString("classNum"));
                                    seoulclass.setCode(coursesObject.getString("code"));
                                    seoulclass.setName(coursesObject.getString("name"));
                                    seoulclass.setProfessor(coursesObject.getString("professor"));
                                    seoulclass.setSel(coursesObject.getString("sel"));
                                    seoulclass.setTime(coursesObject.getString("time"));
                                      arrayList.add(seoulclass);
                                }

                            }

                        }
                        // education 추가
                        JSONObject seoulObject_education = seoulArray.getJSONObject(2);
                        JSONArray educationArray = seoulObject_education.getJSONArray("education");
                        for (int b = 0; b < educationArray.length(); b++) {
                            JSONObject majorObject = educationArray.getJSONObject(b);
                            JSONArray coursesArray = majorObject.getJSONArray("courses");
                            for (int i = 0; i < coursesArray.length(); i++) {
                                JSONObject coursesObject = coursesArray.getJSONObject(i);
                                SeoulClass seoulclass = new SeoulClass();
                                seoulclass.setClassNum(coursesObject.getString("classNum"));
                                seoulclass.setCode(coursesObject.getString("code"));
                                seoulclass.setName(coursesObject.getString("name"));
                                seoulclass.setProfessor(coursesObject.getString("professor"));
                                seoulclass.setSel(coursesObject.getString("sel"));
                                seoulclass.setTime(coursesObject.getString("time"));
                                arrayList.add(seoulclass);
                            }
                        }


                        // military 추가
                        JSONObject seoulObject_military = seoulArray.getJSONObject(3);
                        JSONArray militaryArray = seoulObject_military.getJSONArray("military");
                        for (int b = 0; b < militaryArray.length(); b++) {
                            JSONObject majorObject = militaryArray.getJSONObject(b);
                            JSONArray coursesArray = majorObject.getJSONArray("courses");
                            for (int i = 0; i < coursesArray.length(); i++) {
                                JSONObject coursesObject = coursesArray.getJSONObject(i);
                                SeoulClass seoulclass = new SeoulClass();
                                seoulclass.setClassNum(coursesObject.getString("classNum"));
                                seoulclass.setCode(coursesObject.getString("code"));
                                seoulclass.setName(coursesObject.getString("name"));
                                seoulclass.setProfessor(coursesObject.getString("professor"));
                                seoulclass.setSel(coursesObject.getString("sel"));
                                seoulclass.setTime(coursesObject.getString("time"));
                                arrayList.add(seoulclass);
                            }
                        }

                        // lifelong 추가
                        JSONObject seoulObject_lifelong = seoulArray.getJSONObject(4);
                        JSONArray lifelongArray = seoulObject_lifelong.getJSONArray("lifelong");
                        for (int b = 0; b < lifelongArray.length(); b++) {
                            JSONObject majorObject = lifelongArray.getJSONObject(b);
                            final JSONArray coursesArray = majorObject.getJSONArray("courses");
                            for (int i = 0; i < coursesArray.length(); i++) {
                                JSONObject coursesObject = coursesArray.getJSONObject(i);
                                SeoulClass seoulclass = new SeoulClass();
                                seoulclass.setClassNum(coursesObject.getString("classNum"));
                                seoulclass.setCode(coursesObject.getString("code"));
                                seoulclass.setName(coursesObject.getString("name"));
                                seoulclass.setProfessor(coursesObject.getString("professor"));
                                seoulclass.setSel(coursesObject.getString("sel"));
                                seoulclass.setTime(coursesObject.getString("time"));
                                arrayList.add(seoulclass);
                            }
                            Button timetableplus_bt = (Button) findViewById(R.id.timetable_plusbtn);
                            timetableplus_bt.setOnClickListener(new Button.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //schedule.setClassTitle(coursesArray.getString(string);
                                    //schedule.setClassPlace(classroomEdit.getText().toString());
                                    //schedule.setProfessorName(professorEdit.getText().toString());

                                }
                            });

                          /*  timetable_plusbtn.setOnClickListener(new Button.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    schedule.setClassTitle(arrayList.get(position).getClassNum());//coursesObject.getString("classNum"));//.getText().toString());
                                    schedule.setClassPlace(arrayList.get(position).getTime());
                                    schedule.setProfessorName(arrayList.get(position).getProfessor());
                                    Intent i = new Intent();
                                    ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                                    //you can add more schedules to ArrayList
                                    schedules.add(schedule);
                                    i.putExtra("schedules",schedules);
                                    setResult(RESULT_OK_ADD,i);
                                    finish();
                                }
                            });*/


                        }
                        classRecyclerViewAdapter.notifyDataSetChanged();




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


    class ClassRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> { // 리사이클러뷰

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            ((CustomViewHolder) holder).classNum_seoul.setText(arrayList.get(position).getClassNum());
            ((CustomViewHolder) holder).code_seoul.setText(arrayList.get(position).getCode());
            ((CustomViewHolder) holder).name_seoul.setText(arrayList.get(position).getName());
            ((CustomViewHolder) holder).professor_seoul.setText(arrayList.get(position).getProfessor());
            ((CustomViewHolder) holder).sel_seoul.setText(arrayList.get(position).getSel());
            ((CustomViewHolder) holder).time_seoul.setText(arrayList.get(position).getTime());

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            TextView classNum_seoul;
            TextView code_seoul;
            TextView name_seoul;
            TextView professor_seoul;
            TextView sel_seoul;
            TextView time_seoul;

            public CustomViewHolder(View view) {
                super(view);
                classNum_seoul = (TextView) view.findViewById(R.id.classNum_seoul);
                code_seoul = (TextView) view.findViewById(R.id.code_seoul);
                name_seoul = (TextView) view.findViewById(R.id.name_seoul);
                professor_seoul = (TextView) view.findViewById(R.id.professor_seoul);
                sel_seoul = (TextView) view.findViewById(R.id.sel_seoul);
                time_seoul = (TextView) view.findViewById(R.id.time_seoul);

            }
        }
    }






    private String getJsonString()
    {
        String json = "";
        Log.d("알림","example01");
        try {
            InputStream is = getAssets().open("seoul_all.json");
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
