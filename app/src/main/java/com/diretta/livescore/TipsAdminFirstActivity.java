package com.diretta.livescore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

public class TipsAdminFirstActivity extends Activity {
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
        setContentView(R.layout.activity_tip_admin_first);
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
    }

    private void bindData(){
        lnContainer.removeAllViews();
        if(Common.getInstance().arrPopularGameListInfo.size() == 0){
        }else{
            for(int j = 0; j < Common.getInstance().arrPopularGameListInfo.size(); j++){
                final int index = j;

                final View v2 = LayoutInflater.from(TipsAdminFirstActivity.this).inflate(R.layout.popular_item, null);
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
                        Intent intent = new Intent(TipsAdminFirstActivity.this, TipsAdminThirdActivity.class);
                        intent.putExtra("event_id", Common.getInstance().arrPopularGameListInfo.get(index).event_id);
                        startActivity(intent);
                    }
                });
            }
        }


        for(int i = 0; i < Common.getInstance().arrLeagues.size(); i++){
            String[] arr = Common.getInstance().arrLeagues.get(i).league_name.split(" ");
            String country = arr[0];
            if(country.equals("republic") || country.equals("ireland"))
                country = "ireland";
            boolean bExist = false;
            for(int j = 0; j < Common.getInstance().arrCountries.size(); j++){
                if(Common.getInstance().arrCountries.get(j).country.equals(country)){

                    bExist = true;
                    break;
                }
            }
            if(bExist == false) {
                if(Common.getInstance().arrLeagues.get(i).league_name.toLowerCase().equals("copa do brasil"))
                    Common.getInstance().arrCountries.add(new CountryInfo("brasil", false));
                else if(country.contains("south")){
                    if(!Common.getInstance().arrLeagues.get(i).league_name.toLowerCase().equals("copa do brasil"))
                    {

                        if (Common.getInstance().arrLeagues.get(i).league_name.contains("south africa")) {
                            Common.getInstance().arrCountries.add(new CountryInfo("south africa", false));
                        } else if (Common.getInstance().arrLeagues.get(i).league_name.contains("south america")) {
                            Common.getInstance().arrCountries.add(new CountryInfo("south america", false));
                        } else if (Common.getInstance().arrLeagues.get(i).league_name.contains("south korea")) {
                            Common.getInstance().arrCountries.add(new CountryInfo("south korea", false));
                        }
                    }
                }else if(country.equals("republic") || country.equals("ireland")){
                    Common.getInstance().arrCountries.add(new CountryInfo("ireland", false));
                }else if(country.equals("el")){
                    Common.getInstance().arrCountries.add(new CountryInfo("el salvador", false));
                }
                else
                    Common.getInstance().arrCountries.add(new CountryInfo(country, false));
            }
        }

        //LeagueListAdapter adapter = new LeagueListAdapter(MainActivity.this, Common.getInstance().arrLeagues);
        //CountryListAdapter adapter = new CountryListAdapter(MainActivity.this, Common.getInstance().arrCountries);
        //listGame.setAdapter(adapter);
        lnContainer.removeAllViews();
        arrCountryViews.clear();
        arrLeagueViews.clear();

        for(int i = 0; i< Common.getInstance().arrCountries.size();i++){
            final CountryInfo countryInfo = Common.getInstance().arrCountries.get(i);
            View v1 = LayoutInflater.from(TipsAdminFirstActivity.this).inflate(R.layout.country_item, null);
            v1.setTag(countryInfo.country);
            arrCountryViews.add(v1);
            if(countryInfo.country.contains(" ")){
                String[] arr = countryInfo.country.split(" ");
                String strCountry = "";
                String country_image = "";
                for(int p = 0; p < arr.length - 1; p++){
                    strCountry = strCountry + arr[p].substring(0, 1).toUpperCase() + arr[p].substring(1) + " ";
                    country_image = country_image + arr[p];
                }
                strCountry = strCountry + arr[arr.length - 1].substring(0, 1).toUpperCase() + arr[arr.length - 1].substring(1);
                country_image = country_image + arr[arr.length - 1];
                ((TextView) v1.findViewById(R.id.txtCountry)).setText(strCountry);

            }else {
                String tempCountry = "";
                if(countryInfo.country.equals("brasil"))
                    tempCountry = "brazil";
                else
                    tempCountry = countryInfo.country;
                //countryInfo.country = tempCountry;
                String firstLetter = tempCountry.substring(0, 1);
                ((TextView) v1.findViewById(R.id.txtCountry)).setText(firstLetter.toUpperCase() + tempCountry.substring(1));
            }
            String newCountry = "";
            switch (countryInfo.country){
                case "usa":
                    ((TextView) v1.findViewById(R.id.txtCountry)).setText("USA");
                    break;
                case "uae":
                    ((TextView) v1.findViewById(R.id.txtCountry)).setText("UAE");
                    break;
                case "uefa":
                    ((TextView) v1.findViewById(R.id.txtCountry)).setText("UEFA");
                    break;
                case "copa":
                    ((TextView) v1.findViewById(R.id.txtCountry)).setText("South America");
                    break;
                case "northern":
                    ((TextView) v1.findViewById(R.id.txtCountry)).setText("Northern Ireland");
                    break;
                case "costa":
                    ((TextView) v1.findViewById(R.id.txtCountry)).setText("Costa Rica");
                    break;
            }
            final ImageView imgArrow = (ImageView)v1.findViewById(R.id.imgArrow);
            imgArrow.setBackgroundResource(R.drawable.ic_arrow_down);
            lnContainer.addView(v1);
            final LinearLayout lnContainer_v1 = (LinearLayout)v1.findViewById(R.id.lnContainer);
            lnContainer_v1.setVisibility(View.GONE);
            lnContainer_v1.removeAllViews();
            lnContainer_v1.setTag(countryInfo.country);

            ((LinearLayout)v1.findViewById(R.id.lnItem)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!mSelectedCountry.equals("")){
                        if(!mSelectedCountry.equals(countryInfo.country)){
                            bClick = false;
                        }
                    }
                    if(bClick == false) {
                        mSelectedCountry = countryInfo.country;
                        for(int k = 0; k < arrCountryViews.size(); k++){
                            if(!arrCountryViews.get(k).getTag().equals(countryInfo.country)){
                                View item = arrCountryViews.get(k);
                                ((LinearLayout)item.findViewById(R.id.lnItem)).setBackgroundColor(getResources().getColor(R.color.clr_white));
                                ((LinearLayout)item.findViewById(R.id.lnContainer)).setVisibility(View.GONE);
                                ((ImageView)item.findViewById(R.id.imgArrow)).setBackgroundResource(R.drawable.ic_arrow_down);
                            }
                        }
                        lnContainer_v1.setVisibility(View.VISIBLE);
                        if(scrollView.getScaleY() < (float) lnContainer_v1.getBottom())
                            scrollView.smoothScrollTo(scrollView.getScrollX(), lnContainer_v1.getBottom());
                        bClick = true;
                    }else {
                        mSelectedCountry = "";
                        lnContainer_v1.setVisibility(View.GONE);
                        v.setBackgroundColor(getResources().getColor(R.color.clr_white));
                        imgArrow.setBackgroundResource(R.drawable.ic_arrow_down);
                        bClick = false;
                    }
                }
            });

            for(int j = 0; j < Common.getInstance().arrLeagues.size(); j++) {
                final int position = j;
                if (Common.getInstance().arrLeagues.get(j).league_name.contains(countryInfo.country)) {
                    if(countryInfo.country.equals("copa")){
                        if(Common.getInstance().arrLeagues.get(j).league_name.toLowerCase().equals("copa do brasil")){
                            continue;
                        }
                    }
                    //leagues.add(Common.getInstance().arrLeagues.get(i));
                    final LeagueInfo leagueInfo = Common.getInstance().arrLeagues.get(j);
                    if(countryInfo.country.equals("ireland")){
                        if(leagueInfo.league_name.contains("northern ireland")){
                            continue;
                        }
                    }
                    final View v2 = LayoutInflater.from(TipsAdminFirstActivity.this).inflate(R.layout.country_item, null);
                    v2.setTag(leagueInfo.league_id);
                    arrLeagueViews.add(v2);

                    String[] arrLeagueName = leagueInfo.league_name.split(" ");
                    String strLeagueName = "";
                    for(int index = 0; index < arrLeagueName.length - 1; index++){
                        String firstLetter1 = arrLeagueName[index].substring(0, 1);
                        strLeagueName = strLeagueName + firstLetter1.toUpperCase() + arrLeagueName[index].substring(1) + " ";
                    }
                    String firstLetter1 = arrLeagueName[arrLeagueName.length - 1].substring(0, 1);
                    strLeagueName = strLeagueName + firstLetter1.toUpperCase() + arrLeagueName[arrLeagueName.length - 1].substring(1);
                    final String finalString = strLeagueName;
                    ((TextView) v2.findViewById(R.id.txtCountry)).setText(strLeagueName);
                    ((View)v2.findViewById(R.id.viewBorder)).setBackgroundColor(getResources().getColor(R.color.clr_white));
                    final ImageView imgArrowV2 = (ImageView)v2.findViewById(R.id.imgArrow);
                    final LinearLayout lnContainerV2 = (LinearLayout) v2.findViewById(R.id.lnContainer);
                    lnContainerV2.removeAllViews();
                    ((LinearLayout) v2.findViewById(R.id.lnItem)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String leagueName = Common.getInstance().arrLeagues.get(position).league_name;
                            Intent intent = new Intent(TipsAdminFirstActivity.this, TipsAdminSecondActivity.class);
                            intent.putExtra("league_name", finalString);
                            startActivity(intent);
                            TipsAdminFirstActivity.this.finish();
                        }
                    });

                    lnContainer_v1.addView(v2);
                    lnContainerV2.setVisibility(View.GONE);
                }
            }
        }
        View view = new View(TipsAdminFirstActivity.this);
        LinearLayout.LayoutParams params_pic = new LinearLayout.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, (int)getResources().getDimension(R.dimen.space_50));
        view.setLayoutParams(params_pic);
        lnContainer.addView(view);


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
                Toast.makeText(TipsAdminFirstActivity.this, "There is no data about the selected date", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(TipsAdminFirstActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };
}
