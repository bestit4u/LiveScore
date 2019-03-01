package com.diretta.livescore;

import android.app.Activity;
import android.app.ProgressDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by suc on 4/26/2018.
 */

public class ChangePasswordActivity extends Activity {
    ImageView imgBack;
    ImageView imgProfile;
    TextView txtName;
    EditText edtCurrentPassword, edtNewPassword, edtRepeatPasword;
    Button btnChangePassword;
    ProgressDialog mProgDlg;
    LinearLayout lnCurrentPassword, lnNewPassword, lnConfirmPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        lnCurrentPassword = (LinearLayout)findViewById(R.id.lnCurrentPassword);
        lnNewPassword = (LinearLayout)findViewById(R.id.lnNewPassword);
        lnConfirmPassword = (LinearLayout)findViewById(R.id.lnConfirmPassword);
        mProgDlg = new ProgressDialog(ChangePasswordActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgProfile = (ImageView)findViewById(R.id.imgProfile);
        txtName = (TextView)findViewById(R.id.txtName);
        edtCurrentPassword = (EditText)findViewById(R.id.edtCurrentPassword);
        edtNewPassword = (EditText)findViewById(R.id.edtNewPassword);
        edtRepeatPasword = (EditText)findViewById(R.id.edtRepeatPasword);

        btnChangePassword = (Button)findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPasswordValid = 0;
                if(edtCurrentPassword.getText().toString().equals("")){
                    lnCurrentPassword.setBackground(getResources().getDrawable(R.drawable.button_empty_red_back));
                    currentPasswordValid = 0;
                }else{
                    lnCurrentPassword.setBackground(getResources().getDrawable(R.drawable.button_empty_back));
                    currentPasswordValid = 1;
                }

                int newPasswordValid = 0;
                if(edtNewPassword.getText().toString().equals("")){
                    lnNewPassword.setBackground(getResources().getDrawable(R.drawable.button_empty_red_back));
                    newPasswordValid = 0;
                }else{
                    lnNewPassword.setBackground(getResources().getDrawable(R.drawable.button_empty_back));
                    newPasswordValid = 1;
                }

                int confirmPasswordValid = 0;
                if(edtRepeatPasword.getText().toString().equals("")){
                    lnConfirmPassword.setBackground(getResources().getDrawable(R.drawable.button_empty_red_back));
                    confirmPasswordValid = 0;
                }else{
                    lnConfirmPassword.setBackground(getResources().getDrawable(R.drawable.button_empty_back));
                    confirmPasswordValid = 1;
                }

                if((currentPasswordValid == 1) && (newPasswordValid == 1) && (confirmPasswordValid == 1)){
                    if(edtNewPassword.getText().toString().equals(edtRepeatPasword.getText().toString())){
                        mProgDlg.show();
                        new Thread(mLoadRunnable_change).start();
                    }else{
                        Toast.makeText(ChangePasswordActivity.this, "Please input the new password again.", Toast.LENGTH_SHORT).show();
                        lnNewPassword.setBackground(getResources().getDrawable(R.drawable.button_empty_red_back));
                        lnConfirmPassword.setBackground(getResources().getDrawable(R.drawable.button_empty_red_back));
                    }
                }
            }
        });
        loadData();
    }
    private void loadData(){
        mProgDlg.show();
        new Thread(mLoadRunnable).start();
    }
    private Runnable mLoadRunnable = new Runnable() {
        @Override
        public void run() {
            int ret = NetworkManager.getManager().profileSetting(Common.getInstance().user_id);
            mLoadHandler.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                try{
                    JSONObject userDic = Common.getInstance().settingProfileJson.getJSONObject("profile");
                    txtName.setText(userDic.getString("full_name"));
                    String strPath = "https://tipyaapp.com/" + userDic.getString("profile_img");
                    ImageLoader.getInstance().displayImage(strPath, imgProfile, Common.getInstance().goodsDisplayImageOptions);

                }catch (JSONException e){

                }
            }else if(msg.what == 0){
                Toast.makeText(ChangePasswordActivity.this, "No data", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ChangePasswordActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Runnable mLoadRunnable_change = new Runnable() {
        @Override
        public void run() {
            int ret = NetworkManager.getManager().changePassword(Common.getInstance().user_id, edtCurrentPassword.getText().toString(), edtNewPassword.getText().toString());
            mLoadHandler_change.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler_change = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                Toast.makeText(ChangePasswordActivity.this, "Your password was changed!", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }else if(msg.what == 0){
                Toast.makeText(ChangePasswordActivity.this, "The current password is wrong", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ChangePasswordActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
