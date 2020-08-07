package com.kuple.zone.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kuple.zone.R;
import com.kuple.zone.chat.ChatRoomActivity;

import java.util.HashMap;

public class AlarmFragment extends Fragment implements View.OnClickListener {

    private Button btn_ku_talk;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        btn_ku_talk = view.findViewById(R.id.btn_ku_talk);
        btn_ku_talk.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == btn_ku_talk){
            startActivity(new Intent(getActivity(), ChatRoomActivity.class));
        }

    }
}