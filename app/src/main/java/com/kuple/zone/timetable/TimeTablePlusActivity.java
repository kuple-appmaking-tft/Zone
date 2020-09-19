package com.kuple.zone.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kuple.zone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

public class TimeTablePlusActivity extends AppCompatActivity {
    // private Schedule schedule; // 스케줄추가
    private TimetableView timetable; // 스케줄
    public static final int RESULT_OK_ADD = 1;
    ArrayList<SeoulClass> arrayList = new ArrayList<>();
    //ArrayList<Schedule> item = new ArrayList<>();
    private int isfull[][]=new int[5][11];
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser user= mAuth.getCurrentUser();

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
        timetable = findViewById(R.id.timetable);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
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

                    try {


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
                            classRecyclerViewAdapter.notifyDataSetChanged();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (position == 1) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RetreiveFireStore();


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


    class ClassRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> { // 리사이클러뷰


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);


            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

            ((CustomViewHolder) holder).classNum_seoul.setText(arrayList.get(position).getClassNum());
            ((CustomViewHolder) holder).code_seoul.setText(arrayList.get(position).getCode());
            ((CustomViewHolder) holder).name_seoul.setText(arrayList.get(position).getName());
            ((CustomViewHolder) holder).professor_seoul.setText(arrayList.get(position).getProfessor());
            ((CustomViewHolder) holder).sel_seoul.setText(arrayList.get(position).getSel());
            ((CustomViewHolder) holder).time_seoul.setText(arrayList.get(position).getTime());

            ((CustomViewHolder) holder).plus_button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] dayarray = arrayList.get(position).getTime().split("\n");


                    if (dayarray.length == 1) {//요일이 한개일때
                        //이름 , 시간 , 장소 //
                        Schedule schedule = new Schedule();
                        schedule.setClassPlace(arrayList.get(position).classNum);
                        schedule.setClassTitle(arrayList.get(position).name);
                        schedule.setClassPlace(arrayList.get(position).time);
                        //요일가져오기
                        SetDay(dayarray[0], schedule);

                        if (dayarray[0].charAt(3) == ')') {//한시간짜리 수업
                            SetTime_oneClass(dayarray[0], schedule);
                            int one_full=Character.getNumericValue(dayarray[0].charAt(2))-1;
                            if(isfull[GetDay(dayarray[0])][one_full]==1){
                                Log.d("중복","중복입니다");
                            }else {
                                isfull[GetDay(dayarray[0])][one_full]=1;
                            }
                        } else {//두시간이상
                            SetTime_twoClass(dayarray[0], schedule);
                            int startnum=Character.getNumericValue(dayarray[0].charAt(2));
                            int endnum=Character.getNumericValue(dayarray[0].charAt(4));
                            for(int i=startnum;i<=endnum;i++){
                                if(isfull[GetDay(dayarray[0])][i]==1){
                                    Log.d("중복","중복입니다");
                                }else {
                                    isfull[GetDay(dayarray[0])][i]=1;
                                }
                            }
                        }
                        int placeStartIdx=arrayList.get(position).getTime().indexOf(")");//장소지정
                        String place=dayarray[0].substring(placeStartIdx+1);
                        schedule.setClassPlace(place); //string의 0 , 2 , 4 번째 가져와야함 example 수(7-9) 농심국제관 308호
                        ArrayList<Schedule> item = new ArrayList<>();
                        item.add(schedule);
                        uploadFireStore(schedule);
                        timetable.add(item);


                    } else {//요일이 2개일때
                        //이름 , 시간 , 장소 //
                        Schedule schedule1 = new Schedule();
                        schedule1.setClassPlace(arrayList.get(position).classNum);
                        schedule1.setClassTitle(arrayList.get(position).name);
                        schedule1.setClassPlace(arrayList.get(position).time);
                        //요일가져오기
                        SetDay(dayarray[0], schedule1);//날짜정하기
                        if (dayarray[0].charAt(3) == ')') {//한시간짜리 수업
                            SetTime_oneClass(dayarray[0], schedule1);//시간정하기
                            int one_full=Character.getNumericValue(dayarray[0].charAt(2))-1;
                            if(isfull[GetDay(dayarray[0])][one_full]==1){
                                Log.d("중복","중복입니다");
                                Intent intent =new Intent(TimeTablePlusActivity.this,TimeTablePlusActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),"중복입니다",Toast.LENGTH_SHORT).show();

                            }else {
                                isfull[GetDay(dayarray[0])][one_full]=1;
                            }
                        } else {//두시간이상
                            SetTime_twoClass(dayarray[0], schedule1);//시간정하기
                            int startnum=Character.getNumericValue(dayarray[0].charAt(2));
                            int endnum=Character.getNumericValue(dayarray[0].charAt(4));
                            for(int i=startnum;i<=endnum;i++){
                                if(isfull[GetDay(dayarray[0])][i]==1){
                                    Log.d("중복","중복입니다");
                                }else {
                                    isfull[GetDay(dayarray[0])][i]=1;
                                }
                            }
                        }
                        int placeStartIdx1=arrayList.get(position).getTime().indexOf(")");
                        String place=dayarray[0].substring(placeStartIdx1+1);
                        schedule1.setClassPlace(place); //string의 0 , 2 , 4 번째 가져와야함 example 수(7-9) 농심국제관 308호
                        ArrayList<Schedule> item1 = new ArrayList<>();
                        item1.add(schedule1);
                        uploadFireStore(schedule1);
                        //스케쥴 1 끝
                        Schedule schedule2 = new Schedule();
                        schedule2.setClassPlace(arrayList.get(position).classNum);
                        schedule2.setClassTitle(arrayList.get(position).name);
                        schedule2.setClassPlace(arrayList.get(position).time);
                        //요일가져오기
                        SetDay(dayarray[1], schedule2);

