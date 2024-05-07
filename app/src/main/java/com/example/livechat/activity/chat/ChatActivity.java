package com.example.livechat.activity.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.livechat.R;
import com.example.livechat.model.MessageModel;
import com.example.livechat.model.UserModel;
import com.example.livechat.services.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import tech.gusavila92.websocketclient.WebSocketClient;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView RC;
    private EditText msg;
    private Button sendBtn;

    private MessageListAdapter messageListAdapter;
    private ArrayList<MessageModel> messageList = new ArrayList<>();
    private ChatController chatController;

    //library
    private SessionManagement sessionManagement;

    protected String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        RC = findViewById(R.id.reyclerview_message_list);
        msg = findViewById(R.id.edittext_chatbox);
        sendBtn = findViewById(R.id.button_chatbox_send);

        sendBtn.setOnClickListener(sendButton);

        sessionManagement = new SessionManagement(this);
        username = sessionManagement.getUsername();
        messageListAdapter = new MessageListAdapter(this, messageList, username);
        chatController = new ChatController(this, messageListAdapter, messageList);


        RC.setHasFixedSize(true);
        RC.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RC.setAdapter(messageListAdapter);

    }

    Button.OnClickListener sendButton = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            String message = msg.getText().toString();
            if(!TextUtils.isEmpty(message)){
                chatController.postMessage(message);
                
                msg.getText().clear();
            }
        }
    };
}
