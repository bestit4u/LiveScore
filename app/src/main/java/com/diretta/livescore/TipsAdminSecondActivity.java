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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.diretta.livescore.common.Common;
import com.diretta.livescore.model.GameListInfo;
import com.diretta.livescore.net.NetworkManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by suc on 5/15/2018.
 */

public class TipsAdminSecondActivity extends Activity {
    String mLeagueName = "";
    ProgressDialog mProgDlg;
    LinearLayout lnContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_admin_second);
        mProgDlg = new ProgressDialog(TipsAdminSecondActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);
        mLeagueName = getIntent().getStringExtra("league_name");
        lnContainer = (LinearLayout)findViewById(R.id.lnContainer);
        loadData();
    }
    private void loadData(){
        mProgDlg.show();
        new Thread(mLoadRunnable).start();
    }
    private Runnable mLoadRunnable = new Runnable() {
        @Override
        public void run() {
            Common.getInstance().arrDateList.clear();
            Common.getInstance().arrGameListInfo.clear();
            int ret = NetworkManager.getManager().gameList(mLeagueName);
            mLoadHandler.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                bindData();
            }
            else if (msg.what == 0) {
                Toast.makeText(TipsAdminSecondActivity.this, "There is no data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TipsAdminSecondActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };
    public String getMonth(String month){
        switch (month){
            case "01":
                return "Jan";
            case "02":
                return "Feb";
            case "03":
                return "Mar";
            case "04":
                return "Apr";
            case "05":
                return "May";
            case "06":
                return "Jun";
            case "07":
                return "Jul";
            case "08":
                return "Aug";
            case "09":
                return "Sep";
            case "10":
                return "Oct";
            case "11":
                return "Nov";
            case "12":
                return "Dec";
        }
        return "";
    }
    public String getDateDay(String date) throws Exception {
        String[] arrDate = date.split("-");
        String day = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date nDate = dateFormat.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                day = "Sun";
                break;
            case 2:
                day = "Mon";
                break;
            case 3:
                day = "Tue";
                break;
            case 4:
                day = "Wed";
                break;
            case 5:
                day = "Thu";
                break;
            case 6:
                day = "Fri";
                break;
            case 7:
                day = "Sat";
                break;

        }
        return day;
    }
    private void bindData(){
        lnContainer.removeAllViews();
        for (int i = 0; i < Common.getInstance().arrDateList.size(); i++) {
            final View v1 = LayoutInflater.from(TipsAdminSecondActivity.this).inflate(R.layout.layout_league, null);
            v1.setTag(Common.getInstance().arrDateList.get(i));
            String[] arrDate = Common.getInstance().arrDateList.get(i).split("/");
            String newDate = "20" + arrDate[2] + "-" + arrDate[1] + "-" + arrDate[0];
            String strToday = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            String strSequence = "";
            try{
                strSequence = getDateDay(newDate);
            }catch (Exception e){

            }
            String strMonth = getMonth(arrDate[1]);
            ImageView imgBack = (ImageView)v1.findViewById(R.id.imgBack);

            if(i == 0) {
                imgBack.setVisibility(View.VISIBLE);
                imgBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                if(strToday.equals(newDate)) {
                    ((TextView) v1.findViewById(R.id.txtLeagueName)).setText(mLeagueName + " - Today");
                }else{
                    ((TextView) v1.findViewById(R.id.txtLeagueName)).setText(mLeagueName + " - " + strSequence + " " + arrDate[0] + " " + strMonth);
                }
            }else{
                imgBack.setVisibility(View.GONE);
                ((TextView) v1.findViewById(R.id.txtLeagueName)).setText(mLeagueName + " - " + strSequence + " " + arrDate[0] + " " + strMonth);
            }
            LinearLayout lnMatchContainer = (LinearLayout) v1.findViewById(R.id.lnMatchContainer);
            lnMatchContainer.removeAllViews();

            ArrayList<GameListInfo> arrMatches = new ArrayList<GameListInfo>();
            for (int j = 0; j < Common.getInstance().arrGameListInfo.size(); j++) {
                final int index = j;
                String[] arrStart = Common.getInstance().arrGameListInfo.get(j).start_date_time.split(" ");
                if (arrStart[0].equals(Common.getInstance().arrDateList.get(i))) {
                    final View v2 = LayoutInflater.from(TipsAdminSecondActivity.this).inflate(R.layout.popular_item, null);
                    v2.setTag(Common.getInstance().arrGameListInfo.get(j));
                    ImageView imgHome = (ImageView)v2.findViewById(R.id.imgHome);
                    ImageLoader.getInstance().displayImage(Common.getInstance().arrGameListInfo.get(j).home_image, imgHome, Common.getInstance().goodsDisplayImageOptions);
                    TextView txtHomeName = (TextView)v2.findViewById(R.id.txtHomeName);
                    txtHomeName.setText(Common.getInstance().arrGameListInfo.get(j).home);
                    ImageView imgAway = (ImageView)v2.findViewById(R.id.imgAway);
                    ImageLoader.getInstance().displayImage(Common.getInstance().arrGameListInfo.get(j).away_image, imgAway, Common.getInstance().goodsDisplayImageOptions);
                    TextView txtAwayName = (TextView)v2.findViewById(R.id.txtAwayName);
                    txtAwayName.setText(Common.getInstance().arrGameListInfo.get(j).away);
                    TextView txtStartTime = (TextView)v2.findViewById(R.id.txtStartTime);
                    txtStartTime.setText(Common.getInstance().arrGameListInfo.get(j).time);
                    TextView txtHomeOdd = (TextView)v2.findViewById(R.id.txtHomeOdd);
                    txtHomeOdd.setText(Common.getInstance().arrGameListInfo.get(j).home_odd);
                    TextView txtDrawOdd = (TextView)v2.findViewById(R.id.txtDrawOdd);
                    txtDrawOdd.setText(Common.getInstance().arrGameListInfo.get(j).draw_odd);
                    TextView txtAwayOdd = (TextView)v2.findViewById(R.id.txtAwayOdd);
                    txtAwayOdd.setText(Common.getInstance().arrGameListInfo.get(j).away_odd);
                    LinearLayout lnItem = (LinearLayout) v2.findViewById(R.id.lnItem);
                    lnItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TipsAdminSecondActivity.this, TipsAdminThirdActivity.class);
                            intent.putExtra("event_id", Common.getInstance().arrGameListInfo.get(index).event_id);
                            startActivity(intent);
                            TipsAdminSecondActivity.this.finish();
                        }
                    });
                    lnMatchContainer.addView(v2);
                }
            }
            lnContainer.addView(v1);
        }
    }
}
