package com.madushanka.imotoristofficer;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.madushanka.imotoristofficer.network.AddressLocater;

import java.util.List;
import java.util.Locale;

/**
 * Created by madushanka on 4/15/17.
 */

public class MapFragment_Offfence extends android.support.v4.app.Fragment implements OnMapReadyCallback {

    public static MapView map;
    public static Location myLocation;
    public static GoogleMap mMap;
    public boolean flag = true;

    public static  String Address;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Button finsh_button;
    Location locationCt;
    String address;
    AddressLocater al;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.mapview_offence, container, false);
        map = (MapView) v.findViewById(R.id.map_offence);
        map.onCreate(savedInstanceState);

        map.getMapAsync(this);


        View locationButton = map.findViewById(Integer.parseInt("2"));

        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        View zoom = map.findViewById(Integer.parseInt("1"));

        RelativeLayout.LayoutParams rlp1 = (RelativeLayout.LayoutParams) zoom.getLayoutParams();

        rlp1.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        rlp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);

        rlp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        rlp1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);

        finsh_button = (Button) v.findViewById(R.id.add_offence_finish);

        finsh_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
              //  Toast.makeText(getActivity().getApplicationContext(),"Finish ",Toast.LENGTH_LONG).show();

                Fragment fragment = new AddOffenceCompleteFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_view, fragment,"add_offence_3");
                ft.addToBackStack("add_offence_3");
                ft.commit();
            }
        });

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        try {

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setTrafficEnabled(true);
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setPadding(0, 400, 30, 150);

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng latLng) {

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                    mMap.clear();
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.addMarker(markerOptions);
                    al = new AddressLocater(getActivity(),latLng.latitude,latLng.longitude,true);
                    al.execute();
                    DashBoardActivity.m.setLocation(address);
                }
            });

            if(DashBoardActivity.mLocation!=null) {
                LatLng latLng = new LatLng(DashBoardActivity.mLocation.getLatitude(), DashBoardActivity.mLocation.getLongitude());
                CameraUpdate l = CameraUpdateFactory.newLatLngZoom(
                        latLng, 15);
                mMap.animateCamera(l);
                al = new AddressLocater(getActivity(),DashBoardActivity.mLocation.getLatitude(),DashBoardActivity.mLocation.getLongitude(),true);
                al.execute();
                DashBoardActivity.m.setLocation(address);
                DashBoardActivity.m.setLocation_lat(DashBoardActivity.mLocation.getLatitude()+"");
                DashBoardActivity.m.setLocation_lon(DashBoardActivity.mLocation.getLongitude()+"");

            }else{ DashBoardActivity.getLocation();}

        }
        catch (SecurityException e){

            Toast.makeText(getActivity().getApplicationContext(),"No Permission for GPS ",Toast.LENGTH_LONG).show();
            DashBoardActivity.getLocation();
            showDialogGPS();
        }
    }


    public void showDialogGPS(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("GPS Location is not enable. Enable GPS ? ");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
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
}

