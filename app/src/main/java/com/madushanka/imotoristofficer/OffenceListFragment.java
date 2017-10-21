package com.madushanka.imotoristofficer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.madushanka.imotoristofficer.controllers.LoginManager;
import com.madushanka.imotoristofficer.controllers.TokenManager;
import com.madushanka.imotoristofficer.entities.AccessToken;
import com.madushanka.imotoristofficer.entities.ApiError;
import com.madushanka.imotoristofficer.entities.Offence;
import com.madushanka.imotoristofficer.entities.RecyclerViewAdapter;
import com.madushanka.imotoristofficer.entities.User;
import com.madushanka.imotoristofficer.network.ApiService;
import com.madushanka.imotoristofficer.network.RetrofitBuilder;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import net.bohush.geometricprogressview.GeometricProgressView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pugman.com.simplelocationgetter.SimpleLocationGetter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class OffenceListFragment extends Fragment implements  SimpleLocationGetter.OnLocationGetListener{

    private Context context;
    private RecyclerViewAdapter adapter;
    private List<Offence> arrayList;
    List<Offence> list_offence;
    TokenManager tokenManager;
    Call<List<Offence>> offence_call;
    ApiService authService;
    GeometricProgressView progressView;
    TextView title;
    SparseBooleanArray mSelectedItemsIds;

    private static final String TAG = "Pick_Offence_fragment";

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

    public OffenceListFragment() {
        mSelectedItemsIds = new SparseBooleanArray();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v  = inflater.inflate(R.layout.recycler_view_fragment, container, false);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        authService = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        progressView = (GeometricProgressView) v.findViewById(R.id.progressView);
        title = (TextView)v.findViewById(R.id.offence_list_title);
        getOffences(v);



        return v;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onClickEvent(view);
    }

    private void populateRecyclerView(View view,List<Offence> list) {

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = list;
        list_offence = new ArrayList<Offence>();
        adapter = new RecyclerViewAdapter(context,list,mSelectedItemsIds);
        recyclerView.setAdapter(adapter);


    }

    private void onClickEvent(View view) {
        view.findViewById(R.id.add_offence_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray selectedRows = adapter.getSelectedIds();
                mSelectedItemsIds = selectedRows;
                if (selectedRows.size() > 0) {

                    for (int i = 0; i < selectedRows.size(); i++) {
                        if (selectedRows.valueAt(i)) {
                            Offence selectedOffence = arrayList.get(selectedRows.keyAt(i));
                            list_offence.add(selectedOffence);

                        }
                    }
                   // Toast.makeText(context, "Selected Rows\n" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                    DashBoardActivity.m.setOffence_list(list_offence);

                    if(CheckEnableGPS()) {
                       // DashBoardActivity.getLocation();
                        goToMap();
                    }

                }
                else{
                    Toast.makeText(context, "Select offence to continue ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onLocationReady(Location location){
        Log.d("LOCATION", "onLocationReady: lat="+location.getLatitude() + " lon="+location.getLongitude());
    }

    @Override
    public void onError(String error){
        Log.e("LOCATION", "Error: "+error);
    }

    public void goToMap(){
        Fragment  fragment = new MapFragment_Offfence();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
       // getActivity().getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.replace(R.id.main_view, fragment,"map_offence");
        ft.addToBackStack("map_offence");
        ft.commit();
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
                // getActivity().finish();
                showManualLocationDialog();

            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showManualLocationDialog(){
        new LovelyTextInputDialog(getActivity(),R.style.TintTheme)
                .setTopColorRes(R.color.bg)
                .setConfirmButtonColor(R.color.bg_register)
                .setMessage("Offence Location\n\nEnter city name or street name")
                .setIcon(R.drawable.ic_location)
                .setInputFilter("Location is empty", new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("\\w+");
                    }
                })
                .setConfirmButton("FINISH", new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                      //  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                        DashBoardActivity.m.setLocation(text);
                        Fragment  fragment = new AddOffenceCompleteFragment();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.main_view, fragment,"add_offence_3");
                        ft.addToBackStack("add_offence_3");
                        ft.commit();

                    }
                })
                .show();
    }

    void getOffences(final View v) {


        offence_call = authService.offences();

        offence_call.enqueue(new Callback<List<Offence>>() {
            @Override
            public void onResponse(Call<List<Offence>>offence_call, Response<List<Offence>> response) {

                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {

                    if (response.code() == 200) {

                        // Toast.makeText(getActivity(), response.body().get(0).getDescription(), Toast.LENGTH_LONG).show();
                        populateRecyclerView(v,response.body());
                        progressView.setVisibility(View.GONE);
                        title.setVisibility(View.VISIBLE);

                    }

                } else {
                    if (response.code() == 422) {


                        Toast.makeText(getActivity(), "Error ", Toast.LENGTH_LONG).show();

                    }
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());

                        Toast.makeText(getActivity(), apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<List<Offence>> offence_call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());

            }
        });


    }
}
