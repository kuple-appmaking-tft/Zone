package com.kuple.zone.chat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.kuple.zone.R;

public class ChatActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ChatFragment chatFragment;
    private UserListInRoomFragment userListInRoomFragment = null;
    public Button button_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String toUid = getIntent().getStringExtra("toUid");
        final String roomID = getIntent().getStringExtra("roomID");
        String roomTitle = getIntent().getStringExtra("roomTitle");
        if (roomTitle!=null) {
            actionBar.setTitle(roomTitle);
        }

        // left drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        findViewById(R.id.rightMenuBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    if (userListInRoomFragment==null) {
                        userListInRoomFragment = UserListInRoomFragment.getInstance(roomID, chatFragment.getUserList());
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.drawerFragment, userListInRoomFragment)
                                .commit();
                    }
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });
        // chatting area
        chatFragment = ChatFragment.getInstance(toUid, roomID);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, chatFragment )
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        chatFragment.backPressed();
        finish();;
    }

}
