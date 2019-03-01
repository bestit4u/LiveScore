package com.diretta.livescore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.diretta.livescore.adapter.GuidePagerAdapter;
import com.diretta.livescore.adapter.PremiumGuidePagerAdapter;
import com.diretta.livescore.common.BaseActivity;
import com.diretta.livescore.common.Common;
import com.diretta.livescore.net.NetworkManager;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by suc on 4/25/2018.
 */

public class ProfileActivity extends BaseActivity {
    ImageView imgSetting;
    ProgressDialog mProgDlg;
    ScrollView scrollView;
    int mCurrentTips = 0;
    TextView txtFollowing, txtFollowers;
    private static final int REQ_CODE_PICK_IMAGE = 3;
    private String strFileName = "";
    private String mSave_path = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        MobileAds.initialize(this, "ca-app-pub-6225734666899710~8077467313");
        //You need to add this code for building the request for the ad
        AdView mAdView = (AdView)findViewById(R.id.adProfile);
        AdRequest adreqeust = new AdRequest.Builder().build();
        mAdView.loadAd(adreqeust);
        mProgDlg = new ProgressDialog(ProfileActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);

        scrollView = (ScrollView)findViewById(R.id.scrollView);

        if(Common.getInstance().free == false){
        }else{
        }
        imgSetting = (ImageView)findViewById(R.id.imgSetting);
        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        mCurrentTips = 0;
        selectTipPart(mCurrentTips);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode != REQ_CODE_PICK_IMAGE) {
            return;
        }
        if (requestCode == REQ_CODE_PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);

            strFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "0.jpg";

            mSave_path = Environment.getExternalStorageDirectory() + "/livescore/" + strFileName;

            try {
                FileOutputStream fos = new FileOutputStream(mSave_path);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("AbaTask", "Failed to scale image!");
            }
        }
        mProgDlg.show();
        new Thread(mLoadRunnable_uploadProfile).start();
    }
    private Runnable mLoadRunnable_uploadProfile = new Runnable() {
        @Override
        public void run() {
            String path = "uploads/" + NetworkManager.getManager().upload(mSave_path);
            int ret = NetworkManager.getManager().placeProfileSettingImage(Common.getInstance().user_id, path);
            mLoadHandler_uploadProfile.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler_uploadProfile = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                Toast.makeText(ProfileActivity.this, "Profile Image update Successfully", Toast.LENGTH_SHORT).show();
            }else if(msg.what == 0){
                Toast.makeText(ProfileActivity.this, "You have to put other email!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ProfileActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private void loadData(){
//        mProgDlg.show();
//        new Thread(mLoadRunnable).start();
    }

    private Runnable mLoadRunnable = new Runnable() {
        @Override
        public void run() {
            Common.getInstance().arrCurrentTips.clear();
            Common.getInstance().arrEndTips.clear();
            int ret = NetworkManager.getManager().getProfile(Common.getInstance().user_id);
            mLoadHandler.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                bindData();
            }
            else if (msg.what == 0) {
                Toast.makeText(ProfileActivity.this, "There is no data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProfileActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };
    private void bindData(){
        txtFollowing.setText(Common.getInstance().userInfo.following_count);
        txtFollowers.setText(Common.getInstance().userInfo.followers_count);
        String photo_url = "https://tipyaapp.com/" + Common.getInstance().userInfo.profile_img;
        if(Common.getInstance().arrCurrentTips.size() > 0){
            scrollView.setVisibility(View.VISIBLE);
            for(int i = 0; i < Common.getInstance().arrCurrentTips.size(); i++){
                final View v1 = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.profile_tip_item, null);
                v1.setTag(Common.getInstance().arrCurrentTips.get(i));
                TextView txtLeagueName = (TextView)v1.findViewById(R.id.txtLeagueName);
                txtLeagueName.setText(Common.getInstance().arrCurrentTips.get(i).league_name);
                TextView txtHomeName = (TextView)v1.findViewById(R.id.txtHomeName);
                txtHomeName.setText(Common.getInstance().arrCurrentTips.get(i).home_name);
                TextView txtAwayName = (TextView)v1.findViewById(R.id.txtAwayName);
                txtAwayName.setText(Common.getInstance().arrCurrentTips.get(i).away_name);
                ImageView imgHome = (ImageView)v1.findViewById(R.id.imgHome);
                ImageLoader.getInstance().displayImage(Common.getInstance().arrCurrentTips.get(i).home_image, imgHome, Common.getInstance().goodsDisplayImageOptions);
                ImageView imgAway = (ImageView)v1.findViewById(R.id.imgAway);
                ImageLoader.getInstance().displayImage(Common.getInstance().arrCurrentTips.get(i).away_image, imgAway, Common.getInstance().goodsDisplayImageOptions);
                TextView txtOddStyle = (TextView)v1.findViewById(R.id.txtOddStyle);
                txtOddStyle.setText(Common.getInstance().arrCurrentTips.get(i).market_style + " " + Common.getInstance().arrCurrentTips.get(i).odd_style + " @" + Common.getInstance().arrCurrentTips.get(i).odd);
                TextView txtTipAmount = (TextView)v1.findViewById(R.id.txtTipAmount);
                txtTipAmount.setText(Common.getInstance().arrCurrentTips.get(i).tip_amount);
                LinearLayout lnViewBorder = (LinearLayout)v1.findViewById(R.id.lnViewBorder);
                lnViewBorder.setBackground(getResources().getDrawable(R.drawable.button_empty_grey_back));

            }
        }else{
            scrollView.setVisibility(View.GONE);
        }
    }
    private void selectTipPart(int index){


    }
    @Override
    protected void onResume() {
        super.onResume();
        initActionBar(4);
        loadData();
    }
}
