package com.diretta.livescore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.diretta.livescore.common.Common;
import com.diretta.livescore.model.AlternativeTotalGoalInfo;
import com.diretta.livescore.model.CorrectScoreInfo;
import com.diretta.livescore.model.TeamTotalGoalInfo;
import com.diretta.livescore.net.NetworkManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by suc on 5/15/2018.
 */

public class GameDetailActivity extends Activity {
    String mEvent_id = "";
    TextView txtHomeName, txtAwayName, txtStartTime, txtLeagueName;
    ImageView imgHome, imgAway;
    ProgressDialog mProgDlg;
    LinearLayout lnContainer;
    ArrayList<CorrectScoreInfo> mArrHomescores = new ArrayList<CorrectScoreInfo>();
    ArrayList<CorrectScoreInfo> mArrDrawScores = new ArrayList<CorrectScoreInfo>();
    ArrayList<CorrectScoreInfo> mArrAwayScores = new ArrayList<CorrectScoreInfo>();
    private RelativeLayout RnFeed, RnTip, RnProfile, RnCalendar;
    ImageView imgBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        mProgDlg = new ProgressDialog(GameDetailActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);
        mEvent_id = getIntent().getStringExtra("event_id");
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        RnFeed = (RelativeLayout) findViewById(R.id.RnFeed);
        RnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.getInstance().status = 1;
                Intent intent_home = new Intent(GameDetailActivity.this, FeedActivity.class);
                intent_home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_home);
            }
        });
        RnCalendar = (RelativeLayout)findViewById(R.id.RnCalendar);
        RnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.getInstance().status = 5;

                Intent intent_classify = new Intent(GameDetailActivity.this, LivescoreActivity.class);
                intent_classify.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_classify);
            }
        });
        RnTip = (RelativeLayout) findViewById(R.id.RnTip);
        RnTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.getInstance().status = 2;

                Intent intent_classify = new Intent(GameDetailActivity.this, TipsActivity.class);
                intent_classify.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_classify);
            }
        });

        RnProfile = (RelativeLayout) findViewById(R.id.RnProfile);
        RnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.getInstance().status = 4;

                Intent intent_login = new Intent(GameDetailActivity.this, ProfileActivity.class);
                intent_login.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_login);
            }
        });
        RnFeed.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
        RnCalendar.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));
        RnTip.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_green_grey));
        RnProfile.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.clr_white));

        lnContainer = (LinearLayout) findViewById(R.id.lnContainer);

        txtHomeName = (TextView) findViewById(R.id.txtHomeName);
        txtAwayName = (TextView) findViewById(R.id.txtAwayName);
        txtLeagueName = (TextView) findViewById(R.id.txtLeagueName);
        txtStartTime = (TextView) findViewById(R.id.txtStartTime);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        imgAway = (ImageView) findViewById(R.id.imgAway);
        loadData();
    }

    private void loadData() {
        mProgDlg.show();
        new Thread(mLoadRunnable).start();
    }

    private Runnable mLoadRunnable = new Runnable() {
        @Override
        public void run() {
            Common.getInstance().arrCorrectScores.clear();
            Common.getInstance().arrAlternativeTotalGoals.clear();
            Common.getInstance().arrExactTotalGoals.clear();
            Common.getInstance().arrTeamTotalGoals.clear();
            Common.getInstance().arrMatchTotalGoals.clear();
            Common.getInstance().arrTotalGoalsBTS.clear();
            int ret = NetworkManager.getManager().gameDetail(mEvent_id);
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
                Toast.makeText(GameDetailActivity.this, "There is no data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(GameDetailActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };
    String mSelectedMarketStyle = "";
    String mSelectedOddStyle = "";
    String mSelectedOdd = "";
    String mTipAmount = "";
    boolean mFullTimeResultShow = false;
    boolean mDoubleChanceShow = false;
    boolean mTotalGoalsShow = false;
    boolean mBTSShow = false;
    boolean mAsianHandicapShow = false;
    boolean mGoalLineShow = false;
    boolean mCorrectScoreShow = false;
    boolean mAlternativeTotalGoalsShow = false;
    boolean mExactTotalGoalsShow = false;
    boolean mTeamTotalGoalsShow = false;
    boolean mMatchResultTotalGoalsShow = false;
    boolean mTotalGoalsBTSShow = false;

    private void bindData() {
        txtLeagueName.setText(Common.getInstance().gameDetailInfo.league_name);
        txtHomeName.setText(Common.getInstance().gameDetailInfo.home);
        txtAwayName.setText(Common.getInstance().gameDetailInfo.away);
        String[] arrStartTime = Common.getInstance().gameDetailInfo.start_date_time.split(" ");
        SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm");
        Date date;
        date = new Date();
        try {
            date = sdformat.parse(arrStartTime[1]);
        } catch (ParseException ex) {

        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, 1);

        String currentGameTime = sdformat.format(cal.getTime());

        txtStartTime.setText(arrStartTime[0] + " " + currentGameTime);
        ImageLoader.getInstance().displayImage(Common.getInstance().gameDetailInfo.home_image, imgHome, Common.getInstance().goodsDisplayImageOptions);
        ImageLoader.getInstance().displayImage(Common.getInstance().gameDetailInfo.away_image, imgAway, Common.getInstance().goodsDisplayImageOptions);

        lnContainer.removeAllViews();

        /////////////////full time result///////
        if (!Common.getInstance().gameDetailInfo.home_odd.equals("")) {
            final View vFullTimeResult = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.full_time_result, null);
            TextView txtFullTimeResult = (TextView) vFullTimeResult.findViewById(R.id.txtFullTimeResult);
            txtFullTimeResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout lnContent = (LinearLayout) vFullTimeResult.findViewById(R.id.lnContent);
                    mFullTimeResultShow = !mFullTimeResultShow;
                    if (mFullTimeResultShow == true) {
                        lnContent.setVisibility(View.GONE);
                    } else {
                        lnContent.setVisibility(View.VISIBLE);
                    }
                }
            });
            LinearLayout lnHomeOdd = (LinearLayout) vFullTimeResult.findViewById(R.id.lnHomeOdd);
//            lnHomeOdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                    tipResultDialog.setCancelable(true);
//                    tipResultDialog.setOddStyle(Common.getInstance().gameDetailInfo.home + " " + Common.getInstance().gameDetailInfo.home_odd);
//                    tipResultDialog.setMarketStyle("Full Time Result");
//                    mSelectedMarketStyle = "Full Time Result";
//                    mSelectedOdd = Common.getInstance().gameDetailInfo.home_odd;
//                    mSelectedOddStyle = Common.getInstance().gameDetailInfo.home;
//                    tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                    tipResultDialog.setListener(mTipResultListener);
//                    tipResultDialog.show();
//                }
//            });
            TextView txtHomeOdd = (TextView) vFullTimeResult.findViewById(R.id.txtHomeOdd);
            txtHomeOdd.setText(Common.getInstance().gameDetailInfo.home_odd);

            LinearLayout lnDrawOdd = (LinearLayout) vFullTimeResult.findViewById(R.id.lnDrawOdd);
//            lnDrawOdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                    tipResultDialog.setCancelable(true);
//                    tipResultDialog.setOddStyle("Draw " + Common.getInstance().gameDetailInfo.draw_odd);
//                    tipResultDialog.setMarketStyle("Full Time Result");
//                    mSelectedMarketStyle = "Full Time Result";
//                    mSelectedOdd = Common.getInstance().gameDetailInfo.draw_odd;
//                    mSelectedOddStyle = "Draw";
//                    tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                    tipResultDialog.setListener(mTipResultListener);
//                    tipResultDialog.show();
//                }
//            });
            TextView txtDrawOdd = (TextView) vFullTimeResult.findViewById(R.id.txtDrawOdd);
            txtDrawOdd.setText(Common.getInstance().gameDetailInfo.draw_odd);

            LinearLayout lnAwayOdd = (LinearLayout) vFullTimeResult.findViewById(R.id.lnAwayOdd);
//            lnAwayOdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                    tipResultDialog.setCancelable(true);
//                    tipResultDialog.setOddStyle(Common.getInstance().gameDetailInfo.away + " " + Common.getInstance().gameDetailInfo.away_odd);
//                    tipResultDialog.setMarketStyle("Full Time Result");
//                    mSelectedMarketStyle = "Full Time Result";
//                    mSelectedOdd = Common.getInstance().gameDetailInfo.away_odd;
//                    mSelectedOddStyle = Common.getInstance().gameDetailInfo.away;
//                    tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                    tipResultDialog.setListener(mTipResultListener);
//                    tipResultDialog.show();
//                }
//            });
            TextView txtAwayOdd = (TextView) vFullTimeResult.findViewById(R.id.txtAwayOdd);
            txtAwayOdd.setText(Common.getInstance().gameDetailInfo.away_odd);
            lnContainer.addView(vFullTimeResult);
        }
        //////////////Double Chance
