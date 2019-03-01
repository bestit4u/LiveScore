package com.diretta.livescore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
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

public class FollowManagementActivity extends Activity {
    RelativeLayout RnFollowers, RnFollowing;
    TextView txtFollowers, txtFollowing;
    View viewFollowersBorder, viewFollowingBorder;
    LinearLayout lnContainer;
    ProgressDialog mProgDlg;
    ImageView imgBack, imgSearch;
    EditText edtSearch;
    String mTabStatus = "0";
    String mSelectedFollowId = "";
    String mSelectedStatus = "";
    LinearLayout lnHeader;
    LinearLayout lnNoFollow;
    ScrollView scrollView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        lnHeader = (LinearLayout)findViewById(R.id.lnHeader);
        lnNoFollow = (LinearLayout)findViewById(R.id.lnNoFollow);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        edtSearch = (EditText)findViewById(R.id.edtSearch);
        imgSearch = (ImageView)findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        mProgDlg = new ProgressDialog(FollowManagementActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);

        RnFollowers = (RelativeLayout)findViewById(R.id.RnFollowers);
        RnFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTabStatus = "0";
                selectMonthPart("0");
                bindData(mTabStatus);
            }
        });

        RnFollowing = (RelativeLayout)findViewById(R.id.RnFollowing);
        RnFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTabStatus = "1";
                selectMonthPart("1");
                bindData(mTabStatus);

            }
        });
        lnContainer = (LinearLayout)findViewById(R.id.lnContainer);
        txtFollowers = (TextView)findViewById(R.id.txtFollowers);
        txtFollowing = (TextView)findViewById(R.id.txtFollowing);
        viewFollowersBorder = (View)findViewById(R.id.viewFollowersBorder);
        viewFollowingBorder = (View)findViewById(R.id.viewFollowingBorder);
        mTabStatus = getIntent().getStringExtra("status");
        selectMonthPart(mTabStatus);
        loadData();
    }
    private void loadData(){
        mProgDlg.show();
        new Thread(mLoadRunnable).start();
    }
    private Runnable mLoadRunnable = new Runnable() {
        @Override
        public void run() {
            int ret = NetworkManager.getManager().followManagement(Common.getInstance().user_id, edtSearch.getText().toString());
            mLoadHandler.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                bindData(mTabStatus);
            }
            else if (msg.what == 0) {
                Toast.makeText(FollowManagementActivity.this, "There is no data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FollowManagementActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };
    private void selectMonthPart(String index){
        txtFollowers.setTextColor(getResources().getColor(R.color.clr_grey));
        txtFollowing.setTextColor(getResources().getColor(R.color.clr_grey));

        viewFollowersBorder.setBackgroundColor(getResources().getColor(R.color.clr_white));
        viewFollowingBorder.setBackgroundColor(getResources().getColor(R.color.clr_white));

        if(index.equals("0")){
            txtFollowers.setTextColor(getResources().getColor(R.color.clr_green));
            viewFollowersBorder.setBackgroundColor(getResources().getColor(R.color.clr_green));
        }else{
            txtFollowing.setTextColor(getResources().getColor(R.color.clr_green));
            viewFollowingBorder.setBackgroundColor(getResources().getColor(R.color.clr_green));
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    private void bindData(String status){
        lnContainer.removeAllViews();

        if(status.equals("0")){
            try{
                JSONArray arrFollowers = Common.getInstance().followManageJson.getJSONArray("follower_users");
                if(arrFollowers.length() == 0){
                    lnHeader.setVisibility(View.GONE);
                    scrollView.setVisibility(View.GONE);
                    lnNoFollow.setVisibility(View.VISIBLE);
                }else {
                    lnHeader.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
                    lnNoFollow.setVisibility(View.GONE);
                    for(int i = 0; i < arrFollowers.length(); i++){
                        final JSONObject obj = arrFollowers.getJSONObject(i);
                        final View v2 = LayoutInflater.from(FollowManagementActivity.this).inflate(R.layout.follow_item, null);
                        ImageView imgProfile = (ImageView)v2.findViewById(R.id.imgProfile);
                        String strPath = "https://tipyaapp.com/" + obj.getString("profile_img");
                        ImageLoader.getInstance().displayImage(strPath, imgProfile, Common.getInstance().goodsDisplayImageOptions);
                        TextView txtUserName = (TextView)v2.findViewById(R.id.txtUserName);
                        txtUserName.setText(obj.getString("name"));

                        int percent = Integer.parseInt(obj.getString("percent"));
                        TextView txtPercent = (TextView)v2.findViewById(R.id.txtPercent);
                        txtPercent.setText(obj.getString("percent") + "%");
                        if(percent >= 50){
                            txtPercent.setTextColor(getResources().getColor(R.color.clr_green));
                        }else{
                            if(percent == 0)
                                txtPercent.setTextColor(getResources().getColor(R.color.clr_black));
                            else
                                txtPercent.setTextColor(getResources().getColor(R.color.clr_red));
                        }

                        int point = Integer.parseInt(obj.getString("point"));
                        TextView txtPoint = (TextView)v2.findViewById(R.id.txtPoint);
                        if(point >= 0){
                            txtPoint.setText("+" + obj.getString("point"));
                            if(point == 0)
                                txtPoint.setTextColor(getResources().getColor(R.color.clr_black));
                            else
                                txtPoint.setTextColor(getResources().getColor(R.color.clr_green));
                        }else{
                            txtPoint.setTextColor(getResources().getColor(R.color.clr_red));
                            txtPoint.setText("" + obj.getString("point"));
                        }

                        ToggleButton toggleFollow = (ToggleButton)v2.findViewById(R.id.toggleFollow);
                        String country_code = Locale.getDefault().getLanguage();
                        if(country_code.equals("da")){
                            toggleFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_toggle_follow_da));
                        }else{
                            toggleFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_toggle_follow_en));
                        }
                        if(obj.getString("status").equals("0")){
                            toggleFollow.setChecked(false);
                        }else{
                            toggleFollow.setChecked(true);
                        }
                        toggleFollow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                                if(isChecked == false){
                                    try {
                                        mSelectedFollowId = obj.getString("user_id");
                                        mSelectedStatus = "0";
                                    }catch (JSONException e){

                                    }
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(FollowManagementActivity.this);

                                    alertDialog.setTitle("Confirm");
                                    alertDialog.setMessage("Are you sure you want to unfollow?");
                                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog,int which) {
                                            loadFollow();
                                        }
                                    });

                                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog, int which){
                                            dialog.cancel();
                                            loadData();
                                        }
                                    });

                                    alertDialog.show();
                                }else{
                                    try {
                                        mSelectedFollowId = obj.getString("user_id");
                                        mSelectedStatus = "1";
                                    }catch (JSONException e){

                                    }
                                    loadFollow();
                                }
                            }
                        });
                        lnContainer.addView(v2);
                    }
                }
            }catch (JSONException e){

            }
        }else{
            try{
                JSONArray arrFollowing = Common.getInstance().followManageJson.getJSONArray("following_users");
                if(arrFollowing.length() == 0){
                    lnHeader.setVisibility(View.GONE);
                    scrollView.setVisibility(View.GONE);
                    lnNoFollow.setVisibility(View.VISIBLE);
                }else{
                    lnHeader.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
                    lnNoFollow.setVisibility(View.GONE);
                    for(int i = 0; i < arrFollowing.length(); i++){
                        final JSONObject obj = arrFollowing.getJSONObject(i);
                        final View v2 = LayoutInflater.from(FollowManagementActivity.this).inflate(R.layout.follow_item, null);
                        ImageView imgProfile = (ImageView)v2.findViewById(R.id.imgProfile);
                        String strPath = "https://tipyaapp.com/" + obj.getString("profile_img");
                        ImageLoader.getInstance().displayImage(strPath, imgProfile, Common.getInstance().goodsDisplayImageOptions);
                        TextView txtUserName = (TextView)v2.findViewById(R.id.txtUserName);
                        txtUserName.setText(obj.getString("name"));

                        int percent = Integer.parseInt(obj.getString("percent"));
                        TextView txtPercent = (TextView)v2.findViewById(R.id.txtPercent);
                        txtPercent.setText(obj.getString("percent") + "%");
                        if(percent >= 50){
                            txtPercent.setTextColor(getResources().getColor(R.color.clr_green));
                        }else{
                            txtPercent.setTextColor(getResources().getColor(R.color.clr_red));
                        }

                        int point = Integer.parseInt(obj.getString("point"));
                        TextView txtPoint = (TextView)v2.findViewById(R.id.txtPoint);
                        if(point >= 0){
                            txtPoint.setText("+" + obj.getString("point"));
                            txtPoint.setTextColor(getResources().getColor(R.color.clr_green));
                        }else{
                            txtPoint.setTextColor(getResources().getColor(R.color.clr_red));
                            txtPoint.setText("" + obj.getString("point"));
                        }

                        ToggleButton toggleFollow = (ToggleButton)v2.findViewById(R.id.toggleFollow);
                        String country_code = Locale.getDefault().getLanguage();
                        if(country_code.equals("da")){
                            toggleFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_toggle_follow_da));
                        }else{
                            toggleFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_toggle_follow_en));
                        }
                        if(obj.getString("status").equals("0")){
                            toggleFollow.setChecked(false);
                        }else{
                            toggleFollow.setChecked(true);
                        }
                        toggleFollow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                                if(isChecked == false){
                                    try {
                                        mSelectedFollowId = obj.getString("user_id");
                                        mSelectedStatus = "0";
                                    }catch (JSONException e){

                                    }
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(FollowManagementActivity.this);

                                    alertDialog.setTitle("Confirm");
                                    alertDialog.setMessage("Are you sure you want to unfollow?");
                                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog,int which) {
                                            loadFollow();
                                        }
                                    });

                                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog, int which){
                                            dialog.cancel();
                                            loadData();
                                        }
                                    });

                                    alertDialog.show();
                                }else{
                                    try {
                                        mSelectedFollowId = obj.getString("user_id");
                                        mSelectedStatus = "1";
                                    }catch (JSONException e){

                                    }
                                    loadFollow();
                                }
                            }
                        });
                        lnContainer.addView(v2);
                    }
                }
            }catch (JSONException e){

            }

        }
    }
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
                    Toast.makeText(FollowManagementActivity.this, "UnFollowed Successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(FollowManagementActivity.this, "Followed Successfully", Toast.LENGTH_SHORT).show();
                }
                loadData();
            } else {
                Toast.makeText(FollowManagementActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
