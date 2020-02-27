package com.example.livechat.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageModel implements Parcelable {
    private String Id;
    private String Msg;
    private String Date;
    private UserModel sender;

    public MessageModel(){
        this.Id = "";
        this.Msg = "";
        this.Date = "";
        this.sender = null;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public UserModel getSender() {
        return sender;
    }

    public void setSender(UserModel sender) {
        this.sender = sender;
    }

    protected MessageModel(Parcel in) {
        Id = in.readString();
        Msg = in.readString();
        Date = in.readString();
        sender = in.readParcelable(UserModel.class.getClassLoader());
    }

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel in) {
            return new MessageModel(in);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(Msg);
        dest.writeString(Date);
        dest.writeParcelable(sender, flags);
    }
}
