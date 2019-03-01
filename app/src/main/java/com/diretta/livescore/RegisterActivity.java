package com.diretta.livescore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
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

import com.diretta.livescore.net.NetworkManager;

/**
 * Created by suc on 4/25/2018.
 */

public class RegisterActivity extends Activity {
    Button btnSignUp;
    TextView txtGoLogin;
    LinearLayout lnFullName, lnEmail, lnPassword;
    EditText edtFullName, edtEmail, edtPassword;
    ImageView imgFullNameCheck, imgEmailCheck, imgPasswordCheck;
    ProgressDialog mProgDlg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ImageView imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mProgDlg = new ProgressDialog(RegisterActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);
        lnFullName = (LinearLayout)findViewById(R.id.lnFullName);
        lnEmail = (LinearLayout)findViewById(R.id.lnEmail);
        lnPassword = (LinearLayout)findViewById(R.id.lnPassword);

        edtFullName = (EditText)findViewById(R.id.edtFullName);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtPassword = (EditText)findViewById(R.id.edtPassword);

        imgFullNameCheck = (ImageView)findViewById(R.id.imgFullNameCheck);
        imgEmailCheck = (ImageView)findViewById(R.id.imgEmailCheck);
        imgPasswordCheck = (ImageView)findViewById(R.id.imgPasswordCheck);

        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fullnamecheck = 0;
                int emailcheck = 0;
                int passwordcheck = 0;

                if(edtFullName.getText().toString().equals("")){
                    lnFullName.setBackground(getResources().getDrawable(R.drawable.button_empty_red_back));
                    imgFullNameCheck.setImageDrawable(getResources().getDrawable(R.drawable.failmark));
                    fullnamecheck = 0;
                }else{
                    lnFullName.setBackground(getResources().getDrawable(R.drawable.button_empty_back));
                    imgFullNameCheck.setImageDrawable(getResources().getDrawable(R.drawable.checkmark));
                    fullnamecheck = 1;
                }

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

                if((fullnamecheck == 1) && (emailcheck == 1) && (passwordcheck == 1)){
                    mProgDlg.show();
                    new Thread(mLoadRunnable).start();
                }
            }
        });
        txtGoLogin = (TextView)findViewById(R.id.txtGoLogin);
        txtGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_home = new Intent(RegisterActivity.this, LoginActivity.class);
                intent_home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_home);
            }
        });
    }
    private Runnable mLoadRunnable = new Runnable() {
        @Override
        public void run() {
            int ret = NetworkManager.getManager().signup(edtFullName.getText().toString(), edtEmail.getText().toString(), edtPassword.getText().toString());
            mLoadHandler.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                Intent intent = new Intent(RegisterActivity.this, ActivationActivity.class);
                startActivity(intent);
                Toast.makeText(RegisterActivity.this, "Sign up Success! Please check the email to activate the account.", Toast.LENGTH_SHORT).show();
            }
            else if (msg.what == 0) {
                lnEmail.setBackground(getResources().getDrawable(R.drawable.button_empty_red_back));
                imgEmailCheck.setImageDrawable(getResources().getDrawable(R.drawable.failmark));
                Toast.makeText(RegisterActivity.this, "The user with same email is already existed. Please input the correct email.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };
}
