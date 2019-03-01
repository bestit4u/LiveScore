package com.diretta.livescore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.diretta.livescore.common.Common;
import com.diretta.livescore.model.PlacedTipInfo;

/**
 * Created by suc on 4/26/2018.
 */

public class ExpertAdmin extends Activity {
    ImageView imgBack;
    Button btnCreateTipOfDay;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_admin);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnCreateTipOfDay = (Button)findViewById(R.id.btnCreateTipOfDay);
        Common.getInstance().mTip = "0";
        Common.getInstance().mTipDictionary = new PlacedTipInfo();

        btnCreateTipOfDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpertAdmin.this, CreateTipOfDayActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.getInstance().mTip = "0";
        Common.getInstance().mTipDictionary = new PlacedTipInfo();
    }
}
