package com.madushanka.imotoristofficer;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.madushanka.imotoristofficer.controllers.LoginManager;
import com.madushanka.imotoristofficer.controllers.TokenManager;
import com.madushanka.imotoristofficer.controllers.UserManager;
import com.madushanka.imotoristofficer.entities.AccessToken;
import com.madushanka.imotoristofficer.entities.ApiError;
import com.madushanka.imotoristofficer.entities.Motorist;
import com.madushanka.imotoristofficer.entities.User;
import com.madushanka.imotoristofficer.network.ApiService;
import com.madushanka.imotoristofficer.network.PushNotification;
import com.madushanka.imotoristofficer.network.RetrofitBuilder;
import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

import pugman.com.simplelocationgetter.SimpleLocationGetter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SimpleLocationGetter.OnLocationGetListener {

    private static final String TAG = "DashBoardActivity";

    Fragment fragment = null;
    private FirebaseAuth mAuth;
    TokenManager tokenManager;
    UserManager userManager;
    Call<AccessToken> call;
    Call<User> user_call;
    ApiService service;
    Call<String> firebase_call;
    LoginManager loginManager;
    GeometricProgressView progressView;
    ApiService authService;
    public static Location mLocation;
    public static SimpleLocationGetter locationGetter;
    public static Motorist m ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mAuth = FirebaseAuth.getInstance();
        loginFirebase();

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
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.replace(R.id.main_view, fragment);
        ft.addToBackStack("home");
        ft.commit();

        getUser();

        locationGetter = new SimpleLocationGetter(this, this);
        locationGetter.getLastLocation();



    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count ==0) {
            super.onBackPressed();

        } else {

            final Fragment homefragment=getSupportFragmentManager().findFragmentByTag("home");
            final Fragment ticketfragment=getSupportFragmentManager().findFragmentByTag("ticket");

            if(homefragment != null && homefragment.isVisible()){
                super.onBackPressed();
            }

            else if(ticketfragment != null && ticketfragment.isVisible()){

                fragment = new HomeFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft.replace(R.id.main_view, fragment);
                ft.addToBackStack("home");
                ft.commit();
            }

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
            m = new Motorist();
            getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (id == R.id.nav_offence_history) {
            fragment = new TicketListFragment();
            s = "ticket_all";
            getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        } else if (id == R.id.nav_notifications) {

        } else if (id == R.id.nav_map) {
            if(CheckEnableGPS()) {
                getLocation();
                fragment = new MapFragment();
                s = "map";
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }else if (id == R.id.nav_profile) {

        }else if (id == R.id.nav_about) {

        }else if (id == R.id.nav_logout) {
                logout();
        }


        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_view, fragment,s);
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

    @Override
    public void onLocationReady(Location location){
        Log.d("LOCATION", "onLocationReady: lat="+location.getLatitude() + " lon="+location.getLongitude());
       // Toast.makeText(DashBoardActivity.this,"lat="+location.getLatitude() + " lon="+location.getLongitude()+"Address : "+getCompleteAddressString(location), Toast.LENGTH_LONG).show();
        mLocation = location;
    }

    @Override
    public void onError(String error){
        Log.e("LOCATION", "Error: "+error);


    }

    public static void getLocation(){

        locationGetter.getLastLocation();

    }


    private boolean CheckEnableGPS(){

        boolean res = false;
        String provider = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.equals(""))

            res =   true;

        else{

            showDialogGPS();

        }

        return res;
    }

    public void showDialogGPS(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is not enable. Enable GPS ?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        getLocation();
                        // finish();
                    }
                });
        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // getActivity().finish();

            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void loginFirebase(){

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d("app", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String s = FirebaseInstanceId.getInstance().getToken();
                            updateFirebase(s);

                            //  Toast.makeText(DashBoardActivity.this, s, Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("app", "signInAnonymously:failure", task.getException());
                            //   Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }


                    }
                });

    }

    void updateFirebase(String token) {


        firebase_call = authService.firebase(token);


        firebase_call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String>  firebase_call, Response<String> response) {

                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {

                        //Toast.makeText(DashBoardActivity.this,"Saved", Toast.LENGTH_LONG).show();

                } else {
                    if (response.code() == 422) {


                      //  Toast.makeText(DashBoardActivity.this, "Invalid", Toast.LENGTH_LONG).show();

                    }
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());

                       // Toast.makeText(DashBoardActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<String>  firebase_call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());

            }
        });


    }
}
