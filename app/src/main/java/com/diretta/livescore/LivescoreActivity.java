package com.diretta.livescore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.diretta.livescore.common.BaseActivity;
import com.diretta.livescore.common.Common;
import com.diretta.livescore.model.LiveGameListInfo;
import com.diretta.livescore.net.NetworkManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by suc on 7/7/2018.
 */

public class LivescoreActivity extends BaseActivity {
    String mDate0, mDate01, mDate02, mDate03, mDate001, mDate002, mDate003;
    TextView txtSeqDay, txtSeqDay001, txtSeqDay002, txtSeqDay003, txtSeqDay01, txtSeqDay02, txtSeqDay03;
    TextView toggleDay, toggleDay001, toggleDay002, toggleDay003, toggleDay01, toggleDay02, toggleDay03;
    String mSelectedDate = "";
    ProgressDialog mProgDlg;
    LinearLayout lnContainer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //int ret = NetworkManager.getManager().oldlogin("aurora92101@gmail.com", "aurora92101");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livescore);
        MobileAds.initialize(this, "ca-app-pub-6225734666899710~8077467313");
        //You need to add this code for building the request for the ad
        AdView mAdView = (AdView)findViewById(R.id.adLive);
        AdRequest adreqeust = new AdRequest.Builder().build();
        mAdView.loadAd(adreqeust);
        mProgDlg = new ProgressDialog(this);
        mProgDlg.setCancelable(false);
        mProgDlg.setMessage("Wait a little!");
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(LivescoreActivity.this));
        lnContainer = (LinearLayout)findViewById(R.id.lnContainer);
        lnContainer.removeAllViews();

        txtSeqDay = (TextView)findViewById(R.id.txtSeqDay);
        txtSeqDay001 = (TextView)findViewById(R.id.txtSeqDay001);
        txtSeqDay002 = (TextView)findViewById(R.id.txtSeqDay002);
        txtSeqDay003 = (TextView)findViewById(R.id.txtSeqDay003);
        txtSeqDay01 = (TextView)findViewById(R.id.txtSeqDay01);
        txtSeqDay02 = (TextView)findViewById(R.id.txtSeqDay02);
        txtSeqDay03 = (TextView)findViewById(R.id.txtSeqDay03);

        toggleDay = (TextView)findViewById(R.id.toggleDay);
        toggleDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mSelectedDate.equals(mDate0)){
                    mSelectedDate = mDate0;
                    toggleDay.setBackground(getResources().getDrawable(R.drawable.ic_green_circle));
                    toggleDay01.setBackground(null);
                    toggleDay02.setBackground(null);
                    toggleDay03.setBackground(null);
                    toggleDay001.setBackground(null);
                    toggleDay002.setBackground(null);
                    toggleDay003.setBackground(null);

                    toggleDay.setTextColor(getResources().getColor(R.color.clr_livescore_select_date));
                    toggleDay01.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay02.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay03.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay001.setTextColor(getResources().getColor(R.color.clr_livescore_date));
                    toggleDay002.setTextColor(getResources().getColor(R.color.clr_livescore_date));
                    toggleDay003.setTextColor(getResources().getColor(R.color.clr_livescore_date));

                    loadData();
                }
            }
        });
        toggleDay001 = (TextView)findViewById(R.id.toggleDay001);
        toggleDay001.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mSelectedDate.equals(mDate001)){
                    mSelectedDate = mDate001;
                    toggleDay001.setBackground(getResources().getDrawable(R.drawable.ic_green_circle));

                    toggleDay01.setBackground(null);
                    toggleDay02.setBackground(null);
                    toggleDay03.setBackground(null);
                    toggleDay.setBackground(null);
                    toggleDay002.setBackground(null);
                    toggleDay003.setBackground(null);

                    toggleDay001.setTextColor(getResources().getColor(R.color.clr_livescore_select_date));
                    toggleDay01.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay02.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay03.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay002.setTextColor(getResources().getColor(R.color.clr_livescore_date));
                    toggleDay003.setTextColor(getResources().getColor(R.color.clr_livescore_date));

                    loadData();
                }
            }
        });
        toggleDay002 = (TextView)findViewById(R.id.toggleDay002);
        toggleDay002.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mSelectedDate.equals(mDate002)){
                    mSelectedDate = mDate002;
                    toggleDay002.setBackground(getResources().getDrawable(R.drawable.ic_green_circle));

                    toggleDay01.setBackground(null);
                    toggleDay02.setBackground(null);
                    toggleDay03.setBackground(null);
                    toggleDay.setBackground(null);
                    toggleDay001.setBackground(null);
                    toggleDay003.setBackground(null);

                    toggleDay002.setTextColor(getResources().getColor(R.color.clr_livescore_select_date));
                    toggleDay01.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay02.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay03.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay001.setTextColor(getResources().getColor(R.color.clr_livescore_date));
                    toggleDay003.setTextColor(getResources().getColor(R.color.clr_livescore_date));

                    loadData();
                }
            }
        });
        toggleDay003 = (TextView)findViewById(R.id.toggleDay003);
        toggleDay003.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mSelectedDate.equals(mDate003)){
                    mSelectedDate = mDate003;
                    toggleDay003.setBackground(getResources().getDrawable(R.drawable.ic_green_circle));

                    toggleDay01.setBackground(null);
                    toggleDay02.setBackground(null);
                    toggleDay03.setBackground(null);
                    toggleDay.setBackground(null);
                    toggleDay002.setBackground(null);
                    toggleDay001.setBackground(null);

                    toggleDay003.setTextColor(getResources().getColor(R.color.clr_livescore_select_date));
                    toggleDay01.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay02.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay03.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay001.setTextColor(getResources().getColor(R.color.clr_livescore_date));
                    toggleDay002.setTextColor(getResources().getColor(R.color.clr_livescore_date));

                    loadData();
                }
            }
        });
        toggleDay01 = (TextView)findViewById(R.id.toggleDay01);
        toggleDay01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mSelectedDate.equals(mDate01)){
                    mSelectedDate = mDate01;
                    toggleDay01.setBackground(getResources().getDrawable(R.drawable.ic_green_circle));

                    toggleDay.setBackground(null);
                    toggleDay02.setBackground(null);
                    toggleDay03.setBackground(null);
                    toggleDay001.setBackground(null);
                    toggleDay002.setBackground(null);
                    toggleDay003.setBackground(null);

                    toggleDay01.setTextColor(getResources().getColor(R.color.clr_livescore_select_date));
                    toggleDay002.setTextColor(getResources().getColor(R.color.clr_livescore_date));
                    toggleDay02.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay03.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay001.setTextColor(getResources().getColor(R.color.clr_livescore_date));
                    toggleDay003.setTextColor(getResources().getColor(R.color.clr_livescore_date));

                    loadData();
                }
            }
        });
        toggleDay02 = (TextView)findViewById(R.id.toggleDay02);
        toggleDay02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mSelectedDate.equals(mDate02)){
                    mSelectedDate = mDate02;
                    toggleDay02.setBackground(getResources().getDrawable(R.drawable.ic_green_circle));

                    toggleDay.setBackground(null);
                    toggleDay01.setBackground(null);
                    toggleDay03.setBackground(null);
                    toggleDay001.setBackground(null);
                    toggleDay002.setBackground(null);
                    toggleDay003.setBackground(null);

                    toggleDay02.setTextColor(getResources().getColor(R.color.clr_livescore_select_date));
                    toggleDay002.setTextColor(getResources().getColor(R.color.clr_livescore_date));
                    toggleDay01.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay03.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay001.setTextColor(getResources().getColor(R.color.clr_livescore_date));
                    toggleDay003.setTextColor(getResources().getColor(R.color.clr_livescore_date));

                    loadData();
                }
            }
        });
        toggleDay03 = (TextView)findViewById(R.id.toggleDay03);
        toggleDay03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mSelectedDate.equals(mDate03)){
                    mSelectedDate = mDate03;
                    toggleDay03.setBackground(getResources().getDrawable(R.drawable.ic_green_circle));

                    toggleDay.setBackground(null);
                    toggleDay02.setBackground(null);
                    toggleDay01.setBackground(null);
                    toggleDay001.setBackground(null);
                    toggleDay002.setBackground(null);
                    toggleDay003.setBackground(null);

                    toggleDay03.setTextColor(getResources().getColor(R.color.clr_livescore_select_date));
                    toggleDay002.setTextColor(getResources().getColor(R.color.clr_livescore_date));
                    toggleDay02.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay01.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                    toggleDay001.setTextColor(getResources().getColor(R.color.clr_livescore_date));
                    toggleDay003.setTextColor(getResources().getColor(R.color.clr_livescore_date));

                    loadData();
                }
            }
        });
        mDate0 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Calendar cal1 = new GregorianCalendar();
        cal1.add(Calendar.DATE, 1);
        mDate01 = "" + cal1.get(Calendar.YEAR) + "-" + formatDate(cal1.get(Calendar.MONTH) + 1) + "-" + formatDate(cal1.get(Calendar.DAY_OF_MONTH));
        Calendar cal2 = new GregorianCalendar();
        cal2.add(Calendar.DATE, 2);
        mDate02 = "" + cal2.get(Calendar.YEAR) + "-" + formatDate(cal2.get(Calendar.MONTH) + 1) + "-" + formatDate(cal2.get(Calendar.DAY_OF_MONTH));
        Calendar cal3 = new GregorianCalendar();
        cal3.add(Calendar.DATE, 3);
        mDate03 = "" + cal3.get(Calendar.YEAR) + "-" + formatDate(cal3.get(Calendar.MONTH) + 1) + "-" + formatDate(cal3.get(Calendar.DAY_OF_MONTH));
        Calendar cal01 = new GregorianCalendar();
        cal01.add(Calendar.DATE, -1);
        mDate001 = "" + cal01.get(Calendar.YEAR) + "-" + formatDate(cal01.get(Calendar.MONTH) + 1) + "-" + formatDate(cal01.get(Calendar.DAY_OF_MONTH));
        Calendar cal02 = new GregorianCalendar();
        cal02.add(Calendar.DATE, -2);
        mDate002 = "" + cal02.get(Calendar.YEAR) + "-" + formatDate(cal02.get(Calendar.MONTH) + 1) + "-" + formatDate(cal02.get(Calendar.DAY_OF_MONTH));
        Calendar cal03 = new GregorianCalendar();
        cal03.add(Calendar.DATE, -3);
        mDate003 = "" + cal03.get(Calendar.YEAR) + "-" + formatDate(cal03.get(Calendar.MONTH) + 1) + "-" + formatDate(cal03.get(Calendar.DAY_OF_MONTH));

        try{
            txtSeqDay.setText(getDateSeqDay(mDate0));
            txtSeqDay01.setText(getDateSeqDay(mDate01));
            txtSeqDay02.setText(getDateSeqDay(mDate02));
            txtSeqDay03.setText(getDateSeqDay(mDate03));
            txtSeqDay001.setText(getDateSeqDay(mDate001));
            txtSeqDay002.setText(getDateSeqDay(mDate002));
            txtSeqDay003.setText(getDateSeqDay(mDate003));
        }catch (Exception e){

        }


        toggleDay.setTextColor(getResources().getColor(R.color.clr_livescore_select_date));
        toggleDay01.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
        toggleDay02.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
        toggleDay03.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
        toggleDay001.setTextColor(getResources().getColor(R.color.clr_livescore_date));
        toggleDay002.setTextColor(getResources().getColor(R.color.clr_livescore_date));
        toggleDay003.setTextColor(getResources().getColor(R.color.clr_livescore_date));

        mSelectedDate = mDate0;

        toggleDay.setText(getDate(mDate0));
        toggleDay01.setText(getDate(mDate01));
        toggleDay02.setText(getDate(mDate02));
        toggleDay03.setText(getDate(mDate03));
        toggleDay001.setText(getDate(mDate001));
        toggleDay002.setText(getDate(mDate002));
        toggleDay003.setText(getDate(mDate003));

        toggleDay.setBackground(getResources().getDrawable(R.drawable.ic_green_circle));
        toggleDay01.setBackground(null);
        toggleDay02.setBackground(null);
        toggleDay03.setBackground(null);
        toggleDay001.setBackground(null);
        toggleDay002.setBackground(null);
        toggleDay003.setBackground(null);
        loadData();
    }
    private void loadData(){
        mProgDlg.show();
        new Thread(mLoadRunnable).start();
    }
    private Runnable mLoadRunnable = new Runnable() {
        @Override
        public void run() {
            Common.getInstance().arrLiveLeagues.clear();
            Common.getInstance().arrLiveGameListInfo.clear();
            int ret = NetworkManager.getManager().loadLivescore(mSelectedDate);
            mLoadHandler.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if(msg.what == 1) {
                bindData();
            }else if(msg.what == 0) {
                Toast.makeText(LivescoreActivity.this, "There is no data about the selected date", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(LivescoreActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };
    private void bindData(){
        lnContainer.removeAllViews();

        for (int i = 0; i < Common.getInstance().arrLiveLeagues.size(); i++){
            int sequence = 0;
            for (int j = 0; j < Common.getInstance().arrLiveGameListInfo.size(); j++){
                if(Common.getInstance().arrLiveGameListInfo.get(j).league_id.equals(Common.getInstance().arrLiveLeagues.get(i).league_id)){
                    if(sequence == 0){
                        final int k = j;
                        TextView txtLeague = new TextView(LivescoreActivity.this);
                        String[] arrLeagueName = Common.getInstance().arrLiveLeagues.get(i).league_name.split(" ");
                        String strLeagueName = "";
                        for(int index = 0; index < arrLeagueName.length - 1; index++){
                            String firstLetter1 = arrLeagueName[index].substring(0, 1);
                            strLeagueName = strLeagueName + firstLetter1.toUpperCase() + arrLeagueName[index].substring(1) + " ";
                        }
                        String firstLetter1 = arrLeagueName[arrLeagueName.length - 1].substring(0, 1);
                        strLeagueName = strLeagueName + firstLetter1.toUpperCase() + arrLeagueName[arrLeagueName.length - 1].substring(1);

                        txtLeague.setText(strLeagueName);
                        final String newLeagueName = strLeagueName;
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.leftMargin = (int)getResources().getDimension(R.dimen.space_10);
                        layoutParams.topMargin = (int)getResources().getDimension(R.dimen.space_15);
                        txtLeague.setLayoutParams(layoutParams);
                        txtLeague.setTextColor(getResources().getColor(R.color.clr_livescore_seq));
                        lnContainer.addView(txtLeague);

                        final View v2 = LayoutInflater.from(LivescoreActivity.this).inflate(R.layout.livescore_item, null);
                        RelativeLayout lnItem = (RelativeLayout)v2.findViewById(R.id.lnItem);
                        lnItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String mStartDateTime = "";
                                SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm");
                                Date date;
                                date = new Date();
                                try {
                                    date = sdformat.parse(Common.getInstance().arrLiveGameListInfo.get(k).time);
                                } catch (ParseException ex) {

                                }
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);
                                cal.add(Calendar.HOUR, 1);

                                String currentGameTime = sdformat.format(cal.getTime());

                                if(mSelectedDate.equals(mDate0)){
                                    mStartDateTime = getResources().getString(R.string.today) + " " + currentGameTime;
                                }else if(mSelectedDate.equals(mDate01)){
                                    mStartDateTime = getResources().getString(R.string.tomorrow) + " "  + currentGameTime;
                                }else if(mSelectedDate.equals(mDate001)){
                                    mStartDateTime = getResources().getString(R.string.yesterday) + " "  + currentGameTime;
                                }else{
                                    mStartDateTime = mSelectedDate + " " + currentGameTime;
                                }

                                Intent intent = new Intent(LivescoreActivity.this, LivescoreDetailActivity.class);
                                LiveGameListInfo info = new LiveGameListInfo();
                                info = Common.getInstance().arrLiveGameListInfo.get(k);

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("detail", info);
                                intent.putExtras(bundle);
                                intent.putExtra("league_name", newLeagueName);
                                intent.putExtra("start_time", mStartDateTime);
                                startActivity(intent);
                            }
                        });
                        TextView txtHomeName = (TextView)v2.findViewById(R.id.txtHomeName);
                        txtHomeName.setText(Common.getInstance().arrLiveGameListInfo.get(j).home);
                        TextView txtAwayName = (TextView)v2.findViewById(R.id.txtAwayName);
                        txtAwayName.setText(Common.getInstance().arrLiveGameListInfo.get(j).away);
                        ImageView imgHome = (ImageView)v2.findViewById(R.id.imgHome);
                        ImageLoader.getInstance().displayImage(Common.getInstance().arrLiveGameListInfo.get(j).home_image, imgHome, Common.getInstance().goodsDisplayImageOptions);
                        ImageView imgAway = (ImageView)v2.findViewById(R.id.imgAway);
                        ImageLoader.getInstance().displayImage(Common.getInstance().arrLiveGameListInfo.get(j).away_image, imgAway, Common.getInstance().goodsDisplayImageOptions);
                        TextView txtStatus = (TextView)v2.findViewById(R.id.txtStatus);

                        if(Common.getInstance().arrLiveGameListInfo.get(j).status.contains(":")){
                            txtStatus.setText("-");
                        }else if(Common.getInstance().arrLiveGameListInfo.get(j).status.equals("FT")){
                            txtStatus.setText(Common.getInstance().arrLiveGameListInfo.get(j).score);
                        }else{
                            txtStatus.setText(Common.getInstance().arrLiveGameListInfo.get(j).status + "'");
                        }

                        final TextView txtStartTime = (TextView)v2.findViewById(R.id.txtStartTime);

                        SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm");
                        Date date;
                        date = new Date();
                        try {
                            date = sdformat.parse(Common.getInstance().arrLiveGameListInfo.get(j).time);
                        } catch (ParseException ex) {

                        }
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        cal.add(Calendar.HOUR, 1);

                        String currentGameTime = sdformat.format(cal.getTime());


                        if(mSelectedDate.equals(mDate0)){
                            txtStartTime.setText(getResources().getString(R.string.today) + " " + currentGameTime);
                        }else if(mSelectedDate.equals(mDate01)){
                            txtStartTime.setText(getResources().getString(R.string.tomorrow) + " "  + currentGameTime);
                        }else if(mSelectedDate.equals(mDate001)){
                            txtStartTime.setText(getResources().getString(R.string.yesterday) + " "  + currentGameTime);
                        }else{
                            txtStartTime.setText(mSelectedDate + " " + currentGameTime);
                        }

                        TextView txtStadium = (TextView)v2.findViewById(R.id.txtStadium);
                        if(Common.getInstance().arrLiveGameListInfo.get(j).stadium.equals("")){
                            txtStadium.setText(arrLeagueName[0]);
                        }else{
                            txtStadium.setText(Common.getInstance().arrLiveGameListInfo.get(j).stadium);
                        }
                        lnContainer.addView(v2);
                        sequence++;
                    }else{
                        final int k = j;
                        String[] arrLeagueName = Common.getInstance().arrLiveLeagues.get(i).league_name.split(" ");
                        String strLeagueName = "";
                        for(int index = 0; index < arrLeagueName.length - 1; index++){
                            String firstLetter1 = arrLeagueName[index].substring(0, 1);
                            strLeagueName = strLeagueName + firstLetter1.toUpperCase() + arrLeagueName[index].substring(1) + " ";
                        }
                        String firstLetter1 = arrLeagueName[arrLeagueName.length - 1].substring(0, 1);
                        strLeagueName = strLeagueName + firstLetter1.toUpperCase() + arrLeagueName[arrLeagueName.length - 1].substring(1);
                        final String newLeagueName = strLeagueName;

                        final View v2 = LayoutInflater.from(LivescoreActivity.this).inflate(R.layout.livescore_item, null);
                        RelativeLayout lnItem = (RelativeLayout)v2.findViewById(R.id.lnItem);
                        lnItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String mStartDateTime = "";
                                SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm");
                                Date date;
                                date = new Date();
                                try {
                                    date = sdformat.parse(Common.getInstance().arrLiveGameListInfo.get(k).time);
                                } catch (ParseException ex) {

                                }
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);
                                cal.add(Calendar.HOUR, 1);

                                String currentGameTime = sdformat.format(cal.getTime());

                                if(mSelectedDate.equals(mDate0)){
                                    mStartDateTime = getResources().getString(R.string.today) + " " + currentGameTime;
                                }else if(mSelectedDate.equals(mDate01)){
                                    mStartDateTime = getResources().getString(R.string.tomorrow) + " "  + currentGameTime;
                                }else if(mSelectedDate.equals(mDate001)){
                                    mStartDateTime = getResources().getString(R.string.yesterday) + " "  + currentGameTime;
                                }else{
                                    mStartDateTime = mSelectedDate + " " + currentGameTime;
                                }

                                Intent intent = new Intent(LivescoreActivity.this, LivescoreDetailActivity.class);
                                LiveGameListInfo info = new LiveGameListInfo();
                                info = Common.getInstance().arrLiveGameListInfo.get(k);

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("detail", info);
                                intent.putExtras(bundle);
                                intent.putExtra("league_name", newLeagueName);
                                intent.putExtra("start_time", mStartDateTime);
                                startActivity(intent);
                            }
                        });

                        TextView txtHomeName = (TextView)v2.findViewById(R.id.txtHomeName);
                        txtHomeName.setText(Common.getInstance().arrLiveGameListInfo.get(j).home);
                        TextView txtAwayName = (TextView)v2.findViewById(R.id.txtAwayName);
                        txtAwayName.setText(Common.getInstance().arrLiveGameListInfo.get(j).away);

                        ImageView imgHome = (ImageView)v2.findViewById(R.id.imgHome);
                        ImageLoader.getInstance().displayImage(Common.getInstance().arrLiveGameListInfo.get(j).home_image, imgHome, Common.getInstance().goodsDisplayImageOptions);
                        ImageView imgAway = (ImageView)v2.findViewById(R.id.imgAway);
                        ImageLoader.getInstance().displayImage(Common.getInstance().arrLiveGameListInfo.get(j).away_image, imgAway, Common.getInstance().goodsDisplayImageOptions);

                        TextView txtStatus = (TextView)v2.findViewById(R.id.txtStatus);
                        if(Common.getInstance().arrLiveGameListInfo.get(j).status.contains(":")){
                            txtStatus.setText("-");
                        }else if(Common.getInstance().arrLiveGameListInfo.get(j).status.equals("FT")){
                            txtStatus.setText(Common.getInstance().arrLiveGameListInfo.get(j).score);
                        }else{
                            txtStatus.setText(Common.getInstance().arrLiveGameListInfo.get(j).status + "'");
                        }

                        TextView txtStartTime = (TextView)v2.findViewById(R.id.txtStartTime);
                        SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm");
                        Date date;
                        date = new Date();
                        try {
                            date = sdformat.parse(Common.getInstance().arrLiveGameListInfo.get(j).time);
                        } catch (ParseException ex) {

                        }
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        cal.add(Calendar.HOUR, 1);

                        String currentGameTime = sdformat.format(cal.getTime());

                        if(mSelectedDate.equals(mDate0)){
                            txtStartTime.setText(getResources().getString(R.string.today) + " " + currentGameTime);
                        }else if(mSelectedDate.equals(mDate01)){
                            txtStartTime.setText(getResources().getString(R.string.tomorrow) + " "  + currentGameTime);
                        }else if(mSelectedDate.equals(mDate001)){
                            txtStartTime.setText(getResources().getString(R.string.yesterday) + " "  + currentGameTime);
                        }else{
                            txtStartTime.setText(mSelectedDate + " " + currentGameTime);
                        }

                        TextView txtStadium = (TextView)v2.findViewById(R.id.txtStadium);
                        if(Common.getInstance().arrLiveGameListInfo.get(j).stadium.equals("")){
                            txtStadium.setText(arrLeagueName[0]);
                        }else{
                            txtStadium.setText(Common.getInstance().arrLiveGameListInfo.get(j).stadium);
                        }
                        lnContainer.addView(v2);
                    }
                }
            }
        }
    }
    public String getDate(String date){
        String[] arrDate = date.split("-");
        return arrDate[2];
    }
    public String getDateSeqDay(String date) throws Exception {
        String[] arrDate = date.split("-");
        String day = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date nDate = dateFormat.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                day = getResources().getString(R.string.sun);
                break;
            case 2:
                day = getResources().getString(R.string.mon);
                break;
            case 3:
                day = getResources().getString(R.string.tue);
                break;
            case 4:
                day = getResources().getString(R.string.wed);
                break;
            case 5:
                day = getResources().getString(R.string.thi);
                break;
            case 6:
                day = getResources().getString(R.string.fri);
                break;
            case 7:
                day = getResources().getString(R.string.sat);
                break;

        }
        return day;
    }
    private String formatDate(int value){
        if(value < 10)
            return "0" + value;
        else
            return "" + value;
    }
    @Override
    protected void onResume() {
        super.onResume();
        initActionBar(5);
    }
}
