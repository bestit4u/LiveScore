package com.diretta.livescore;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.diretta.livescore.adapter.PremiumGuidePagerAdapter;
import com.diretta.livescore.common.Common;
import com.diretta.livescore.util.IabHelper;
import com.diretta.livescore.util.IabResult;
import com.diretta.livescore.util.Inventory;
import com.diretta.livescore.util.Purchase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * Created by suc on 4/25/2018.
 */

public class PremiumSubscriptionActivity extends FragmentActivity {
    Button btnBack;
    ViewPager viewPager;
    PremiumGuidePagerAdapter mAdapter;
    ImageView imgCircle1, imgCircle2, imgCircle3, imgCircle4;
    LinearLayout ln1Month, ln3Month;
    IInAppBillingService mService;
    String mPublic_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtCLb0FaTJTWHcID598fmSSYGNlA0CkA2P3MYjDCczgGJbYKYDIrZWHXZ00PqOeOR/ozrjihgy0Y7dsws6o47VqVCv598VGihIbb13WNE6Yy/P+N7Qm+T3bBSLs9VQo7YYBKesH00K1drk7tdF2m+eHh7mm1YLN+X0lqbcYkR+IVdjrbGhdHHbdK87VXXHN0WIpRzxRzZuBirek3R3x86nvkPIkSt1XeddZDZ1XmAeplEf0wRp7htSRJdshDj6+HBUbaBNxi74HMNZaXzuVRoUgSoNAXFz/ZDKtpXlwsmGHBT/bsWkdZ/9usDrMMy3lIqOFrSidudbQrnlKyzwVOOaQIDAQAB";
    IabHelper mHelper;
    TextView txtPrivacy;
    TextView txtTerms;
    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_subscription);
        txtPrivacy = (TextView)findViewById(R.id.txtPrivacy);
        txtPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language = Locale.getDefault().getLanguage();
                if(language.equals("da")){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://tipyaapp.com/DA/privatlivspolitik.html"));
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://tipyaapp.com/EN/privacypolicy.html"));
                    startActivity(intent);
                }

            }
        });
        txtTerms = (TextView)findViewById(R.id.txtTerms);
        txtTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language = Locale.getDefault().getLanguage();
                if(language.equals("da")){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://tipyaapp.com/DA/aftalevilk%C3%A5rbetingelser.html"));
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://tipyaapp.com/EN/termsofagreementbusinessconditions.html"));
                    startActivity(intent);
                }

            }
        });
        ln1Month = (LinearLayout)findViewById(R.id.ln1Month);
        ln1Month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InAppBuyItem_U("1month");

            }
        });

        ln3Month = (LinearLayout)findViewById(R.id.ln3Month);
        ln3Month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InAppBuyItem_U("3month");

            }
        });
        btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgCircle1 = (ImageView)findViewById(R.id.imgCircle1);
        imgCircle2 = (ImageView)findViewById(R.id.imgCircle2);
        imgCircle3 = (ImageView)findViewById(R.id.imgCircle3);
        imgCircle4 = (ImageView)findViewById(R.id.imgCircle4);
        imgCircle1.setImageDrawable(getResources().getDrawable(R.drawable.ic_white_circle));
        imgCircle2.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_circle));
        imgCircle3.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_circle));
        imgCircle4.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_circle));

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        mAdapter = new PremiumGuidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                imgCircle1.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_circle));
                imgCircle2.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_circle));
                imgCircle3.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_circle));
                imgCircle4.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_circle));

                switch (position){
                    case 0:
                        imgCircle1.setImageDrawable(getResources().getDrawable(R.drawable.ic_white_circle));
                        break;
                    case 1:
                        imgCircle2.setImageDrawable(getResources().getDrawable(R.drawable.ic_white_circle));
                        break;
                    case 2:
                        imgCircle3.setImageDrawable(getResources().getDrawable(R.drawable.ic_white_circle));
                        break;
                    case 3:
                        imgCircle4.setImageDrawable(getResources().getDrawable(R.drawable.ic_white_circle));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
        //loadData();
        InAppInit_U(mPublic_key, true);
    }
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

                }catch (IabHelper.IabAsyncInProgressException e){

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
            if(inappList.size() >0 ){
                Common.getInstance().free = false;
            }else
                Common.getInstance().free = true;
        }
    };
    public void InAppBuyItem_U(final String strItemId) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                /*
                 * TODO: for security, generate your payload here for
                 * verification. See the comments on verifyDeveloperPayload()
                 * for more info. Since this is a SAMPLE, we just use an empty
                 * string, but on a production app you should carefully generate
                 * this.
                 */
                String payload = "user_id";
                try {
                    mHelper.launchSubscriptionPurchaseFlow(PremiumSubscriptionActivity.this, strItemId,
                            1001, mPurchaseFinishedListener, payload);
                    //mHelper.launchPurchaseFlow(UpgradeActivity.this, strItemId,
                    //      1001, mPurchaseFinishedListener, payload);
                }catch (IabHelper.IabAsyncInProgressException e){

                }

                Log.d("myLog", "InAppBuyItem_U " + strItemId);
            }
        });
    }
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d("myLog", "Purchase finished: " + result + ", purchase: "
                    + purchase);  //결제 완료 되었을때 각종 결제 정보들을 볼 수 있습니다. 이정보들을 서버에 저장해 놓아야 결제 취소시 변경된 정보를 관리 할 수 있을것 같습니다~

            if (purchase != null) {
                if (!verifyDeveloperPayload(purchase)) {
                    Log.d("myLog",
                            "Error purchasing. Authenticity verification failed.");
                }
                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                }catch (IabHelper.IabAsyncInProgressException e){

                }
            } else {
            }
        }
    };
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        return true;
    }

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d("myLog", "Consumption finished. Purchase: " + purchase
                    + ", result: " + result);
            SendConsumeResult(purchase, result);
        }
    };

    protected void SendConsumeResult(Purchase purchase, IabResult result) {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("Result", result.getResponse());
            if (purchase != null) {
                jsonObj.put("OrderId", purchase.getOrderId());
                jsonObj.put("Sku", purchase.getSku());
                jsonObj.put("purchaseData", purchase.getOriginalJson());
                jsonObj.put("signature", purchase.getSignature());

                Log.d("myLog", "OrderId"  + purchase.getOrderId());
                Log.d("myLog", "Sku"  + purchase.getSku());
                Log.d("myLog", "purchaseData"  + purchase.getOriginalJson());
                Log.d("myLog", "signature"  + purchase.getSignature());
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("myLog", "onActivityResult(" + requestCode + "," + resultCode
                + "," + data);
        if (requestCode == 1001) {
            // Pass on the activity result to the helper for handling
            if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
                // not handled, so handle it ourselves (here's where you'd
                // perform any handling of activity results not related to
                // in-app
                // billing...
                super.onActivityResult(requestCode, resultCode, data);
            } else {
                Log.d("myLog", "onActivityResult handled by IABUtil.");
            }
        }
        InAppInit_U(mPublic_key, true);
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }

}
