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
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.diretta.livescore.adapter.DiscoveryPagerAdapter;
import com.diretta.livescore.adapter.GuidePagerAdapter;
import com.diretta.livescore.common.Common;
import com.diretta.livescore.net.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by suc on 4/26/2018.
 */

public class SearchNewActivity extends FragmentActivity {
    ImageView imgBack;
    LinearLayout lnContainer;
    RelativeLayout RnPopular, RnFire;
    TextView txtFire;
    View viewPopularBorder, viewFireBorder;
    EditText edtSearch;
    ImageView imgSearch;
    ProgressDialog mProgDlg;
    String mSelectedFollowId = "";
    String mSelectedStatus = "";
    ViewPager viewPager;
    DiscoveryPagerAdapter mAdapter;
    TextView txtSportDescription;
    int selectPopular = 0;
    String mSelectedSport = "soccer";
    LinearLayout lnTabPart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_new);
        mProgDlg = new ProgressDialog(SearchNewActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        lnTabPart = (LinearLayout)findViewById(R.id.lnTabPart);
        lnTabPart.setVisibility(View.VISIBLE);
        edtSearch = (EditText)findViewById(R.id.edtSearch);
        imgSearch = (ImageView)findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                loadData();
            }
        });

        lnContainer = (LinearLayout)findViewById(R.id.lnContainer);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        mAdapter = new DiscoveryPagerAdapter(getSupportFragmentManager());
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(20);
        viewPager.setAdapter(mAdapter);

        txtSportDescription = (TextView)findViewById(R.id.txtSportDescription);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        txtSportDescription.setText("Players that recently made tips on soccer");
                        mSelectedSport = "soccer";
                        bindData();
                        break;
                    case 1:
                        txtSportDescription.setText("Players that recently made tips on tennis");
                        mSelectedSport = "tennis";
                        bindData();
                        break;
                    case 2:
                        txtSportDescription.setText("Players that recently made tips on basketball");
                        mSelectedSport = "basketball";
                        bindData();
                        break;
                    case 3:
                        txtSportDescription.setText("Players that recently made tips on cricket");
                        mSelectedSport = "cricket";
                        bindData();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        txtFire = (TextView)findViewById(R.id.txtFire);
        viewPopularBorder = (View)findViewById(R.id.viewPopularBorder);
        viewFireBorder = (View)findViewById(R.id.viewFireBorder);
        RnPopular = (RelativeLayout)findViewById(R.id.RnPopular);
        RnFire = (RelativeLayout)findViewById(R.id.RnFire);
        selectPopular = 0;
        mSelectedSport = "soccer";
        selectSearch(0);
        RnPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSearch(0);
                bindData();
            }
        });
        RnFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSearch(1);
                bindData();
            }
        });
        loadData();
        hideKeyboard();
    }
    private void selectSearch(int index){
        selectPopular = index;
        txtFire.setTextColor(getResources().getColor(R.color.clr_grey));

        viewPopularBorder.setBackgroundColor(getResources().getColor(R.color.clr_white));
        viewFireBorder.setBackgroundColor(getResources().getColor(R.color.clr_white));

        if(index == 0){
            viewPopularBorder.setBackgroundColor(getResources().getColor(R.color.clr_green));
        }else{
            txtFire.setTextColor(getResources().getColor(R.color.clr_black));
            viewFireBorder.setBackgroundColor(getResources().getColor(R.color.clr_green));
        }
    }
    private void loadData(){
        mProgDlg.show();
        new Thread(mLoadRunnable).start();
    }
    private Runnable mLoadRunnable = new Runnable() {
        @Override
        public void run() {
            int ret = NetworkManager.getManager().discoverUser(Common.getInstance().user_id, edtSearch.getText().toString());
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

                Toast.makeText(SearchNewActivity.this, "No users.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SearchNewActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };
    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
    private void bindData(){
        lnContainer.removeAllViews();
        try{
            if(edtSearch.getText().toString().equals("")){
                lnTabPart.setVisibility(View.VISIBLE);
                txtSportDescription.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                if(selectPopular == 0){
                    JSONArray arrSearch = Common.getInstance().searchJson.getJSONArray("popular_users");
                    for(int i = 0; i < arrSearch.length(); i++){
                        final JSONObject obj = arrSearch.getJSONObject(i);
                        if(!obj.getString("game_style").equals(mSelectedSport)){
                            continue;
                        }
                        final View v2 = LayoutInflater.from(SearchNewActivity.this).inflate(R.layout.search_item, null);
                        ImageView imgProfile = (ImageView)v2.findViewById(R.id.imgProfile);
                        String strPath = "https://tipyaapp.com/" + obj.getString("profile_img");
                        ImageLoader.getInstance().displayImage(strPath, imgProfile, Common.getInstance().goodsDisplayImageOptions);
                        imgProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try{
                                    Intent intent = new Intent(SearchNewActivity.this, UserProfileActivity.class);
                                    intent.putExtra("user_id", obj.getString("user_id"));
                                    startActivity(intent);
                                }catch (JSONException e){

                                }
                            }
                        });
                        TextView txtUserName = (TextView)v2.findViewById(R.id.txtUserName);
                        txtUserName.setText(obj.getString("user_name"));
                        ToggleButton toggleFollow = (ToggleButton)v2.findViewById(R.id.toggleFollow);

                        if(obj.getString("follow").equals("0")){
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
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchNewActivity.this);

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
                                            buttonView.setChecked(true);
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
                }else{
                    JSONArray arrSearch = Common.getInstance().searchJson.getJSONArray("fire_user");
                    for(int i = 0; i < arrSearch.length(); i++){
                        final JSONObject obj = arrSearch.getJSONObject(i);
                        final View v2 = LayoutInflater.from(SearchNewActivity.this).inflate(R.layout.search_item, null);
                        ImageView imgProfile = (ImageView)v2.findViewById(R.id.imgProfile);
                        String strPath = "https://tipyaapp.com/" + obj.getString("profile_img");
                        ImageLoader.getInstance().displayImage(strPath, imgProfile, Common.getInstance().goodsDisplayImageOptions);
                        imgProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try{
                                    Intent intent = new Intent(SearchNewActivity.this, UserProfileActivity.class);
                                    intent.putExtra("user_id", obj.getString("user_id"));
                                    startActivity(intent);
                                }catch (JSONException e){

                                }
                            }
                        });
                        TextView txtUserName = (TextView)v2.findViewById(R.id.txtUserName);
                        txtUserName.setText(obj.getString("user_name"));
                        ToggleButton toggleFollow = (ToggleButton)v2.findViewById(R.id.toggleFollow);

                        if(obj.getString("follow").equals("0")){
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
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchNewActivity.this);

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
                                            buttonView.setChecked(true);
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
            }else{
                lnTabPart.setVisibility(View.GONE);
                txtSportDescription.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                TextView txtGoodsName = new TextView(SearchNewActivity.this);
                LinearLayout.LayoutParams param_goodsName = new LinearLayout.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT);
                param_goodsName.leftMargin = (int) getResources().getDimension(R.dimen.space_10);
                param_goodsName.topMargin = (int) getResources().getDimension(R.dimen.space_10);
                txtGoodsName.setText("Search result");
                txtGoodsName.setLayoutParams(param_goodsName);
                txtGoodsName.setSingleLine(true);
                txtGoodsName.setTextColor(getResources().getColor(R.color.clr_grey));
                lnContainer.addView(txtGoodsName);

                JSONArray arrSearch = Common.getInstance().searchJson.getJSONArray("popular_users");
                for(int i = 0; i < arrSearch.length(); i++){
                    final JSONObject obj = arrSearch.getJSONObject(i);

                    final View v2 = LayoutInflater.from(SearchNewActivity.this).inflate(R.layout.search_item, null);
                    ImageView imgProfile = (ImageView)v2.findViewById(R.id.imgProfile);
                    String strPath = "https://tipyaapp.com/" + obj.getString("profile_img");
                    ImageLoader.getInstance().displayImage(strPath, imgProfile, Common.getInstance().goodsDisplayImageOptions);
                    imgProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try{
                                Intent intent = new Intent(SearchNewActivity.this, UserProfileActivity.class);
                                intent.putExtra("user_id", obj.getString("user_id"));
                                startActivity(intent);
                            }catch (JSONException e){

                            }
                        }
                    });
                    TextView txtUserName = (TextView)v2.findViewById(R.id.txtUserName);
                    txtUserName.setText(obj.getString("user_name"));
                    ToggleButton toggleFollow = (ToggleButton)v2.findViewById(R.id.toggleFollow);

                    if(obj.getString("follow").equals("0")){
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
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchNewActivity.this);

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
                                        buttonView.setChecked(true);
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
                    Toast.makeText(SearchNewActivity.this, "UnFollowed Successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SearchNewActivity.this, "Followed Successfully", Toast.LENGTH_SHORT).show();
                }
                loadData();
            } else {
                Toast.makeText(SearchNewActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
