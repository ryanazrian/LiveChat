package com.example.livechat.activity.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.livechat.R;
import com.example.livechat.model.MessageModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 3;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private ArrayList<MessageModel> messageList;
    private String username;

    public MessageListAdapter(Context context, ArrayList<MessageModel> messageList, String username) {
        mContext = context;
        this.messageList = messageList;
        this.username = username;
    }


    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        MessageModel message = (MessageModel) messageList.get(position);
        if (message.getSender().getName().matches(username))
            return VIEW_TYPE_MESSAGE_SENT;
        return VIEW_TYPE_MESSAGE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case VIEW_TYPE_MESSAGE_SENT:
                View viewItem = inflater.inflate(R.layout.item_message_sent, parent, false);
                viewHolder = new SentMessageHolder(viewItem);
                break;

            case VIEW_TYPE_MESSAGE_RECEIVED:
                View viewLoading = inflater.inflate(R.layout.item_message_received, parent, false);
                viewHolder = new ReceivedMessageHolder(viewLoading);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = messageList.get(position);
        switch (getItemViewType(position)){
            case VIEW_TYPE_MESSAGE_SENT:
                final SentMessageHolder sentMessageHolder  = (SentMessageHolder) holder;
                sentMessageHolder.messageText.setText(messageModel.getMsg());
                sentMessageHolder.timeText.setText(messageModel.getDate());
                break;

            case VIEW_TYPE_MESSAGE_RECEIVED:
                final ReceivedMessageHolder receivedMessageHolder  = (ReceivedMessageHolder) holder;
                receivedMessageHolder.messageText.setText(messageModel.getMsg());
                receivedMessageHolder.nameText.setText(messageModel.getSender().getName());
                receivedMessageHolder.timeText.setText(messageModel.getDate());
                break;
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }
    }

    @Override
    public int getItemCount() {
            return messageList.size();
    }
}
