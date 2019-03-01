package com.diretta.livescore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.diretta.livescore.common.Common;
import com.diretta.livescore.model.LiveGameListInfo;
import com.diretta.livescore.model.LivescoreEventInfo;
import com.diretta.livescore.net.NetworkManager;

/**
 * Created by suc on 7/10/2018.
 */

public class LivescoreDetailActivity extends Activity {
    ImageView imgBack;
    TextView txtLeagueName, txtStartTime, txtStadium, txtHomeName, txtStatus, txtAwayName, txtHomeOdd, txtDrawOdd, txtAwayOdd;
    ImageView imgHome, imgAway;
    LinearLayout lnEventContainer;
    ProgressDialog mProgDlg;
    String mStatic_id = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livescore_detail);
        mProgDlg = new ProgressDialog(this);
        mProgDlg.setCancelable(false);
        mProgDlg.setMessage("Wait a little!");
        final LiveGameListInfo info   =  (LiveGameListInfo) getIntent().getSerializableExtra("detail");
        mStatic_id = info.static_id;
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        txtLeagueName = (TextView)findViewById(R.id.txtLeagueName);
        txtLeagueName.setText(getIntent().getStringExtra("league_name"));
        txtStartTime = (TextView)findViewById(R.id.txtStartTime);
        txtStartTime.setText(getIntent().getStringExtra("start_time"));
        txtStadium = (TextView)findViewById(R.id.txtStadium);
        if(!info.stadium.equals("")){
            txtStadium.setText(info.stadium);
        }else{
            String[] arrLeague = getIntent().getStringExtra("league_name").split(" ");
            txtStadium.setText(arrLeague[0]);
        }
        imgHome = (ImageView)findViewById(R.id.imgHome);
        ImageLoader.getInstance().displayImage(info.home_image, imgHome, Common.getInstance().goodsDisplayImageOptions);

        txtHomeName = (TextView)findViewById(R.id.txtHomeName);
        txtHomeName.setText(info.home);
        txtStatus = (TextView)findViewById(R.id.txtStatus);
        if(info.status.contains(":")){
            txtStatus.setText("-");
        }else if(info.status.equals("FT")){
            txtStatus.setText(info.score);
        }else{
            txtStatus.setText(info.status + "'");
        }

        //txtStatus.setText(info.status);
        imgAway = (ImageView)findViewById(R.id.imgAway);
        ImageLoader.getInstance().displayImage(info.away_image, imgAway, Common.getInstance().goodsDisplayImageOptions);
        txtAwayName = (TextView)findViewById(R.id.txtAwayName);
        txtAwayName.setText(info.away);
        txtHomeOdd = (TextView)findViewById(R.id.txtHomeOdd);
        txtHomeOdd.setText(info.home_odd);
        txtDrawOdd = (TextView)findViewById(R.id.txtDrawOdd);
        txtDrawOdd.setText(info.draw_odd);
        txtAwayOdd = (TextView)findViewById(R.id.txtAwayOdd);
        txtAwayOdd.setText(info.away_odd);
        lnEventContainer = (LinearLayout)findViewById(R.id.lnEventContainer);
        lnEventContainer.removeAllViews();
        LinearLayout lnOdd = (LinearLayout)findViewById(R.id.lnOdd);
        lnOdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LivescoreDetailActivity.this, GameDetailActivity.class);
                intent.putExtra("event_id", info.event_id);
                startActivity(intent);
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
            Common.getInstance().arrLiveEventInfo.clear();
            int ret = NetworkManager.getManager().loadLivescoreDetail(mStatic_id);
            mLoadHandler.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if(msg.what == 1) {
                bindData();
            }else if(msg.what == 0) {
                Toast.makeText(LivescoreDetailActivity.this, "There is no data about the selected date", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(LivescoreDetailActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };
    private void bindData(){
        lnEventContainer.removeAllViews();
        for(int i = 0; i < Common.getInstance().arrLiveEventInfo.size(); i++) {
            final LivescoreEventInfo info = Common.getInstance().arrLiveEventInfo.get(i);

            View event_item = LayoutInflater.from(LivescoreDetailActivity.this).inflate(R.layout.livescore_event_item, null);

            if (info.event_team.equals("home")) {
                ((LinearLayout) event_item.findViewById(R.id.lnAwayPart)).setVisibility(View.GONE);
                ((ImageView) event_item.findViewById(R.id.imgAwayEvent)).setVisibility(View.GONE);

                switch (info.type) {
                    case "goal":
                        ((ImageView) event_item.findViewById(R.id.imgHomeEvent)).setBackgroundResource(R.drawable.goal);
                        ((TextView) event_item.findViewById(R.id.txtHomeTimeStyle)).setText(info.event_min + "'" + " " + getResources().getString(R.string.goal_by));
                        ((TextView) event_item.findViewById(R.id.txtHomePlayer1)).setText(info.event_player);
                        ((TextView) event_item.findViewById(R.id.txtHomePlayer2)).setVisibility(View.GONE);
                        break;
                    case "yellowcard":
                        ((ImageView) event_item.findViewById(R.id.imgHomeEvent)).setBackgroundResource(R.drawable.yellow);
                        ((TextView) event_item.findViewById(R.id.txtHomeTimeStyle)).setText(info.event_min + "'" + " " + getResources().getString(R.string.yellow_card));
                        ((TextView) event_item.findViewById(R.id.txtHomePlayer1)).setText(info.event_player);
                        ((TextView) event_item.findViewById(R.id.txtHomePlayer2)).setVisibility(View.GONE);
                        break;
                    case "yellowred":
                        ((ImageView) event_item.findViewById(R.id.imgHomeEvent)).setBackgroundResource(R.drawable.yellow_red);
                        ((TextView) event_item.findViewById(R.id.txtHomeTimeStyle)).setText(info.event_min + "'" + " " + getResources().getString(R.string.yellow_red));
                        ((TextView) event_item.findViewById(R.id.txtHomePlayer1)).setText(info.event_player);
                        ((TextView) event_item.findViewById(R.id.txtHomePlayer2)).setVisibility(View.GONE);
                        break;
                    case "redcard":
                        ((ImageView) event_item.findViewById(R.id.imgHomeEvent)).setBackgroundResource(R.drawable.red);
                        ((TextView) event_item.findViewById(R.id.txtHomeTimeStyle)).setText(info.event_min + "'" + " " + getResources().getString(R.string.red_card));
                        ((TextView) event_item.findViewById(R.id.txtHomePlayer1)).setText(info.event_player);
                        ((TextView) event_item.findViewById(R.id.txtHomePlayer2)).setVisibility(View.GONE);
                        break;
                    case "subst":
                        ((ImageView) event_item.findViewById(R.id.imgHomeEvent)).setBackgroundResource(R.drawable.substitution);
                        ((TextView) event_item.findViewById(R.id.txtHomeTimeStyle)).setText(info.event_min + "'" + " " + getResources().getString(R.string.substitution));
                        ((TextView) event_item.findViewById(R.id.txtHomePlayer1)).setText(info.event_player);
                        ((TextView)event_item.findViewById(R.id.txtHomePlayer2)).setText(info.event_assist);
                        break;
                }
                lnEventContainer.addView(event_item);
            } else {
                ((LinearLayout) event_item.findViewById(R.id.lnHomePart)).setVisibility(View.GONE);
                ((ImageView) event_item.findViewById(R.id.imgHomeEvent)).setVisibility(View.GONE);

                switch (info.type) {
                    case "goal":
                        ((ImageView) event_item.findViewById(R.id.imgAwayEvent)).setBackgroundResource(R.drawable.goal);
                        ((TextView) event_item.findViewById(R.id.txtAwayTimeStyle)).setText(info.event_min + "'" + " " + getResources().getString(R.string.goal_by));
                        ((TextView) event_item.findViewById(R.id.txtAwayPlayer1)).setText(info.event_player);
                        ((TextView) event_item.findViewById(R.id.txtAwayPlayer2)).setVisibility(View.GONE);
                        break;
                    case "yellowcard":
                        ((ImageView) event_item.findViewById(R.id.imgAwayEvent)).setBackgroundResource(R.drawable.yellow);
                        ((TextView) event_item.findViewById(R.id.txtAwayTimeStyle)).setText(info.event_min + "'" + " " + getResources().getString(R.string.yellow_card));
                        ((TextView) event_item.findViewById(R.id.txtAwayPlayer1)).setText(info.event_player);
                        ((TextView) event_item.findViewById(R.id.txtAwayPlayer2)).setVisibility(View.GONE);
                        break;
                    case "yellowred":
                        ((ImageView) event_item.findViewById(R.id.imgAwayEvent)).setBackgroundResource(R.drawable.yellow_red);
                        ((TextView) event_item.findViewById(R.id.txtAwayTimeStyle)).setText(info.event_min + "'" + " " + getResources().getString(R.string.yellow_red));
                        ((TextView) event_item.findViewById(R.id.txtAwayPlayer1)).setText(info.event_player);
                        ((TextView) event_item.findViewById(R.id.txtAwayPlayer2)).setVisibility(View.GONE);
                        break;
                    case "redcard":
                        ((ImageView) event_item.findViewById(R.id.imgAwayEvent)).setBackgroundResource(R.drawable.red);
                        ((TextView) event_item.findViewById(R.id.txtAwayTimeStyle)).setText(info.event_min + "'" + " " + getResources().getString(R.string.red_card));
                        ((TextView) event_item.findViewById(R.id.txtAwayPlayer1)).setText(info.event_player);
                        ((TextView) event_item.findViewById(R.id.txtAwayPlayer2)).setVisibility(View.GONE);
                        break;
                    case "subst":
                        ((ImageView) event_item.findViewById(R.id.imgAwayEvent)).setBackgroundResource(R.drawable.substitution);
                        ((TextView) event_item.findViewById(R.id.txtAwayTimeStyle)).setText(info.event_min + "'" + " " + getResources().getString(R.string.substitution));
                        ((TextView) event_item.findViewById(R.id.txtAwayPlayer1)).setText(info.event_player);
                        ((TextView)event_item.findViewById(R.id.txtAwayPlayer2)).setText(info.event_assist);
                        break;
                }
                lnEventContainer.addView(event_item);
            }
        }
    }
}
