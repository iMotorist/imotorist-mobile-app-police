package com.madushanka.imotoristofficer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.madushanka.imotoristofficer.entities.AccessToken;
import com.madushanka.imotoristofficer.entities.ApiError;
import com.madushanka.imotoristofficer.entities.User;
import com.madushanka.imotoristofficer.network.ApiService;
import com.madushanka.imotoristofficer.network.RetrofitBuilder;

import net.bohush.geometricprogressview.GeometricProgressView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "DashBoardActivity";

    Fragment fragment = null;
    private FirebaseAuth mAuth;
    TokenManager tokenManager;
    UserManager userManager;
    Call<AccessToken> call;
    Call<User> user_call;
    ApiService service;
    Call<String> call1;
    LoginManager loginManager;
    GeometricProgressView progressView;
    ApiService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mAuth = FirebaseAuth.getInstance();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        service = RetrofitBuilder.createService(ApiService.class);


        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        loginManager = LoginManager.getInstance(getSharedPreferences("pref_login", MODE_PRIVATE));
        authService = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        userManager = UserManager.getInstance(getSharedPreferences("pref_user", MODE_PRIVATE));


        fragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_view, fragment);
        ft.addToBackStack("home");
        ft.commit();

        getUser();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count ==0) {
            super.onBackPressed();

        } else {
            getFragmentManager().popBackStack();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String s= "";
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            s= "home";
            getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (id == R.id.nav_add_offence) {
            fragment = new AddOffenceFragment();
            s = "offence";
            getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (id == R.id.nav_offence_history) {

        } else if (id == R.id.nav_notifications) {

        } else if (id == R.id.nav_map) {

        }else if (id == R.id.nav_profile) {

        }else if (id == R.id.nav_about) {

        }else if (id == R.id.nav_logout) {
                logout();
        }


        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_view, fragment);
            ft.addToBackStack(s);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void logoutUser(){

        mAuth.signOut();
        tokenManager.deleteToken();
        loginManager.deleteLogin();
        userManager.deleteUser();
        startActivity(new Intent(DashBoardActivity.this,MainActivity.class ));
        finish();
    }

    void logout() {

        call = authService.logout();


        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {

                    if (response.code() == 204) {


                        Toast.makeText(DashBoardActivity.this, "Logout Successful ", Toast.LENGTH_LONG).show();
                        logoutUser();

                    }

                } else {
                    if (response.code() == 422) {


                        Toast.makeText(DashBoardActivity.this, "Error ", Toast.LENGTH_LONG).show();

                    }
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());

                        Toast.makeText(DashBoardActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());

            }
        });

    }

    void getUser() {

        user_call = authService.user();


        user_call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> user_call, Response<User> response) {

                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {

                    //Toast.makeText(DashBoardActivity.this, response.body().getEmail(), Toast.LENGTH_LONG).show();
                    User u = new User(response.body().getId(),response.body().getEmail(),response.body().getName());
                    userManager.saveUser(u);

                    TextView full_name = (TextView) findViewById(R.id.name);
                    TextView nic = (TextView) findViewById(R.id.no);

                    full_name.setText(response.body().getFull_name());
                    nic.setText(response.body().getEmail());


                    if (response.code() == 204) {

                        //  Toast.makeText(HomeActivity.this, response.body().getFull_name(), Toast.LENGTH_LONG).show();

                    }

                } else {
                    if (response.code() == 422) {


                        Toast.makeText(DashBoardActivity.this, "Error ", Toast.LENGTH_LONG).show();

                    }
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());

                        Toast.makeText(DashBoardActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<User> user_call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());

            }
        });


    }
}
