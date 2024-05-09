package com.example.livechat.activity.users;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livechat.R;
import com.example.livechat.activity.chat.ChatActivity;
import com.example.livechat.model.UserModel;
import com.example.livechat.services.SessionManagement;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements UserListAdapter.ItemClickListener {
    private RecyclerView RC;
    private SessionManagement sessionManagement;
    private UsersController usersController;
    private UserListAdapter userListAdapter;
    private List<UserModel> userList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);

        RC = findViewById(R.id.reyclerview_user_list);

        sessionManagement = new SessionManagement(this);
        userListAdapter = new UserListAdapter(this, userList);
        usersController = new UsersController(this, userListAdapter, userList, sessionManagement);

        RC.setHasFixedSize(true);
        RC.setLayoutManager(new LinearLayoutManager(this));
        RC.setAdapter(userListAdapter);

        userListAdapter.addItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        Intent moveIntent = new Intent(this, ChatActivity.class);
        UserModel clickedItem = userList.get(position);

        moveIntent.putExtra("userModel", clickedItem);
        startActivity(moveIntent);
    }
}
