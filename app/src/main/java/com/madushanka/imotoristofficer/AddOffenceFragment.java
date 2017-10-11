package com.madushanka.imotoristofficer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class AddOffenceFragment extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v =inflater.inflate(R.layout.fragment_add_offence,container,false);
        Button confirm_offence = (Button) v.findViewById(R.id.add_offence_confirm);
        confirm_offence.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment  fragment = new AddOffenceCompleteFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_view, fragment);
                ft.addToBackStack("add offence complete");
                ft.commit();
            }
        });
        return v;

    }


}