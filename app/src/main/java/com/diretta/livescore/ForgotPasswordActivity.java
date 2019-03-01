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
import android.widget.Toast;

import com.diretta.livescore.common.Common;
import com.diretta.livescore.net.NetworkManager;

/**
 * Created by suc on 4/26/2018.
 */

public class ForgotPasswordActivity extends Activity {
    ImageView imgBack;
    Button btnChangePassword;
    EditText edtEmail;
    ProgressDialog mProgDlg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        mProgDlg = new ProgressDialog(ForgotPasswordActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        btnChangePassword = (Button)findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtEmail.getText().toString().equals("")){
                    edtEmail.setBackground(getResources().getDrawable(R.drawable.button_empty_red_back));
                    Toast.makeText(ForgotPasswordActivity.this, "Please input the email.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!edtEmail.getText().toString().contains("@")){
                    edtEmail.setBackground(getResources().getDrawable(R.drawable.button_empty_red_back));
                    Toast.makeText(ForgotPasswordActivity.this, "Please input the correct email.", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    mProgDlg.show();
                    new Thread(mLoadRunnable).start();
                }
            }
        });
    }
    private Runnable mLoadRunnable = new Runnable() {
        @Override
        public void run() {
            int ret = NetworkManager.getManager().resetPassword(edtEmail.getText().toString());
            mLoadHandler.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                Toast.makeText(ForgotPasswordActivity.this, "Your password was changed. Please check your email", Toast.LENGTH_SHORT).show();
            }else if(msg.what == 0){
                Toast.makeText(ForgotPasswordActivity.this, "The email that you inputed is not valid.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ForgotPasswordActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
