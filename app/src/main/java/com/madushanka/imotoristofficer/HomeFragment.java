package com.madushanka.imotoristofficer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


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
                Fragment  fragment = new AddOffenceFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                getActivity().getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft.replace(R.id.main_view, fragment,"offence");
                ft.addToBackStack("offence");
                ft.commit();
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment  fragment = new MapFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                getActivity().getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft.replace(R.id.main_view, fragment,"map");
                ft.addToBackStack("map");
                ft.commit();
            }
        });

        return v;

    }


}