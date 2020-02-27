package com.example.livechat.activity.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.livechat.R;
import com.example.livechat.model.MessageModel;
import com.example.livechat.model.UserModel;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView RC;
    private EditText msg;
    private Button sendBtn;

    private MessageListAdapter messageListAdapter;
    private ArrayList<MessageModel> messageList = new ArrayList<>();
    private MessageModel messageModel;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        RC = findViewById(R.id.reyclerview_message_list);
        msg = findViewById(R.id.edittext_chatbox);
        sendBtn = findViewById(R.id.button_chatbox_send);

        messageListAdapter = new MessageListAdapter(this, messageList);
        RC.setHasFixedSize(true);
        RC.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RC.setAdapter(messageListAdapter);

        sendBtn.setOnClickListener(sendButton);
    }

    Button.OnClickListener sendButton = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!TextUtils.isEmpty(msg.getText().toString())){
                messageModel = new MessageModel();
                userModel = new UserModel();

                userModel.setName("Ryan");

                messageModel.setId("0");
                messageModel.setMsg(msg.getText().toString());
                messageModel.setDate(new Date().toString());
                messageModel.setSender(userModel);

                messageList.add(messageModel);
                messageListAdapter.notifyDataSetChanged();

                msg.getText().clear();
            }
        }
    };
}
