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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;

import tech.gusavila92.websocketclient.WebSocketClient;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView RC;
    private EditText msg;
    private Button sendBtn;

    private MessageListAdapter messageListAdapter;
    private ArrayList<MessageModel> messageList = new ArrayList<>();
    private MessageModel messageModel;
    private UserModel userModel;

    //library
    private WebSocketClient webSocketClient;

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

        createWebSocketClient();
    }

    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://10.0.2.2:8080/websocket");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("Websocket", "Session is starting");
                webSocketClient.send("HELLO WORLD");
            }

            @Override
            public void onTextReceived(String s) {
                Log.i("WebSocket", "Message received");
                Log.i("Websocket", s);
                final String message = s;
            }

            @Override
            public void onBinaryReceived(byte[] data) {

            }

            @Override
            public void onPingReceived(byte[] data) {

            }

            @Override
            public void onPongReceived(byte[] data) {

            }

            @Override
            public void onException(Exception e) {

            }

            @Override
            public void onCloseReceived() {

            }
        };

        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.addHeader("authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyeWFuYXpyaWFuIiwiaWF0IjoxNTg3Mzk1ODY2LCJleHAiOjE1ODc0ODIyNjZ9.DtxWII_3TfT3BNX45aoNeYPNENtWacsd7i54cbxux6FwuQxUX9L3ZzfMEvjGljfODpCOrFZRY-q8SvbxpHtGNQ");
        webSocketClient.connect();
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

                webSocketClient.send(msg.getText().toString());

                messageList.add(messageModel);
                messageListAdapter.notifyDataSetChanged();

                msg.getText().clear();
            }
        }
    };
}
