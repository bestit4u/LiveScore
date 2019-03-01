package com.diretta.livescore;

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
import com.diretta.livescore.common.BaseActivity;
import com.diretta.livescore.common.Common;
import com.diretta.livescore.net.NetworkManager;

import org.json.JSONException;

/**
 * Created by suc on 4/25/2018.
 */

public class RankActivity extends BaseActivity {
    RelativeLayout RnThisMonth, RnLastMonth;
    TextView txtThisMonth, txtLastMonth;
    View viewThisMonthBorder, viewLastMonthBorder;
    LinearLayout lnContainer;
    ProgressDialog mProgDlg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        mProgDlg = new ProgressDialog(RankActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);

        RnThisMonth = (RelativeLayout) findViewById(R.id.RnThisMonth);
        RnThisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonthPart(0);
                lnContainer.removeAllViews();
                for (int i = 0; i < Common.getInstance().arrThisMonthRank.size(); i++) {
                    final int index = i;

                    final View v2 = LayoutInflater.from(RankActivity.this).inflate(R.layout.rank_item, null);
                    LinearLayout lnItem = (LinearLayout) v2.findViewById(R.id.lnItem);
                    if (Common.getInstance().user_id.equals(Common.getInstance().arrThisMonthRank.get(i).user_id)) {
                        lnItem.setBackgroundColor(getResources().getColor(R.color.clr_green_rank));
                    } else {
                        lnItem.setBackgroundColor(getResources().getColor(R.color.clr_white));
                    }
                    TextView txtPosition = (TextView) v2.findViewById(R.id.txtPosition);
                    txtPosition.setText("" + Common.getInstance().arrThisMonthRank.get(i).position);
                    TextView txtName = (TextView) v2.findViewById(R.id.txtName);
                    txtName.setText(Common.getInstance().arrThisMonthRank.get(i).name);
                    ImageView imgProfile = (ImageView) v2.findViewById(R.id.imgProfile);
                    ImageLoader.getInstance().displayImage(Common.getInstance().arrThisMonthRank.get(i).profile_img, imgProfile, Common.getInstance().goodsDisplayImageOptions);
                    imgProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(RankActivity.this, UserProfileActivity.class);
                            intent.putExtra("user_id", Common.getInstance().arrThisMonthRank.get(index).user_id);
                            startActivity(intent);
                        }
                    });
                    TextView txtWinnerCount = (TextView) v2.findViewById(R.id.txtWinnerCount);
                    int winnerCount = Integer.parseInt(Common.getInstance().arrThisMonthRank.get(i).placed_tip_count) - Integer.parseInt(Common.getInstance().arrThisMonthRank.get(i).loses_count);
                    txtWinnerCount.setText("" + winnerCount);
                    TextView txtLosesCount = (TextView) v2.findViewById(R.id.txtLosesCount);
                    txtLosesCount.setText(Common.getInstance().arrThisMonthRank.get(i).loses_count);
                    TextView txtPercent = (TextView) v2.findViewById(R.id.txtPercent);
                    txtPercent.setText(Common.getInstance().arrThisMonthRank.get(i).percent + "%");
                    TextView txtPoint = (TextView) v2.findViewById(R.id.txtPoint);
                    txtPoint.setText(Common.getInstance().arrThisMonthRank.get(i).point);
                    lnContainer.addView(v2);
                }
            }
        });

        RnLastMonth = (RelativeLayout) findViewById(R.id.RnLastMonth);
        RnLastMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonthPart(1);
                lnContainer.removeAllViews();
                for (int i = 0; i < Common.getInstance().arrLastMonthRank.size(); i++) {
                    final int index = i;
                    final View v2 = LayoutInflater.from(RankActivity.this).inflate(R.layout.rank_item, null);
                    LinearLayout lnItem = (LinearLayout) v2.findViewById(R.id.lnItem);
                    if (Common.getInstance().user_id.equals(Common.getInstance().arrLastMonthRank.get(i).user_id)) {
                        lnItem.setBackgroundColor(getResources().getColor(R.color.clr_green_rank));
                    } else {
                        lnItem.setBackgroundColor(getResources().getColor(R.color.clr_white));
                    }
                    TextView txtPosition = (TextView) v2.findViewById(R.id.txtPosition);
                    txtPosition.setText("" + Common.getInstance().arrLastMonthRank.get(i).position);
                    TextView txtName = (TextView) v2.findViewById(R.id.txtName);
                    txtName.setText(Common.getInstance().arrLastMonthRank.get(i).name);
                    ImageView imgProfile = (ImageView) v2.findViewById(R.id.imgProfile);
                    ImageLoader.getInstance().displayImage(Common.getInstance().arrLastMonthRank.get(i).profile_img, imgProfile, Common.getInstance().goodsDisplayImageOptions);
                    imgProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(RankActivity.this, UserProfileActivity.class);
                            intent.putExtra("user_id", Common.getInstance().arrLastMonthRank.get(index).user_id);
                            startActivity(intent);
                        }
                    });
                    TextView txtWinnerCount = (TextView) v2.findViewById(R.id.txtWinnerCount);
                    int winnerCount = Integer.parseInt(Common.getInstance().arrLastMonthRank.get(i).placed_tip_count) - Integer.parseInt(Common.getInstance().arrLastMonthRank.get(i).loses_count);
                    txtWinnerCount.setText("" + winnerCount);
                    TextView txtLosesCount = (TextView) v2.findViewById(R.id.txtLosesCount);
                    txtLosesCount.setText(Common.getInstance().arrLastMonthRank.get(i).loses_count);
                    TextView txtPercent = (TextView) v2.findViewById(R.id.txtPercent);
                    txtPercent.setText(Common.getInstance().arrLastMonthRank.get(i).percent + "%");
                    TextView txtPoint = (TextView) v2.findViewById(R.id.txtPoint);
                    txtPoint.setText(Common.getInstance().arrLastMonthRank.get(i).point);
                    lnContainer.addView(v2);
                }
            }
        });
        lnContainer = (LinearLayout) findViewById(R.id.lnContainer);
        txtThisMonth = (TextView) findViewById(R.id.txtThisMonth);
        txtLastMonth = (TextView) findViewById(R.id.txtLastMonth);
        viewThisMonthBorder = (View) findViewById(R.id.viewThisMonthBorder);
        viewLastMonthBorder = (View) findViewById(R.id.viewLastMonthBorder);
    }

    private void loadData() {
        mProgDlg.show();
        new Thread(mLoadRunnable).start();
    }

    private Runnable mLoadRunnable = new Runnable() {
        @Override
        public void run() {
            Common.getInstance().arrThisMonthRank.clear();
            Common.getInstance().arrLastMonthRank.clear();
            int ret = NetworkManager.getManager().rank(Common.getInstance().user_id);
            mLoadHandler.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                bindData();
            } else if (msg.what == 0) {
                Toast.makeText(RankActivity.this, "There is no data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RankActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };

    private void selectMonthPart(int index) {
        txtThisMonth.setTextColor(getResources().getColor(R.color.clr_grey));
        txtLastMonth.setTextColor(getResources().getColor(R.color.clr_grey));

        viewThisMonthBorder.setBackgroundColor(getResources().getColor(R.color.clr_white));
        viewLastMonthBorder.setBackgroundColor(getResources().getColor(R.color.clr_white));

        if (index == 0) {
            txtThisMonth.setTextColor(getResources().getColor(R.color.clr_green));
            viewThisMonthBorder.setBackgroundColor(getResources().getColor(R.color.clr_green));
        } else {
            txtLastMonth.setTextColor(getResources().getColor(R.color.clr_green));
            viewLastMonthBorder.setBackgroundColor(getResources().getColor(R.color.clr_green));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initActionBar(3);
        selectMonthPart(0);
        loadData();
    }

    private void bindData() {
        lnContainer.removeAllViews();
        for (int i = 0; i < Common.getInstance().arrThisMonthRank.size(); i++) {
            final int index = i;
            final View v2 = LayoutInflater.from(RankActivity.this).inflate(R.layout.rank_item, null);
            LinearLayout lnItem = (LinearLayout) v2.findViewById(R.id.lnItem);
            if (Common.getInstance().user_id.equals(Common.getInstance().arrThisMonthRank.get(i).user_id)) {
                lnItem.setBackgroundColor(getResources().getColor(R.color.clr_green_rank));
            } else {
                lnItem.setBackgroundColor(getResources().getColor(R.color.clr_white));
            }
            TextView txtPosition = (TextView) v2.findViewById(R.id.txtPosition);
            txtPosition.setText("" + Common.getInstance().arrThisMonthRank.get(i).position);
            TextView txtName = (TextView) v2.findViewById(R.id.txtName);
            txtName.setText(Common.getInstance().arrThisMonthRank.get(i).name);
            ImageView imgProfile = (ImageView) v2.findViewById(R.id.imgProfile);
            ImageLoader.getInstance().displayImage(Common.getInstance().arrThisMonthRank.get(i).profile_img, imgProfile, Common.getInstance().goodsDisplayImageOptions);
            imgProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RankActivity.this, UserProfileActivity.class);
                    intent.putExtra("user_id", Common.getInstance().arrThisMonthRank.get(index).user_id);
                    startActivity(intent);
                }
            });
            TextView txtWinnerCount = (TextView) v2.findViewById(R.id.txtWinnerCount);
            int winnerCount = Integer.parseInt(Common.getInstance().arrThisMonthRank.get(i).placed_tip_count) - Integer.parseInt(Common.getInstance().arrThisMonthRank.get(i).loses_count);
            txtWinnerCount.setText("" + winnerCount);
            TextView txtLosesCount = (TextView) v2.findViewById(R.id.txtLosesCount);
            txtLosesCount.setText(Common.getInstance().arrThisMonthRank.get(i).loses_count);
            TextView txtPercent = (TextView) v2.findViewById(R.id.txtPercent);
            txtPercent.setText(Common.getInstance().arrThisMonthRank.get(i).percent + "%");
            TextView txtPoint = (TextView) v2.findViewById(R.id.txtPoint);
            txtPoint.setText(Common.getInstance().arrThisMonthRank.get(i).point);
            lnContainer.addView(v2);
        }

    }
}
