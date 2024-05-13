package com.example.livechat.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.livechat.model.MessageModel;
import com.example.livechat.model.UserModel;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    // static variable
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "ChatManager";

    // table name
    private static final String TABEL_CHAT = "chats";

    // column tables
    private static final String KEY_ID = "id";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_DATE = "date";
    private static final String KEY_SENDER = "sender";
    private static final String KEY_ROOM_ID = "roomId";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABEL_CHAT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MESSAGE + " TEXT,"
                + KEY_DATE + " TEXT," + KEY_SENDER + " TEXT," + KEY_ROOM_ID + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABEL_CHAT);
        onCreate(sqLiteDatabase);
    }

    public void addRecord(MessageModel messageModel){
        SQLiteDatabase db  = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGE, messageModel.getMsg());
        values.put(KEY_DATE, messageModel.getDate());
        values.put(KEY_SENDER, messageModel.getSender().getName());
        values.put(KEY_ROOM_ID, messageModel.getRoomID());

        db.insert(TABEL_CHAT, null, values);
        db.close();
    }

    // get All Record
    public ArrayList<MessageModel> getAllMessages() {
        ArrayList<MessageModel> messageList = new ArrayList<MessageModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABEL_CHAT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MessageModel messageModel = new MessageModel();
                UserModel userModel = new UserModel();

                userModel.setName(cursor.getString(3));

                messageModel.setId(cursor.getString(0));
                messageModel.setMsg(cursor.getString(1));
                messageModel.setDate(cursor.getString(2));
                messageModel.setSender(userModel);

                messageList.add(messageModel);
            } while (cursor.moveToNext());
        }

        // return contact list
        return messageList;
    }

    public ArrayList<MessageModel> getMessagesByRoomId(String roomID) {
        ArrayList<MessageModel> messageList = new ArrayList<MessageModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABEL_CHAT + " WHERE " + KEY_ROOM_ID + " = '" + roomID +"'" ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MessageModel messageModel = new MessageModel();
                UserModel userModel = new UserModel();

                userModel.setName(cursor.getString(3));

                messageModel.setId(cursor.getString(0));
                messageModel.setMsg(cursor.getString(1));
                messageModel.setDate(cursor.getString(2));
                messageModel.setRoomID(cursor.getString(3));
                messageModel.setSender(userModel);


                messageList.add(messageModel);
            } while (cursor.moveToNext());
        }

        // return contact list
        return messageList;
    }

    public int getMessageCount() {
        String countQuery = "SELECT  * FROM " + TABEL_CHAT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
