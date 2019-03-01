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
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.diretta.livescore.common.Common;
import com.diretta.livescore.net.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by suc on 4/26/2018.
 */

public class SearchActivity extends Activity {
    ImageView imgBack;
    LinearLayout lnContainer;
    EditText edtSearch;
    ImageView imgSearch;
    ProgressDialog mProgDlg;
    String mSelectedFollowId = "";
    String mSelectedStatus = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mProgDlg = new ProgressDialog(SearchActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);
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

        lnContainer = (LinearLayout)findViewById(R.id.lnContainer);
        loadData();
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
                lnContainer.removeAllViews();
                try{
                    JSONArray arrSearch = Common.getInstance().searchJson.getJSONArray("suggested_users");
                    if(edtSearch.getText().toString().equals("")){
                        TextView txtGoodsName = new TextView(SearchActivity.this);
                        LinearLayout.LayoutParams param_goodsName = new LinearLayout.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT);
                        param_goodsName.leftMargin = (int) getResources().getDimension(R.dimen.space_10);
                        param_goodsName.topMargin = (int) getResources().getDimension(R.dimen.space_10);
                        txtGoodsName.setText(getResources().getString(R.string.suggested));
                        txtGoodsName.setLayoutParams(param_goodsName);
                        txtGoodsName.setSingleLine(true);
                        txtGoodsName.setTextColor(getResources().getColor(R.color.clr_grey));
                        lnContainer.addView(txtGoodsName);
                    }
                    for(int i = 0; i < arrSearch.length(); i++){
                        final JSONObject obj = arrSearch.getJSONObject(i);
                        final View v2 = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_item, null);
                        ImageView imgProfile = (ImageView)v2.findViewById(R.id.imgProfile);
                        String strPath = "https://tipyaapp.com/" + obj.getString("profile_img");
                        ImageLoader.getInstance().displayImage(strPath, imgProfile, Common.getInstance().goodsDisplayImageOptions);
                        imgProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try{
                                    Intent intent = new Intent(SearchActivity.this, UserProfileActivity.class);
                                    intent.putExtra("user_id", obj.getString("user_id"));
                                    startActivity(intent);
                                }catch (JSONException e){

                                }
                            }
                        });
                        TextView txtUserName = (TextView)v2.findViewById(R.id.txtUserName);
                        txtUserName.setText(obj.getString("user_name"));
                        ToggleButton toggleFollow = (ToggleButton)v2.findViewById(R.id.toggleFollow);
                        String country_code = Locale.getDefault().getLanguage();
                        if(country_code.equals("da")){
                            toggleFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_toggle_follow_da));
                        }else{
                            toggleFollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_toggle_follow_en));
                        }
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
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchActivity.this);

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
                }catch (JSONException e){

                }

            } else if (msg.what == 0) {

                Toast.makeText(SearchActivity.this, "No users.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SearchActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };
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
                    Toast.makeText(SearchActivity.this, "UnFollowed Successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SearchActivity.this, "Followed Successfully", Toast.LENGTH_SHORT).show();
                }
                loadData();
            } else {
                Toast.makeText(SearchActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