//        if (!Common.getInstance().gameDetailInfo.double_chance_12.equals("")) {
//            final View vDoubleChance = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.double_chance, null);
//            TextView txtDoubleChance = (TextView) vDoubleChance.findViewById(R.id.txtDoubleChance);
//            txtDoubleChance.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LinearLayout lnContent = (LinearLayout) vDoubleChance.findViewById(R.id.lnContent);
//                    mDoubleChanceShow = !mDoubleChanceShow;
//                    if (mDoubleChanceShow == true) {
//                        lnContent.setVisibility(View.GONE);
//                    } else {
//                        lnContent.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//            LinearLayout ln1XOdd = (LinearLayout) vDoubleChance.findViewById(R.id.ln1XOdd);
//            ln1XOdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                    tipResultDialog.setCancelable(true);
//                    tipResultDialog.setOddStyle(Common.getInstance().gameDetailInfo.home + " or Draw - " + Common.getInstance().gameDetailInfo.double_chance_1X);
//                    tipResultDialog.setMarketStyle("Double Chance");
//                    mSelectedMarketStyle = "Double Chance";
//                    mSelectedOdd = Common.getInstance().gameDetailInfo.double_chance_1X;
//                    mSelectedOddStyle = Common.getInstance().gameDetailInfo.home + " or Draw";
//                    tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                    tipResultDialog.setListener(mTipResultListener);
//                    tipResultDialog.show();
//                }
//            });
//            TextView txt1XOdd = (TextView) vDoubleChance.findViewById(R.id.txt1XOdd);
//            txt1XOdd.setText(Common.getInstance().gameDetailInfo.double_chance_1X);
//
//            LinearLayout lnX2Odd = (LinearLayout) vDoubleChance.findViewById(R.id.lnX2Odd);
//            lnX2Odd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                    tipResultDialog.setCancelable(true);
//                    tipResultDialog.setOddStyle("Draw or " + Common.getInstance().gameDetailInfo.away + " - " + Common.getInstance().gameDetailInfo.double_chance_X2);
//                    tipResultDialog.setMarketStyle("Double Chance");
//                    mSelectedMarketStyle = "Double Chance";
//                    mSelectedOdd = Common.getInstance().gameDetailInfo.double_chance_X2;
//                    mSelectedOddStyle = "Draw or " + Common.getInstance().gameDetailInfo.away;
//                    tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                    tipResultDialog.setListener(mTipResultListener);
//                    tipResultDialog.show();
//                }
//            });
//            TextView txtX2Odd = (TextView) vDoubleChance.findViewById(R.id.txtX2Odd);
//            txtX2Odd.setText(Common.getInstance().gameDetailInfo.double_chance_X2);
//
//            LinearLayout ln12Odd = (LinearLayout) vDoubleChance.findViewById(R.id.ln12Odd);
//            ln12Odd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                    tipResultDialog.setCancelable(true);
//                    tipResultDialog.setOddStyle(Common.getInstance().gameDetailInfo.home + " or " + Common.getInstance().gameDetailInfo.away + " - " + Common.getInstance().gameDetailInfo.double_chance_12);
//                    tipResultDialog.setMarketStyle("Double Chance");
//                    mSelectedMarketStyle = "Double Chance";
//                    mSelectedOdd = Common.getInstance().gameDetailInfo.double_chance_12;
//                    mSelectedOddStyle = Common.getInstance().gameDetailInfo.home + " or " + Common.getInstance().gameDetailInfo.away;
//                    tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                    tipResultDialog.setListener(mTipResultListener);
//                    tipResultDialog.show();
//                }
//            });
//            TextView txt12Odd = (TextView) vDoubleChance.findViewById(R.id.txt12Odd);
//            txt12Odd.setText(Common.getInstance().gameDetailInfo.double_chance_12);
//            lnContainer.addView(vDoubleChance);
//        }
//        ////////Total Goals
//        if (!Common.getInstance().gameDetailInfo.total_goal_over.equals("")) {
//            final View vTotalGoals = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.total_goals, null);
//            TextView txtTotalGoals = (TextView) vTotalGoals.findViewById(R.id.txtTotalGoals);
//            txtTotalGoals.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LinearLayout lnContent = (LinearLayout) vTotalGoals.findViewById(R.id.lnContent);
//                    mTotalGoalsShow = !mTotalGoalsShow;
//                    if (mTotalGoalsShow == true) {
//                        lnContent.setVisibility(View.GONE);
//                    } else {
//                        lnContent.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//            LinearLayout lnOverOdd = (LinearLayout) vTotalGoals.findViewById(R.id.lnOverOdd);
//            lnOverOdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                    tipResultDialog.setCancelable(true);
//                    tipResultDialog.setOddStyle("Over 2.5 - " + Common.getInstance().gameDetailInfo.total_goal_over);
//                    tipResultDialog.setMarketStyle("Total Goals");
//                    mSelectedMarketStyle = "Total Goals";
//                    mSelectedOdd = Common.getInstance().gameDetailInfo.total_goal_over;
//                    mSelectedOddStyle = "Over 2.5";
//                    tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                    tipResultDialog.setListener(mTipResultListener);
//                    tipResultDialog.show();
//                }
//            });
//            TextView txtOverOdd = (TextView) vTotalGoals.findViewById(R.id.txtOverOdd);
//            txtOverOdd.setText(Common.getInstance().gameDetailInfo.total_goal_over);
//
//            LinearLayout lnUnderOdd = (LinearLayout) vTotalGoals.findViewById(R.id.lnUnderOdd);
//            lnUnderOdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                    tipResultDialog.setCancelable(true);
//                    tipResultDialog.setOddStyle("Under 2.5 - " + Common.getInstance().gameDetailInfo.total_goal_under);
//                    tipResultDialog.setMarketStyle("Total Goals");
//                    mSelectedMarketStyle = "Total Goals";
//                    mSelectedOdd = Common.getInstance().gameDetailInfo.total_goal_under;
//                    mSelectedOddStyle = "Under 2.5";
//                    tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                    tipResultDialog.setListener(mTipResultListener);
//                    tipResultDialog.show();
//                }
//            });
//            TextView txtUnderOdd = (TextView) vTotalGoals.findViewById(R.id.txtUnderOdd);
//            txtUnderOdd.setText(Common.getInstance().gameDetailInfo.total_goal_under);
//            lnContainer.addView(vTotalGoals);
//        }
//        //////////////Both Teams to Score////////
//        if (!Common.getInstance().gameDetailInfo.bts_yes.equals("0")) {
//            final View vBTS = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.both_team_score, null);
//            TextView txtBTS = (TextView) vBTS.findViewById(R.id.txtBTS);
//            txtBTS.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LinearLayout lnContent = (LinearLayout) vBTS.findViewById(R.id.lnContent);
//                    mBTSShow = !mBTSShow;
//                    if (mBTSShow == true) {
//                        lnContent.setVisibility(View.GONE);
//                    } else {
//                        lnContent.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//            LinearLayout lnYesOdd = (LinearLayout) vBTS.findViewById(R.id.lnYesOdd);
//            lnYesOdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                    tipResultDialog.setCancelable(true);
//                    tipResultDialog.setOddStyle("Yes - " + Common.getInstance().gameDetailInfo.bts_yes);
//                    tipResultDialog.setMarketStyle("Both Teams to Score");
//                    mSelectedMarketStyle = "Both Teams to Score";
//                    mSelectedOdd = Common.getInstance().gameDetailInfo.bts_yes;
//                    mSelectedOddStyle = "Yes";
//                    tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                    tipResultDialog.setListener(mTipResultListener);
//                    tipResultDialog.show();
//                }
//            });
//            TextView txtYesOdd = (TextView) vBTS.findViewById(R.id.txtYesOdd);
//            txtYesOdd.setText(Common.getInstance().gameDetailInfo.bts_yes);
//
//            LinearLayout lnNoOdd = (LinearLayout) vBTS.findViewById(R.id.lnNoOdd);
//            lnNoOdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                    tipResultDialog.setCancelable(true);
//                    tipResultDialog.setOddStyle("No - " + Common.getInstance().gameDetailInfo.bts_no);
//                    tipResultDialog.setMarketStyle("Both Teams to Score");
//                    mSelectedMarketStyle = "Both Teams to Score";
//                    mSelectedOdd = Common.getInstance().gameDetailInfo.bts_no;
//                    mSelectedOddStyle = "No";
//                    tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                    tipResultDialog.setListener(mTipResultListener);
//                    tipResultDialog.show();
//                }
//            });
//            TextView txtNoOdd = (TextView) vBTS.findViewById(R.id.txtNoOdd);
//            txtNoOdd.setText(Common.getInstance().gameDetailInfo.bts_no);
//            lnContainer.addView(vBTS);
//        }
//        /*
//        /////////////Asian Handicap
//        if(!Common.getInstance().gameDetailInfo.asian_home_odd.equals("")) {
//            final View vAsianHandicap = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.asian_handicap, null);
//            TextView txtAsianHandicap = (TextView) vAsianHandicap.findViewById(R.id.txtAsianHandicap);
//            txtAsianHandicap.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LinearLayout lnContent = (LinearLayout) vAsianHandicap.findViewById(R.id.lnContent);
//                    mAsianHandicapShow = !mAsianHandicapShow;
//                    if (mAsianHandicapShow == true) {
//                        lnContent.setVisibility(View.GONE);
//                    } else {
//                        lnContent.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//            LinearLayout lnAsianHomeOdd = (LinearLayout) vAsianHandicap.findViewById(R.id.lnAsianHomeOdd);
//            lnAsianHomeOdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                    tipResultDialog.setCancelable(true);
//                    tipResultDialog.setOddStyle(Common.getInstance().gameDetailInfo.home + " - " + Common.getInstance().gameDetailInfo.asian_home_odd);
//                    tipResultDialog.setMarketStyle("Asian Handicap");
//                    mSelectedMarketStyle = "Asian Handicap";
//                    mSelectedOdd = Common.getInstance().gameDetailInfo.asian_home_odd;
//                    mSelectedOddStyle = Common.getInstance().gameDetailInfo.home;
//                    tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                    tipResultDialog.setListener(mTipResultListener);
//                    tipResultDialog.show();
//                }
//            });
//            TextView txtAsianHomeOdd = (TextView) vAsianHandicap.findViewById(R.id.txtAsianHomeOdd);
//            txtAsianHomeOdd.setText(Common.getInstance().gameDetailInfo.asian_home_odd);
//
//            LinearLayout lnAsianAwayOdd = (LinearLayout) vAsianHandicap.findViewById(R.id.lnAsianAwayOdd);
//            lnAsianAwayOdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                    tipResultDialog.setCancelable(true);
//                    tipResultDialog.setOddStyle(Common.getInstance().gameDetailInfo.away + " - " + Common.getInstance().gameDetailInfo.asian_away_odd);
//                    tipResultDialog.setMarketStyle("Asian Handicap");
//                    mSelectedMarketStyle = "Asian Handicap";
//                    mSelectedOdd = Common.getInstance().gameDetailInfo.asian_away_odd;
//                    mSelectedOddStyle = Common.getInstance().gameDetailInfo.away;
//                    tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                    tipResultDialog.setListener(mTipResultListener);
//                    tipResultDialog.show();
//                }
//            });
//            TextView txtAsianHome = (TextView) vAsianHandicap.findViewById(R.id.txtAsianHome);
//            txtAsianHome.setText(Common.getInstance().gameDetailInfo.home);
//            TextView txtAsianAway = (TextView) vAsianHandicap.findViewById(R.id.txtAsianAway);
//            txtAsianAway.setText(Common.getInstance().gameDetailInfo.away);
//            TextView txtAsianAwayOdd = (TextView) vAsianHandicap.findViewById(R.id.txtAsianAwayOdd);
//            txtAsianAwayOdd.setText(Common.getInstance().gameDetailInfo.asian_away_odd);
//            lnContainer.addView(vAsianHandicap);
//        }
//        /////////Goal line//////
//        if(!Common.getInstance().gameDetailInfo.goal_line_under.equals("")) {
//            final View vGoalLine = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.goal_line, null);
//            TextView txtGoalLine = (TextView) vGoalLine.findViewById(R.id.txtGoalLine);
//            txtGoalLine.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LinearLayout lnContent = (LinearLayout) vGoalLine.findViewById(R.id.lnContent);
//                    mGoalLineShow = !mGoalLineShow;
//                    if (mGoalLineShow == true) {
//                        lnContent.setVisibility(View.GONE);
//                    } else {
//                        lnContent.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//            LinearLayout lnGoalOverOdd = (LinearLayout) vGoalLine.findViewById(R.id.lnGoalOverOdd);
//            lnGoalOverOdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                    tipResultDialog.setCancelable(true);
//                    tipResultDialog.setOddStyle("Over - " + Common.getInstance().gameDetailInfo.goal_line_over);
//                    tipResultDialog.setMarketStyle("Goal Line");
//                    mSelectedMarketStyle = "Goal Line";
//                    mSelectedOdd = Common.getInstance().gameDetailInfo.goal_line_over;
//                    mSelectedOddStyle = "Over";
//                    tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                    tipResultDialog.setListener(mTipResultListener);
//                    tipResultDialog.show();
//                }
//            });
//            TextView txtGoalOverOdd = (TextView) vGoalLine.findViewById(R.id.txtGoalOverOdd);
//            txtGoalOverOdd.setText(Common.getInstance().gameDetailInfo.goal_line_over);
//
//            LinearLayout lnGoalUnderOdd = (LinearLayout) vGoalLine.findViewById(R.id.lnGoalUnderOdd);
//            lnGoalUnderOdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                    tipResultDialog.setCancelable(true);
//                    tipResultDialog.setOddStyle("Under - " + Common.getInstance().gameDetailInfo.goal_line_under);
//                    tipResultDialog.setMarketStyle("Goal Line");
//                    mSelectedMarketStyle = "Goal Line";
//                    mSelectedOdd = Common.getInstance().gameDetailInfo.goal_line_under;
//                    mSelectedOddStyle = "Under";
//                    tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                    tipResultDialog.setListener(mTipResultListener);
//                    tipResultDialog.show();
//                }
//            });
//            TextView txtGoalUnderOdd = (TextView) vGoalLine.findViewById(R.id.txtGoalUnderOdd);
//            txtGoalUnderOdd.setText(Common.getInstance().gameDetailInfo.goal_line_under);
//            lnContainer.addView(vGoalLine);
//        }
//        */
//        //////////////Correct Score///////
//        if (Common.getInstance().arrCorrectScores.size() > 0) {
//            final View vCorrectScore = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.correct_score, null);
//            TextView txtCorrectScore = (TextView) vCorrectScore.findViewById(R.id.txtCorrectScore);
//            txtCorrectScore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LinearLayout lnContent = (LinearLayout) vCorrectScore.findViewById(R.id.lnContent);
//                    mCorrectScoreShow = !mCorrectScoreShow;
//                    if (mCorrectScoreShow == true) {
//                        lnContent.setVisibility(View.GONE);
//                    } else {
//                        lnContent.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//            LinearLayout lnCorectScoreContainer = (LinearLayout) vCorrectScore.findViewById(R.id.lnCorectScoreContainer);
//            lnCorectScoreContainer.removeAllViews();
//
//            mArrHomescores.clear();
//            mArrDrawScores.clear();
//            mArrAwayScores.clear();
//
//
//            for (int i = 0; i < Common.getInstance().arrCorrectScores.size(); i++) {
//                if (Common.getInstance().arrCorrectScores.get(i).name.contains(Common.getInstance().gameDetailInfo.home)) {
//                    mArrHomescores.add(Common.getInstance().arrCorrectScores.get(i));
//                }
//            }
//            for (int i = 0; i < Common.getInstance().arrCorrectScores.size(); i++) {
//                if (Common.getInstance().arrCorrectScores.get(i).name.contains("Draw")) {
//                    mArrDrawScores.add(Common.getInstance().arrCorrectScores.get(i));
//                }
//            }
//            for (int i = 0; i < Common.getInstance().arrCorrectScores.size(); i++) {
//                if (Common.getInstance().arrCorrectScores.get(i).name.contains(Common.getInstance().gameDetailInfo.away)) {
//                    mArrAwayScores.add(Common.getInstance().arrCorrectScores.get(i));
//                }
//            }
//            Log.d("Home Scores", "" + mArrHomescores.size());
//            Log.d("Draw Scores", "" + mArrDrawScores.size());
//            Log.d("Away Scores", "" + mArrAwayScores.size());
//
//            if ((mArrHomescores.size() >= mArrDrawScores.size()) && (mArrHomescores.size() >= mArrAwayScores.size())) {
//                for (int i = 0; i < mArrHomescores.size(); i++) {
//                    final CorrectScoreInfo homeInfo = mArrHomescores.get(i);
//                    final View vCorrectScoreItem = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.correct_score_item, null);
//                    TextView txtHomeScore = (TextView) vCorrectScoreItem.findViewById(R.id.txtHomeScore);
//                    String[] arrHomeScore = homeInfo.name.split("#");
//                    txtHomeScore.setText(arrHomeScore[1]);
//                    TextView txtHomeScoreOdd = (TextView) vCorrectScoreItem.findViewById(R.id.txtHomeScoreOdd);
//                    txtHomeScoreOdd.setText(homeInfo.odd);
//                    LinearLayout lnCorrectHomeOdd = (LinearLayout) vCorrectScoreItem.findViewById(R.id.lnCorrectHomeOdd);
//                    lnCorrectHomeOdd.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                            tipResultDialog.setCancelable(true);
//                            tipResultDialog.setOddStyle(Common.getInstance().gameDetailInfo.home + " - " + homeInfo.odd);
//                            tipResultDialog.setMarketStyle("Correct Score");
//                            mSelectedMarketStyle = "Correct Score";
//                            mSelectedOdd = homeInfo.odd;
//                            String[] arrHomeScore = homeInfo.name.split("#");
//                            mSelectedOddStyle = Common.getInstance().gameDetailInfo.home + " " + arrHomeScore[1];
//                            tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                            tipResultDialog.setListener(mTipResultListener);
//                            tipResultDialog.show();
//                        }
//                    });
//
//                    if (i < mArrDrawScores.size()) {
//                        final CorrectScoreInfo drawInfo = mArrDrawScores.get(i);
//                        TextView txtDrawScore = (TextView) vCorrectScoreItem.findViewById(R.id.txtDrawScore);
//                        String[] arrDrawScore = drawInfo.name.split("#");
//                        txtDrawScore.setText(arrDrawScore[1]);
//                        TextView txtDrawScoreOdd = (TextView) vCorrectScoreItem.findViewById(R.id.txtDrawScoreOdd);
//                        txtDrawScoreOdd.setText(drawInfo.odd);
//                        LinearLayout lnCorrectDrawOdd = (LinearLayout) vCorrectScoreItem.findViewById(R.id.lnCorrectDrawOdd);
//                        lnCorrectDrawOdd.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                                tipResultDialog.setCancelable(true);
//                                tipResultDialog.setOddStyle("Draw - " + drawInfo.odd);
//                                tipResultDialog.setMarketStyle("Correct Score");
//                                mSelectedMarketStyle = "Correct Score";
//                                mSelectedOdd = drawInfo.odd;
//                                String[] arrHomeScore = drawInfo.name.split("#");
//                                mSelectedOddStyle = "Draw " + arrHomeScore[1];
//                                tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                                tipResultDialog.setListener(mTipResultListener);
//                                tipResultDialog.show();
//                            }
//                        });
//                    }
//
//                    if (i < mArrAwayScores.size()) {
//                        final CorrectScoreInfo awayInfo = mArrAwayScores.get(i);
//                        TextView txtAwayScore = (TextView) vCorrectScoreItem.findViewById(R.id.txtAwayScore);
//                        String[] arrAwayScore = awayInfo.name.split("#");
//                        txtAwayScore.setText(arrAwayScore[1]);
//                        TextView txtAwayScoreOdd = (TextView) vCorrectScoreItem.findViewById(R.id.txtAwayScoreOdd);
//                        txtAwayScoreOdd.setText(awayInfo.odd);
//                        LinearLayout lnCorrectAwayOdd = (LinearLayout) vCorrectScoreItem.findViewById(R.id.lnCorrectAwayOdd);
//                        lnCorrectAwayOdd.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                                tipResultDialog.setCancelable(true);
//                                tipResultDialog.setOddStyle(Common.getInstance().gameDetailInfo.away + " - " + awayInfo.odd);
//                                tipResultDialog.setMarketStyle("Correct Score");
//                                mSelectedMarketStyle = "Correct Score";
//                                mSelectedOdd = awayInfo.odd;
//                                String[] arrHomeScore = awayInfo.name.split("#");
//                                mSelectedOddStyle = Common.getInstance().gameDetailInfo.away + " " + arrHomeScore[1];
//                                tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                                tipResultDialog.setListener(mTipResultListener);
//                                tipResultDialog.show();
//                            }
//                        });
//                    }
//                    lnCorectScoreContainer.addView(vCorrectScoreItem);
//                }
//            } else if ((mArrDrawScores.size() >= mArrHomescores.size()) && (mArrDrawScores.size() >= mArrAwayScores.size())) {
//                for (int i = 0; i < mArrDrawScores.size(); i++) {
//                    final View vCorrectScoreItem = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.correct_score_item, null);
//                    if (i < mArrHomescores.size()) {
//                        final CorrectScoreInfo homeInfo = mArrHomescores.get(i);
//                        TextView txtHomeScore = (TextView) vCorrectScoreItem.findViewById(R.id.txtHomeScore);
//                        String[] arrHomeScore = homeInfo.name.split("#");
//                        txtHomeScore.setText(arrHomeScore[1]);
//                        TextView txtHomeScoreOdd = (TextView) vCorrectScoreItem.findViewById(R.id.txtHomeScoreOdd);
//                        txtHomeScoreOdd.setText(homeInfo.odd);
//                        LinearLayout lnCorrectHomeOdd = (LinearLayout) vCorrectScoreItem.findViewById(R.id.lnCorrectHomeOdd);
//                        lnCorrectHomeOdd.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                                tipResultDialog.setCancelable(true);
//                                tipResultDialog.setOddStyle(Common.getInstance().gameDetailInfo.home + " - " + homeInfo.odd);
//                                tipResultDialog.setMarketStyle("Correct Score");
//                                mSelectedMarketStyle = "Correct Score";
//                                mSelectedOdd = homeInfo.odd;
//                                String[] arrHomeScore = homeInfo.name.split("#");
//                                mSelectedOddStyle = Common.getInstance().gameDetailInfo.home + " " + arrHomeScore[1];
//                                tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                                tipResultDialog.setListener(mTipResultListener);
//                                tipResultDialog.show();
//                            }
//                        });
//                    }
//                    final CorrectScoreInfo drawInfo = mArrDrawScores.get(i);
//                    TextView txtDrawScore = (TextView) vCorrectScoreItem.findViewById(R.id.txtDrawScore);
//                    String[] arrDrawScore = drawInfo.name.split("#");
//                    txtDrawScore.setText(arrDrawScore[1]);
//                    TextView txtDrawScoreOdd = (TextView) vCorrectScoreItem.findViewById(R.id.txtDrawScoreOdd);
//                    txtDrawScoreOdd.setText(drawInfo.odd);
//                    LinearLayout lnCorrectDrawOdd = (LinearLayout) vCorrectScoreItem.findViewById(R.id.lnCorrectDrawOdd);
//                    lnCorrectDrawOdd.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                            tipResultDialog.setCancelable(true);
//                            tipResultDialog.setOddStyle("Draw - " + drawInfo.odd);
//                            tipResultDialog.setMarketStyle("Correct Score");
//                            mSelectedMarketStyle = "Correct Score";
//                            mSelectedOdd = drawInfo.odd;
//                            String[] arrHomeScore = drawInfo.name.split("#");
//                            mSelectedOddStyle = "Draw " + arrHomeScore[1];
//                            tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                            tipResultDialog.setListener(mTipResultListener);
//                            tipResultDialog.show();
//                        }
//                    });
//
//
//                    if (i < mArrAwayScores.size()) {
//                        final CorrectScoreInfo awayInfo = mArrAwayScores.get(i);
//                        TextView txtAwayScore = (TextView) vCorrectScoreItem.findViewById(R.id.txtAwayScore);
//                        String[] arrAwayScore = awayInfo.name.split("#");
//                        txtAwayScore.setText(arrAwayScore[1]);
//                        TextView txtAwayScoreOdd = (TextView) vCorrectScoreItem.findViewById(R.id.txtAwayScoreOdd);
//                        txtAwayScoreOdd.setText(awayInfo.odd);
//                        LinearLayout lnCorrectAwayOdd = (LinearLayout) vCorrectScoreItem.findViewById(R.id.lnCorrectAwayOdd);
//                        lnCorrectAwayOdd.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                                tipResultDialog.setCancelable(true);
//                                tipResultDialog.setOddStyle(Common.getInstance().gameDetailInfo.away + " - " + awayInfo.odd);
//                                tipResultDialog.setMarketStyle("Correct Score");
//                                mSelectedMarketStyle = "Correct Score";
//                                mSelectedOdd = awayInfo.odd;
//                                String[] arrHomeScore = awayInfo.name.split("#");
//                                mSelectedOddStyle = Common.getInstance().gameDetailInfo.away + " " + arrHomeScore[1];
//                                tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                                tipResultDialog.setListener(mTipResultListener);
//                                tipResultDialog.show();
//                            }
//                        });
//                    }
//                    lnCorectScoreContainer.addView(vCorrectScoreItem);
//                }
//            } else if ((mArrAwayScores.size() >= mArrHomescores.size()) && (mArrAwayScores.size() >= mArrDrawScores.size())) {
//                for (int i = 0; i < mArrAwayScores.size(); i++) {
//                    final View vCorrectScoreItem = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.correct_score_item, null);
//                    if (i < mArrHomescores.size()) {
//                        final CorrectScoreInfo homeInfo = mArrHomescores.get(i);
//                        TextView txtHomeScore = (TextView) vCorrectScoreItem.findViewById(R.id.txtHomeScore);
//                        String[] arrHomeScore = homeInfo.name.split("#");
//                        txtHomeScore.setText(arrHomeScore[1]);
//                        TextView txtHomeScoreOdd = (TextView) vCorrectScoreItem.findViewById(R.id.txtHomeScoreOdd);
//                        txtHomeScoreOdd.setText(homeInfo.odd);
//                        LinearLayout lnCorrectHomeOdd = (LinearLayout) vCorrectScoreItem.findViewById(R.id.lnCorrectHomeOdd);
//                        lnCorrectHomeOdd.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                                tipResultDialog.setCancelable(true);
//                                tipResultDialog.setOddStyle(Common.getInstance().gameDetailInfo.home + " - " + homeInfo.odd);
//                                tipResultDialog.setMarketStyle("Correct Score");
//                                mSelectedMarketStyle = "Correct Score";
//                                mSelectedOdd = homeInfo.odd;
//                                String[] arrHomeScore = homeInfo.name.split("#");
//                                mSelectedOddStyle = Common.getInstance().gameDetailInfo.home + " " + arrHomeScore[1];
//                                tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                                tipResultDialog.setListener(mTipResultListener);
//                                tipResultDialog.show();
//                            }
//                        });
//                    }
//                    if (i < mArrDrawScores.size()) {
//                        final CorrectScoreInfo drawInfo = mArrDrawScores.get(i);
//                        TextView txtDrawScore = (TextView) vCorrectScoreItem.findViewById(R.id.txtDrawScore);
//                        String[] arrDrawScore = drawInfo.name.split("#");
//                        txtDrawScore.setText(arrDrawScore[1]);
//                        TextView txtDrawScoreOdd = (TextView) vCorrectScoreItem.findViewById(R.id.txtDrawScoreOdd);
//                        txtDrawScoreOdd.setText(drawInfo.odd);
//                        LinearLayout lnCorrectDrawOdd = (LinearLayout) vCorrectScoreItem.findViewById(R.id.lnCorrectDrawOdd);
//                        lnCorrectDrawOdd.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                                tipResultDialog.setCancelable(true);
//                                tipResultDialog.setOddStyle("Draw - " + drawInfo.odd);
//                                tipResultDialog.setMarketStyle("Correct Score");
//                                mSelectedMarketStyle = "Correct Score";
//                                mSelectedOdd = drawInfo.odd;
//                                String[] arrHomeScore = drawInfo.name.split("#");
//                                mSelectedOddStyle = "Draw " + arrHomeScore[1];
//                                tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                                tipResultDialog.setListener(mTipResultListener);
//                                tipResultDialog.show();
//                            }
//                        });
//                    }
//
//
//                    final CorrectScoreInfo awayInfo = mArrAwayScores.get(i);
//                    TextView txtAwayScore = (TextView) vCorrectScoreItem.findViewById(R.id.txtAwayScore);
//                    String[] arrAwayScore = awayInfo.name.split("#");
//                    txtAwayScore.setText(arrAwayScore[1]);
//                    TextView txtAwayScoreOdd = (TextView) vCorrectScoreItem.findViewById(R.id.txtAwayScoreOdd);
//                    txtAwayScoreOdd.setText(awayInfo.odd);
//                    LinearLayout lnCorrectAwayOdd = (LinearLayout) vCorrectScoreItem.findViewById(R.id.lnCorrectAwayOdd);
//                    lnCorrectAwayOdd.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                            tipResultDialog.setCancelable(true);
//                            tipResultDialog.setOddStyle(Common.getInstance().gameDetailInfo.away + " - " + awayInfo.odd);
//                            tipResultDialog.setMarketStyle("Correct Score");
//                            mSelectedMarketStyle = "Correct Score";
//                            mSelectedOdd = awayInfo.odd;
//                            String[] arrHomeScore = awayInfo.name.split("#");
//                            mSelectedOddStyle = Common.getInstance().gameDetailInfo.away + " " + arrHomeScore[1];
//                            tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                            tipResultDialog.setListener(mTipResultListener);
//                            tipResultDialog.show();
//                        }
//                    });
//
//                    lnCorectScoreContainer.addView(vCorrectScoreItem);
//                }
//            }
//
//            lnContainer.addView(vCorrectScore);
//        }
//        //////////////Alternative Total Goals///////
//        if (Common.getInstance().arrAlternativeTotalGoals.size() > 0) {
//            final View vAlternativeTotalGoals = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.alternative_total_goal, null);
//            TextView txtAlternativeTotalGoals = (TextView) vAlternativeTotalGoals.findViewById(R.id.txtAlternativeTotalGoals);
//            txtAlternativeTotalGoals.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LinearLayout lnContent = (LinearLayout) vAlternativeTotalGoals.findViewById(R.id.lnContent);
//                    mAlternativeTotalGoalsShow = !mAlternativeTotalGoalsShow;
//                    if (mAlternativeTotalGoalsShow == true) {
//                        lnContent.setVisibility(View.GONE);
//                    } else {
//                        lnContent.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//            LinearLayout lnAlterTotalGoalContainer = (LinearLayout) vAlternativeTotalGoals.findViewById(R.id.lnAlterTotalGoalContainer);
//            lnAlterTotalGoalContainer.removeAllViews();
//
//            for (int i = 0; i < Common.getInstance().arrAlternativeTotalGoals.size(); i++) {
//                final AlternativeTotalGoalInfo alterTotalGoalInfo = Common.getInstance().arrAlternativeTotalGoals.get(i);
//                final View vAlternativeTotalGoalsItem = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.alternative_total_goal_item, null);
//                TextView txtAlterHandicap = (TextView) vAlternativeTotalGoalsItem.findViewById(R.id.txtAlterHandicap);
//                txtAlterHandicap.setText(alterTotalGoalInfo.handicap);
//                TextView txtAlterOverOdd = (TextView) vAlternativeTotalGoalsItem.findViewById(R.id.txtAlterOverOdd);
//                txtAlterOverOdd.setText(alterTotalGoalInfo.over);
//                LinearLayout lnAlterOverOdd = (LinearLayout) vAlternativeTotalGoalsItem.findViewById(R.id.lnAlterOverOdd);
//                lnAlterOverOdd.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                        tipResultDialog.setCancelable(true);
//                        tipResultDialog.setOddStyle("Over " + alterTotalGoalInfo.handicap + " - " + alterTotalGoalInfo.over);
//                        tipResultDialog.setMarketStyle("Alternative Total Goals");
//                        mSelectedMarketStyle = "Alternative Total Goals";
//                        mSelectedOdd = alterTotalGoalInfo.over;
//                        mSelectedOddStyle = "Over " + alterTotalGoalInfo.handicap;
//                        tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                        tipResultDialog.setListener(mTipResultListener);
//                        tipResultDialog.show();
//                    }
//                });
//                TextView txtAlterUnderOdd = (TextView) vAlternativeTotalGoalsItem.findViewById(R.id.txtAlterUnderOdd);
//                txtAlterUnderOdd.setText(alterTotalGoalInfo.under);
//                LinearLayout lnAlterUnderOdd = (LinearLayout) vAlternativeTotalGoalsItem.findViewById(R.id.lnAlterUnderOdd);
//                lnAlterUnderOdd.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                        tipResultDialog.setCancelable(true);
//                        tipResultDialog.setOddStyle("Under " + alterTotalGoalInfo.handicap + " - " + alterTotalGoalInfo.under);
//                        tipResultDialog.setMarketStyle("Alternative Total Goals");
//                        mSelectedMarketStyle = "Alternative Total Goals";
//                        mSelectedOdd = alterTotalGoalInfo.under;
//                        mSelectedOddStyle = "Under " + alterTotalGoalInfo.handicap;
//                        tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                        tipResultDialog.setListener(mTipResultListener);
//                        tipResultDialog.show();
//                    }
//                });
//
//                lnAlterTotalGoalContainer.addView(vAlternativeTotalGoalsItem);
//            }
//
//            lnContainer.addView(vAlternativeTotalGoals);
//        }
//
//        //////////////Exact Total Goals///////
//        if (Common.getInstance().arrExactTotalGoals.size() > 0) {
//            final View vExactTotalGoals = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.exact_total_goal, null);
//            TextView txtExactTotalGoals = (TextView) vExactTotalGoals.findViewById(R.id.txtExactTotalGoals);
//            txtExactTotalGoals.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LinearLayout lnContent = (LinearLayout) vExactTotalGoals.findViewById(R.id.lnContent);
//                    mExactTotalGoalsShow = !mExactTotalGoalsShow;
//                    if (mExactTotalGoalsShow == true) {
//                        lnContent.setVisibility(View.GONE);
//                    } else {
//                        lnContent.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//            LinearLayout lnExactTotalGoalContainer = (LinearLayout) vExactTotalGoals.findViewById(R.id.lnExactTotalGoalContainer);
//            lnExactTotalGoalContainer.removeAllViews();
//
//            for (int i = 0; i < Common.getInstance().arrExactTotalGoals.size(); i++) {
//                final CorrectScoreInfo exactTotalGoalInfo = Common.getInstance().arrExactTotalGoals.get(i);
//                final View vExactTotalGoalsItem = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.exact_total_goal_item, null);
//                TextView txtExactGoals = (TextView) vExactTotalGoalsItem.findViewById(R.id.txtExactGoals);
//                txtExactGoals.setText(exactTotalGoalInfo.name);
//                TextView txtExactGoalsOdd = (TextView) vExactTotalGoalsItem.findViewById(R.id.txtExactGoalsOdd);
//                txtExactGoalsOdd.setText(exactTotalGoalInfo.odd);
//                LinearLayout lnExactGoalsOdd = (LinearLayout) vExactTotalGoalsItem.findViewById(R.id.lnExactGoalsOdd);
//                lnExactGoalsOdd.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                        tipResultDialog.setCancelable(true);
//                        tipResultDialog.setOddStyle(exactTotalGoalInfo.name + " - " + exactTotalGoalInfo.odd);
//                        tipResultDialog.setMarketStyle("Exact Total Goals");
//                        mSelectedMarketStyle = "Exact Total Goals";
//                        mSelectedOdd = exactTotalGoalInfo.odd;
//                        mSelectedOddStyle = exactTotalGoalInfo.name;
//                        tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                        tipResultDialog.setListener(mTipResultListener);
//                        tipResultDialog.show();
//                    }
//                });
//                lnExactTotalGoalContainer.addView(vExactTotalGoalsItem);
//            }
//
//            lnContainer.addView(vExactTotalGoals);
//        }
//
//        //////////////Team Total Goals///////
//        if (Common.getInstance().arrTeamTotalGoals.size() > 0) {
//            final View vTeamTotalGoals = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.team_total_goals, null);
//            TextView txtTeamTotalGoals = (TextView) vTeamTotalGoals.findViewById(R.id.txtTeamTotalGoals);
//            txtTeamTotalGoals.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LinearLayout lnContent = (LinearLayout) vTeamTotalGoals.findViewById(R.id.lnContent);
//                    mTeamTotalGoalsShow = !mTeamTotalGoalsShow;
//                    if (mTeamTotalGoalsShow == true) {
//                        lnContent.setVisibility(View.GONE);
//                    } else {
//                        lnContent.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//            TextView txtTeamTotalHome = (TextView) vTeamTotalGoals.findViewById(R.id.txtTeamTotalHome);
//            txtTeamTotalHome.setText(Common.getInstance().gameDetailInfo.home);
//            TextView txtTeamTotalAway = (TextView) vTeamTotalGoals.findViewById(R.id.txtTeamTotalAway);
//            txtTeamTotalAway.setText(Common.getInstance().gameDetailInfo.away);
//
//            LinearLayout lnTeamTotalGoalContainer = (LinearLayout) vTeamTotalGoals.findViewById(R.id.lnTeamTotalGoalsContainer);
//            lnTeamTotalGoalContainer.removeAllViews();
//
//            ArrayList<String> arrHandicap = new ArrayList<String>();
//            for (int i = 0; i < Common.getInstance().arrTeamTotalGoals.size(); i++) {
//                int exist = 0;
//                for (int j = 0; j < arrHandicap.size(); j++) {
//                    if (Common.getInstance().arrTeamTotalGoals.get(i).handicap.equals(arrHandicap.get(j))) {
//                        exist = 1;
//                        break;
//                    }
//                }
//                if (exist == 0) {
//                    arrHandicap.add(Common.getInstance().arrTeamTotalGoals.get(i).handicap);
//                }
//            }
//
//            for (int i = 0; i < arrHandicap.size(); i++) {
//                String home_over = "";
//                String home_under = "";
//                String away_over = "";
//                String away_under = "";
//
//                String home_over_odd = "";
//                String home_under_odd = "";
//                String away_over_odd = "";
//                String away_under_odd = "";
//                for (int j = 0; j < Common.getInstance().arrTeamTotalGoals.size(); j++) {
//                    if (Common.getInstance().arrTeamTotalGoals.get(j).handicap.equals(arrHandicap.get(i))) {
//                        if (Common.getInstance().arrTeamTotalGoals.get(j).name.contains(Common.getInstance().gameDetailInfo.home)) {
//                            if (Common.getInstance().arrTeamTotalGoals.get(j).name.contains("Over")) {
//                                home_over = "Over " + arrHandicap.get(i);
//                                home_over_odd = Common.getInstance().arrTeamTotalGoals.get(j).odd;
//                            } else {
//                                home_under = "Under " + arrHandicap.get(i);
//                                home_under_odd = Common.getInstance().arrTeamTotalGoals.get(j).odd;
//                            }
//                        } else if (Common.getInstance().arrTeamTotalGoals.get(j).name.contains(Common.getInstance().gameDetailInfo.away)) {
//                            if (Common.getInstance().arrTeamTotalGoals.get(j).name.contains("Over")) {
//                                away_over = "Over " + arrHandicap.get(i);
//                                away_over_odd = Common.getInstance().arrTeamTotalGoals.get(j).odd;
//                            } else {
//                                away_under = "Under " + arrHandicap.get(i);
//                                away_under_odd = Common.getInstance().arrTeamTotalGoals.get(j).odd;
//                            }
//                        }
//                    }
//                }
//
//                final View vTeamTotalGoalsItem = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.team_total_goals_item, null);
//                final TextView txtTeamTotalHomeOver = (TextView) vTeamTotalGoalsItem.findViewById(R.id.txtTeamTotalHomeOver);
//                txtTeamTotalHomeOver.setText(home_over);
//                final TextView txtTeamTotalHomeOverOdd = (TextView) vTeamTotalGoalsItem.findViewById(R.id.txtTeamTotalHomeOverOdd);
//                txtTeamTotalHomeOverOdd.setText(home_over_odd);
//                if ((!home_over.equals("")) && (!home_over_odd.equals(""))) {
//                    LinearLayout lnTeamTotalHomeOver = (LinearLayout) vTeamTotalGoalsItem.findViewById(R.id.lnTeamTotalHomeOver);
//                    lnTeamTotalHomeOver.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                            tipResultDialog.setCancelable(true);
//                            tipResultDialog.setOddStyle(Common.getInstance().gameDetailInfo.home + " " + txtTeamTotalHomeOver.getText().toString() + " - " + txtTeamTotalHomeOverOdd.getText().toString());
//                            tipResultDialog.setMarketStyle("Team Total Goals");
//                            mSelectedMarketStyle = "Team Total Goals";
//                            mSelectedOdd = txtTeamTotalHomeOverOdd.getText().toString();
//                            mSelectedOddStyle = Common.getInstance().gameDetailInfo.home + " " + txtTeamTotalHomeOver.getText().toString();
//                            tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                            tipResultDialog.setListener(mTipResultListener);
//                            tipResultDialog.show();
//                        }
//                    });
//                }
//
//                final TextView txtTeamTotalHomeUnder = (TextView) vTeamTotalGoalsItem.findViewById(R.id.txtTeamTotalHomeUnder);
//                txtTeamTotalHomeUnder.setText(home_under);
//                final TextView txtTeamTotalHomeUnderOdd = (TextView) vTeamTotalGoalsItem.findViewById(R.id.txtTeamTotalHomeUnderOdd);
//                txtTeamTotalHomeUnderOdd.setText(home_under_odd);
//                if ((!home_under.equals("")) && (!home_under_odd.equals(""))) {
//                    LinearLayout lnTeamTotalHomeUnder = (LinearLayout) vTeamTotalGoalsItem.findViewById(R.id.lnTeamTotalHomeUnder);
//                    lnTeamTotalHomeUnder.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                            tipResultDialog.setCancelable(true);
//                            tipResultDialog.setOddStyle(Common.getInstance().gameDetailInfo.home + " " + txtTeamTotalHomeUnder.getText().toString() + " - " + txtTeamTotalHomeUnderOdd.getText().toString());
//                            tipResultDialog.setMarketStyle("Team Total Goals");
//                            mSelectedMarketStyle = "Team Total Goals";
//                            mSelectedOdd = txtTeamTotalHomeUnderOdd.getText().toString();
//                            mSelectedOddStyle = Common.getInstance().gameDetailInfo.home + " " + txtTeamTotalHomeUnder.getText().toString();
//                            tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                            tipResultDialog.setListener(mTipResultListener);
//                            tipResultDialog.show();
//                        }
//                    });
//                }
//
//                final TextView txtTeamTotalAwayOver = (TextView) vTeamTotalGoalsItem.findViewById(R.id.txtTeamTotalAwayOver);
//                txtTeamTotalAwayOver.setText(away_over);
//                final TextView txtTeamTotalAwayOverOdd = (TextView) vTeamTotalGoalsItem.findViewById(R.id.txtTeamTotalAwayOverOdd);
//                txtTeamTotalAwayOverOdd.setText(away_over_odd);
//                if ((!away_over.equals("")) && (!away_over_odd.equals(""))) {
//                    LinearLayout lnTeamTotalAwayOver = (LinearLayout) vTeamTotalGoalsItem.findViewById(R.id.lnTeamTotalAwayOver);
//                    lnTeamTotalAwayOver.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                            tipResultDialog.setCancelable(true);
//                            tipResultDialog.setOddStyle(Common.getInstance().gameDetailInfo.away + " " + txtTeamTotalAwayOver.getText().toString() + " - " + txtTeamTotalAwayOverOdd.getText().toString());
//                            tipResultDialog.setMarketStyle("Team Total Goals");
//                            mSelectedMarketStyle = "Team Total Goals";
//                            mSelectedOdd = txtTeamTotalAwayOverOdd.getText().toString();
//                            mSelectedOddStyle = Common.getInstance().gameDetailInfo.away + " " + txtTeamTotalAwayOver.getText().toString();
//                            tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                            tipResultDialog.setListener(mTipResultListener);
//                            tipResultDialog.show();
//                        }
//                    });
//                }
//
//                final TextView txtTeamTotalAwayUnder = (TextView) vTeamTotalGoalsItem.findViewById(R.id.txtTeamTotalAwayUnder);
//                txtTeamTotalAwayUnder.setText(away_under);
//                final TextView txtTeamTotalAwayUnderOdd = (TextView) vTeamTotalGoalsItem.findViewById(R.id.txtTeamTotalAwayUnderOdd);
//                txtTeamTotalAwayUnderOdd.setText(away_under_odd);
//                if ((!away_under.equals("")) && (!away_under_odd.equals(""))) {
//                    LinearLayout lnTeamTotalAwayUnder = (LinearLayout) vTeamTotalGoalsItem.findViewById(R.id.lnTeamTotalAwayUnder);
//                    lnTeamTotalAwayUnder.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                            tipResultDialog.setCancelable(true);
//                            tipResultDialog.setOddStyle(Common.getInstance().gameDetailInfo.away + " " + txtTeamTotalAwayUnder.getText().toString() + " - " + txtTeamTotalAwayUnderOdd.getText().toString());
//                            tipResultDialog.setMarketStyle("Team Total Goals");
//                            mSelectedMarketStyle = "Team Total Goals";
//                            mSelectedOdd = txtTeamTotalAwayUnderOdd.getText().toString();
//                            mSelectedOddStyle = Common.getInstance().gameDetailInfo.away + " " + txtTeamTotalAwayUnder.getText().toString();
//                            tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                            tipResultDialog.setListener(mTipResultListener);
//                            tipResultDialog.show();
//                        }
//                    });
//                }
//
//                lnTeamTotalGoalContainer.addView(vTeamTotalGoalsItem);
//
//            }
//            lnContainer.addView(vTeamTotalGoals);
//        }
//
//        //////////////Match Total Goals///////
//        if (Common.getInstance().arrMatchTotalGoals.size() > 0) {
//            final View vMatchTotalGoals = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.match_total_goal, null);
//            TextView txtMatchTotalGoals1 = (TextView) vMatchTotalGoals.findViewById(R.id.txtMatchTotalGoals);
//            txtMatchTotalGoals1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LinearLayout lnContent = (LinearLayout) vMatchTotalGoals.findViewById(R.id.lnContent);
//                    mMatchResultTotalGoalsShow = !mMatchResultTotalGoalsShow;
//                    if (mMatchResultTotalGoalsShow == true) {
//                        lnContent.setVisibility(View.GONE);
//                    } else {
//                        lnContent.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//            LinearLayout lnMatchTotalGoalContainer = (LinearLayout) vMatchTotalGoals.findViewById(R.id.lnMatchTotalGoalContainer);
//            lnMatchTotalGoalContainer.removeAllViews();
//
//            for (int i = 0; i < Common.getInstance().arrMatchTotalGoals.size(); i++) {
//                final TeamTotalGoalInfo matchTotalGoalInfo = Common.getInstance().arrMatchTotalGoals.get(i);
//                final View vMatchTotalGoalsItem = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.match_total_goal_item, null);
//                TextView txtMatchTotalGoals = (TextView) vMatchTotalGoalsItem.findViewById(R.id.txtMatchTotalGoals);
//                txtMatchTotalGoals.setText(matchTotalGoalInfo.name + " " + matchTotalGoalInfo.handicap);
//                TextView txtMatchTotalGoalsOdd = (TextView) vMatchTotalGoalsItem.findViewById(R.id.txtMatchTotalGoalsOdd);
//                txtMatchTotalGoalsOdd.setText(matchTotalGoalInfo.odd);
//                LinearLayout lnMatchTotalGoals = (LinearLayout) vMatchTotalGoalsItem.findViewById(R.id.lnMatchTotalGoals);
//                lnMatchTotalGoals.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                        tipResultDialog.setCancelable(true);
//                        tipResultDialog.setOddStyle(matchTotalGoalInfo.name + " " + matchTotalGoalInfo.handicap + " - " + matchTotalGoalInfo.odd);
//                        tipResultDialog.setMarketStyle("Match Result / Total Goals");
//                        mSelectedMarketStyle = "Match Result / Total Goals";
//                        mSelectedOdd = matchTotalGoalInfo.odd;
//                        mSelectedOddStyle = matchTotalGoalInfo.name + " " + matchTotalGoalInfo.handicap;
//                        tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                        tipResultDialog.setListener(mTipResultListener);
//                        tipResultDialog.show();
//                    }
//                });
//                lnMatchTotalGoalContainer.addView(vMatchTotalGoalsItem);
//            }
//
//            lnContainer.addView(vMatchTotalGoals);
//        }
//
//        //////////////Total Goals BTS///////
//        if (Common.getInstance().arrTotalGoalsBTS.size() > 0) {
//            final View vTotalGoalsBTS = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.total_goal_bts, null);
//            TextView txtTotalGoalsBTS = (TextView) vTotalGoalsBTS.findViewById(R.id.txtTotalGoalsBTS);
//            txtTotalGoalsBTS.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LinearLayout lnContent = (LinearLayout) vTotalGoalsBTS.findViewById(R.id.lnContent);
//                    mTotalGoalsBTSShow = !mTotalGoalsBTSShow;
//                    if (mTotalGoalsBTSShow == true) {
//                        lnContent.setVisibility(View.GONE);
//                    } else {
//                        lnContent.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//            LinearLayout lnTotalGoalBTSContainer = (LinearLayout) vTotalGoalsBTS.findViewById(R.id.lnTotalGoalBTSContainer);
//            lnTotalGoalBTSContainer.removeAllViews();
//
//            for (int i = 0; i < Common.getInstance().arrTotalGoalsBTS.size(); i++) {
//                final TeamTotalGoalInfo totalGoalBTSInfo = Common.getInstance().arrTotalGoalsBTS.get(i);
//                final View vTotalGoalsBTSItem = LayoutInflater.from(GameDetailActivity.this).inflate(R.layout.total_goal_bts_item, null);
//                TextView txtMatchTotalGoals = (TextView) vTotalGoalsBTSItem.findViewById(R.id.txtMatchTotalGoals);
//                txtMatchTotalGoals.setText(totalGoalBTSInfo.name + " " + totalGoalBTSInfo.handicap);
//                TextView txtMatchTotalGoalsOdd = (TextView) vTotalGoalsBTSItem.findViewById(R.id.txtMatchTotalGoalsOdd);
//                txtMatchTotalGoalsOdd.setText(totalGoalBTSInfo.odd);
//                LinearLayout lnMatchTotalGoals = (LinearLayout) vTotalGoalsBTSItem.findViewById(R.id.lnMatchTotalGoals);
//                lnMatchTotalGoals.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        TipResultDialog tipResultDialog = new TipResultDialog(GameDetailActivity.this);
//                        tipResultDialog.setCancelable(true);
//                        tipResultDialog.setOddStyle(totalGoalBTSInfo.name + " " + totalGoalBTSInfo.handicap + " - " + totalGoalBTSInfo.odd);
//                        tipResultDialog.setMarketStyle("Total Goals / Both Teams to Score");
//                        mSelectedMarketStyle = "Total Goals / Both Teams to Score";
//                        mSelectedOdd = totalGoalBTSInfo.odd;
//                        mSelectedOddStyle = totalGoalBTSInfo.name + " " + totalGoalBTSInfo.handicap;
//                        tipResultDialog.setMatchName(Common.getInstance().gameDetailInfo.home + " vs " + Common.getInstance().gameDetailInfo.away);
//                        tipResultDialog.setListener(mTipResultListener);
//                        tipResultDialog.show();
//                    }
//                });
//                lnTotalGoalBTSContainer.addView(vTotalGoalsBTSItem);
//            }
//
//            lnContainer.addView(vTotalGoalsBTS);
//        }
    }

    private TipResultDialog.OnCancelOrderListener mTipResultListener = new TipResultDialog.OnCancelOrderListener() {
        @Override
        public void OnCancel(String strReason) {
            mTipAmount = strReason;
            mProgDlg.show();
            new Thread(mLoadRunnable_place).start();

        }
    };
    private Runnable mLoadRunnable_place = new Runnable() {
        @Override
        public void run() {
            String event_id = Common.getInstance().gameDetailInfo.event_id;
            String game_style = "soccer";
            int ret = NetworkManager.getManager().placeTip(Common.getInstance().user_id, event_id, game_style, mSelectedMarketStyle, mSelectedOddStyle, mSelectedOdd, mTipAmount, Common.getInstance().gameDetailInfo.home, Common.getInstance().gameDetailInfo.away, Common.getInstance().gameDetailInfo.start_date_time, Common.getInstance().gameDetailInfo.league_name);
            mLoadHandler_Place.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler_Place = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                Toast.makeText(GameDetailActivity.this, "Your tip was placed successfully!", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {
                Toast.makeText(GameDetailActivity.this, "You can't place the tip because the game was already started.", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 0) {
                Toast.makeText(GameDetailActivity.this, "The same tip is already existed. Please try to place other tip", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(GameDetailActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }

        }
    };
}
