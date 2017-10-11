package com.madushanka.imotoristofficer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class AddOffenceCompleteFragment extends Fragment {

    AutoCompleteTextView autoTextView;
    TextView offence_list ;
    Button clear;
    Button submit;
    Button add_offence;
    int count = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v =inflater.inflate(R.layout.fragment_add_offence_complete,container,false);

        autoTextView = (AutoCompleteTextView) v.findViewById(R.id.autocompleteEditTextView);
        offence_list = (TextView) v.findViewById(R.id.offence_list) ;


        String[] Offence_list = { "Not attempting to avoid an accident",
                "Fast driving in dangerous places",
                "Careless driving in Highways",
                "Driving away after an accident",
                "Not reporting an accident to the nearest police station",
                "Fast driving neglecting the speed limits",
                "Not driving on left",
                "Overtaking when the road is not clear",
                "Overtaking on the wrong side",
                "Not giving way to the vehicles on the right",
                "Following on the Highway without any reason",
                "Ignoring Road Signs",
                "Ignoring police orders, signs or verbal statements",
                "Driving vehicles which haven’t passed the environmental friendly tests",
                "Driving vehicles which has loud noise than what is recommended",
                "Tooting near prohibited places",
                "Using vehicles which are prone to hurt other vehicles, people or property",
                "Not wearing seat belts",
                "Over taking in junctions",
                "Inability to stop vehicles near zebra crossings",
                "Riding motor bikes without helmets",
                "Driving while using mobile phones"
        };


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Offence_list);

        autoTextView.setThreshold(1);

        autoTextView.setAdapter(arrayAdapter);

        add_offence = (Button) v.findViewById(R.id.add_offence);
        submit = (Button) v.findViewById(R.id.add_offence_submit);
        clear = (Button) v.findViewById(R.id.offence_clear);

        add_offence.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                add_offence_to_list();
            }
        });

        return v;

    }



    public void add_offence_to_list(){

        autoTextView.onEditorAction(EditorInfo.IME_ACTION_DONE);
        String s = autoTextView.getText().toString();

        if(!s.equals("")) {


            if (count == 0) {

                clear.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
            }

            if (count < 3) {

                String offence_list_text = offence_list.getText().toString();

                offence_list.setText(offence_list_text + s + "\n\n");

                autoTextView.setText("");
                count++;

            }
            else{

                autoTextView.setText("");
                autoTextView.setHint("You have reached maximum offence count");
                autoTextView.setHintTextColor(getResources().getColor(R.color.fbutton_color_pomegranate));

            }
        }
    }


}