package com.kuple.zone.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kuple.zone.Inteface.OnItemClick;
import com.kuple.zone.R;
import com.kuple.zone.model.BoardInfo;
import com.kuple.zone.model.SliderItem;
import com.kuple.zone.model.UserModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private ArrayList<BoardInfo> arrayList_feed;
    private Context mContext;
    private FirebaseUser mFirebaseUser;//현재 사용중인 앱의 주인의 정보 .getCurrent 까지 된정보
    private OnItemClick mCallback;
    private int count = 0;
    private String mBoardName;
    private RequestManager glide;

    public FeedAdapter(ArrayList<BoardInfo> arrayList_feed, Context mContext) {
        this.arrayList_feed = arrayList_feed;
        this.mContext = mContext;
    }

    public interface OnItemClickListener {
        void onitemClick(View v, int pos);
    }

    private FeedAdapter.OnItemClickListener mListener = null;

    public void setOnIemlClickListner(FeedAdapter.OnItemClickListener listner) {
        this.mListener = listner;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType) {
        return new FeedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_feed, parent, false));
    }

    @Override
    public void onBindViewHolder(final FeedViewHolder holder, int position) {
        final BoardInfo modelFeed = arrayList_feed.get(position);
        final String documentId = modelFeed.getDocumentId();
        //제목 가져오기
        holder.tv_feedtitle.setText(modelFeed.getTitle());
        //작성자 닉네임 가져오기
        holder.tv_name.setText(modelFeed.getNickname());
        //보드 이름 가져오기
        holder.tv_boardtitle.setText(modelFeed.getBoardName());
        //댓글수 가져오기
        final String commentCount = "+" + String.valueOf(modelFeed.getReplycount());
        holder.tv_commentcount.setText(commentCount);
        //조회수 가져오기
        final String viewCount = String.valueOf(modelFeed.getViewcount());
        holder.tv_view.setText(viewCount);
        //종야요수 가져오기
        final String likeCount = String.valueOf(modelFeed.getUidList().size() - 1);
        holder.tv_like.setText(likeCount);
        //올린시간 가져오기
        String date = modelFeed.getDate().toString();
        String dateTime = date.substring(11, 16);//시간 부분
        String dateYear = date.substring(24, 28);//연도 부분
        final String finaldate = dateTime;
        String dateCalc = new Date().toString();
        String dateMonthDay = dateCalc.substring(4, 10);//월 일부분
        String time = dateMonthDay + ", " + dateYear + " " + dateTime;
        holder.tv_time.setText(time);
        Log.d("dateMonthDay", dateMonthDay);
        //n표시
        if (date.substring(4, 10).equals(dateMonthDay)) {
        }
        SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(mContext);
        glide = Glide.with(mContext);
        if (modelFeed.getmDownloadURIList().size() != 0) {
            for (int i = 0; i < modelFeed.getmDownloadURIList().size(); i++) {
                sliderAdapterExample.addItem(new SliderItem(modelFeed.getmDownloadURIList().get(i)));
                Glide.with(holder.img_post).load(modelFeed.getmDownloadURIList().get(i)).into(holder.img_post);
            }
        } else {
            holder.img_post.setVisibility(View.GONE);
        }

                //작성자
//        String writer = modelFeed.getUid();
//        mStore.collection("users").document(writer).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                UserModel fm = documentSnapshot.toObject(UserModel.class);
//                assert fm != null;
//                try {
//                    holder.tv_post.setText(fm.nickname + " " + finaldate + " ");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList_feed.size();
    }

    class FeedViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private TextView tv_feedtitle;
        private TextView tv_post;
        private TextView tv_view;
        private TextView tv_time;
        private TextView tv_commentcount;
        private TextView tv_boardtitle;
        private TextView tv_like;
        private ImageView img_post;

        public FeedViewHolder(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_boardtitle = (TextView) itemView.findViewById(R.id.tv_boardtitle);
            tv_feedtitle = (TextView) itemView.findViewById(R.id.tv_feed_title);
            tv_post = (TextView) itemView.findViewById(R.id.tv_post);
            tv_commentcount = (TextView) itemView.findViewById(R.id.tv_comment);
            img_post = (ImageView) itemView.findViewById(R.id.img_post);
            tv_like = (TextView) itemView.findViewById(R.id.tv_like);
            tv_view = (TextView) itemView.findViewById(R.id.tv_view);

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