                        if (dayarray[0].charAt(3) == ')') {//한시간짜리 수업
                            SetTime_oneClass(dayarray[1], schedule2);//시간정하기
                            int one_full=Character.getNumericValue(dayarray[1].charAt(2))-1;
                            if(isfull[GetDay(dayarray[1])][one_full]==1){
                                Log.d("중복","중복입니다");
                            }else {
                                isfull[GetDay(dayarray[1])][one_full]=1;
                            }
                        } else {//두시간이상
                            SetTime_twoClass(dayarray[1], schedule2);//시간정하기
                            int startnum=Character.getNumericValue(dayarray[1].charAt(2));
                            int endnum=Character.getNumericValue(dayarray[1].charAt(4));
                            for(int i=startnum;i<=endnum;i++){
                                if(isfull[GetDay(dayarray[1])][i]==1){
                                    Log.d("중복","중복입니다");
                                }else {
                                    isfull[GetDay(dayarray[1])][i]=1;
                                }
                            }
                        }
                        int placeStartIdx2=arrayList.get(position).getTime().indexOf(")");
                        String place2=dayarray[0].substring(placeStartIdx2+1);
                        schedule1.setClassPlace(place2); //string의 0 , 2 , 4 번째 가져와야함 example 수(7-9) 농심국제관 308호
                        ArrayList<Schedule> item2 = new ArrayList<>();
                        item2.add(schedule2);
                        uploadFireStore(schedule2);
                        timetable.add(item2);

                    }


                }

                private void uploadFireStore(Schedule schedule) {

                    assert user != null;
                    db.collection("users")
                            .document(user.getUid())
                            .collection("TimeTable")
                            .document()
                            .set(schedule)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"스토어 업로드성공",Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                private void SetDay(String s, Schedule schedule1) {
                    if (s.substring(0, 1).equals("월")) {
                        schedule1.setDay(0);
                    } else if (s.substring(0, 1).equals("화")) {
                        schedule1.setDay(1);
                    } else if (s.substring(0, 1).equals("수")) {
                        schedule1.setDay(2);
                    } else if (s.substring(0, 1).equals("목")) {
                        schedule1.setDay(3);
                    } else if (s.substring(0, 1).equals("금")) {
                        schedule1.setDay(4);
                    } else if (s.substring(0, 1).equals("토")) {
                        schedule1.setDay(5);
                    } else if (s.substring(0, 1).equals("일")) {
                        schedule1.setDay(6);
                    }
                }
                private int GetDay(String s){
                    if (s.substring(0, 1).equals("월")) {
                        return 0;
                    } else if (s.substring(0, 1).equals("화")) {
                        return 1;
                    } else if (s.substring(0, 1).equals("수")) {
                        return 2;
                    } else if (s.substring(0, 1).equals("목")) {
                        return 3;
                    } else if (s.substring(0, 1).equals("금")) {
                        return 4;
                    } else if (s.substring(0, 1).equals("토")) {
                        return 5;
                    } else if (s.substring(0, 1).equals("일")) {
                        return 6;
                    }
                    return 7;
                }

                private void SetTime_twoClass(String s, Schedule schedule1) {
                    char period_start = s.charAt(2);
                    char period_end = s.charAt(4);
                    int start = Character.getNumericValue(period_start);
                    int end = Character.getNumericValue(period_end);
                    switch (start) {
                        case 1:
                            schedule1.setStartTime(new Time(9, 0));
                            break;
                        case 2:
                            schedule1.setStartTime(new Time(10, 0));
                            break;
                        case 3:
                            schedule1.setStartTime(new Time(11, 0));
                            break;
                        case 4:
                            schedule1.setStartTime(new Time(12, 0));
                            break;
                        case 5:
                            schedule1.setStartTime(new Time(13, 0));
                            break;
                        case 6:
                            schedule1.setStartTime(new Time(14, 0));
                            break;
                        case 7:
                            schedule1.setStartTime(new Time(15, 0));
                            break;
                        case 8:
                            schedule1.setStartTime(new Time(16, 0));
                            break;
                        case 9:
                            schedule1.setStartTime(new Time(17, 0));
                            break;
                        case 10:
                            schedule1.setStartTime(new Time(18, 0));
                            break;
                    }
                    switch (end) {
                        case 1:
                            schedule1.setEndTime(new Time(10, 0));
                            break;
                        case 2:
                            schedule1.setEndTime(new Time(11, 0));
                            break;
                        case 3:
                            schedule1.setEndTime(new Time(12, 0));
                            break;
                        case 4:
                            schedule1.setEndTime(new Time(13, 0));
                            break;
                        case 5:
                            schedule1.setEndTime(new Time(14, 0));
                            break;
                        case 6:
                            schedule1.setEndTime(new Time(15, 0));
                            break;
                        case 7:
                            schedule1.setEndTime(new Time(16, 0));
                            break;
                        case 8:
                            schedule1.setEndTime(new Time(17, 0));
                            break;
                        case 9:
                            schedule1.setEndTime(new Time(18, 0));
                            break;
                        case 10:
                            schedule1.setEndTime(new Time(19, 0));
                            break;
                    }
                }

                private void SetTime_oneClass(String dayarray, Schedule schedule1) {
                    char period = dayarray.charAt(2);
                    int sum = Character.getNumericValue(period);
                    Log.d("되나안되나", String.valueOf(sum));
                    switch (sum) {
                        case 1:
                            schedule1.setStartTime(new Time(9, 0));
                            schedule1.setEndTime(new Time(10, 0));
                            break;
                        case 2:
                            schedule1.setStartTime(new Time(10, 0));
                            schedule1.setEndTime(new Time(11, 0));
                            break;
                        case 3:
                            schedule1.setStartTime(new Time(11, 0));
                            schedule1.setEndTime(new Time(12, 0));
                            break;
                        case 4:
                            schedule1.setStartTime(new Time(12, 0));
                            schedule1.setEndTime(new Time(13, 0));
                            break;
                        case 5:
                            schedule1.setStartTime(new Time(13, 0));
                            schedule1.setEndTime(new Time(14, 0));
                            break;
                        case 6:
                            schedule1.setStartTime(new Time(14, 0));
                            schedule1.setEndTime(new Time(15, 0));
                            break;
                        case 7:
                            schedule1.setStartTime(new Time(15, 0));
                            schedule1.setEndTime(new Time(16, 0));
                            break;
                        case 8:
                            schedule1.setStartTime(new Time(16, 0));
                            schedule1.setEndTime(new Time(17, 0));
                            break;
                        case 9:
                            schedule1.setStartTime(new Time(17, 0));
                            schedule1.setEndTime(new Time(18, 0));
                            break;
                        case 10:
                            schedule1.setStartTime(new Time(18, 0));
                            schedule1.setEndTime(new Time(19, 0));
                    }
                }
            });


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
            Button plus_button;


            public CustomViewHolder(View view) {
                super(view);
                classNum_seoul = (TextView) view.findViewById(R.id.classNum_seoul);
                code_seoul = (TextView) view.findViewById(R.id.code_seoul);
                name_seoul = (TextView) view.findViewById(R.id.name_seoul);
                professor_seoul = (TextView) view.findViewById(R.id.professor_seoul);
                sel_seoul = (TextView) view.findViewById(R.id.sel_seoul);
                time_seoul = (TextView) view.findViewById(R.id.time_seoul);
                plus_button = view.findViewById(R.id.timetable_plusbtn);


            }
        }
    }


    private String getJsonString() {
        String json = "";
        Log.d("알림", "example01");
        try {
            InputStream is = getAssets().open("seoul_all.json");
            Log.d("알림", "example02");
            int fileSize = is.available();
            Log.d("알림", "example03");
            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            Log.d("알림", "example04");
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return json;
    }
}
