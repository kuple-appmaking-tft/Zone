package com.kuple.zone.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kuple.zone.R;
import com.kuple.zone.model.HeaderModel;

import java.util.List;


public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {
    private List<String> mChildList;
    private Context mContext;
    public ChildAdapter(List<String>mTitleList,Context mContext){
        this.mChildList=mTitleList;
        this.mContext=mContext;
    }
    public interface OnItemClickListener{
        void onitemClick(View v,int pos);
    }
    private static OnItemClickListener mListener=null;
    public void setOnIemlClickListner(ChildAdapter.OnItemClickListener listner){
        mListener=listner;
    }

    @NonNull
    @Override
    public ChildAdapter.ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChildViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_child, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChildAdapter.ChildViewHolder holder, int position) {
        final String data = mChildList.get(position);
        holder.textView.setText(data);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"구독하기",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mChildList.size();
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.child_title);
            imageView=itemView.findViewById(R.id.child_image);

            itemView.setOnClickListener(new View.OnClickListener() {//클릭했을때
                @Override
                public void onClick(View v) {//들어가는 기능 detail로
                    int pos=getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        if(mListener!=null){
                            mListener.onitemClick(v,pos);
                        }
                    }
                }
            });

        }

    }
}
