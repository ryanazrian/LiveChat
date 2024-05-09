package com.example.livechat.activity.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livechat.R;
import com.example.livechat.activity.chat.MessageListAdapter;
import com.example.livechat.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<UserModel> userList;
    private ItemClickListener mItemClickListener;

    protected UserListAdapter(Context mContext, List<UserModel> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewItem = inflater.inflate(R.layout.item_user, parent, false);
        viewHolder = new UserHolder(viewItem);
        return viewHolder;
    }

    public void addItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserModel userModel = userList.get(position);

        UserHolder userHolder = (UserHolder) holder;
        userHolder.username.setText(userModel.getName());
        userHolder.email.setText(userModel.getEmail());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    //Define your Interface method here
    public interface ItemClickListener {
        void onItemClick(int position);
    }

    private class UserHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView email;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.usernameList);
            email = (TextView) itemView.findViewById(R.id.emailList);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mItemClickListener != null) {
                        int position = getAdapterPosition();
                        mItemClickListener.onItemClick(position);

                    }
                }
            });

        }
    }
}
