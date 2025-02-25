package com.example.livechat.activity.chat;

import android.util.Log;

import com.example.livechat.R;
import com.example.livechat.model.MessageModel;
import com.example.livechat.model.UserModel;
import com.example.livechat.services.DatabaseHandler;
import com.example.livechat.services.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private UserModel chatTarget;
    private StompClient mStompClient;

    private String token;
    private String username;
    private String id;
    private String roomID;
    private ArrayList<MessageModel> messageList;
    private SimpleDateFormat sdf;

    private MessageListAdapter messageListAdapter;
    private DatabaseHandler databaseHandler;

    protected ChatController(ChatActivity chatActivity, MessageListAdapter messageListAdapter, ArrayList<MessageModel> messageList, UserModel chatTarget) {
        this.chatActivity = chatActivity;
        this.messageListAdapter = messageListAdapter;
        this.messageList = messageList;
        this.chatTarget = chatTarget;


        sessionManagement = new SessionManagement(chatActivity);
        sdf = new SimpleDateFormat("HH:mm");
        databaseHandler = new DatabaseHandler(chatActivity);

        roomID = createRoomID();

        getSessionData();
        createWebSocketClient();
        getMessageList();
//        Log.d("messageList", this.messageList.toString());
    }

    protected void getSessionData() {
        token = sessionManagement.getSessionToken();
        username = sessionManagement.getUsername();
        id = sessionManagement.getID();


        Log.d("Session Token", token);
    }

    private String createRoomID() {
        String unsortedID = username + id + chatTarget.getName() + chatTarget.getID();

        char[] chars = unsortedID.toCharArray();
        Arrays.sort(chars);
        String sortedID = new String(chars);

        return sortedID;
    }

    protected void createWebSocketClient() {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("authorization", token);
            mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, chatActivity.getString(R.string.ws_uri), headers);

            mStompClient.connect();

            mStompClient.topic("/topic2/" + roomID).subscribe(topicMessage -> {
                createMessageAdapter(topicMessage);
            }, throwable -> {
                Log.d("ERORR", "Error in subscribe to WS");
                sessionManagement.logoutUser();
            });
        } catch (Exception e) {
            Log.d("ERORR", "Error in connecting to WS");
        }
    }

    protected void getMessageList() {
        messageList.addAll((databaseHandler.getMessagesByRoomId(roomID)));
        Collections.reverse(messageList);
        messageListAdapter.notifyDataSetChanged();
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
                    messageModel.setRoomID(roomID);

                    databaseHandler.addRecord(messageModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                messageList.add(0, messageModel);
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
        messageModel.setRoomID(roomID);

        JSONObject messageObject = new JSONObject();
        try {
            messageObject.put("name", username);
            messageObject.put("message", messageModel.getMsg());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mStompClient.send("/app/topic2/" + roomID, messageObject.toString()).subscribe();
//        databaseHandler.addRecord(messageModel);
    }





}
