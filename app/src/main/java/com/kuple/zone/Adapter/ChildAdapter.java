package com.kuple.zone.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kuple.zone.R;
import com.kuple.zone.board.CommonboardActivity;
import com.kuple.zone.board.CorpActivity;
import com.kuple.zone.board.PhotoboardActivity;
import com.kuple.zone.model.HeaderModel;
import com.kuple.zone.model.UserModel;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;


public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {
    private List<String> mChildList;
    private Context mContext;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private int headerpos;
    private Activity activity;

    public ChildAdapter(List<String> mTitleList, Context mContext, int headerpos, Activity activity) {
        this.mChildList = mTitleList;
        this.mContext = mContext;
        this.headerpos = headerpos;
        this.activity = activity;
    }

    public interface OnItemClickListener {
        void onitemClick(View v, int pos);
    }

    private static OnItemClickListener mListener = null;

    public void setOnIemlClickListner(ChildAdapter.OnItemClickListener listner) {
        mListener = listner;
    }

    @NonNull
    @Override
    public ChildAdapter.ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChildViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_child, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ChildAdapter.ChildViewHolder holder, int position) {
        final String data = mChildList.get(position);
        final DocumentReference df = mStore.collection("users").document(firebaseUser.getUid());
        holder.textView.setText(data);
        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                df.update("favoritList", FieldValue.arrayUnion(data));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                df.update("favoritList", FieldValue.arrayRemove(data));
            }
        });
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserModel userModel = documentSnapshot.toObject(UserModel.class);
                assert userModel != null;
                if (userModel.getFavoritList().contains(data)) {
                    holder.likeButton.setLiked(true);
                }
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
        return mChildList.size();
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView image;
        LikeButton likeButton;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.child_title);
            likeButton = itemView.findViewById(R.id.child_likeButton);
            image=itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener() {//클릭했을때
                @Override
                public void onClick(View v) {//들어가는 기능 detail로
                    int pos = getAdapterPosition();
//                    if(pos!=RecyclerView.NO_POSITION){
//                        if(mListener!=null){
//                            mListener.onitemClick(v,pos);
//                        }
//                    }

                    switch (headerpos) {
                        case 0:
                            final List<String> list0 = new ArrayList<>();
                            list0.add("쿠플광장");
                            list0.add("고민상담");
                            list0.add("쑥덕쑥덕");
                            list0.add("졸업생 게시판");
                            Intent intent0 = new Intent(activity, CommonboardActivity.class);
                            intent0.putExtra("BoardName", list0.get(pos));
                            activity.startActivity(intent0);
                            break;
                        case 1:
                            final List<String> list1 = new ArrayList<>();
                            list1.add("쿠플툰");
                            list1.add("먹쿠먹쿠");
                            if (pos == 0) {
                                Intent intent = new Intent(activity, CommonboardActivity.class);
                                intent.putExtra("BoardName", list1.get(pos));
                                activity.startActivity(intent);

                            } else if (pos == 1) {
                                Intent intent = new Intent(activity, PhotoboardActivity.class);
                                intent.putExtra("BoardName", list1.get(pos));
                                activity.startActivity(intent);
                            }
                            break;
                        case 2:
                            final List<String> list2 = new ArrayList<>();
                            list2.add("강의평가");
                            list2.add("합격수기");
                            list2.add("취업광장");
                            list2.add("스터디게시판");
                            list2.add("꿀팁게시판");
                            Intent intent2 = new Intent(activity, CommonboardActivity.class);
                            intent2.putExtra("BoardName", list2.get(pos));
                            activity.startActivity(intent2);
                            break;
                        case 3:
                            final List<String> list3 = new ArrayList<>();

                            list3.add("부동산");//사진
                            list3.add("구인구직");//일반
                            list3.add("중고거래");//사진
                            list3.add("분실물신고");//사진
                            if (pos == 1) {
                                Intent intent = new Intent(activity, CommonboardActivity.class);
                                intent.putExtra("BoardName", list3.get(pos));
                                activity.startActivity(intent);
                            } else {
                                Intent intent = new Intent(activity, PhotoboardActivity.class);
                                intent.putExtra("BoardName", list3.get(pos));
                                activity.startActivity(intent);
                            }
                            break;
                        case 4:
                            final List<String> list4 = new ArrayList<>();
                            list4.add("총학생회");
                            Intent intent = new Intent(activity, CorpActivity.class);
                            intent.putExtra("BoardName", list4.get(pos));
                            activity.startActivity(intent);


                    }


                }
            });

        }

    }
}
