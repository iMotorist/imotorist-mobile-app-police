package com.madushanka.imotoristofficer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.madushanka.imotoristofficer.entities.Motorist;
import com.madushanka.imotoristofficer.entities.Offence;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import net.bohush.geometricprogressview.GeometricProgressView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;


public class AddOffenceCompleteFragment extends Fragment {

    AutoCompleteTextView autoTextView;
    TextView offence_summary ;
    Button issue_ticket;
    Button offence_list_view;
    GeometricProgressView progressView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v =inflater.inflate(R.layout.fragment_add_offence_complete,container,false);

        progressView = (GeometricProgressView) v.findViewById(R.id.progressView);

        offence_list_view = (Button) v.findViewById(R.id.view_offence_list);

        offence_list_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            showOffenceDialog();
            }
        });

        offence_summary = (TextView) v.findViewById(R.id.offence_summary);

        setOffence_summary();

        return v;

    }


    public void setOffence_summary(){

        Motorist m = DashBoardActivity.m;

        String s = "Full Name : "+m.getName()+
                   "\n\nDriving License No : "+m.getMotorist().getLicense_no()+"\n\nVehicle No : "+m.getVehicle_number()+"\n\nLocation : \n\n"+m.getLocation();

        offence_summary.setText(s);

    }

    public void showOffenceDialog()
    {

        String offence = "";

        List<Offence> list  = DashBoardActivity.m.getOffence_list();

        for (Offence f:list) {
            offence = offence +f.getDescription()+"\n\n";

        }

        new LovelyStandardDialog(getActivity())
                .setTopColorRes(R.color.bg)
                .setButtonsColorRes(R.color.bg_register)
                .setIcon(R.drawable.ic_offence)
                .setMessage(offence)
                .setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(context, "positive clicked", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }


}