package com.example.livechat.services;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public interface IResult {
    public void notifySuccess(String requestType, JSONObject response) throws JSONException;
    public void notifyError(String requestType, VolleyError error);
}
