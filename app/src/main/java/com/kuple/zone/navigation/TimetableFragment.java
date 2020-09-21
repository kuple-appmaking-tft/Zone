package com.kuple.zone.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
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
import com.kuple.zone.timetable.SeoulClass;
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
    private int isfull[][] = new int[5][11];


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
    public void RetreiveFireStore() {
        db.collection("users")
                .document(user.getUid())
                .collection("TimeTable")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                SeoulClass seoulClass=document.toObject(SeoulClass.class);

                                String[] dayarray = seoulClass.getTime().split("\n");
                                if (dayarray.length == 1) {//요일이 한개일때
                                    //이름 , 시간 , 장소 //
                                    Schedule schedule = new Schedule();
                                    schedule.setClassPlace(seoulClass.classNum);
                                    schedule.setClassTitle(seoulClass.name);
                                    schedule.setClassPlace(seoulClass.time);
                                    //요일가져오기
                                    SetDay(dayarray[0], schedule);

                                    if (dayarray[0].charAt(3) == ')') {//한시간짜리 수업
                                        SetTime_oneClass(dayarray[0], schedule);
                                        int one_full = Character.getNumericValue(dayarray[0].charAt(2)) - 1;

                                    } else {//두시간이상
                                        SetTime_twoClass(dayarray[0], schedule);
                                        int startnum = Character.getNumericValue(dayarray[0].charAt(2));
                                        int endnum = Character.getNumericValue(dayarray[0].charAt(4));

                                    }
                                    int placeStartIdx = seoulClass.getTime().indexOf(")");//장소지정
                                    String place = dayarray[0].substring(placeStartIdx + 1);
                                    schedule.setClassPlace(place); //string의 0 , 2 , 4 번째 가져와야함 example 수(7-9) 농심국제관 308호
                                    ArrayList<Schedule> item = new ArrayList<>();
                                    item.add(schedule);
                                    ArrayList<Schedule> forstoreList = new ArrayList<>();
                                    forstoreList.add(schedule);

                                    timetable.add(item);


                                } else {//요일이 2개일때
                                    //이름 , 시간 , 장소 //
                                    Schedule schedule1 = new Schedule();
                                    schedule1.setClassPlace(seoulClass.classNum);
                                    schedule1.setClassTitle(seoulClass.name);
                                    schedule1.setClassPlace(seoulClass.time);
                                    //요일가져오기
                                    SetDay(dayarray[0], schedule1);//날짜정하기
                                    if (dayarray[0].charAt(3) == ')') {//한시간짜리 수업
                                        SetTime_oneClass(dayarray[0], schedule1);//시간정하기
                                        int one_full = Character.getNumericValue(dayarray[0].charAt(2)) - 1;
                                    } else {//두시간이상
                                        SetTime_twoClass(dayarray[0], schedule1);//시간정하기
                                        int startnum = Character.getNumericValue(dayarray[0].charAt(2));
                                        int endnum = Character.getNumericValue(dayarray[0].charAt(4));

                                    }
                                    int placeStartIdx1 =seoulClass.getTime().indexOf(")");
                                    String place = dayarray[0].substring(placeStartIdx1 + 1);
                                    schedule1.setClassPlace(place); //string의 0 , 2 , 4 번째 가져와야함 example 수(7-9) 농심국제관 308호
                                    ArrayList<Schedule> item1 = new ArrayList<>();
                                    item1.add(schedule1);
                                    //uploadFireStore(schedule1);
                                    //스케쥴 1 끝
                                    Schedule schedule2 = new Schedule();
                                    schedule2.setClassPlace(seoulClass.classNum);
                                    schedule2.setClassTitle(seoulClass.name);
                                    schedule2.setClassPlace(seoulClass.time);
                                    //요일가져오기
                                    SetDay(dayarray[1], schedule2);

                                    if (dayarray[0].charAt(3) == ')') {//한시간짜리 수업
                                        SetTime_oneClass(dayarray[1], schedule2);//시간정하기
                                        int one_full = Character.getNumericValue(dayarray[1].charAt(2)) - 1;

                                    } else {//두시간이상
                                        SetTime_twoClass(dayarray[1], schedule2);//시간정하기
                                        int startnum = Character.getNumericValue(dayarray[1].charAt(2));
                                        int endnum = Character.getNumericValue(dayarray[1].charAt(4));

                                    }
                                    int placeStartIdx2 =seoulClass.getTime().indexOf(")");
                                    String place2 = dayarray[0].substring(placeStartIdx2 + 1);
                                    schedule1.setClassPlace(place2); //string의 0 , 2 , 4 번째 가져와야함 example 수(7-9) 농심국제관 308호
                                    ArrayList<Schedule> item2 = new ArrayList<>();
                                    item1.add(schedule2);
                                    timetable.add(item1);

                                }
                            }
                        } else {
                        }

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
    private int GetDay(String s) {
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



}