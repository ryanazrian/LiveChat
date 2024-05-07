package com.example.livechat.activity.chat;

import android.util.Log;

import com.example.livechat.R;
import com.example.livechat.model.MessageModel;
import com.example.livechat.model.UserModel;
import com.example.livechat.services.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

public class ChatController {
    private ChatActivity chatActivity;
    private SessionManagement sessionManagement;
    private MessageModel messageModel;
    private UserModel userModel;
    private StompClient mStompClient;

    private String token;
    private String username;
    private ArrayList<MessageModel> messageList;
    private SimpleDateFormat sdf;

    private MessageListAdapter messageListAdapter;

    protected ChatController(ChatActivity chatActivity, MessageListAdapter messageListAdapter, ArrayList<MessageModel> messageList) {
        this.chatActivity = chatActivity;
        this.messageListAdapter = messageListAdapter;
        this.messageList = messageList;

        sessionManagement = new SessionManagement(chatActivity);
        sdf = new SimpleDateFormat("HH:mm");

        getSessionData();
        createWebSocketClient();
    }

    protected void getSessionData() {
        token = sessionManagement.getSessionToken();
        username = sessionManagement.getUsername();

        Log.d("Session Token", token);
    }

    protected void createWebSocketClient() {

        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", token);
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, chatActivity.getString(R.string.ws_uri), headers);

        mStompClient.connect();

        // Receive greetings
        mStompClient.topic("/topic/messages").subscribe(topicMessage -> {
            createMessageAdapter(topicMessage);
        });
    }

    protected void createMessageAdapter(StompMessage msg) throws JSONException {

        chatActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String msgString = msg.getPayload();
                JSONObject messageObject = null;
                try {
                    messageObject = new JSONObject(msgString);
                    messageModel = new MessageModel();
                    userModel = new UserModel();
                    userModel.setName(messageObject.getString("name"));

                    messageModel.setId(UUID.randomUUID().toString());
                    messageModel.setMsg(messageObject.getString("message"));
                    messageModel.setDate(sdf.format(new Date()));
                    messageModel.setSender(userModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                messageList.add(messageModel);
                messageListAdapter.notifyDataSetChanged();
            }
        });
    }

    protected void postMessage(String message) {
        messageModel = new MessageModel();
        userModel = new UserModel();

        userModel.setName(username);

        messageModel.setId(UUID.randomUUID().toString());
        messageModel.setMsg(message);
        messageModel.setDate(sdf.format(new Date()));
        messageModel.setSender(userModel);

        JSONObject messageObject = new JSONObject();
        try {
            messageObject.put("name", username);
            messageObject.put("message", messageModel.getMsg());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mStompClient.send("/app/chat", messageObject.toString()).subscribe();
    }





}
