package com.kuple.zone.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kuple.zone.Adapter.FeedAdapter;
import com.kuple.zone.R;
import com.kuple.zone.board.WebViewActivity;
import com.kuple.zone.model.FeedModel;

import java.util.ArrayList;

public class HomeFragment extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<FeedModel> modelFeedArrayList = new ArrayList<>();
    FeedAdapter adapterFeed;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        recyclerView = (RecyclerView) recyclerView.findViewById(R.id.feedKuple);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapterFeed = new FeedAdapter(this, modelFeedArrayList);
        recyclerView.setAdapter(adapterFeed);

        populateRecyclerView();
    }

    @Override
    public void onClick(View v) {
        switch(view.getId()){
            case R.id.btnBus:
            case R.id.btnMeal:
            case R.id.btnTimeTable:
        }
    }

    public void populateRecyclerView() {

        FeedModel modelFeed = new FeedModel(1, 9, 2, R.drawable.ic_profile_24dp, R.drawable.ic_profile_24dp,
                "Sajin Maharjan", "2 hrs", "The cars we drive say a lot about us.");
        modelFeedArrayList.add(modelFeed);
        modelFeed = new FeedModel(2, 26, 6, R.drawable.rounded_profile, 0,
                "Karun Shrestha", "9 hrs", "Don't be afraid of your fears. They're not there to scare you. They're there to \n" +
                "let you know that something is worth it.");
        modelFeedArrayList.add(modelFeed);
        modelFeed = new FeedModel(3, 17, 5, R.drawable.ic_home, R.drawable.img_link,
                "Lakshya Ram", "13 hrs", "That reflection!!!");
        modelFeedArrayList.add(modelFeed);

        adapterFeed.notifyDataSetChanged();
    }
}