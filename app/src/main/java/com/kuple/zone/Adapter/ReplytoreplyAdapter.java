package com.kuple.zone.Adapter;

import android.content.Context;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kuple.zone.Inteface.OnItemClick;
import com.kuple.zone.R;
import com.kuple.zone.model.ReplyInfo;
import com.kuple.zone.model.UserModel;
import com.like.LikeButton;

import java.util.Date;
import java.util.List;

public class ReplytoreplyAdapter extends RecyclerView.Adapter<ReplytoreplyAdapter.ReplytoreplyAdapterViewHolder> {
    private List<ReplyInfo> mReplyList;
    private Context mContext;
    private FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DocumentReference documentReference_replyInreply;
    private OnItemClick mCallback;

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

//    public BoardAdapter.OnItemClickListener mListener=null;
//    public void setOnIemlClickListner(BoardAdapter.OnItemClickListener listner){
//        this.mListener=listner;
//    }public interface OnItemClickListener{
//        void onitemClick(View v, int pos);
//    }

    ReplytoreplyAdapter(List<ReplyInfo> mReplyList, Context mContext, DocumentReference documentReference_replyInreply, OnItemClick listener) {
        this.mReplyList = mReplyList;
        this.mContext = mContext;
        this.documentReference_replyInreply = documentReference_replyInreply;
        this.mCallback = listener;
    }

    public ReplytoreplyAdapter(List<ReplyInfo> mReplyList, Context mContext, OnItemClick listener) {
        this.mReplyList = mReplyList;
        this.mContext = mContext;
        this.mCallback = listener;
    }

    @NonNull
    @Override
    public ReplytoreplyAdapter.ReplytoreplyAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReplytoreplyAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_replyinreply, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ReplytoreplyAdapter.ReplytoreplyAdapterViewHolder holder, final int position) {
        final ReplyInfo replyInfo = mReplyList.get(position);
        holder.mContent.setText(replyInfo.getContent());
        holder.mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String documentId = replyInfo.getDocumentId();
                show_menu(v, position);
            }


        });
        mStore.collection("users")//닉네임 가져오기
                .document(replyInfo.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserModel fm = documentSnapshot.toObject(UserModel.class);
                        assert fm != null;
                        String date = replyInfo.getDate().toString();
                        String date1 = date.substring(11, 16);
                        String date2 = replyInfo.getDate().toString().substring(11, 13);//시간부분
                        int hour = (Integer.parseInt(date2) + 9) % 24;
                        //String finaldate=String.valueOf(hour)+replyInfo.getDate().toString().substring(13,16);
                        String finaldate = date1;
                        Log.d("홈 댓글시간", finaldate);
                        //String str=fm.getUserNickName()+"("+fm.getNickname()+")\n"+finaldate;
                        String str = fm.nickname;
                        holder.mNickname.setText(str);
                    }
                });
        String dateTime2 = new Date().toString();
        String dateTime = replyInfo.getDate().toString().substring(4, 10);
        Log.d("date1", dateTime);
        if (dateTime2.substring(4, 10).equals(dateTime)) {
//            holder.mN.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mReplyList.size();
    }

    class ReplytoreplyAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView mContent;
        private ImageView mMenu, mN;
        private TextView mNickname;
        private LikeButton mLikebutton;
        private TextView mLikecount;


        ReplytoreplyAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mContent = itemView.findViewById(R.id.item_reply_content);
            mMenu = itemView.findViewById(R.id.item_reply_menu_imageView);
            mNickname = itemView.findViewById(R.id.item_nickname_level);
            mLikebutton = itemView.findViewById(R.id.item_reply_likebutton);
            mLikecount = itemView.findViewById(R.id.item_reply_likecount);
         //   mN = itemView.findViewById(R.id.reply_new);

        }
    }

    private void show_menu(View v, final int position) {
        final FirebaseFirestore mStore = FirebaseFirestore.getInstance();
        PopupMenu popup = new PopupMenu(mContext, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.remove_superficially) {//Toast.makeText(mContext, "무슨수정이냐 그냥 쳐 삭제해라", Toast.LENGTH_LONG).show();
                    // mBoardInfo.remove(position);
                    return true;
                } else if (itemId == R.id.remove_firebase) {
                    Date date = new Date();
                    if (mReplyList.get(position).getUid().equals(mFirebaseUser.getUid())) {
                        documentReference_replyInreply.collection("replyInreply").document(mReplyList.get(position).getDocumentId())
                                .update("deleted_at", date.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(mContext, "파이어베이스 deleted_at 현재신간으로 업데이트", Toast.LENGTH_LONG).show();
                                mCallback.onClick("실시간 댓글 삭제");//삭제하면 콜백함수로 양성열 보내짐.//이 어댑터에서 보낼 정보는 이렇게쓰면댐
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext, "파이어베이스 deleted_at 업데이트실패", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(mContext, "너가 올린 댓글이 아니다", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
                return false;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_showup, popup.getMenu());
        popup.show();

    }
}
