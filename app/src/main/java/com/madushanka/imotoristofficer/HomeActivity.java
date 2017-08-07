package com.madushanka.imotoristofficer;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.nightonke.boommenu.Util;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        setMenu();

    }

    public void setMenu(){

        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_5);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_5);
        bmb.setHighlightedColor(Color.DKGRAY);
        bmb.setDimColor(Color.TRANSPARENT);


        HamButton.Builder builder = new HamButton.Builder()
                .normalImageRes(R.drawable.plus)
                .normalTextRes(R.string.menu_1)
                .highlightedColorRes(R.color.fbutton_color_silver)
                .typeface(Typeface.SANS_SERIF)
                .normalColorRes(R.color.bg);
        bmb.addBuilder(builder);

        HamButton.Builder builder1 = new HamButton.Builder()
                .normalImageRes(R.drawable.history)
                .normalTextRes(R.string.menu_2)
                .highlightedColorRes(R.color.fbutton_color_silver)
                .typeface(Typeface.SANS_SERIF)
                .normalColorRes(R.color.bg);
        bmb.addBuilder(builder1);

        HamButton.Builder builder2 = new HamButton.Builder()
                .normalImageRes(R.drawable.maps)
                .normalTextRes(R.string.menu_3)
                .highlightedColorRes(R.color.fbutton_color_silver)
                .typeface(Typeface.SANS_SERIF)
                .normalColorRes(R.color.bg);
        bmb.addBuilder(builder2);

        HamButton.Builder builder3 = new HamButton.Builder()
                .normalImageRes(R.drawable.account)
                .normalTextRes(R.string.menu_4)
                .highlightedColorRes(R.color.fbutton_color_silver)
                .typeface(Typeface.SANS_SERIF)
                .normalColorRes(R.color.bg);
        bmb.addBuilder(builder3);

        HamButton.Builder builder4 = new HamButton.Builder()
                .normalImageRes(R.drawable.logout)
                .normalTextRes(R.string.menu_5)
                .highlightedColorRes(R.color.fbutton_color_silver)
                .typeface(Typeface.SANS_SERIF)
                .normalColorRes(R.color.bg);
        bmb.addBuilder(builder4);


        bmb.setRippleEffect(false);
        bmb.setNormalColor(Color.DKGRAY);
        bmb.setAutoBoomImmediately(true);

    }


}
