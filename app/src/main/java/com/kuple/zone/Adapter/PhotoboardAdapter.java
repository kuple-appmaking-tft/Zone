package com.kuple.zone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kuple.zone.MainActivity;
import com.kuple.zone.R;
import com.kuple.zone.model.BoardInfo;
import com.kuple.zone.model.SliderItem;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.Date;
import java.util.List;


public class PhotoboardAdapter extends RecyclerView.Adapter<PhotoboardAdapter.MainViewHolder>{
    private Context mContext;
    private List<BoardInfo> mPostingInfoList;
    private List<String> mDocumentIdList;
    //커스텀 리스터 정의
///////////////////////////클릭리스너
    public interface OnItemClickListener{
        void onitemClick(View v, int pos);
    }
    private OnItemClickListener mListener=null;
    public void setOnIemlClickListner(OnItemClickListener listner){
        this.mListener=listner;
    }
////////////////////////////////
    public PhotoboardAdapter(List<BoardInfo> mPostingInfoList, Context mContext) {
        this.mContext = mContext;
        this.mPostingInfoList = mPostingInfoList;

    }
    class MainViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleTextView;        //item_main의 객체를 불러옴...작은네모칸에 들어갈 얘들 선언
        private TextView mNameTextView;
        private TextView mContentsTextView;
        private SliderView mImageSliderView;
        private TextView mDateTextView;
        private LikeButton mLikeButton;
        private TextView mLikeButton_count;
        private ImageView mImageview;//매뉴클릭릭
        private ImageView mShareImageView;
        private ImageView mNewDateImageView;
        private TextView mReplycount;


       public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView=itemView.findViewById(R.id.item_title_text);
            mNameTextView=itemView.findViewById(R.id.item_name_text);
            mContentsTextView=itemView.findViewById(R.id.item_contents_text);
            mImageSliderView=itemView.findViewById(R.id.item_imageslider);
            mImageview=itemView.findViewById(R.id.item_menudot_imageview);
            mLikeButton=itemView.findViewById(R.id.item_likeButton_likeButton);
            mLikeButton_count=itemView.findViewById(R.id.item_likeButton_textView);
            mShareImageView=itemView.findViewById(R.id.item_reply_imageview);
            mDateTextView=itemView.findViewById(R.id.item_date);
           mNewDateImageView=itemView.findViewById(R.id.item_dateN_ImageView);
            mImageSliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            mImageSliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            mImageSliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
            mImageSliderView.setIndicatorSelectedColor(Color.WHITE);
            mImageSliderView.setIndicatorUnselectedColor(Color.GRAY);
            mImageSliderView.setScrollTimeInSec(3);
            mImageSliderView.setAutoCycle(false);
           mReplycount=itemView.findViewById(R.id.item_reply_count);

            //////클릭리스너
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo,parent,false));//아이템메뉴는 작은내모가 확장되있는거
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {//class MainViewHolder의 holder <Board>형식의 data값을 참조.
        BoardInfo data = mPostingInfoList.get(position);
        final String documentId = data.getDocumentId();
        String replycount=String.valueOf(data.getReplycount());
        holder.mReplycount.setText(replycount);
        holder.mTitleTextView.setText(data.getTitle());
        holder.mNameTextView.setText(data.getNickname());
        holder.mContentsTextView.setText(data.getContent());
        SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(mContext);
        for (int i = 0; i < data.getmDownloadURIList().size(); i++) {
            sliderAdapterExample.addItem(new SliderItem(data.getmDownloadURIList().get(i)));
        }
        holder.mImageSliderView.setSliderAdapter(sliderAdapterExample);
        final FirebaseFirestore mStore = FirebaseFirestore.getInstance();
        holder.mLikeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                mStore.collection("Testing").document(documentId).update("likebutton_count", FieldValue.increment(1));
            }
            @Override
            public void unLiked(LikeButton likeButton) {
                mStore.collection("Testing").document(documentId).update("likebutton_count", FieldValue.increment(-1));
            }
        });
        holder.mLikeButton_count.setText(String.valueOf(data.getUidList().size()));
        holder.mImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v,documentId);
            }
        });
//        holder.mShareImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//// 타입별 템플릿 만들기 상세 예제코드는 [메시지 만들기] 참고
//                LinkObject link = LinkObject.newBuilder()
//                        //.setWebUrl(data.getDynamicLink())
//                        .setMobileWebUrl(data.getDynamicLink())
//                        .build();
//                TemplateParams params = TextTemplate.newBuilder(data.getTitle(), link)
//                        .setButtonTitle(data.getTitle())
//                        .build();
//
//
//// 기본 템플릿으로 카카오링크 보내기
//                KakaoLinkService.getInstance()
//                        .sendDefault(mContext, params, new ResponseCallback<KakaoLinkResponse>() {
//                            @Override
//                            public void onFailure(ErrorResult errorResult) {
//                                Log.e("KAKAO_API", "카카오링크 공유 실패: " + errorResult);
//                            }
//
//                            @Override
//                            public void onSuccess(KakaoLinkResponse result) {
//
//
//                                // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
//
//                            }
//                        });
//            }
//        });

        String date=data.getDate().toString();
        String date1=date.substring(11,16);
        String date2=date.substring(0,13)+" "+date.substring(30,34);
        //Log.d("dateYY", date2);
        holder.mDateTextView.setText(date1);

//        Calendar calendar=Calendar.getInstance();
////        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("ee MMM dd :HH:mm yyyy");
////        String dateTime=simpleDateFormat.format(new Date());
        String dateTime=new Date().toString();
        dateTime=dateTime.substring(0,13)+" "+date.substring(30,34);
        //Log.d("dateYY", dateTime);
       // Log.d("date", dateTime);
        holder.mNewDateImageView.setVisibility(View.INVISIBLE);
        if(dateTime.equals(date2)){
            holder.mNewDateImageView.setVisibility(View.VISIBLE);
        }
    }
    public void showPopup(final View v,final String documentId) {
        final FirebaseFirestore mStore=FirebaseFirestore.getInstance();
        PopupMenu popup = new PopupMenu(mContext, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.remove_superficially) {
                    Toast.makeText(mContext, "무슨수정이냐 그냥 쳐 삭제해라", Toast.LENGTH_LONG).show();
                    return true;
                } else if (itemId == R.id.remove_firebase) {
                    mStore.collection("Testing").document(documentId)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(mContext, "데이터베이스에서 삭제됨.새로고침안해도댐", Toast.LENGTH_LONG).show();
                                    Intent intent1 = new Intent(v.getContext(), MainActivity.class);
                                    intent1.putExtra("Refresh", "success");
                                    mContext.startActivity(intent1);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                    return true;
                }
                return false;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_showup, popup.getMenu());
        popup.show();
    }
    public void shareKaKao(){

    }


    @Override
    public int getItemCount() {
        return mPostingInfoList.size();
    }



}


