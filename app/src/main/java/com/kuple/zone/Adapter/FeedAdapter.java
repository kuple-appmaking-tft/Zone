package com.kuple.zone.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder>{
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private List<BoardInfo> list_feed;
    private Context mContext;
    private FirebaseUser mFirebaseUser;//현재 사용중인 앱의 주인의 정보 .getCurrent 까지 된정보
    private OnItemClick mCallback;
    private int count = 0;
    private String mBoardName;
    private RequestManager glide;

    public FeedAdapter(List<BoardInfo> list_feed) {
        this.list_feed = list_feed;
        this.mContext = mContext;
        this.mFirebaseUser = mFirebaseUser;
        this.mCallback = mCallback;
        this.mBoardName = mBoardName;
        glide = Glide.with(mContext);
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
        return new FeedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_feed,parent,false));
    }

    @Override
    public void onBindViewHolder(final FeedViewHolder holder, int position) {
        final BoardInfo modelFeed = list_feed.get(position);
        final String documentId = modelFeed.getDocumentId();
        holder.tv_boardtitle.setText(modelFeed.getBoardTitle());
        holder.tv_feedtitle.setText(modelFeed.getTitle());
        //댓글수 가져오기
        String commentCount = "댓글 " + String.valueOf(modelFeed.getReplycount()) + "개";
        holder.tv_commentcount.setText(commentCount);
        //종야요수 가져오기
        final String likeCount = String.valueOf(modelFeed.getUidList().size()) + "개";
        holder.tv_like.setText(likeCount);
        //올린시간 가져오기
        String date = modelFeed.getDate().toString();
        String date1 = date.substring(11, 16);
        String date2 = date.substring(11, 13);//시간부분
        final String finaldate = date1;
        String dateTime2 = new Date().toString();
        String dateTime = dateTime2.substring(4, 10);
        Log.d("date1", dateTime);
        //n표시
        if (date.substring(4, 10).equals(dateTime)) {
        }
        //이미지 불러오기
        SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(mContext);
        for (int i = 0; i < modelFeed.getmDownloadURIList().size(); i++) {
            if(modelFeed.getmDownloadURIList() == null)
                holder.img_post.setVisibility(View.GONE);
            sliderAdapterExample.addItem(new SliderItem(modelFeed.getmDownloadURIList().get(i)));
        }
        //작성자
        String writer = modelFeed.getUid();
        mStore.collection("users").document(writer).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserModel fm = documentSnapshot.toObject(UserModel.class);
                assert fm != null;
                try {
                    holder.tv_post.setText(fm.nickname + " " + finaldate + " ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        /* 이미지그려주기
        if (modelFeed.getmDownloadURIList().size() != 0) {
            Glide.with(holder.img_post).load(modelFeed.getmDownloadURIList().get(0)).into(holder.img_post);
        } else {
            holder.img_post.setVisibility(View.INVISIBLE);
        }
        */
        holder.ll_like.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mStore.collection("Testing").document(documentId).update("likebutton_count", FieldValue.increment(1));
                holder.ll_like.setEnabled(false);
            }
        });
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
        return list_feed.size();
    }

    class FeedViewHolder extends RecyclerView.ViewHolder {
        private ViewGroup ll_like;
        private TextView tv_feedtitle;
        private TextView tv_post;
        private TextView tv_commentcount;
        private TextView tv_boardtitle;
        private TextView tv_like;
        private ImageView img_post;

        public FeedViewHolder(View itemView) {
            super(itemView);
            ll_like = (ViewGroup) itemView.findViewById(R.id.ll_like);
            tv_boardtitle = (TextView) itemView.findViewById(R.id.tv_boardtitle);
            tv_feedtitle = (TextView) itemView.findViewById(R.id.tv_feed_title);
            tv_post = (TextView) itemView.findViewById(R.id.tv_post);
            tv_commentcount = (TextView) itemView.findViewById(R.id.tv_comment);
            img_post = (ImageView) itemView.findViewById(R.id.img_post);
            tv_like = (TextView) itemView.findViewById(R.id.tv_like);

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