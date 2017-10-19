package com.madushanka.imotoristofficer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.madushanka.imotoristofficer.controllers.LoginManager;
import com.madushanka.imotoristofficer.controllers.TokenManager;
import com.madushanka.imotoristofficer.controllers.UserManager;
import com.madushanka.imotoristofficer.entities.AccessToken;
import com.madushanka.imotoristofficer.entities.ApiError;
import com.madushanka.imotoristofficer.entities.Motorist;
import com.madushanka.imotoristofficer.network.ApiService;
import com.madushanka.imotoristofficer.network.RetrofitBuilder;

import net.bohush.geometricprogressview.GeometricProgressView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class AddOffenceFragment extends android.support.v4.app.Fragment {

    TokenManager tokenManager;
    UserManager userManager;
    Call<AccessToken> call;
    Call<Motorist> motorist_call;
    ApiService service;
    Call<String> call1;
    LoginManager loginManager;
    GeometricProgressView progressView;
    ApiService authService;
    private EditText dnumberField;
    private EditText vnumberField;

    TextView motorist;
    Button confirm_offence;

    private static final String TAG = "Add offence 1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v =inflater.inflate(R.layout.fragment_add_offence,container,false);
         confirm_offence = (Button) v.findViewById(R.id.add_offence_next);


        service = RetrofitBuilder.createService(ApiService.class);


        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        loginManager = LoginManager.getInstance(getActivity().getSharedPreferences("pref_login", MODE_PRIVATE));
        authService = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        userManager = UserManager.getInstance(getActivity().getSharedPreferences("pref_user", MODE_PRIVATE));

        progressView = (GeometricProgressView) v.findViewById(R.id.progressView);

        motorist = (TextView) v.findViewById(R.id.motorist_info);

        dnumberField = (EditText) v.findViewById(R.id.dlnumber);
        vnumberField = (EditText) v.findViewById(R.id.vehicle_number);

        dnumberField.setText("B507614");
        vnumberField.setText("CAC 5067");

        Button validate = (Button) v.findViewById(R.id.add_offence_validate);

        confirm_offence.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                Fragment  fragment = new RecyclerViewFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_view, fragment,"add_offence_2");
                ft.addToBackStack("add_offence_2");
                ft.commit();
            }
        });

        validate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(validate()){
                getMotorist();
                }
            }
        });



        return v;

    }


    void getMotorist() {

        motorist.setText("");
        progressView.setVisibility(View.GONE);
        confirm_offence.setVisibility(View.GONE);

        String dlNumber = dnumberField.getText().toString();
        Log.w(TAG, "DL Number: " + dlNumber);
        motorist_call = authService.motorist(dlNumber);

        progressView.setVisibility(View.VISIBLE);

        motorist_call.enqueue(new Callback<Motorist>() {
            @Override
            public void onResponse(Call<Motorist> motorist_call, Response<Motorist> response) {

                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {

                    progressView.setVisibility(View.GONE);
                   // Toast.makeText(getActivity(), response.body().getId(), Toast.LENGTH_LONG).show();
                    setMotoristInfo(response.body());
                    DashBoardActivity.m = response.body();
                    DashBoardActivity.m.setVehicle_number(vnumberField.getText().toString());

                    if (response.code() == 204) {

                            setMotoristInfo(response.body());
                            Toast.makeText(getActivity(), response.body().getId(), Toast.LENGTH_LONG).show();

                    }

                } else {
                    progressView.setVisibility(View.GONE);
                    if (response.code() == 422) {


                        Toast.makeText(getActivity(), "Invalid License Number", Toast.LENGTH_LONG).show();

                    } if (response.code() == 500) {

                        dnumberField.setError("Invalid License number");
                       // Toast.makeText(getActivity(), "Invalid License Number", Toast.LENGTH_LONG).show();

                    }
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());

                        Toast.makeText(getActivity(), apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    if (response.code() == 404) {

                        dnumberField.setError("Invalid License number");
                    }

                }

            }

            @Override
            public void onFailure(Call<Motorist> motorist_call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
                progressView.setVisibility(View.GONE);
            }
        });


    }


    public void setMotoristInfo(Motorist m){

        String s = "Full Name : "+m.getName()+"\n\nNIC No : "+m.getNic()+"\n\nDOB : "+m.getDate_of_birth();
        motorist.setText(s);
        confirm_offence.setVisibility(View.VISIBLE);


    }

    private boolean validate() {

        String dlno = dnumberField.getText().toString();
        String vno = vnumberField.getText().toString();


        if (TextUtils.isEmpty(dlno)) {
            dnumberField.setError("License number is empty ");
            return false;

        }

        if (TextUtils.isEmpty(vno)) {
            vnumberField.setError("Vehicle Number is empty ");
            return false;

        }

        return true;
    }

}