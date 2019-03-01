package com.diretta.livescore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.diretta.livescore.common.BaseActivity;
import com.diretta.livescore.common.Common;
import com.diretta.livescore.model.CountryInfo;
import com.diretta.livescore.model.LeagueInfo;
import com.diretta.livescore.net.NetworkManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by suc on 4/25/2018.
 */

public class TipsActivity extends BaseActivity {
    LinearLayout lnContainer;
    ProgressDialog mProgDlg;
    private ArrayList<View> arrCountryViews = new ArrayList<View>();
    private ArrayList<View> arrLeagueViews = new ArrayList<View>();
    String mSelectedCountry = "";
    boolean bClick = false;
    ScrollView scrollView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tip);
        MobileAds.initialize(this, "ca-app-pub-6225734666899710~8077467313");
        //You need to add this code for building the request for the ad
        AdView mAdView = (AdView)findViewById(R.id.adTip);
        AdRequest adreqeust = new AdRequest.Builder().build();
        mAdView.loadAd(adreqeust);
        mProgDlg = new ProgressDialog(this);
        mProgDlg.setCancelable(false);
        mProgDlg.setMessage("Wait a little!");


        selectGame(0);
        lnContainer = (LinearLayout)findViewById(R.id.lnContainer);
        loadData();
    }
    private void selectGame(int index){

    }
    @Override
    protected void onResume() {
        super.onResume();
        initActionBar(2);
    }

    private void bindData(){
        lnContainer.removeAllViews();

        if(Common.getInstance().arrPopularGameListInfo.size() == 0){
        }else{
            for(int j = 0; j < Common.getInstance().arrPopularGameListInfo.size(); j++){
                final int index = j;
                Display display = getWindowManager().getDefaultDisplay();
                int width = display.getWidth() / 2;

                final View v2 = LayoutInflater.from(TipsActivity.this).inflate(R.layout.popular_item, null);
                v2.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
                v2.setTag(Common.getInstance().arrPopularGameListInfo.get(j));
                ImageView imgHome = (ImageView)v2.findViewById(R.id.imgHome);
                ImageLoader.getInstance().displayImage(Common.getInstance().arrPopularGameListInfo.get(j).home_image, imgHome, Common.getInstance().goodsDisplayImageOptions);
                TextView txtHomeName = (TextView)v2.findViewById(R.id.txtHomeName);
                txtHomeName.setText(Common.getInstance().arrPopularGameListInfo.get(j).home);
                ImageView imgAway = (ImageView)v2.findViewById(R.id.imgAway);
                ImageLoader.getInstance().displayImage(Common.getInstance().arrPopularGameListInfo.get(j).away_image, imgAway, Common.getInstance().goodsDisplayImageOptions);
                TextView txtAwayName = (TextView)v2.findViewById(R.id.txtAwayName);
                txtAwayName.setText(Common.getInstance().arrPopularGameListInfo.get(j).away);
                TextView txtStartTime = (TextView)v2.findViewById(R.id.txtStartTime);
                SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm");
                Date date;
                date = new Date();
                try {
                    date = sdformat.parse(Common.getInstance().arrPopularGameListInfo.get(j).time);
                } catch (ParseException ex) {

                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.HOUR, 1);

                String currentGameTime = sdformat.format(cal.getTime());
                txtStartTime.setText(currentGameTime);
                TextView txtHomeOdd = (TextView)v2.findViewById(R.id.txtHomeOdd);
                txtHomeOdd.setText(Common.getInstance().arrPopularGameListInfo.get(j).home_odd);
                TextView txtDrawOdd = (TextView)v2.findViewById(R.id.txtDrawOdd);
                txtDrawOdd.setText(Common.getInstance().arrPopularGameListInfo.get(j).draw_odd);
                TextView txtAwayOdd = (TextView)v2.findViewById(R.id.txtAwayOdd);
                txtAwayOdd.setText(Common.getInstance().arrPopularGameListInfo.get(j).away_odd);
                LinearLayout lnItem = (LinearLayout) v2.findViewById(R.id.lnItem);
                lnItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TipsActivity.this, GameDetailActivity.class);
                        intent.putExtra("event_id", Common.getInstance().arrPopularGameListInfo.get(index).event_id);
                        startActivity(intent);
                    }
                });
            }
        }
        for(int i = 0; i < Common.getInstance().arrLeagues.size(); i++){
            String[] arrLeagueName = Common.getInstance().arrLeagues.get(i).league_name.split(" ");
            String strLeagueName = "";
            for(int index = 0; index < arrLeagueName.length - 1; index++){
                String firstLetter1 = arrLeagueName[index].substring(0, 1);
                strLeagueName = strLeagueName + firstLetter1.toUpperCase() + arrLeagueName[index].substring(1) + " ";
            }
            String firstLetter1 = arrLeagueName[arrLeagueName.length - 1].substring(0, 1);
            strLeagueName = strLeagueName + firstLetter1.toUpperCase() + arrLeagueName[arrLeagueName.length - 1].substring(1);
            final String finalLeaguename = strLeagueName;
            final View v2 = LayoutInflater.from(TipsActivity.this).inflate(R.layout.country_tip_item, null);
            v2.setTag(Common.getInstance().arrLeagues.get(i));
            ImageView imgCountry = (ImageView)v2.findViewById(R.id.imgCountry);
            ImageLoader.getInstance().displayImage(Common.getInstance().arrLeagues.get(i).country_image, imgCountry, Common.getInstance().goodsDisplayImageOptions);
            TextView txtCountryLeauge = (TextView)v2.findViewById(R.id.txtCountryLeauge);
            txtCountryLeauge.setText(strLeagueName);
            LinearLayout lnItem = (LinearLayout)v2.findViewById(R.id.lnItem);
            lnItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TipsActivity.this, GameListActivity.class);
                    intent.putExtra("league_name", finalLeaguename);
                    startActivity(intent);
                }
            });
            lnContainer.addView(v2);
        }
    }

    private void loadData(){
        mProgDlg.show();
        new Thread(mLoadRunnable).start();
    }
    private Runnable mLoadRunnable = new Runnable() {
        @Override
        public void run() {
            Common.getInstance().arrMatches.clear();
            Common.getInstance().arrLeagues.clear();
            Common.getInstance().arrCountries.clear();
            Common.getInstance().arrPopularGameListInfo.clear();
            int ret = NetworkManager.getManager().loadPrediction("2018-04-21");
            mLoadHandler.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if(msg.what == 1)
                bindData();
            else if(msg.what == 0) {
                Toast.makeText(TipsActivity.this, "There is no data about the selected date", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(TipsActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };
}
