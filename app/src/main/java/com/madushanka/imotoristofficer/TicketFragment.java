package com.madushanka.imotoristofficer;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import com.madushanka.imotoristofficer.controllers.TokenManager;
import com.madushanka.imotoristofficer.entities.Motorist;
import com.madushanka.imotoristofficer.entities.Ticket;
import com.madushanka.imotoristofficer.network.ApiService;
import com.madushanka.imotoristofficer.network.RetrofitBuilder;
import net.bohush.geometricprogressview.GeometricProgressView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import retrofit2.Call;
import static android.content.Context.MODE_PRIVATE;


public class TicketFragment extends Fragment {

    private static final String TAG = "_Ticket";
    Button print_ticket;
    GeometricProgressView progressView;
    Call<Ticket> ticket_call;
    ApiService authService;
    TokenManager tokenManager;
    WebView wv;
    TicketCreater tc;
    Ticket t;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        tc = new TicketCreater();
      //  t = DashBoardActivity.m.getTicket();

        View v =inflater.inflate(R.layout.fragment_ticket,container,false);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        authService = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        progressView = (GeometricProgressView) v.findViewById(R.id.progressView);

        wv = (WebView) v.findViewById(R.id.webView);

        WebSettings webSetting =wv.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);
        webSetting.setDomStorageEnabled(true);

        AssetManager mgr = getActivity().getBaseContext().getAssets();

        wv.loadDataWithBaseURL(null, tc.getTicketData(t), "text/html", "utf-8", null);

        print_ticket = (Button) v.findViewById(R.id.ticket_print);

        print_ticket.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createWebPrintJob(wv,t.getTicket_no());

            }
        });

        DashBoardActivity.m = new Motorist();
        return v;

    }

    public static String StreamToString(InputStream in) throws IOException {
        if(in == null) {
            return "";
        }
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
        }
        return writer.toString();
    }


    private void createWebPrintJob(WebView webView,String no) {

        PrintManager printManager = (PrintManager) getActivity()
                .getSystemService(Context.PRINT_SERVICE);

        PrintDocumentAdapter printAdapter =
                webView.createPrintDocumentAdapter();

        String jobName = getString(R.string.app_name) + " : "+no;

        printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }


    public Ticket getT() {
        return t;
    }

    public void setT(Ticket t) {
        this.t = t;
    }

}