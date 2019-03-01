package com.diretta.livescore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.diretta.livescore.common.BaseActivity;
import com.diretta.livescore.common.Common;
import com.diretta.livescore.net.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by suc on 4/25/2018.
 */

public class UserProfileActivity extends Activity {
    RelativeLayout RnCurrentTip, RnEndTip;
    TextView txtCurrentTips, txtEndTips;
    View viewCurrentTipBorder, viewEndTipBorder;
    TextView txtNoTips;
    ImageView imgBack;
    ToggleButton toggleFollow;
    TextView txtClickHere;
    ProgressDialog mProgDlg;
    TextView txtProfileName;
    ImageView imgProfile;
    ScrollView scrollView;
    LinearLayout lnContainer, lnNoTips;
    int mCurrentTips = 0;
    TextView txtPoint, txtPercent, txtBio;
    ImageView imgExpertThisMonth;
    TextView txtFollowing, txtFollowers;
    LinearLayout lnFollowing, lnFollowers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_other);

        imgExpertThisMonth = (ImageView) findViewById(R.id.imgExpertThisMonth);
        imgExpertThisMonth.setVisibility(View.GONE);
        txtFollowers = (TextView) findViewById(R.id.txtFollowers);
        txtFollowing = (TextView) findViewById(R.id.txtFollowing);
        lnFollowers = (LinearLayout)findViewById(R.id.lnFollowers);
        lnFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, FollowManagementActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("status", "0");
                startActivity(intent);
            }
        });

        lnFollowing = (LinearLayout)findViewById(R.id.lnFollowing);
        lnFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, FollowManagementActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("status", "1");
                startActivity(intent);
            }
        });
        imgExpertThisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, ExpertThisMonth.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        mProgDlg = new ProgressDialog(UserProfileActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);
        txtPercent = (TextView) findViewById(R.id.txtPercent);
        txtPoint = (TextView) findViewById(R.id.txtPoint);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        lnContainer = (LinearLayout) findViewById(R.id.lnContainer);
        lnNoTips = (LinearLayout) findViewById(R.id.lnNoTips);

        txtProfileName = (TextView) findViewById(R.id.txtProfileName);
        txtProfileName.setText("");
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toggleFollow = (ToggleButton) findViewById(R.id.toggleFollow);
        String country_code = Locale.getDefault().getLanguage();
        if(country_code.equals("da")){
            toggleFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_toggle_follow_da));
        }else{
            toggleFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_toggle_follow_en));
        }
        toggleFollow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                if(mStatus.equals("1")) {
                    if (isChecked == false) {
                        mSelectedFollowId = getIntent().getStringExtra("user_id");
                        mSelectedStatus = "0";

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserProfileActivity.this);

                        alertDialog.setTitle("Confirm");
                        alertDialog.setMessage("Are you sure you want to unfollow?");
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                loadFollow();
                            }
                        });

                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                loadData();
                            }
                        });

                        alertDialog.show();
                    } else {
                        mSelectedFollowId = getIntent().getStringExtra("user_id");
                        mSelectedStatus = "1";
                        loadFollow();
                    }
                }
            }
        });
        RnCurrentTip = (RelativeLayout) findViewById(R.id.RnCurrentTip);
        RnCurrentTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTipPart(0);
                try {
                    JSONArray arrCurrentTips = Common.getInstance().otherProfileJson.getJSONArray("current_tips");
                    if (arrCurrentTips.length() > 0) {
                        scrollView.setVisibility(View.VISIBLE);
                        lnNoTips.setVisibility(View.GONE);
                        lnContainer.removeAllViews();
                        for (int i = 0; i < arrCurrentTips.length(); i++) {
                            JSONObject obj = arrCurrentTips.getJSONObject(i);
                            final View v1 = LayoutInflater.from(UserProfileActivity.this).inflate(R.layout.profile_tip_item, null);
                            v1.setTag(obj);
                            TextView txtLeagueName = (TextView) v1.findViewById(R.id.txtLeagueName);
                            txtLeagueName.setText(obj.getString("league_name"));
                            TextView txtHomeName = (TextView) v1.findViewById(R.id.txtHomeName);
                            txtHomeName.setText(obj.getString("home_name"));
                            TextView txtAwayName = (TextView) v1.findViewById(R.id.txtAwayName);
                            txtAwayName.setText(obj.getString("away_name"));
                            ImageView imgHome = (ImageView) v1.findViewById(R.id.imgHome);
                            ImageLoader.getInstance().displayImage(obj.getString("home_image"), imgHome, Common.getInstance().goodsDisplayImageOptions);
                            ImageView imgAway = (ImageView) v1.findViewById(R.id.imgAway);
                            ImageLoader.getInstance().displayImage(obj.getString("away_image"), imgAway, Common.getInstance().goodsDisplayImageOptions);
                            TextView txtOddStyle = (TextView) v1.findViewById(R.id.txtOddStyle);
                            txtOddStyle.setText(obj.getString("market_style") + " " + obj.getString("odd_style") + " @" + obj.getString("odd"));
                            TextView txtTipAmount = (TextView) v1.findViewById(R.id.txtTipAmount);
                            txtTipAmount.setText(obj.getString("tip_amount"));
                            LinearLayout lnViewBorder = (LinearLayout) v1.findViewById(R.id.lnViewBorder);
                            lnViewBorder.setBackground(getResources().getDrawable(R.drawable.button_empty_grey_back));
                            ImageView imgBlur = (ImageView) v1.findViewById(R.id.imgBlur);

                            if(Common.getInstance().is_expert.equals("1") || (Common.getInstance().free == false)) {
                                imgBlur.setVisibility(View.GONE);
                            }else{
                                if(Common.getInstance().otherProfileJson.getJSONObject("profile").getString("is_expert").equals("1")) {
                                    imgBlur.setVisibility(View.VISIBLE);
                                }else{
                                    imgBlur.setVisibility(View.GONE);
                                }
                            }
                            lnContainer.addView(v1);
                        }
                    } else {
                        lnNoTips.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {

                }
            }
        });

        RnEndTip = (RelativeLayout) findViewById(R.id.RnEndTip);
        RnEndTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTipPart(1);
                try {
                    JSONArray arrEndTips = Common.getInstance().otherProfileJson.getJSONArray("end_tips");
                    if (arrEndTips.length() > 0) {
                        scrollView.setVisibility(View.VISIBLE);
                        lnNoTips.setVisibility(View.GONE);
                        lnContainer.removeAllViews();
                        for (int i = 0; i < arrEndTips.length(); i++) {
                            JSONObject obj = arrEndTips.getJSONObject(i);
                            final View v1 = LayoutInflater.from(UserProfileActivity.this).inflate(R.layout.profile_tip_item, null);
                            v1.setTag(obj);
                            TextView txtLeagueName = (TextView) v1.findViewById(R.id.txtLeagueName);
                            txtLeagueName.setText(obj.getString("league_name"));
                            TextView txtHomeName = (TextView) v1.findViewById(R.id.txtHomeName);
                            txtHomeName.setText(obj.getString("home_name"));
                            TextView txtAwayName = (TextView) v1.findViewById(R.id.txtAwayName);
                            txtAwayName.setText(obj.getString("away_name"));
                            ImageView imgHome = (ImageView) v1.findViewById(R.id.imgHome);
                            ImageLoader.getInstance().displayImage(obj.getString("home_image"), imgHome, Common.getInstance().goodsDisplayImageOptions);
                            ImageView imgAway = (ImageView) v1.findViewById(R.id.imgAway);
                            ImageLoader.getInstance().displayImage(obj.getString("away_image"), imgAway, Common.getInstance().goodsDisplayImageOptions);
                            TextView txtOddStyle = (TextView) v1.findViewById(R.id.txtOddStyle);
                            txtOddStyle.setText(obj.getString("market_style") + " " + obj.getString("odd_style") + " @" + obj.getString("odd"));
                            TextView txtTipAmount = (TextView) v1.findViewById(R.id.txtTipAmount);
                            txtTipAmount.setText(obj.getString("tip_amount"));
                            LinearLayout lnViewBorder = (LinearLayout) v1.findViewById(R.id.lnViewBorder);
                            ImageView imgResultStatus = (ImageView) v1.findViewById(R.id.imgResultStatus);
                            if (obj.getString("result_status").equals("0")) {
                                lnViewBorder.setBackground(getResources().getDrawable(R.drawable.button_empty_red_back));
                                imgResultStatus.setBackground(getResources().getDrawable(R.drawable.ic_lost));
                            } else if (obj.getString("result_status").equals("1")) {
                                lnViewBorder.setBackground(getResources().getDrawable(R.drawable.button_empty_back));
                                imgResultStatus.setBackground(getResources().getDrawable(R.drawable.ic_won));
                            } else {
                                lnViewBorder.setBackground(getResources().getDrawable(R.drawable.button_empty_grey_back));
                            }
                            lnContainer.addView(v1);

                        }
                    } else {
                        lnNoTips.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {

                }
            }
        });

        txtCurrentTips = (TextView) findViewById(R.id.txtCurrentTips);
        txtEndTips = (TextView) findViewById(R.id.txtEndTips);
        viewCurrentTipBorder = (View) findViewById(R.id.viewCurrentTipBorder);
        viewEndTipBorder = (View) findViewById(R.id.viewEndTipBorder);
        txtNoTips = (TextView) findViewById(R.id.txtNoTips);
        txtBio = (TextView) findViewById(R.id.txtBio);
        mCurrentTips = 0;
        selectTipPart(mCurrentTips);
    }
    String mSelectedFollowId = "";
    String mSelectedStatus = "";
    private void loadFollow(){
        mProgDlg.show();
        new Thread(mLoadRunnable_follow).start();
    }
    private Runnable mLoadRunnable_follow = new Runnable() {
        @Override
        public void run() {
            int ret = NetworkManager.getManager().follow(Common.getInstance().user_id, mSelectedFollowId, mSelectedStatus);
            mLoadHandler_follow.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler_follow = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                if(mSelectedStatus.equals("0")){
                    Toast.makeText(UserProfileActivity.this, "UnFollowed Successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UserProfileActivity.this, "Followed Successfully", Toast.LENGTH_SHORT).show();
                }
                loadData();
            } else {
                Toast.makeText(UserProfileActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void loadData() {
        mProgDlg.show();
        new Thread(mLoadRunnable).start();
    }

    private Runnable mLoadRunnable = new Runnable() {
        @Override
        public void run() {
            int ret = NetworkManager.getManager().getOtherProfile(Common.getInstance().user_id, getIntent().getStringExtra("user_id"));
            mLoadHandler.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                bindData();
            } else if (msg.what == 0) {
                Toast.makeText(UserProfileActivity.this, "There is no data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserProfileActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };
    String mStatus = "0";
    private void bindData() {
        mStatus = "0";
        try {
            JSONObject userDic = Common.getInstance().otherProfileJson.getJSONObject("profile");
            if (userDic.getString("is_expert").equals("1")) {
                imgExpertThisMonth.setVisibility(View.VISIBLE);
            } else {
                imgExpertThisMonth.setVisibility(View.GONE);
            }
            txtBio.setText(userDic.getString("bio"));
            txtFollowing.setText(userDic.getString("following_count"));
            txtFollowers.setText(userDic.getString("followers_count"));
            txtPoint.setText(userDic.getString("point"));
            txtPercent.setText(userDic.getString("percent") + "%");
            txtProfileName.setText(userDic.getString("full_name"));
            String photo_url = "https://tipyaapp.com/" + userDic.getString("profile_img");
            ImageLoader.getInstance().displayImage(photo_url, imgProfile, Common.getInstance().goodsDisplayImageOptions);

            if (userDic.getString("follow_status").equals("0")) {
                toggleFollow.setChecked(false);
            } else {
                toggleFollow.setChecked(true);
            }

            JSONArray arrCurrentTips = Common.getInstance().otherProfileJson.getJSONArray("current_tips");
            if (arrCurrentTips.length() > 0) {
                scrollView.setVisibility(View.VISIBLE);
                lnNoTips.setVisibility(View.GONE);
                lnContainer.removeAllViews();
                for (int i = 0; i < arrCurrentTips.length(); i++) {
                    JSONObject obj = arrCurrentTips.getJSONObject(i);
                    final View v1 = LayoutInflater.from(UserProfileActivity.this).inflate(R.layout.profile_tip_item, null);
                    v1.setTag(obj);
                    TextView txtLeagueName = (TextView) v1.findViewById(R.id.txtLeagueName);
                    txtLeagueName.setText(obj.getString("league_name"));
                    TextView txtHomeName = (TextView) v1.findViewById(R.id.txtHomeName);
                    txtHomeName.setText(obj.getString("home_name"));
                    TextView txtAwayName = (TextView) v1.findViewById(R.id.txtAwayName);
                    txtAwayName.setText(obj.getString("away_name"));
                    ImageView imgHome = (ImageView) v1.findViewById(R.id.imgHome);
                    ImageLoader.getInstance().displayImage(obj.getString("home_image"), imgHome, Common.getInstance().goodsDisplayImageOptions);
                    ImageView imgAway = (ImageView) v1.findViewById(R.id.imgAway);
                    ImageLoader.getInstance().displayImage(obj.getString("away_image"), imgAway, Common.getInstance().goodsDisplayImageOptions);
                    TextView txtOddStyle = (TextView) v1.findViewById(R.id.txtOddStyle);
                    txtOddStyle.setText(obj.getString("market_style") + " " + obj.getString("odd_style") + " @" + obj.getString("odd"));
                    TextView txtTipAmount = (TextView) v1.findViewById(R.id.txtTipAmount);
                    txtTipAmount.setText(obj.getString("tip_amount"));
                    LinearLayout lnViewBorder = (LinearLayout) v1.findViewById(R.id.lnViewBorder);
                    lnViewBorder.setBackground(getResources().getDrawable(R.drawable.button_empty_grey_back));
                    ImageView imgBlur = (ImageView) v1.findViewById(R.id.imgBlur);

                    if(userDic.getString("expert_tips").equals("1")){
                        imgBlur.setVisibility(View.GONE);
                    }else{
                        if(Common.getInstance().is_expert.equals("1") || (Common.getInstance().free == false)) {
                            imgBlur.setVisibility(View.GONE);
                        }else{
                            if(Common.getInstance().otherProfileJson.getJSONObject("profile").getString("is_expert").equals("1")) {
                                imgBlur.setVisibility(View.VISIBLE);
                            }else{
                                imgBlur.setVisibility(View.GONE);
                            }
                        }
                    }

                    lnContainer.addView(v1);

                }
            } else {
                lnNoTips.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            }
        } catch (JSONException e) {

        }
        mStatus = "1";
    }

    private void selectTipPart(int index) {
        txtCurrentTips.setTextColor(getResources().getColor(R.color.clr_grey));
        txtEndTips.setTextColor(getResources().getColor(R.color.clr_grey));

        viewCurrentTipBorder.setBackgroundColor(getResources().getColor(R.color.clr_white));
        viewEndTipBorder.setBackgroundColor(getResources().getColor(R.color.clr_white));

        if (index == 0) {
            txtCurrentTips.setTextColor(getResources().getColor(R.color.clr_green));
            viewCurrentTipBorder.setBackgroundColor(getResources().getColor(R.color.clr_green));
            txtNoTips.setText("There is no any tips yet");
        } else {
            txtEndTips.setTextColor(getResources().getColor(R.color.clr_green));
            viewEndTipBorder.setBackgroundColor(getResources().getColor(R.color.clr_green));
            txtNoTips.setText("There is no ended tips yet");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}
