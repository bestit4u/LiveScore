package com.diretta.livescore.common;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.diretta.livescore.GameListActivity;
import com.diretta.livescore.LivescoreActivity;
import com.diretta.livescore.model.AlternativeTotalGoalInfo;
import com.diretta.livescore.model.CorrectScoreInfo;
import com.diretta.livescore.model.CountryInfo;
import com.diretta.livescore.model.GameDetailInfo;
import com.diretta.livescore.model.GameListInfo;
import com.diretta.livescore.model.LeagueInfo;
import com.diretta.livescore.model.LiveGameListInfo;
import com.diretta.livescore.model.LivescoreEventInfo;
import com.diretta.livescore.model.MatchInfo;
import com.diretta.livescore.model.PlacedTipInfo;
import com.diretta.livescore.model.RankInfo;
import com.diretta.livescore.model.TeamTotalGoalInfo;
import com.diretta.livescore.model.UserInfo;
import com.diretta.livescore.util.Inventory;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shevchenko on 2015-11-26.
 */
public class Common {
    private static Common s_instance = null;

    public static Common getInstance() {
        if (s_instance == null) {
            s_instance = new Common();

        }
        synchronized (s_instance) {
            return s_instance;
        }
    }
    public ArrayList<MatchInfo> arrMatches;
    public ArrayList<LeagueInfo> arrLeagues;
    public ArrayList<CountryInfo> arrCountries;
    public ArrayList<PlacedTipInfo> arrCurrentTips;
    public ArrayList<PlacedTipInfo> arrEndTips;
    public ArrayList<RankInfo> arrThisMonthRank;
    public ArrayList<RankInfo> arrLastMonthRank;
    public ArrayList<String> arrDateList;
    public ArrayList<GameListInfo> arrGameListInfo;
    public ArrayList<GameListInfo> arrPopularGameListInfo;
    public ArrayList<LiveGameListInfo> arrLiveGameListInfo;
    public ArrayList<LeagueInfo> arrLiveLeagues;
    public String popular_league_name;
    public UserInfo userInfo;
    public boolean bClose;
    public int status;
    public String user_id;
    public String soccer_id;
    public String is_expert;
    public DisplayImageOptions goodsDisplayImageOptions;
    public GameDetailInfo gameDetailInfo;
    public ArrayList<CorrectScoreInfo> arrCorrectScores;
    public ArrayList<AlternativeTotalGoalInfo> arrAlternativeTotalGoals;
    public ArrayList<CorrectScoreInfo> arrExactTotalGoals;
    public ArrayList<TeamTotalGoalInfo> arrTeamTotalGoals;
    public ArrayList<TeamTotalGoalInfo> arrMatchTotalGoals;
    public ArrayList<TeamTotalGoalInfo> arrTotalGoalsBTS;
    public PlacedTipInfo mTipDictionary;
    public String mTip;
    public boolean free;
    public Inventory inv;
    public JSONObject tipOfDayJson;
    public JSONObject feedJson;
    public JSONObject searchJson;
    public JSONObject followManageJson;
    public JSONObject otherProfileJson;
    public JSONObject settingProfileJson;
    public JSONObject notificationJson;
    public String social;
    public String newSocial;
    public ArrayList<LivescoreEventInfo> arrLiveEventInfo;

    public Common()
    {
        bClose = false;
        status = 0;
        free = true;
        arrMatches = new ArrayList<MatchInfo>();
        arrLeagues = new ArrayList<LeagueInfo>();
        arrCountries = new ArrayList<CountryInfo>();
        user_id = "";
        userInfo = new UserInfo();
        arrCurrentTips = new ArrayList<PlacedTipInfo>();
        arrEndTips = new ArrayList<PlacedTipInfo>();
        arrThisMonthRank = new ArrayList<RankInfo>();
        arrLastMonthRank = new ArrayList<RankInfo>();
        arrDateList = new ArrayList<String>();
        arrGameListInfo = new ArrayList<GameListInfo>();
        gameDetailInfo = new GameDetailInfo();
        arrCorrectScores = new ArrayList<CorrectScoreInfo>();
        arrAlternativeTotalGoals = new ArrayList<AlternativeTotalGoalInfo>();
        arrExactTotalGoals = new ArrayList<CorrectScoreInfo>();
        arrTeamTotalGoals = new ArrayList<TeamTotalGoalInfo>();
        arrMatchTotalGoals = new ArrayList<TeamTotalGoalInfo>();
        arrTotalGoalsBTS = new ArrayList<TeamTotalGoalInfo>();
        mTipDictionary = new PlacedTipInfo();
        mTip = "0";
        is_expert = "";
        tipOfDayJson = new JSONObject();
        feedJson = new JSONObject();
        searchJson = new JSONObject();
        followManageJson = new JSONObject();
        otherProfileJson = new JSONObject();
        settingProfileJson = new JSONObject();
        notificationJson = new JSONObject();
        popular_league_name = new String();
        arrPopularGameListInfo = new ArrayList<GameListInfo>();
        social = new String();
        newSocial = new String();
        arrLiveGameListInfo = new ArrayList<LiveGameListInfo>();
        arrLiveLeagues = new ArrayList<LeagueInfo>();
        arrLiveEventInfo = new ArrayList<LivescoreEventInfo>();
    }

    public final static int 		LEFTMENU_ANITIME = 250;

}
