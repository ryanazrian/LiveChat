package com.example.livechat.activity.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.livechat.R;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView RC;
    private EditText msg;
    private Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        RC = findViewById(R.id.reyclerview_message_list);
        msg = findViewById(R.id.edittext_chatbox);
        sendBtn = findViewById(R.id.button_chatbox_send);
    }
}
