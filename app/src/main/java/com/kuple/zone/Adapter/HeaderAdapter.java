package com.kuple.zone.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kuple.zone.R;
import com.kuple.zone.board.CommonboardActivity;
import com.kuple.zone.board.CorpActivity;
import com.kuple.zone.board.PhotoboardActivity;
import com.kuple.zone.model.HeaderModel;

import java.util.ArrayList;
import java.util.List;

import io.grpc.okhttp.internal.framed.Header;

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder> {

    private List<String> mTitleList;
    private Context mContext;
    private Activity mActivity;
    private List<String> list0 = new ArrayList<>();
    private List<String> list1 = new ArrayList<>();

    public HeaderAdapter(List<String> mTitleList, Context mContext, Activity mActivity) {
        this.mTitleList = mTitleList;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public HeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final HeaderViewHolder holder, final int position) {
        final String data = mTitleList.get(position);
        holder.textView.setText(data);
//        String mystring = "Hello.....";
//        SpannableString content = new SpannableString(data);
//        content.setSpan(new UnderlineSpan(), 0, data.length(), 0);
//        holder.textView.setText(content);



        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        //final List<String> list0 = new ArrayList<>();
                        if (holder.flag == 0) {

                            list0.add("쿠플광장");
                            list0.add("고민상담");
                            list0.add("쑥덕쑥덕");
                            list0.add("졸업생 게시판");
                            ChildAdapter childAdapter0 = new ChildAdapter(list0, mContext,position,mActivity);
//                            childAdapter0.setOnIemlClickListner(new ChildAdapter.OnItemClickListener() {
//                                @Override
//                                public void onitemClick(View v, int pos) {
//                                    Intent intent = new Intent(mActivity, CommonboardActivity.class);
//                                    intent.putExtra("BoardName", list0.get(pos));
//                                    mActivity.startActivity(intent);
//
//                                }
//                            });
                            holder.recyclerView.setAdapter(childAdapter0);

                            holder.imageView.setImageResource(R.drawable.ic_arrow_up);
                            holder.flag = 1;
                        } else {
                            list0.clear();
                            holder.recyclerView.setAdapter(new ChildAdapter(list0, mContext,position,mActivity));
                            ;
                            holder.imageView.setImageResource(R.drawable.ic_arrow_down);
                            holder.flag = 0;
                        }


//                        Toast.makeText(mContext, "0번째클릭", Toast.LENGTH_SHORT).show();
                        break;
                    case 1://쿠플웹진
                       // final List<String> list1 = new ArrayList<>();
                        if (holder.flag == 0) {
                            list1.add("쿠플툰");
                            list1.add("먹쿠먹쿠");
                            ChildAdapter childAdapter1 = new ChildAdapter(list1, mContext,position,mActivity);
//                            childAdapter1.setOnIemlClickListner(new ChildAdapter.OnItemClickListener() {
//                                @Override
//                                public void onitemClick(View v, int pos) {
//                                    if (pos == 0) {
//                                        Intent intent = new Intent(mActivity, CommonboardActivity.class);
//                                        intent.putExtra("BoardName", list1.get(pos));
//                                        mActivity.startActivity(intent);
//
//                                    } else if (pos == 1) {
//                                        Intent intent = new Intent(mActivity, PhotoboardActivity.class);
//                                        intent.putExtra("BoardName", list1.get(pos));
//                                        mActivity.startActivity(intent);
//                                    }
//                                }
//                            });
                            holder.recyclerView.setAdapter(childAdapter1);
                            holder.imageView.setImageResource(R.drawable.ic_arrow_up);
                            holder.flag = 1;
                        } else {
                            list1.clear();
                            holder.recyclerView.setAdapter(new ChildAdapter(list1, mContext,position,mActivity));
                            ;
                            holder.imageView.setImageResource(R.drawable.ic_arrow_down);
                            holder.flag = 0;
                        }

                        break;
                    case 2:
                        //학업정보
                        final List<String> list2 = new ArrayList<>();
                        if (holder.flag == 0) {
                            list2.add("강의평가");
                            list2.add("합격수기");
                            list2.add("취업광장");
                            list2.add("스터디게시판");
                            list2.add("꿀팁게시판");
                            ChildAdapter childAdapter2 = new ChildAdapter(list2, mContext,position,mActivity);
//                            childAdapter2.setOnIemlClickListner(new ChildAdapter.OnItemClickListener() {
//                                @Override
//                                public void onitemClick(View v, int pos) {
//                                    Intent intent = new Intent(mActivity, CommonboardActivity.class);
//                                    intent.putExtra("BoardName", list2.get(pos));
//                                    mActivity.startActivity(intent);
//                                }
//                            });
                            holder.recyclerView.setAdapter(childAdapter2);
                            holder.imageView.setImageResource(R.drawable.ic_arrow_up);
                            holder.flag = 1;
                        } else {
                            list2.clear();
                            holder.recyclerView.setAdapter(new ChildAdapter(list2, mContext,position,mActivity));
                            ;
                            holder.imageView.setImageResource(R.drawable.ic_arrow_down);
                            holder.flag = 0;
                        }
                        break;
                    case 3:
                        //생활정보
                        final List<String> list3 = new ArrayList<>();
                        if (holder.flag == 0) {
                            list3.add("부동산");//사진
                            list3.add("구인구직");//일반
                            list3.add("중고거래");//사진
                            list3.add("분실물신고");//사진
                            ChildAdapter childAdapter3 = new ChildAdapter(list3, mContext,position,mActivity);
                            childAdapter3.setOnIemlClickListner(new ChildAdapter.OnItemClickListener() {
                                @Override
                                public void onitemClick(View v, int pos) {
                                    if (pos == 1) {
                                        Intent intent = new Intent(mActivity, CommonboardActivity.class);
                                        intent.putExtra("BoardName", list3.get(pos));
                                        mActivity.startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(mActivity, PhotoboardActivity.class);
                                        intent.putExtra("BoardName", list3.get(pos));
                                        mActivity.startActivity(intent);
                                    }

                                }
                            });
                            holder.recyclerView.setAdapter(childAdapter3);
                            ;

                            holder.imageView.setImageResource(R.drawable.ic_arrow_up);
                            holder.flag = 1;
                        } else {
                            list3.clear();
                            holder.recyclerView.setAdapter(new ChildAdapter(list3, mContext,position,mActivity));
                            ;
                            holder.imageView.setImageResource(R.drawable.ic_arrow_down);
                            holder.flag = 0;
                        }
                        break;
                    case 4:
                        //교내단체게시판
                        final List<String> list4 = new ArrayList<>();
                        if (holder.flag == 0) {
                            list4.add("총학생회");
                            ChildAdapter childAdapter4=new ChildAdapter(list4,mContext,position,mActivity);
                            childAdapter4.setOnIemlClickListner(new ChildAdapter.OnItemClickListener() {
                                @Override
                                public void onitemClick(View v, int pos) {
                                    Intent intent = new Intent(mActivity, CorpActivity.class);
                                    intent.putExtra("BoardName", list4.get(pos));
                                    mActivity.startActivity(intent);
                                }
                            });
                           // holder.recyclerView.setAdapter(new InSchoolChildAdapter(list4, mContext));
                            holder.recyclerView.setAdapter(childAdapter4);
                            holder.imageView.setImageResource(R.drawable.ic_arrow_up);
                            holder.flag = 1;
                        } else {
                            list4.clear();
                            holder.recyclerView.setAdapter(new ChildAdapter(list4, mContext,position,mActivity));
                            ;
                            holder.imageView.setImageResource(R.drawable.ic_arrow_down);
                            holder.flag = 0;
                        }
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTitleList.size();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private RecyclerView recyclerView;
        private int flag = 0;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.header_recyclerview);
            textView = itemView.findViewById(R.id.header_title);
            imageView = itemView.findViewById(R.id.header_btn_expand_toggle);
        }
    }
}
