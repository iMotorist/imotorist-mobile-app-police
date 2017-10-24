package com.madushanka.imotoristofficer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.madushanka.imotoristofficer.entities.Motorist;


public class HomeFragment extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v =inflater.inflate(R.layout.fragment_home,container,false);
        Button addOffenceButton = (Button) v.findViewById(R.id.primary_button1);
        Button offenceHistoryButton = (Button) v.findViewById(R.id.primary_button2);
        Button mapButton = (Button) v.findViewById(R.id.primary_button3);
        Button profileButton = (Button) v.findViewById(R.id.primary_button4);


        addOffenceButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DashBoardActivity.m = new Motorist();
                Fragment  fragment = new AddOffenceFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                getActivity().getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft.replace(R.id.main_view, fragment,"offence");
                ft.addToBackStack("offence");
                ft.commit();
            }
        });

        offenceHistoryButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Fragment  fragment = new TicketListFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                getActivity().getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft.replace(R.id.main_view, fragment,"all_ticket");
                ft.addToBackStack("all_ticket");
                ft.commit();
            }
        });


        mapButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DashBoardActivity.getLocation();
                if(CheckEnableGPS()) {
                   // DashBoardActivity.getLocation();
                    Fragment fragment = new MapFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    getActivity().getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ft.replace(R.id.main_view, fragment, "map");
                    ft.addToBackStack("map");
                    ft.commit();
                }
            }
        });

        return v;

    }


    private boolean CheckEnableGPS(){

        boolean res = false;
        String provider = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.equals(""))

            res =   true;

        else{

            showDialogGPS();
        }

        return res;
    }

    public void showDialogGPS(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("GPS is not enable. Enable GPS ?");
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
                //getActivity().finish();

            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}