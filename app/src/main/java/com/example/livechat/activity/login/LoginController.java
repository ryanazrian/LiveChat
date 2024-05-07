package com.example.livechat.activity.login;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.livechat.R;
import com.example.livechat.model.UserModel;
import com.example.livechat.services.IResult;
import com.example.livechat.services.SessionManagement;
import com.example.livechat.services.VolleyService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginController {
    private LoginActivity loginActivity;
    private UserModel userModel;
    private SessionManagement sessionManagement;
    private FirebaseAuth auth;
    private VolleyService mVolleyService;

    private int RC_SIGN_IN = 1009;
    private String id;
    private String token;
    private String name;
    private String email;
    private IResult mResultCallback = null;



    protected LoginController(LoginActivity loginActivity){
        this.loginActivity = loginActivity;

        auth = FirebaseAuth.getInstance();

        //Model
        userModel = new UserModel();

        //session
        initVolleyCallback();
        sessionManagement = new SessionManagement(loginActivity);
        mVolleyService = new VolleyService(mResultCallback, loginActivity);
    }

    protected void login(String email, String password){

        Map<String, String> param = new HashMap<>();
        param.put("username", email);
        param.put("password", password);

        String URI = loginActivity.getString(R.string.main_uri) + "/auth/signin";

        mVolleyService.postDataVolley("LOGIN", URI, param);

    }

    protected void googleLogin(){
        //sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("372116183431-u7l56oikk1sf3jd7vvs0fpen05ak7c8f.apps.googleusercontent.com") //id token get from google console developer
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(loginActivity, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        loginActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //handle when sign in succeed
    protected void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            id = account.getId();
            token = account.getIdToken();
            name = account.getDisplayName();
            email = account.getEmail();

            userModel.setID(id);
            userModel.setToken(token);
            userModel.setName(name);
            userModel.setEmail(email);

            sessionManagement.createLoginSession(userModel);
            loginActivity.onSucceedGoogleLogin();

            Log.d("GOOGLESIGNIN", account.getEmail());


        } catch (ApiException e) {
            Log.w("DDD", "signInResult:failed code=" + e.getStatusCode());
//            utility.showProgress(loginActivity.mProgressView, loginActivity, false);
            Toast.makeText(loginActivity, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void handleSignInEmailUsername(JSONObject response) throws JSONException {
        id = response.getString("id");
        token = "Bearer " + response.getString("accessToken");
        name = response.getString("username");
        email = response.getString("email");

        userModel.setID(id);
        userModel.setToken(token);
        userModel.setName(name);
        userModel.setEmail(email);

        sessionManagement.createLoginSession(userModel);
        loginActivity.onSucceedGoogleLogin();
    }

    //result API handling
    private void initVolleyCallback(){
        mResultCallback = new IResult() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void notifySuccess(String requestType, JSONObject response) throws JSONException {
                switch (requestType){
                    //posting data result handling
                    case "LOGIN":
                        handleSignInEmailUsername(response);
                        Log.w("Login Response", response.toString());

                }

            }

            //error handling
            @Override
            public void notifyError(String requestType, VolleyError error) {
                System.out.println("ERROR");

                Log.d("ERRor", error.networkResponse.toString());
            }
        };
    }



}
