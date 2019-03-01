package com.diretta.livescore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diretta.livescore.common.Common;
import com.diretta.livescore.net.NetworkManager;
import com.diretta.livescore.util.IabHelper;
import com.diretta.livescore.util.IabResult;
import com.diretta.livescore.util.Inventory;

import java.util.List;

/**
 * Created by suc on 4/25/2018.
 */

public class LoginActivity extends Activity {
    TextView txtGoRegister, txtForgotPassword;
    Button btnLogin;
    LinearLayout lnEmail, lnPassword;
    EditText edtEmail, edtPassword;
    ImageView imgEmailCheck, imgPasswordCheck;
    ProgressDialog mProgDlg;
    android.content.SharedPreferences.Editor ed;
    SharedPreferences sp;
    IabHelper mHelper;
    String mPublic_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtCLb0FaTJTWHcID598fmSSYGNlA0CkA2P3MYjDCczgGJbYKYDIrZWHXZ00PqOeOR/ozrjihgy0Y7dsws6o47VqVCv598VGihIbb13WNE6Yy/P+N7Qm+T3bBSLs9VQo7YYBKesH00K1drk7tdF2m+eHh7mm1YLN+X0lqbcYkR+IVdjrbGhdHHbdK87VXXHN0WIpRzxRzZuBirek3R3x86nvkPIkSt1XeddZDZ1XmAeplEf0wRp7htSRJdshDj6+HBUbaBNxi74HMNZaXzuVRoUgSoNAXFz/ZDKtpXlwsmGHBT/bsWkdZ/9usDrMMy3lIqOFrSidudbQrnlKyzwVOOaQIDAQAB";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InAppInit_U(mPublic_key, true);
        ImageView imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        ed = sp.edit();
        mProgDlg = new ProgressDialog(LoginActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);

        lnEmail = (LinearLayout)findViewById(R.id.lnEmail);
        lnPassword = (LinearLayout)findViewById(R.id.lnPassword);

        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtPassword = (EditText)findViewById(R.id.edtPassword);

        imgEmailCheck = (ImageView)findViewById(R.id.imgEmailCheck);
        imgPasswordCheck = (ImageView)findViewById(R.id.imgPasswordCheck);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int emailcheck = 0;
                int passwordcheck = 0;

                if(edtEmail.getText().toString().equals("")){
                    lnEmail.setBackground(getResources().getDrawable(R.drawable.button_empty_red_back));
                    imgEmailCheck.setImageDrawable(getResources().getDrawable(R.drawable.failmark));
                    emailcheck = 0;
                }else{
                    if(edtEmail.getText().toString().contains("@")){
                        lnEmail.setBackground(getResources().getDrawable(R.drawable.button_empty_back));
                        imgEmailCheck.setImageDrawable(getResources().getDrawable(R.drawable.checkmark));
                        emailcheck = 1;
                    }else{
                        lnEmail.setBackground(getResources().getDrawable(R.drawable.button_empty_red_back));
                        imgEmailCheck.setImageDrawable(getResources().getDrawable(R.drawable.failmark));
                        emailcheck = 0;
                    }
                }

                if(edtPassword.getText().toString().equals("")){
                    lnPassword.setBackground(getResources().getDrawable(R.drawable.button_empty_red_back));
                    imgPasswordCheck.setImageDrawable(getResources().getDrawable(R.drawable.failmark));
                    passwordcheck = 0;
                }else{
                    lnPassword.setBackground(getResources().getDrawable(R.drawable.button_empty_back));
                    imgPasswordCheck.setImageDrawable(getResources().getDrawable(R.drawable.checkmark));
                    passwordcheck = 1;
                }

                if((emailcheck == 1) && (passwordcheck == 1)){
                    mProgDlg.show();
                    new Thread(mLoadRunnable).start();
                }
            }
        });
        txtGoRegister = (TextView)findViewById(R.id.txtGoRegister);
        txtGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_home = new Intent(LoginActivity.this, RegisterActivity.class);
                intent_home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_home);
            }
        });
        txtForgotPassword = (TextView)findViewById(R.id.txtForgotPassword);
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_home = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                intent_home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_home);
            }
        });
        if(sp.getBoolean("remember", false) == true){
            mProgDlg.show();
            edtEmail.setText(sp.getString("email", ""));
            edtPassword.setText(sp.getString("password", ""));
            new Thread(mLoadRunnable).start();
        }
    }
    private Runnable mLoadRunnable = new Runnable() {
        @Override
        public void run() {
            int ret = NetworkManager.getManager().login(edtEmail.getText().toString(), edtPassword.getText().toString());
            mLoadHandler.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                Toast.makeText(LoginActivity.this, "Sign in Success!", Toast.LENGTH_SHORT).show();
                ed.putBoolean("remember", true);
                ed.putString("email", edtEmail.getText().toString());
                ed.putString("password", edtPassword.getText().toString());
                ed.commit();
                Intent intent_home = new Intent(LoginActivity.this, FeedActivity.class);
                intent_home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_home);
            }
            else if (msg.what == 0) {
                lnEmail.setBackground(getResources().getDrawable(R.drawable.button_empty_red_back));
                imgEmailCheck.setImageDrawable(getResources().getDrawable(R.drawable.failmark));
                lnPassword.setBackground(getResources().getDrawable(R.drawable.button_empty_red_back));
                imgPasswordCheck.setImageDrawable(getResources().getDrawable(R.drawable.failmark));
                Toast.makeText(LoginActivity.this, "Please input the correct email or password!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };

    public void InAppInit_U(String strPublicKey, boolean bDebug) {
        Log.d("myLog", "Creating IAB helper." + bDebug);
        mHelper = new IabHelper(this, strPublicKey);

        if (bDebug == true) {
            mHelper.enableDebugLogging(true, "IAB");
        }

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {

            @Override
            public void onIabSetupFinished(IabResult result) {
                // TODO Auto-generated method stub
                boolean bInit = result.isSuccess();
                Log.d("myLog", "IAB Init " + bInit + result.getMessage());
                try {
                    if (bInit == true) {
                        Log.d("myLog", "Querying inventory.");

                        mHelper.queryInventoryAsync(mGotInventoryListener);
                    }

                } catch (IabHelper.IabAsyncInProgressException e) {

                }
            }
        });
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {
            if (result.isFailure()) {
                Log.d("myLog", "Failed to query inventory: " + result);
                return;
            }
            List<String> inappList = inventory.getAllOwnedSkus(IabHelper.ITEM_TYPE_SUBS);
            // inappList.add("item01");
            if (inappList.size() > 0) {
                Common.getInstance().free = false;
            } else
                Common.getInstance().free = true;

        }
    };
}
