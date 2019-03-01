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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.diretta.livescore.common.Common;
import com.diretta.livescore.net.NetworkManager;

import org.json.JSONException;

/**
 * Created by suc on 4/26/2018.
 */

public class TipOfDayActivity extends Activity {
    ImageView imgBack;
    ImageView imgExpertThisMonth;
    TextView txtProfileName, txtFollowers, txtFollowing;
    ImageView imgProfile;
    TextView txtHomeName, txtAwayName, txtLeagueName, txtOddStyle, txtTipAmount;
    ImageView imgHome, imgAway;
    ImageView imgBlur;
    TextView txtDescription;
    ProgressDialog mProgDlg;
    LinearLayout lnFollowers, lnFollowing;
    LinearLayout lnTipPart;
    TextView txtNoTips;
    LinearLayout lnViewTip;
    TextView txtTipTopic;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipofday);
        mProgDlg = new ProgressDialog(TipOfDayActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);
        txtTipTopic = (TextView)findViewById(R.id.txtTipTopic);
        txtNoTips = (TextView)findViewById(R.id.txtNoTips);
        txtNoTips.setVisibility(View.GONE);
        lnViewTip = (LinearLayout)findViewById(R.id.lnViewTip);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgExpertThisMonth = (ImageView)findViewById(R.id.imgExpertThisMonth);
        imgExpertThisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TipOfDayActivity.this, ExpertThisMonth.class));
            }
        });
        lnFollowers = (LinearLayout)findViewById(R.id.lnFollowers);
        lnFollowing = (LinearLayout)findViewById(R.id.lnFollowing);
        lnTipPart = (LinearLayout)findViewById(R.id.lnTipPart);

        txtProfileName = (TextView)findViewById(R.id.txtProfileName);
        txtFollowers = (TextView)findViewById(R.id.txtFollowers);
        txtFollowing = (TextView)findViewById(R.id.txtFollowing);
        imgProfile = (ImageView)findViewById(R.id.imgProfile);

        txtHomeName = (TextView)findViewById(R.id.txtHomeName);
        txtAwayName = (TextView)findViewById(R.id.txtAwayName);
        txtLeagueName = (TextView)findViewById(R.id.txtLeagueName);
        txtOddStyle = (TextView)findViewById(R.id.txtOddStyle);
        txtTipAmount = (TextView)findViewById(R.id.txtTipAmount);
        imgHome = (ImageView)findViewById(R.id.imgHome);
        imgAway = (ImageView)findViewById(R.id.imgAway);

        imgBlur = (ImageView) findViewById(R.id.imgBlur);
        txtDescription = (TextView)findViewById(R.id.txtDescription);

    }
    private Runnable mLoadRunnable = new Runnable() {
        @Override
        public void run() {
            String game_style = "soccer";
            int ret = NetworkManager.getManager().tipOfDay(Common.getInstance().user_id);
            mLoadHandler.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                try{
                    txtDescription.setVisibility(View.VISIBLE);
                    imgBlur.setVisibility(View.GONE);

                    if(Common.getInstance().tipOfDayJson.getString("expert_tip_of_day").equals("1")){
                        txtProfileName.setText(Common.getInstance().tipOfDayJson.getString("name"));
                        ImageLoader.getInstance().displayImage(Common.getInstance().tipOfDayJson.getString("profile_img"), imgProfile, Common.getInstance().goodsDisplayImageOptions);
                        imgProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    if (Common.getInstance().user_id.equals(Common.getInstance().tipOfDayJson.getString("user_id"))) {
                                        Common.getInstance().status = 4;

                                        Intent intent_login = new Intent(TipOfDayActivity.this, ProfileActivity.class);
                                        intent_login.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent_login);
                                    } else {
                                        Intent intent_login = new Intent(TipOfDayActivity.this, UserProfileActivity.class);
                                        intent_login.putExtra("user_id", Common.getInstance().tipOfDayJson.getString("user_id"));
                                        startActivity(intent_login);
                                    }
                                }catch (JSONException e){

                                }
                            }
                        });
                        txtLeagueName.setText(Common.getInstance().tipOfDayJson.getString("league_name"));
                        txtHomeName.setText(Common.getInstance().tipOfDayJson.getString("home_name"));
                        txtAwayName.setText(Common.getInstance().tipOfDayJson.getString("away_name"));
                        txtTipAmount.setText(Common.getInstance().tipOfDayJson.getString("tip_amount"));
                        ImageLoader.getInstance().displayImage(Common.getInstance().tipOfDayJson.getString("home_image"), imgHome, Common.getInstance().goodsDisplayImageOptions);
                        ImageLoader.getInstance().displayImage(Common.getInstance().tipOfDayJson.getString("away_image"), imgAway, Common.getInstance().goodsDisplayImageOptions);
                        txtOddStyle.setText(Common.getInstance().tipOfDayJson.getString("market_style") + " " + Common.getInstance().tipOfDayJson.getString("odd_style") + " @" + Common.getInstance().tipOfDayJson.getString("odd"));
                        txtDescription.setText(Common.getInstance().tipOfDayJson.getString("description"));
                        txtDescription.setVisibility(View.VISIBLE);
                        imgBlur.setVisibility(View.GONE);
                        txtFollowers.setText(Common.getInstance().tipOfDayJson.getString("followers"));
                        txtFollowing.setText(Common.getInstance().tipOfDayJson.getString("followings"));
                    }else{
                        if(Common.getInstance().is_expert.equals("1") || (Common.getInstance().free == false)){
                            txtProfileName.setText(Common.getInstance().tipOfDayJson.getString("name"));
                            ImageLoader.getInstance().displayImage(Common.getInstance().tipOfDayJson.getString("profile_img"), imgProfile, Common.getInstance().goodsDisplayImageOptions);
                            imgProfile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        if (Common.getInstance().user_id.equals(Common.getInstance().tipOfDayJson.getString("user_id"))) {
                                            Common.getInstance().status = 4;

                                            Intent intent_login = new Intent(TipOfDayActivity.this, ProfileActivity.class);
                                            intent_login.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent_login);
                                        } else {
                                            Intent intent_login = new Intent(TipOfDayActivity.this, UserProfileActivity.class);
                                            intent_login.putExtra("user_id", Common.getInstance().tipOfDayJson.getString("user_id"));
                                            startActivity(intent_login);
                                        }
                                    }catch (JSONException e){

                                    }
                                }
                            });
                            txtLeagueName.setText(Common.getInstance().tipOfDayJson.getString("league_name"));
                            txtHomeName.setText(Common.getInstance().tipOfDayJson.getString("home_name"));
                            txtAwayName.setText(Common.getInstance().tipOfDayJson.getString("away_name"));
                            txtTipAmount.setText(Common.getInstance().tipOfDayJson.getString("tip_amount"));
                            ImageLoader.getInstance().displayImage(Common.getInstance().tipOfDayJson.getString("home_image"), imgHome, Common.getInstance().goodsDisplayImageOptions);
                            ImageLoader.getInstance().displayImage(Common.getInstance().tipOfDayJson.getString("away_image"), imgAway, Common.getInstance().goodsDisplayImageOptions);
                            txtOddStyle.setText(Common.getInstance().tipOfDayJson.getString("market_style") + " " + Common.getInstance().tipOfDayJson.getString("odd_style") + " @" + Common.getInstance().tipOfDayJson.getString("odd"));
                            txtDescription.setText(Common.getInstance().tipOfDayJson.getString("description"));
                            txtDescription.setVisibility(View.VISIBLE);
                            imgBlur.setVisibility(View.GONE);
                            txtFollowers.setText(Common.getInstance().tipOfDayJson.getString("followers"));
                            txtFollowing.setText(Common.getInstance().tipOfDayJson.getString("followings"));
                        }else{
                            txtDescription.setVisibility(View.GONE);
                            imgBlur.setVisibility(View.VISIBLE);
                            imgExpertThisMonth.setVisibility(View.GONE);
                            lnFollowing.removeAllViews();
                            lnFollowers.removeAllViews();
                            lnTipPart.setVisibility(View.GONE);
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(TipOfDayActivity.this);

                            alertDialog.setTitle("Confirm");
                            alertDialog.setMessage("You need Premium user to view tip");
                            alertDialog.setPositiveButton("Premium", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog,int which) {
                                    Intent intent_home = new Intent(TipOfDayActivity.this, PremiumSubscriptionActivity.class);
                                    intent_home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent_home);
                                }
                            });

                            alertDialog.setNegativeButton("Later", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which){
                                    dialog.cancel();
                                    onBackPressed();
                                }
                            });

                            alertDialog.show();
                        }
                    }

                }catch (JSONException e){

                }

            } else if (msg.what == 0) {
                imgBlur.setVisibility(View.GONE);
                txtDescription.setVisibility(View.GONE);
                txtTipTopic.setText(getResources().getString(R.string.no_tip));
                lnTipPart.setVisibility(View.VISIBLE);
                txtNoTips.setVisibility(View.VISIBLE);
                lnViewTip.setVisibility(View.GONE);
                imgExpertThisMonth.setVisibility(View.GONE);
                lnFollowing.removeAllViews();
                lnFollowers.removeAllViews();
            } else {
                imgBlur.setVisibility(View.VISIBLE);
                txtDescription.setVisibility(View.GONE);
                txtTipTopic.setVisibility(View.GONE);
                lnTipPart.setVisibility(View.GONE);
                Toast.makeText(TipOfDayActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        txtDescription.setVisibility(View.GONE);
        imgBlur.setVisibility(View.VISIBLE);
        mProgDlg.show();
        new Thread(mLoadRunnable).start();
    }
}
