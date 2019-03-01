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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.diretta.livescore.common.Common;
import com.diretta.livescore.net.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by suc on 4/26/2018.
 */

public class NotificationActivity extends Activity {
    TextView txtEdit;
    ImageView imgBack;
    LinearLayout lnContainer;
    ProgressDialog mProgDlg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mProgDlg = new ProgressDialog(NotificationActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtEdit = (TextView)findViewById(R.id.txtEdit);
        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, NotificationSettingActivity.class);
                startActivity(intent);
            }
        });
        lnContainer = (LinearLayout)findViewById(R.id.lnContainer);
        mProgDlg.show();
        new Thread(mLoadRunnable).start();
    }
    private Runnable mLoadRunnable = new Runnable() {
        @Override
        public void run() {
            int ret = NetworkManager.getManager().notification(Common.getInstance().user_id);
            mLoadHandler.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                try{
                    JSONArray arrNotification = Common.getInstance().notificationJson.getJSONArray("notifications");
                    for(int i = 0; i < arrNotification.length(); i++){
                        final JSONObject obj = arrNotification.getJSONObject(i);
                        final View v2 = LayoutInflater.from(NotificationActivity.this).inflate(R.layout.notification_item, null);
                        ImageView imgProfile = (ImageView)v2.findViewById(R.id.imgProfile);
                        String strPath = "https://tipyaapp.com/" + obj.getString("profile_img");
                        ImageLoader.getInstance().displayImage(strPath, imgProfile, Common.getInstance().goodsDisplayImageOptions);
                        imgProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent intent = new Intent(NotificationActivity.this, UserProfileActivity.class);
                                    intent.putExtra("user_id", obj.getString("from_id"));
                                    startActivity(intent);
                                }catch (JSONException e){

                                }
                            }
                        });
                        TextView txtTitle = (TextView)v2.findViewById(R.id.txtTitle);
                        txtTitle.setText(obj.getString("description"));
                        TextView txtDate = (TextView)v2.findViewById(R.id.txtDate);
                        txtDate.setText(obj.getString("date"));
                        TextView txtDescription = (TextView)v2.findViewById(R.id.txtDescription);
                        if(obj.getString("description1").equals("")){
                            txtDescription.setText("");
                        }else{
                            txtDescription.setText(obj.getString("description1"));
                        }
                        lnContainer.addView(v2);
                    }
                }catch (JSONException e){

                }
            }else if(msg.what == 0){
                Toast.makeText(NotificationActivity.this, "No data", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(NotificationActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
