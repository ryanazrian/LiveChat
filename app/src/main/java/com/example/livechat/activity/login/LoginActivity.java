package com.example.livechat.activity.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.livechat.R;
import com.example.livechat.activity.MainActivity;
import com.example.livechat.activity.chat.ChatActivity;
import com.example.livechat.model.UserModel;
import com.example.livechat.services.SessionManagement;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar loadingProgressBar;
    private LoginController loginController;
    private SessionManagement sessionManagement;

    private int RC_SIGN_IN = 1009;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        final View signInGoogle = findViewById(R.id.sign_in_button);


        //controller
        loginController = new LoginController(this);
        sessionManagement = new SessionManagement(this);

        sessionManagement.checkLogin();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String email = usernameEditText.getText().toString();
//                String password = passwordEditText.getText().toString();

                String email = "ryanazrian";
                String password = "qwerty123";

                try {
                    if(email.matches("")|| password.matches("")){
                        throw new Exception("Fill the form!");
                    } else {
                        loadingProgressBar.setVisibility(View.VISIBLE);
                        loginController.login(email, password);
                    }
                }
                catch (Exception e) {
                    Log.w("Login Err", e.getMessage());
                }

            }
        });

        signInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.GoogleLoginTask googleLoginTask = new LoginActivity.GoogleLoginTask();
                googleLoginTask.execute();
                loadingProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    protected void onFirebaseLogin(Task<AuthResult> task){
        loadingProgressBar.setVisibility(View.GONE);
        if (!task.isSuccessful()) {
            // there was an error
//            System.out.println(task.isSuccessful());
            Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    protected void onSucceedGoogleLogin(){
        Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            loginController.handleSignInResult(task);
        }
    }

    protected class GoogleLoginTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            loginController.googleLogin();

            return null;
        }
    }
}
