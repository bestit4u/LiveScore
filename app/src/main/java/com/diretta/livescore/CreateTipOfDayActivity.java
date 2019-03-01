package com.diretta.livescore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.diretta.livescore.common.Common;
import com.diretta.livescore.net.NetworkManager;

/**
 * Created by suc on 6/3/2018.
 */

public class CreateTipOfDayActivity extends Activity {
    TextView txtProfileName;
    ImageView imgBack, imgProfile, imgExpertThisMonth;
    LinearLayout lnCreateTip;
    Button btnPublish;
    EditText edtDescription;
    LinearLayout lnTipPart, lnChooseTipPart;
    TextView txtHomeName, txtAwayName, txtLeagueName, txtOddStyle, txtTipAmount;
    ImageView imgHome, imgAway;
    ProgressDialog mProgDlg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_of_day);
        mProgDlg = new ProgressDialog(CreateTipOfDayActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtHomeName = (TextView)findViewById(R.id.txtHomeName);
        txtAwayName = (TextView)findViewById(R.id.txtAwayName);
        txtLeagueName = (TextView)findViewById(R.id.txtLeagueName);
        txtOddStyle = (TextView)findViewById(R.id.txtOddStyle);
        txtTipAmount = (TextView)findViewById(R.id.txtTipAmount);
        imgHome = (ImageView)findViewById(R.id.imgHome);
        imgAway = (ImageView)findViewById(R.id.imgAway);

        txtProfileName = (TextView)findViewById(R.id.txtProfileName);
        txtProfileName.setText(Common.getInstance().userInfo.full_name);
        imgProfile = (ImageView)findViewById(R.id.imgProfile);
        String photo_url = "https://tipyaapp.com/" + Common.getInstance().userInfo.profile_img;
        ImageLoader.getInstance().displayImage(photo_url, imgProfile, Common.getInstance().goodsDisplayImageOptions);
        imgExpertThisMonth = (ImageView)findViewById(R.id.imgExpertThisMonth);
        imgExpertThisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateTipOfDayActivity.this, ExpertThisMonth.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        lnCreateTip = (LinearLayout)findViewById(R.id.lnCreateTip);
        lnCreateTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateTipOfDayActivity.this, TipsAdminFirstActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                CreateTipOfDayActivity.this.finish();
            }
        });
        lnTipPart = (LinearLayout)findViewById(R.id.lnTipPart);
        lnChooseTipPart = (LinearLayout)findViewById(R.id.lnChooseTipPart);
        if(Common.getInstance().mTip.equals("0")){
            lnChooseTipPart.setVisibility(View.VISIBLE);
            lnTipPart.setVisibility(View.GONE);
        }else{
            lnChooseTipPart.setVisibility(View.GONE);
            lnTipPart.setVisibility(View.VISIBLE);
            txtHomeName.setText(Common.getInstance().mTipDictionary.home_name);
            txtAwayName.setText(Common.getInstance().mTipDictionary.away_name);
            ImageLoader.getInstance().displayImage(Common.getInstance().mTipDictionary.home_image, imgHome, Common.getInstance().goodsDisplayImageOptions);
            ImageLoader.getInstance().displayImage(Common.getInstance().mTipDictionary.away_image, imgAway, Common.getInstance().goodsDisplayImageOptions);
            txtLeagueName.setText(Common.getInstance().mTipDictionary.league_name);
            txtTipAmount.setText(Common.getInstance().mTipDictionary.tip_amount);
            txtOddStyle.setText(Common.getInstance().mTipDictionary.market_style + " " + Common.getInstance().mTipDictionary.odd_style + " @" + Common.getInstance().mTipDictionary.odd);
        }
        edtDescription = (EditText)findViewById(R.id.edtDescription);

        btnPublish = (Button)findViewById(R.id.btnPublish);
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtDescription.getText().toString().equals("")){
                    Toast.makeText(CreateTipOfDayActivity.this, "Please input the detailed analasis..", Toast.LENGTH_SHORT).show();
                    return;
                }else if(Common.getInstance().mTip.equals("0")){
                    Toast.makeText(CreateTipOfDayActivity.this, "Please choose the tip at first..", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    mProgDlg.show();
                    new Thread(mLoadRunnable_place).start();
                }
            }
        });
    }
    private Runnable mLoadRunnable_place = new Runnable() {
        @Override
        public void run() {
            String game_style = "soccer";
            int ret = NetworkManager.getManager().placeTipOfDay(Common.getInstance().user_id, Common.getInstance().mTipDictionary.event_id, game_style, Common.getInstance().mTipDictionary.market_style, Common.getInstance().mTipDictionary.odd_style, Common.getInstance().mTipDictionary.odd, Common.getInstance().mTipDictionary.tip_amount, Common.getInstance().mTipDictionary.home_name, Common.getInstance().mTipDictionary.away_name, Common.getInstance().mTipDictionary.start_date_time, Common.getInstance().mTipDictionary.league_name, edtDescription.getText().toString());
            mLoadHandler_Place.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler_Place = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                Toast.makeText(CreateTipOfDayActivity.this, "Your tip was placed successfully for Today!", Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else if (msg.what == 0) {
                Toast.makeText(CreateTipOfDayActivity.this, "The same tip is already existed. Please try to place other tip", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreateTipOfDayActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        if(Common.getInstance().mTip.equals("0")){
            lnChooseTipPart.setVisibility(View.VISIBLE);
            lnTipPart.setVisibility(View.GONE);
        }else{
            lnChooseTipPart.setVisibility(View.GONE);
            lnTipPart.setVisibility(View.VISIBLE);
            txtHomeName.setText(Common.getInstance().mTipDictionary.home_name);
            txtAwayName.setText(Common.getInstance().mTipDictionary.away_name);
            ImageLoader.getInstance().displayImage(Common.getInstance().mTipDictionary.home_image, imgHome, Common.getInstance().goodsDisplayImageOptions);
            ImageLoader.getInstance().displayImage(Common.getInstance().mTipDictionary.away_image, imgAway, Common.getInstance().goodsDisplayImageOptions);
            txtLeagueName.setText(Common.getInstance().mTipDictionary.league_name);
            txtTipAmount.setText(Common.getInstance().mTipDictionary.tip_amount);
            txtOddStyle.setText(Common.getInstance().mTipDictionary.market_style + " " + Common.getInstance().mTipDictionary.odd_style + " @" + Common.getInstance().mTipDictionary.odd);
        }
    }
}
