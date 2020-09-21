package com.kuple.zone.Adapter;

import android.app.ProgressDialog;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kuple.zone.Inteface.OnItemClick;
import com.kuple.zone.R;
import com.kuple.zone.model.ReplyActModel;
import com.kuple.zone.model.UserModel;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ReplyActAdapter extends RecyclerView.Adapter<ReplyActAdapter.ReplyViewHolder> {
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private Context mContext;
    private OnItemClick mCallback;
    private DocumentReference documentReference_reply;
    ///
    private ProgressDialog loadingbar;
    private UserModel userModel;
    private List<ReplyActModel> mReplyList;
    private int count = 0;

    public ReplyActAdapter() {
    }

    public ReplyActAdapter(UserModel userModel, List<ReplyActModel> mReplyList, Context context) {//생성자
        this.userModel = userModel;
        this.mReplyList = mReplyList;
        this.mContext = context;
    }


    @NonNull
    @Override
    public ReplyActAdapter.ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReplyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ReplyActAdapter.ReplyViewHolder holder, final int position) {
        final ConstraintLayout constraintLayout = new ConstraintLayout(mContext);
        final ReplyActModel replyActModel = mReplyList.get(position);

        holder.mContent.setText(replyActModel.getContent());

        mStore.collection("users")//닉네임 가져오기
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserModel fm = documentSnapshot.toObject(UserModel.class);
                        assert fm != null;
                        String date = replyActModel.getDate().toString();
                        String date1 = date.substring(11, 16);
                        String date2 = replyActModel.getDate().toString().substring(11, 13);//시간부분
                        int hour = (Integer.parseInt(date2) + 9) % 24;
                        String finaldate = date1;
                        String str = fm.nickname;

                    }
                });
        holder.mNickname.setText(userModel.getNickname());
/*
        holder.mLikebutton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                final FirebaseFirestore mStore = FirebaseFirestore.getInstance();
                documentReference_reply.collection("reply").document(replyActModel.getReplyId())
                        .update("uidLikelist", FieldValue.arrayUnion(mFirebaseUser.getUid())).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        documentReference_reply.collection("reply").document(replyActModel.getReplyId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                int count = task.getResult().toObject(replyActModel.getClass()).getUidLikelist().size();
                                holder.mLikecount.setText(String.valueOf(count - 1));
                            }
                        });
                        mStore.collection("users").document(firebaseUser.getUid()).update("likecount", FieldValue.increment(1));//경험치+1
                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {

                documentReference_reply.collection("reply").document(replyActModel.getReplyId())
                        .update("uidLikelist", FieldValue.arrayRemove(mFirebaseUser.getUid())).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        documentReference_reply.collection("reply").document(replyActModel.getReplyId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                int count = task.getResult().toObject(replyActModel.getClass()).getUidLikelist().size();
                                holder.mLikecount.setText(String.valueOf(count - 1));
                            }
                        });
                        mStore.collection("users").document(firebaseUser.getUid()).update("likecount", FieldValue.increment(-1));//경험치 -1
                    }
                });
            }
        });*/

        holder.mLikecount.setText(String.valueOf(replyActModel.getUidLikelist().size() - 1));
        assert firebaseUser != null;
        if (replyActModel.getUidLikelist().contains(firebaseUser.getUid())) {
            holder.mLikebutton.setLiked(true);
        }

        holder.date.setText(replyActModel.getDate().toString());

    }

    @Override
    public int getItemCount() {
        return mReplyList.size();
    }


    class ReplyViewHolder extends RecyclerView.ViewHolder {
        private TextView mContent;
        private TextView mNickname;
        private LikeButton mLikebutton;
        private TextView mLikecount;
        private ImageView mN;
        private TextView date;


        ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            mContent = itemView.findViewById(R.id.item_reply_content);
            mNickname = itemView.findViewById(R.id.item_nickname_level);
            mLikebutton = itemView.findViewById(R.id.item_reply_likebutton);
            mLikecount = itemView.findViewById(R.id.item_reply_likecount);
            loadingbar = new ProgressDialog(mContext);
            date=itemView.findViewById(R.id.date);

        }
    }

/*
    private void show_menu(View v, final int position) {
        final FirebaseFirestore mStore = FirebaseFirestore.getInstance();
        PopupMenu popup = new PopupMenu(mContext, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.remove_superficially) {
                    return true;
                } else if (itemId == R.id.remove_firebase) {
                    Date date = new Date();
                    documentReference_reply.collection("reply").document(mReplyList.get(position).getReplyId())
                            .update("deleted_at", date.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(mContext, "파이어베이스 deleted_at 현재신간으로 업데이트", Toast.LENGTH_LONG).show();

                            mCallback.onClick("실시간 댓글 삭제");//삭제하면 콜백함수로 양성열 보내짐.//이 어댑터에서 보낼 정보는 이렇게쓰면댐
                            documentReference_reply.update("replycount", FieldValue.increment(-1));//댓글수 1증가.
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mContext, "파이어베이스 deleted_at 업데이트실패", Toast.LENGTH_LONG).show();
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

    }*/

}
