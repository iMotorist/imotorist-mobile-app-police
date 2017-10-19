package com.madushanka.imotoristofficer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.madushanka.imotoristofficer.entities.ApiError;
import com.madushanka.imotoristofficer.network.RetrofitBuilder;
import okhttp3.ResponseBody;
import retrofit2.Converter;


public class Utils {


    public static ApiError convertErrors(ResponseBody response) {
        Converter<ResponseBody, ApiError> converter = RetrofitBuilder.getRetrofit().responseBodyConverter(ApiError.class, new Annotation[0]);

        ApiError apiError = null;

        try {
            apiError = converter.convert(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiError;
    }

    public static boolean CheckEnableGPS(Context c){

        boolean res = false;
        String provider = Settings.Secure.getString(c.getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.equals(""))

            res =   true;

        else{

            showDialogGPS(c);
        }

        return res;
    }


    public static boolean wifi(Context c){

        boolean res = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            res = true;
        } else {

            showDialogData(c);
        }

        return res;

    }

    public static void showDialogGPS(final Context c){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
        alertDialogBuilder.setMessage("GPS Location is not enable. Enable GPS ? ");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        c.startActivity(intent);
                        //finish();
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    public static  void showDialogData(final Context c){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
        alertDialogBuilder.setMessage("You seems to be offline . Enable Mobile Data or Wifi ? ") ;
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        c.startActivity(intent);

                    }
                });



        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
