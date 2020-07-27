package com.kuple.zone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kuple.zone.R;

import java.util.ArrayList;
import java.util.List;

public class InSchoolChildAdapter extends RecyclerView.Adapter<InSchoolChildAdapter.InSchoolChildViewHolder> {
    private List<String> mTitleList;
    private Context mContext;

    public InSchoolChildAdapter(List<String> mTitleList, Context mContext) {
        this.mTitleList = mTitleList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public InSchoolChildAdapter.InSchoolChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InSchoolChildViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_inschool, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final InSchoolChildAdapter.InSchoolChildViewHolder holder, final int position) {
        final String data = mTitleList.get(position);
        holder.textView.setText(data);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (position) {
                    case 0:
                        List<String> list0 = new ArrayList<>();
                        if (holder.flag == 0) {
                            list0.add("쿠플광장");
                            list0.add("고민상담");
                            list0.add("쑥덕쑥덕");
                            list0.add("졸업생 게시판");
                            holder.recyclerView.setAdapter(new ChildAdapter(list0, mContext));
                            holder.imageView.setImageResource(R.drawable.minusarrow);
                            holder.flag = 1;
                        } else {
                            list0.clear();
                            holder.recyclerView.setAdapter(new ChildAdapter(list0, mContext));
                            ;
                            holder.imageView.setImageResource(R.drawable.plusarrow);
                            holder.flag = 0;
                        }


//                        Toast.makeText(mContext, "0번째클릭", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        //쿠플웹진
                        //holder.recyclerView.setAdapter(new ChildAdapter(list,mContext));
                        break;
                    case 2:
                        //학업정보
                        List<String> list1 = new ArrayList<>();
                        if (holder.flag == 0) {
                            list1.add("강의평가");
                            list1.add("합격수기");
                            list1.add("취업광장");
                            list1.add("스터디게시판");
                            list1.add("꿀팁게시판");
                            holder.recyclerView.setAdapter(new ChildAdapter(list1, mContext));
                            holder.imageView.setImageResource(R.drawable.minusarrow);
                            holder.flag = 1;
                        } else {
                            list1.clear();
                            holder.recyclerView.setAdapter(new ChildAdapter(list1, mContext));
                            ;
                            holder.imageView.setImageResource(R.drawable.plusarrow);
                            holder.flag = 0;
                        }
                        break;
                    case 3:
                        //생활정보
                        break;
                    case 4:
                        //교내단체게시판
                        break;


                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTitleList.size();
    }

    public class InSchoolChildViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private RecyclerView recyclerView;
        private int flag = 0;

        public InSchoolChildViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.inschool_recyclerview);
            textView = itemView.findViewById(R.id.inschool_title);
            imageView = itemView.findViewById(R.id.inschool_btn_expand_toggle);
        }
    }
}
