package com.example.livechat.activity.login;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.livechat.model.UserModel;
import com.example.livechat.services.SessionManagement;
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

public class LoginController {
    private LoginActivity loginActivity;
    private UserModel userModel;
    private SessionManagement sessionManagement;
    private FirebaseAuth auth;

    private int RC_SIGN_IN = 1009;
    private String id;
    private String token;
    private String name;
    private String email;


    protected LoginController(LoginActivity loginActivity){
        this.loginActivity = loginActivity;

        auth = FirebaseAuth.getInstance();

        //Model
        userModel = new UserModel();

        //session
        sessionManagement = new SessionManagement(loginActivity);
    }

    protected void login(String email, String password){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                loginActivity.onFirebaseLogin(task);
            }
        });
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



}
