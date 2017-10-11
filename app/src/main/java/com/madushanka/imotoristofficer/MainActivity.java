package com.madushanka.imotoristofficer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.madushanka.imotoristofficer.entities.AccessToken;
import com.madushanka.imotoristofficer.entities.ApiError;
import com.madushanka.imotoristofficer.entities.Login;
import com.madushanka.imotoristofficer.network.ApiService;
import com.madushanka.imotoristofficer.network.RetrofitBuilder;

import net.bohush.geometricprogressview.GeometricProgressView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    ApiService service;
    ApiService authService;
    TokenManager tokenManager;
    LoginManager loginManager;
    Call<AccessToken> call;
    GeometricProgressView progressView;
    private EditText mUsernameField;
    private EditText mPasswordField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();


        service = RetrofitBuilder.createService(ApiService.class);


        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        loginManager = LoginManager.getInstance(getSharedPreferences("pref_login", MODE_PRIVATE));
        authService = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        mUsernameField = (EditText) findViewById(R.id.empno);
        mPasswordField = (EditText) findViewById(R.id.password);
        progressView = (GeometricProgressView) findViewById(R.id.progressView);


        mAuth = FirebaseAuth.getInstance();


       mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d("app", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, user.getUid(), Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("app", "signInAnonymously:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }


                    }
                });

        if (loginManager.getLogin().getUsername() != null) {
                loginUserReturn();

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    public  void goHome(View v){
       // Intent i = new Intent(MainActivity.this,DashBoardActivity.class);
       // startActivity(i);
      //  finish();

        login();

    }

    void login() {

        if (validate()) {
            progressView.setVisibility(View.VISIBLE);
            final String email = mUsernameField.getText().toString().trim();
            final String password = mPasswordField.getText().toString().trim();

            call = service.login(email, password);


            call.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                    Log.w(TAG, "onResponse: " + response);

                    if (response.isSuccessful()) {
                        loginManager.saveLogin(new Login(email, password));
                        tokenManager.saveToken(response.body());
                        progressView.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, response.body() + "", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, DashBoardActivity.class));
                        finish();
                    } else {
                        if (response.code() == 422) {

                            progressView.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "Invalid Username or Password ", Toast.LENGTH_LONG).show();

                        }
                        if (response.code() == 401) {
                            ApiError apiError = Utils.convertErrors(response.errorBody());
                            progressView.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }

                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());

                }
            });


        }
    }

    private boolean validate() {

        String username = mUsernameField.getText().toString();
        String password = mPasswordField.getText().toString();


        if (TextUtils.isEmpty(username)) {
            mUsernameField.setError("Employee number is empty ");
            return false;

        }

        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Password is empty ");
            return false;

        }

        return true;
    }

    void loginUserReturn() {


        progressView.setVisibility(View.VISIBLE);
        final String email = loginManager.getLogin().getUsername();
        final String password = loginManager.getLogin().getPassword();

        call = service.login(email, password);


        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    loginManager.saveLogin(new Login(email, password));
                    tokenManager.saveToken(response.body());
                    progressView.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, response.body() + "", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, DashBoardActivity.class));
                    finish();
                } else {
                    if (response.code() == 422) {

                        progressView.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "Invalid Username or Password ", Toast.LENGTH_LONG).show();

                    }
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        progressView.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());

            }
        });



    }


}
