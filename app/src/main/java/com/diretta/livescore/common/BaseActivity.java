package com.diretta.livescore.common;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.diretta.livescore.FeedActivity;
import com.diretta.livescore.LivescoreActivity;
import com.diretta.livescore.ProfileActivity;
import com.diretta.livescore.R;
import com.diretta.livescore.RankActivity;
import com.diretta.livescore.TipsActivity;

/**
 * Created by suc on 4/25/2018.
 */

public class BaseActivity extends FragmentActivity implements View.OnClickListener {
    protected Context mContext;
    private RelativeLayout RnFeed, RnCalendar, RnTip, RnProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }
    public void initActionBar(int value) {

        findViewById(R.id.RnFeed).setOnClickListener(this);
        findViewById(R.id.RnCalendar).setOnClickListener(this);
        findViewById(R.id.RnTip).setOnClickListener(this);
        findViewById(R.id.RnProfile).setOnClickListener(this);

        RnFeed = (RelativeLayout) findViewById(R.id.RnFeed);
        RnCalendar = (RelativeLayout) findViewById(R.id.RnCalendar);
        RnTip = (RelativeLayout) findViewById(R.id.RnTip);
        RnProfile = (RelativeLayout) findViewById(R.id.RnProfile);

        switch (value) {
            case 0:
                RnFeed.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                RnCalendar.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                RnTip.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                RnProfile.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                Common.getInstance().status = 0;
                break;
            case 1:
                RnFeed.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_green_grey));
                RnCalendar.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                RnTip.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                RnProfile.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                Common.getInstance().status = 1;

                break;
            case 2:
                RnFeed.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                RnCalendar.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                RnTip.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_green_grey));
                RnProfile.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                Common.getInstance().status = 2;
                break;
            case 3:
                RnFeed.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                RnCalendar.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                RnTip.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                RnProfile.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                Common.getInstance().status = 3;
                break;
            case 4:
                RnFeed.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                RnCalendar.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                RnTip.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                RnProfile.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_green_grey));
                Common.getInstance().status = 4;
                break;
            case 5:
                RnFeed.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                RnCalendar.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_green_grey));
                RnTip.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                RnProfile.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                Common.getInstance().status = 5;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RnFeed:
                if (Common.getInstance().status != 1) {
                    RnFeed.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_green_grey));
                    RnCalendar.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                    RnTip.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                    RnProfile.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));Common.getInstance().status = 1;
                    Common.getInstance().status = 1;
                    Intent intent_home = new Intent(mContext, FeedActivity.class);
                    intent_home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent_home);
                }
                break;
            case R.id.RnTip:
                if (Common.getInstance().status != 2) {
                    RnFeed.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                    RnCalendar.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                    RnTip.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_green_grey));
                    RnProfile.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));Common.getInstance().status = 1;
                    Common.getInstance().status = 2;

                    Intent intent_classify = new Intent(mContext, TipsActivity.class);
                    intent_classify.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent_classify);
                }
                break;
            case R.id.RnProfile:
                if (Common.getInstance().status != 4) {
                    RnFeed.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                    RnCalendar.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                    RnTip.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                    RnProfile.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_green_grey));Common.getInstance().status = 1;
                    Common.getInstance().status = 4;

                    Intent intent_login = new Intent(mContext, ProfileActivity.class);
                    intent_login.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent_login);
                }
                break;
            case R.id.RnCalendar:
                if (Common.getInstance().status != 5) {
                    RnFeed.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                    RnCalendar.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_green_grey));
                    RnTip.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
                    RnProfile.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));Common.getInstance().status = 1;
                    Common.getInstance().status = 5;

                    Intent intent_login = new Intent(mContext, LivescoreActivity
                            .class);
                    intent_login.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent_login);
                }
                break;
        }
    }
}
