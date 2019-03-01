package com.diretta.livescore.model;

/**
 * Created by shevchenko on 2015-11-29.
 */
public class MatchDetailInfo {

    public String home_name;
    public String home_image;
    public String away_name;
    public String away_image;
    public String start_date;
    public String start_time;
    public String winner;
    public int winner_home_result;
    public int winner_draw_result;
    public int winner_away_result;
    public String total_goal_handicap;
    public String total_goal;
    public int total_goal_under;
    public int total_goal_over;
    public String both_team_score;
    public int both_team_score_yes_value;
    public int both_team_score_no_value;
    public String full_time_result;
    public String full_time_score;

    public MatchDetailInfo()
    {
        this.home_name = "";
        this.home_image = "";
        this.away_name = "";
        this.away_image = "";
        this.start_date = "";
        this.start_time = "";
        this.winner = "";
        this.winner_home_result = 0;
        this.winner_draw_result = 0;
        this.winner_away_result = 0;
        this.total_goal_handicap = "";
        this.total_goal = "";
        this.total_goal_under = 0;
        this.total_goal_over = 0;
        this.both_team_score = "";
        this.both_team_score_yes_value = 0;
        this.both_team_score_no_value = 0;
        this.full_time_result = "";
        this.full_time_score = "";
    }
    public MatchDetailInfo(String home_name, String home_image, String away_name, String away_image, String start_date, String start_time, String winner, int winner_home_result, int winner_draw_result, int winner_away_result, String total_goal_handicap, String total_goal, int total_goal_under, int total_goal_over, String both_team_score, int both_team_score_yes_value, int both_team_score_no_value, String full_time_result, String full_time_score)
    {
        this.home_name = home_name;
        this.home_image = home_image;
        this.away_name = away_name;
        this.away_image = away_image;
        this.start_date = start_date;
        this.start_time = start_time;
        this.winner = winner;
        this.winner_home_result = winner_home_result;
        this.winner_draw_result = winner_draw_result;
        this.winner_away_result = winner_away_result;
        this.total_goal_handicap = total_goal_handicap;
        this.total_goal = total_goal;
        this.total_goal_under = total_goal_under;
        this.total_goal_over = total_goal_over;
        this.both_team_score = both_team_score;
        this.both_team_score_yes_value = both_team_score_yes_value;
        this.both_team_score_no_value = both_team_score_no_value;
        this.full_time_result = full_time_result;
        this.full_time_score = full_time_score;
    }
}
