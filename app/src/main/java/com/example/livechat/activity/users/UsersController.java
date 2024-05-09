package com.example.livechat.activity.users;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.VolleyError;
import com.example.livechat.R;
import com.example.livechat.model.UserModel;
import com.example.livechat.services.IResult;
import com.example.livechat.services.SessionManagement;
import com.example.livechat.services.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersController {
    private UsersActivity usersActivity;
    private SessionManagement sessionManagement;
    private UserListAdapter userListAdapter;
    private VolleyService volleyService;
    private IResult mResultCallback = null;

    private List<UserModel> userList;

    private String token;
    private String username;

    protected UsersController(UsersActivity usersActivity, UserListAdapter userListAdapter, List<UserModel> userList, SessionManagement sessionManagement) {
        this.usersActivity = usersActivity;
        this.userListAdapter = userListAdapter;
        this.sessionManagement = sessionManagement;
        this.userList = userList;

        initVolleyCallback();
        volleyService = new VolleyService(mResultCallback, usersActivity);

        getSessionData();
        getUsersList();
    }

    protected void getSessionData() {
        token = sessionManagement.getSessionToken();
        username = sessionManagement.getUsername();
    }

    protected void getUsersList() {
        String URI = usersActivity.getString(R.string.main_uri) + "/auth/getAllUser";

        HashMap<String, String> headers = new HashMap<>();
        headers.put("authorization", token);

        volleyService.getDataVolley("GETUSERS", URI, headers);
    }

    private void processAPIData(JSONObject response) throws JSONException {
        List<UserModel> userList = new ArrayList<>();
        JSONArray users= response.getJSONArray("list");

        for(int i = 0; i< users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            UserModel userJava = new UserModel();
            userJava.setID(user.getString("id"));
            userJava.setName(user.getString("username"));
            userJava.setEmail(user.getString("email"));

            userList.add(userJava);
        }

        this.userList.addAll(userList);
        userListAdapter.notifyDataSetChanged();
    }

    //result API handling
    private void initVolleyCallback(){
        mResultCallback = new IResult() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void notifySuccess(String requestType, JSONObject response) throws JSONException {
                switch (requestType){
                    //posting data result handling
                    case "GETUSERS":
                        processAPIData(response);
                        break;

                }

            }

            //error handling
            @Override
            public void notifyError(String requestType, VolleyError error) {
                System.out.println(error);
            }
        };
    }
}
