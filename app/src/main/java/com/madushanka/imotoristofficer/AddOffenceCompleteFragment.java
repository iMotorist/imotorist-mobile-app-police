package com.madushanka.imotoristofficer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.madushanka.imotoristofficer.controllers.TokenManager;
import com.madushanka.imotoristofficer.entities.ApiError;
import com.madushanka.imotoristofficer.entities.Motorist;
import com.madushanka.imotoristofficer.entities.Offence;
import com.madushanka.imotoristofficer.entities.Ticket;
import com.madushanka.imotoristofficer.network.ApiService;
import com.madushanka.imotoristofficer.network.RetrofitBuilder;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;
import net.bohush.geometricprogressview.GeometricProgressView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class AddOffenceCompleteFragment extends Fragment {

    private static final String TAG = "Store_Ticket";
    AutoCompleteTextView autoTextView;
    TextView offence_summary ;
    Button issue_ticket;
    Button add_remark;
    Button offence_list_view;
    GeometricProgressView progressView;
    Call<Ticket> ticket_call;
    ApiService authService;
    TokenManager tokenManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v =inflater.inflate(R.layout.fragment_add_offence_complete,container,false);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        authService = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        progressView = (GeometricProgressView) v.findViewById(R.id.progressView);

        offence_list_view = (Button) v.findViewById(R.id.view_offence_list);

        add_remark = (Button) v.findViewById(R.id.add_remark);

        issue_ticket = (Button) v.findViewById(R.id.add_offence_confirm);

        issue_ticket.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

               saveTicket();
            }
        });

        add_remark.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

               showRemarkDialog();
            }
        });

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


    public void showRemarkDialog(){
        new LovelyTextInputDialog(getActivity(),R.style.TintTheme)
                .setTopColorRes(R.color.bg)
                .setConfirmButtonColor(R.color.bg_register)
                .setMessage("Enter offence remark")
                .setInitialInput(DashBoardActivity.m.getRemark())
                .setIcon(R.drawable.ic_remark)
                .setInputFilter("Remark is empty", new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        if(text.isEmpty()){
                            return false;
                        }
                        return true;
                    }
                })
                .setConfirmButton("SAVE", new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {

                        DashBoardActivity.m.setRemark(text);

                    }
                })
                .show();
    }


    void saveTicket() {


        Motorist m = DashBoardActivity.m;

        List<String> offence_ids = new ArrayList<String>();

        for (Offence f:m.getOffence_list()) {

            offence_ids.add(f.getId());
        }

        ticket_call = authService.issueTicket(m.getMotorist().getLicense_no(),m.getVehicle_number(),m.getLocation_lat(),m.getLocation_lon(),m.getLocation(),m.getRemark(),offence_ids);


        ticket_call.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> ticket_call, Response<Ticket> response) {

                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {

                    Toast.makeText(getActivity(),response.body().getMotorist_vehicle_classes().get(0), Toast.LENGTH_LONG).show();

                } else {
                    if (response.code() == 422) {


                        Toast.makeText(getActivity(), "Invalid", Toast.LENGTH_LONG).show();

                    }
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());

                        Toast.makeText(getActivity(), apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<Ticket>  ticket_call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());

            }
        });


    }
}