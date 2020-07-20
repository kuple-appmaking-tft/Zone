package com.kuple.zone.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kuple.zone.Adapter.HeaderAdapter;
import com.kuple.zone.Adapter.HorizentalAdapter;
import com.kuple.zone.R;
import com.kuple.zone.model.HeaderModel;

import java.util.ArrayList;
import java.util.List;

public class BoardFragment extends Fragment {
    private RecyclerView recyclerview;
    private RecyclerView horizentalRecyclerView;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        recyclerview =view.findViewById(R.id.board_recyclerview_title);
        horizentalRecyclerView=view.findViewById(R.id.board_favorite);

        List<String> data = new ArrayList<>();
        data.add("커뮤니티");
        data.add("쿠플웹진");
        data.add("학업정보");
        data.add("생활정보");
        data.add("교내단체게시판");
        HeaderAdapter headerAdapter=new HeaderAdapter(data,getContext(),getActivity());
        recyclerview.setAdapter(headerAdapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        horizentalRecyclerView.setLayoutManager(layoutManager);
        ArrayList<String> mName=new ArrayList<>();
        mName.add("양성열");
        mName.add("김세진");
        HorizentalAdapter adapter=new HorizentalAdapter(getContext(),mName);
        horizentalRecyclerView.setAdapter(adapter);


        return view;
    }

}