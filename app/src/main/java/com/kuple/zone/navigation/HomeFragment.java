package com.kuple.zone.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kuple.zone.Adapter.FeedAdapter;
import com.kuple.zone.R;
import com.kuple.zone.board.WebViewActivity;
import com.kuple.zone.model.FeedModel;
import org.w3c.dom.Text;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<FeedModel> modelFeedArrayList = new ArrayList<>();
    private FeedAdapter adapterFeed;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.feedKuple);

        adapterFeed = new FeedAdapter(getActivity(), modelFeedArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapterFeed);

        Log.e("fragment", "HomeFragment");
        TextView btnMeal = (TextView) view.findViewById(R.id.btnMeal);
        btnMeal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),WebViewActivity.class);
                startActivity(intent);
            }
        });
        return view;
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