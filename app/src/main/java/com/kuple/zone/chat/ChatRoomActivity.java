package com.kuple.zone.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kuple.zone.R;
import com.kuple.zone.model.ChatRoomModel;
import com.kuple.zone.model.Message;
import com.kuple.zone.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.annotation.Nullable;

public class ChatRoomActivity extends AppCompatActivity {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private RecyclerViewAdapter mAdapter;
    public Button button_back;

    ArrayList<String> array = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(mAdapter);

        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    // =============================================================================================
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        final private RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(90));
        private List<ChatRoomModel> roomList = new ArrayList<>();
        private Map<String, UserModel> userList = new HashMap<>();
        private String myUid;
        private StorageReference storageReference;
        private FirebaseFirestore firestore;
        private ListenerRegistration listenerRegistration;
        private ListenerRegistration listenerUsers;

        RecyclerViewAdapter() {
            firestore = FirebaseFirestore.getInstance();
            storageReference  = FirebaseStorage.getInstance().getReference();
            myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // all users information
            listenerUsers = firestore.collection("users")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {return;}

                            for (QueryDocumentSnapshot doc : value) {
                                userList.put(doc.getId(), doc.toObject(UserModel.class));
                            }
                            getRoomInfo();
                        }
                    });
        }

        Integer unreadTotal = 0;
        public void getRoomInfo() {
            // my chatting room information
            listenerRegistration = firestore.collection("rooms").whereGreaterThanOrEqualTo("users."+myUid, 0)
//                    a.orderBy("timestamp", Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {return;}

                            TreeMap<Date, ChatRoomModel> orderedRooms = new TreeMap<Date, ChatRoomModel>(Collections.reverseOrder());

                            for (final QueryDocumentSnapshot document : value) {
                                Message message = document.toObject(Message.class);
                                if (message.getMsg() !=null & message.getTimestamp() == null) {continue;} // FieldValue.serverTimestamp is so late

                                ChatRoomModel chatRoomModel = new ChatRoomModel();
                                chatRoomModel.setRoomID(document.getId());

                                if (message.getMsg() !=null) { // there are no last message
                                    chatRoomModel.setLastDatetime(simpleDateFormat.format(message.getTimestamp()));
                                    switch(message.getMsgtype()){
                                        case "1": chatRoomModel.setLastMsg("Image"); break;
                                        case "2": chatRoomModel.setLastMsg("File"); break;
                                        default:  chatRoomModel.setLastMsg(message.getMsg());
                                    }
                                }
                                Map<String, Long> users = (Map<String, Long>) document.get("users");
                                chatRoomModel.setUserCount(users.size());
                                for( String key : users.keySet() ){
                                    if (myUid.equals(key)) {
                                        Integer unread = (int) (long) users.get(key);
                                        unreadTotal += unread;
                                        chatRoomModel.setUnreadCount(unread);
                                        break;
                                    }
                                }
                                if (users.size()==2) {
                                    for( String key : users.keySet() ){
                                        if (myUid.equals(key)) continue;
                                        UserModel userModel = userList.get(key);
                                        chatRoomModel.setTitle(userModel.getNickname());
                                        chatRoomModel.setPhoto(userModel.getUserphoto());
                                    }
                                } else {                // group chat room
                                    chatRoomModel.setTitle(document.getString("title"));
                                }
                                if (message.getTimestamp()==null) message.setTimestamp(new Date());
                                orderedRooms.put(message.getTimestamp(), chatRoomModel);
                            }
                            roomList.clear();
                            for(Map.Entry<Date,ChatRoomModel> entry : orderedRooms.entrySet()) {
                                roomList.add(entry.getValue());
                            }
                            notifyDataSetChanged();
                            setBadge(getApplicationContext(), unreadTotal);
                        }
                    });
        }

        public void stopListening() {
            if (listenerRegistration != null) {
                listenerRegistration.remove();
                listenerRegistration = null;
            }
            if (listenerUsers != null) {
                listenerUsers.remove();
                listenerUsers = null;
            }

            roomList.clear();
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatroom, parent, false);
            return new RoomViewHolder(view);
        }

        @Override
        @NonNull
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            RoomViewHolder roomViewHolder = (RoomViewHolder) holder;

            final ChatRoomModel chatRoomModel = roomList.get(position);

            roomViewHolder.room_title.setText(chatRoomModel.getTitle());
            roomViewHolder.last_msg.setText(chatRoomModel.getLastMsg());
            roomViewHolder.last_time.setText(chatRoomModel.getLastDatetime());

            if (chatRoomModel.getPhoto()==null) {
                Glide.with(getApplicationContext()).load(R.drawable.user)
                        .apply(requestOptions)
                        .into(roomViewHolder.room_image);
            } else{
                Glide.with(getApplicationContext()).load(storageReference.child("userPhoto/"+chatRoomModel.getPhoto()))
                        .apply(requestOptions)
                        .into(roomViewHolder.room_image);
            }

            if (chatRoomModel.getUnreadCount() > 0) {
                roomViewHolder.unread_count.setText(chatRoomModel.getUnreadCount().toString());
                roomViewHolder.unread_count.setVisibility(View.VISIBLE);
            } else {
                roomViewHolder.unread_count.setVisibility(View.INVISIBLE);
            }

            roomViewHolder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChatActivity.class);
                    intent.putExtra("roomID", chatRoomModel.getRoomID());
                    intent.putExtra("roomTitle", chatRoomModel.getTitle());
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return roomList.size();
        }

        private class RoomViewHolder extends RecyclerView.ViewHolder {
            public ImageView room_image;
            public TextView room_title;
            public TextView last_msg;
            public TextView last_time;
            public TextView unread_count;

            RoomViewHolder(View view) {
                super(view);
                room_image = view.findViewById(R.id.room_image);
                room_title = view.findViewById(R.id.room_title);
                last_msg = view.findViewById(R.id.last_msg);
                last_time = view.findViewById(R.id.last_time);
                unread_count = view.findViewById(R.id.unread_count);
            }
        }
    }

    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    public static String getLauncherClassName(Context context) {
        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                return resolveInfo.activityInfo.name;
            }
        }
        return null;
    }
}
