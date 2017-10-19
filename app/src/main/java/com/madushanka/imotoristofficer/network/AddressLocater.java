package com.madushanka.imotoristofficer.network;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.madushanka.imotoristofficer.DashBoardActivity;

import java.util.List;
import java.util.Locale;

/**
 * Created by madushanka on 10/16/17.
 */

public class AddressLocater extends AsyncTask<String, String, String> {

    private static final String TAG = "AddressLocater";

    Location location;
    Context c;
    String address;
    Double lat;
    Double lon;
    boolean flag = false;

    public AddressLocater(Context c, Double lat, Double lon,boolean flag) {
        this.c = c;
        this.lat = lat;
        this.lon = lon;
        this.flag = flag;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Context getC() {
        return c;
    }

    public void setC(Context c) {
        this.c = c;
    }

    @Override
    protected String doInBackground(String... params) {

        address = getCompleteAddressString(lat,lon);
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(c, "Address : "+address, Toast.LENGTH_LONG).show();
        if(flag){
            DashBoardActivity.m.setLocation(address);
        }
    }


    @Override
    protected void onPreExecute() {

    }


    @Override
    protected void onProgressUpdate(String... text) {
        Toast.makeText(c, "Getting Address.....", Toast.LENGTH_LONG).show();

    }

    public String getCompleteAddressString(Double lat,Double lon) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(c, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w(TAG, strReturnedAddress.toString());
            } else {
                Log.w(TAG, "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "Canont get Address!");
        }
        return strAdd;
    }
}
