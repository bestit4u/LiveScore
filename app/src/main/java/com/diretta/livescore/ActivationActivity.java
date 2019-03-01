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
import android.widget.Toast;

import com.diretta.livescore.common.Common;
import com.diretta.livescore.net.NetworkManager;

/**
 * Created by suc on 5/2/2018.
 */

public class ActivationActivity extends Activity {
    ImageView imgCodeCheck, imgBack;
    Button btnActivate;
    EditText edtCode;
    LinearLayout lnCode;
    ProgressDialog mProgDlg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mProgDlg = new ProgressDialog(ActivationActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);
        lnCode = (LinearLayout)findViewById(R.id.lnCode);
        edtCode = (EditText)findViewById(R.id.edtCode);
        imgCodeCheck = (ImageView)findViewById(R.id.imgCodeCheck);
        btnActivate = (Button)findViewById(R.id.btnActivate);
        btnActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtCode.getText().toString().equals("")){
                    Toast.makeText(ActivationActivity.this, "You have to input the activation code.", Toast.LENGTH_SHORT).show();
                }else if(edtCode.getText().toString().length() != 4){
                    Toast.makeText(ActivationActivity.this, "You have to input the activation code with only 4 digits.", Toast.LENGTH_SHORT).show();
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
            int ret = NetworkManager.getManager().activate(Common.getInstance().user_id, edtCode.getText().toString());
            mLoadHandler.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                Toast.makeText(ActivationActivity.this, "Sign up Success!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivationActivity.this, FeedActivity.class);
                startActivity(intent);
            }
            else if (msg.what == 0) {
                Toast.makeText(ActivationActivity.this, "Activation code is wrong. Please input again.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActivationActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };
}
