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

import com.bumptech.glide.Glide;
import com.kuple.zone.Inteface.OnItemClick;
import com.kuple.zone.R;

import java.util.ArrayList;

public class HorizentalAdapter extends RecyclerView.Adapter<HorizentalAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mTextList = new ArrayList<>();

    public HorizentalAdapter(Context mContext, ArrayList<String> mTextList) {
        this.mContext = mContext;
        this.mTextList = mTextList;
    }

    public interface OnItemClickListener {
        void onitemClick(View v, int pos);
    }

    private static ChildAdapter.OnItemClickListener mListener = null;

    public void setOnIemlClickListner(ChildAdapter.OnItemClickListener listner) {
        mListener = listner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizental, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Glide.with(mContext).asBitmap().load(mImageUrl.get(position)).into(holder.image);
        String data=mTextList.get(position);
        holder.name.setText(mTextList.get(position));
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "클릭", Toast.LENGTH_SHORT).show();
            }
        });
        if(data.equals("쿠플광장")){
            holder.image.setImageResource(R.drawable.kuplegwangjang);
        }else if(data.equals("고민상담"))
        {
            holder.image.setImageResource(R.drawable.gominsnagdam);
        }else if(data.equals("쑥덕쑥덕")){
            holder.image.setImageResource(R.drawable.talk);
        }else if(data.equals("졸업생 게시판")){
            holder.image.setImageResource(R.drawable.graduate);
        }

    }

    @Override
    public int getItemCount() {
        return mTextList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.horizontal_textview);
            image=itemView.findViewById(R.id.horizontal_imageview);
            itemView.setOnClickListener(new View.OnClickListener() {//클릭했을때
                @Override
                public void onClick(View v) {//들어가는 기능 detail로
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onitemClick(v, pos);
                        }
                    }
                }
            });
        }
    }

}
