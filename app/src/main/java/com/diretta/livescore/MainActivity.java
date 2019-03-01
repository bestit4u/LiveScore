package com.diretta.livescore;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.diretta.livescore.common.Common;
import com.diretta.livescore.net.NetworkManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends Activity {
        Button btnSignUp, btnSignIn;
        private static final String[] INITIAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        private CallbackManager callbackManager;

        LoginButton facebook_login;
        ProgressDialog mProgDlg;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                setContentView(R.layout.activity_main);
                mProgDlg = new ProgressDialog(MainActivity.this);
                mProgDlg.setMessage("Wait a little!");
                mProgDlg.setCancelable(false);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(INITIAL_PERMS, 1337);
                }
                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MainActivity.this));

                Common.getInstance().goodsDisplayImageOptions = new DisplayImageOptions.Builder()
                        .cacheOnDisk(true)
                        .showImageOnLoading(0)
                        .build();
                OneSignal.startInit(this).init();
                OneSignal.startInit(this).setNotificationOpenedHandler(new OneSignal.NotificationOpenedHandler() {
                        @Override
                        public void notificationOpened(OSNotificationOpenResult result) {

                String launchURL = result.notification.payload.launchURL;
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("url", launchURL);
                startActivity(intent);
                /*
                if (launchURL != null) {
                    Log.d("Debug", "Launch URL: " + launchURL);
                    if(launchURL.contains("home")){

                    }else if(launchURL.contains("accumulator")){
                        Intent intent = new Intent(getApplicationContext(), PickScreenActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("url", launchURL);
                        startActivity(intent);
                    }
                }
                */
                }
        }).init();
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(intent);
                }
        });

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                }
        });
        //getHashKey();
        facebook_login = (LoginButton) findViewById(R.id.login_button);
        facebook_login.setVisibility(View.GONE);
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn == true){
                LoginManager.getInstance().logOut();
        }
        LoginButton facebook_login = (LoginButton) findViewById(R.id.login_button);

        facebook_login.setReadPermissions("email");
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                                // App code
                                Log.d("TAG", "onSucces LoginResult=" + loginResult);
                                requestUserProfile(loginResult);
                        }

                        @Override
                        public void onCancel() {
                                // App code
                                Toast.makeText(MainActivity.this, "Cancelled..", Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "onCancel");
                        }

                        @Override
                        public void onError(FacebookException exception) {
                                // App code
                                Log.d("TAG", "onError");
                                Toast.makeText(MainActivity.this, "Network Error..", Toast.LENGTH_SHORT).show();
                        }
                });

        }

        private void getHashKey() {
                try {
                        PackageInfo info = getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
                        for (Signature signature : info.signatures) {
                                MessageDigest md = MessageDigest.getInstance("SHA");
                                md.update(signature.toByteArray());
                                Log.d("TAG", "key_hash=" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
                        }
                } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                }
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        String id = "";
        String full_name = "";
        String email = "";
        String sub_url = "";
        public void requestUserProfile(LoginResult loginResult){
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                        try {
                                                if(Profile.getCurrentProfile() == null){
                                                        LoginManager.getInstance().logOut();
                                                        Toast.makeText(MainActivity.this, "Network Error! Please try to login with facebook later", Toast.LENGTH_SHORT).show();
                                                        return;
                                                }
                                                id = Profile.getCurrentProfile().getId();
                                                full_name = Profile.getCurrentProfile().getName();
                                                email = response.getJSONObject().getString("email").toString();
                                                String url = Profile.getCurrentProfile().getProfilePictureUri(100, 100).toString();
                                                String[] arr = url.split("&");
                                                sub_url = arr[arr.length - 1];

                                                mProgDlg.show();
                                                new Thread(mLoadRunnable).start();
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                        }

                                }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email");
                request.setParameters(parameters);
                request.executeAsync();
        }
        private Runnable mLoadRunnable = new Runnable() {
                @Override
                public void run() {
                        int ret = NetworkManager.getManager().socialFacebook(full_name, email, sub_url, id);
                        mLoadHandler.sendEmptyMessage(ret);
                }
        };
        private Handler mLoadHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                        mProgDlg.hide();
                        if (msg.what == 1) {
                                if (Common.getInstance().newSocial.equals("1")){
                                        Intent intent = new Intent(MainActivity.this, GuideActivity.class);
                                        startActivity(intent);
                                }else{
                                        Intent intent_home = new Intent(MainActivity.this, FeedActivity.class);
                                        intent_home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent_home);
                                }
                        } else {
                                Toast.makeText(MainActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
                        }

                }
        };
}
