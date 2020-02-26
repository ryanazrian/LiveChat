package com.example.livechat.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {
    private String ID;
    private String Token;
    private String Name;
    private String Email;
    private String BirthDate;

    public UserModel(){
        this.ID = "";
        this.Token = "";
        this.Name = "";
        this.Email = "";
        this.BirthDate ="";
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    protected UserModel(Parcel in) {
        ID = in.readString();
        Token = in.readString();
        Name = in.readString();
        Email = in.readString();
        BirthDate = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(Token);
        dest.writeString(Name);
        dest.writeString(Email);
        dest.writeString(BirthDate);
    }
}
