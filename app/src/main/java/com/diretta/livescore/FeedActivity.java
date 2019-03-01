package com.diretta.livescore;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.diretta.livescore.common.BaseActivity;
import com.diretta.livescore.common.Common;
import com.diretta.livescore.net.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by suc on 4/25/2018.
 */

public class FeedActivity extends BaseActivity {
    LinearLayout lnContainer;
    ProgressDialog mProgDlg;
    ScrollView scrollView;
    LinearLayout lnNoFollow;
    JSONObject mSelectedTip = new JSONObject();
    TextView txtNoFollow;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        MobileAds.initialize(this, "ca-app-pub-6225734666899710~8077467313");
        //You need to add this code for building the request for the ad
        AdView mAdView = (AdView)findViewById(R.id.adFeed);
        AdRequest adreqeust = new AdRequest.Builder().build();
        mAdView.loadAd(adreqeust);
        mProgDlg = new ProgressDialog(FeedActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);

        scrollView = (ScrollView)findViewById(R.id.scrollView);
        lnContainer = (LinearLayout)findViewById(R.id.lnContainer);
    }


    private void loadData(){
    }
    private Runnable mLoadRunnable = new Runnable() {
        @Override
        public void run() {
            int ret = NetworkManager.getManager().feed(Common.getInstance().user_id);
            mLoadHandler.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                lnContainer.removeAllViews();
                try{
                    JSONArray arrFeed = Common.getInstance().feedJson.getJSONArray("feed_array");
                    if(arrFeed.length() == 0){
                        scrollView.setVisibility(View.GONE);
                        lnNoFollow.setVisibility(View.VISIBLE);
                        txtNoFollow.setText(getResources().getString(R.string.you_not_follow));
                    }else{
                        scrollView.setVisibility(View.VISIBLE);
                        lnNoFollow.setVisibility(View.GONE);
                    }
                    for(int i = 0; i < arrFeed.length(); i++){
                        final JSONObject obj = arrFeed.getJSONObject(i);
                        final View v2 = LayoutInflater.from(FeedActivity.this).inflate(R.layout.feed_item, null);
                        RelativeLayout lnItem = (RelativeLayout)v2.findViewById(R.id.lnItem);

                        TextView txtLeagueName = (TextView)v2.findViewById(R.id.txtLeagueName);
                        txtLeagueName.setText(obj.getString("league_name"));
                        TextView txtHomeName = (TextView)v2.findViewById(R.id.txtHomeName);
                        txtHomeName.setText(obj.getString("home_name"));
                        TextView txtAwayName = (TextView)v2.findViewById(R.id.txtAwayName);
                        txtAwayName.setText(obj.getString("away_name"));
                        TextView txtTipAmount = (TextView)v2.findViewById(R.id.txtTipAmount);
                        txtTipAmount.setText(obj.getString("tip_amount"));
                        ImageView imgHome = (ImageView)v2.findViewById(R.id.imgHome);
                        ImageLoader.getInstance().displayImage(obj.getString("home_image"), imgHome, Common.getInstance().goodsDisplayImageOptions);
                        ImageView imgAway = (ImageView)v2.findViewById(R.id.imgAway);
                        ImageLoader.getInstance().displayImage(obj.getString("away_image"), imgAway, Common.getInstance().goodsDisplayImageOptions);
                        TextView txtOddStyle = (TextView)v2.findViewById(R.id.txtOddStyle);
                        txtOddStyle.setText(obj.getString("market_style") + " " + obj.getString("odd_style") + " @" + obj.getString("odd"));
                        ImageView imgProfile = (ImageView)v2.findViewById(R.id.imgProfile);
                        ImageLoader.getInstance().displayImage(obj.getString("profile_img"), imgProfile, Common.getInstance().goodsDisplayImageOptions);
                        imgProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent intent = new Intent(FeedActivity.this, UserProfileActivity.class);
                                    intent.putExtra("user_id", obj.getString("user_id"));
                                    startActivity(intent);
                                }catch (JSONException e){

                                }
                            }
                        });
                        ImageView imgBlur = (ImageView) v2.findViewById(R.id.imgBlur);

                        if(Common.getInstance().is_expert.equals("1") || (Common.getInstance().free == false)) {
                            imgBlur.setVisibility(View.GONE);
                        }else{
                            if(obj.getString("is_expert").equals("1")) {
                                imgBlur.setVisibility(View.VISIBLE);
                            }else{
                                imgBlur.setVisibility(View.GONE);
                            }
                        }

                        ImageView toggleStar = (ImageView)v2.findViewById(R.id.toggleStar);
                        String star = obj.getString("star");
                        if(star.equals("")){
                            //toggleStar.setChecked(false);
                        }else{
                            String[] arrStar = star.split(",");
                            int exist = 0;
                            for(int k = 0; k < arrStar.length; k++){
                                if(arrStar[k].equals(Common.getInstance().user_id)){
                                    //toggleStar.setChecked(true);
                                    exist = 1;
                                    break;
                                }
                            }

                            if(exist == 0) {
                                //toggleStar.setChecked(false);
                            }
                            toggleStar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Boolean status = v.isSelected();
                                    int a = 0;
                                }
                            });
                            /*
                            toggleStar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    try {
                                        Log.d("Star", "clicked");
                                        if (isChecked == false) {
                                            mSelectedStatus = "0";
                                            mSelectedTipId = obj.getString("tip_id");
                                            loadStar();
                                        } else {
                                            mSelectedStatus = "1";
                                            mSelectedTipId = obj.getString("tip_id");
                                            loadStar();
                                        }
                                    }catch (JSONException e){

                                    }
                                }
                            });
                            */
                        }
                        lnContainer.addView(v2);

                    }
                }catch (JSONException e){

                }

            } else if (msg.what == 0) {

                Toast.makeText(FeedActivity.this, getResources().getString(R.string.no_tip_today), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FeedActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };
    String mTipAmount = "";
    private TipResultDialog.OnCancelOrderListener mTipResultListener = new TipResultDialog.OnCancelOrderListener() {
        @Override
        public void OnCancel(String strReason) {
            mTipAmount = strReason;
            mProgDlg.show();
            new Thread(mLoadRunnable_place).start();

        }
    };
    private Runnable mLoadRunnable_place = new Runnable() {
        @Override
        public void run() {
            String game_style = "soccer";
            String free = "0";
            if(Common.getInstance().free == true){
                free = "1";
            }else{
                free = "0";
            }
            try {
                int ret = NetworkManager.getManager().placeTip(Common.getInstance().user_id, mSelectedTip.getString("event_id"), game_style, mSelectedTip.getString("market_style"), mSelectedTip.getString("odd_style"), mSelectedTip.getString("odd"), mTipAmount, mSelectedTip.getString("home_name"), mSelectedTip.getString("away_name"), mSelectedTip.getString("start_date_time"), mSelectedTip.getString("league_name"));
                mLoadHandler_Place.sendEmptyMessage(ret);
            }catch (JSONException e){

            }
        }
    };
    private Handler mLoadHandler_Place = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                Toast.makeText(FeedActivity.this, "Your tip was placed successfully!", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 0) {
                Toast.makeText(FeedActivity.this, "The same tip is already existed. Please try to place other tip", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FeedActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };
    String mSelectedTipId = "";
    String mSelectedStatus = "";
    private void loadStar(){
        mProgDlg.show();
        new Thread(mLoadRunnable_star).start();
    }
    private Runnable mLoadRunnable_star = new Runnable() {
        @Override
        public void run() {
            int ret = NetworkManager.getManager().star(Common.getInstance().user_id, mSelectedTipId, mSelectedStatus);
            mLoadHandler_follow.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler_follow = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                loadData();
            } else {
                Toast.makeText(FeedActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private void selectFollowingPart(int index){


        if(index == 0){
        }else{
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initActionBar(1);
        selectFollowingPart(0);
        loadData();
    }
}
