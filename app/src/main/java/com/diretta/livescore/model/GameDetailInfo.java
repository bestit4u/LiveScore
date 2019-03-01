package com.diretta.livescore.model;

/**
 * Created by suc on 5/15/2018.
 */

public class GameDetailInfo {
    public String league_id;
    public String league_name;
    public String home;
    public String home_image;
    public String away;
    public String away_image;
    public String time;
    public String event_id;
    public String start_date_time;
    public String home_odd;
    public String draw_odd;
    public String away_odd;
    public String double_chance_1X;
    public String double_chance_X2;
    public String double_chance_12;
    public String total_goal_over;
    public String total_goal_under;
    public String bts_yes;
    public String bts_no;
    public String asian_home_odd;
    public String asian_away_odd;
    public String goal_line_over;
    public String goal_line_under;

    public GameDetailInfo(){
        this.league_id = "";
        this.league_name = "";
        this.home = "";
        this.home_image = "";
        this.away = "";
        this.away_image = "";
        this.time = "";
        this.event_id = "";
        this.start_date_time = "";
        this.home_odd = "";
        this.draw_odd = "";
        this.away_odd = "";
        this.double_chance_1X = "";
        this.double_chance_X2 = "";
        this.double_chance_12 = "";
        this.total_goal_over = "";
        this.total_goal_under = "";
        this.bts_yes = "";
        this.bts_no = "";
        this.asian_home_odd = "";
        this.asian_away_odd = "";
        this.goal_line_over = "";
        this.goal_line_under = "";
    }
    public GameDetailInfo(String league_id, String league_name, String home, String home_image, String away, String away_image, String time, String event_id, String start_date_time, String home_odd, String draw_odd, String away_odd, String double_chance_1X, String double_chance_X2, String double_chance_12, String total_goal_over, String total_goal_under, String bts_yes, String bts_no, String asian_home_odd, String asian_away_odd, String goal_line_over, String goal_line_under){
        this.league_id = league_id;
        this.league_name = league_name;
        this.home = home;
        this.home_image = home_image;
        this.away = away;
        this.away_image = away_image;
        this.time = time;
        this.event_id = event_id;
        this.start_date_time = start_date_time;
        this.home_odd = home_odd;
        this.draw_odd = draw_odd;
        this.away_odd = away_odd;
        this.double_chance_1X = double_chance_1X;
        this.double_chance_X2 = double_chance_X2;
        this.double_chance_12 = double_chance_12;
        this.total_goal_over = total_goal_over;
        this.total_goal_under = total_goal_under;
        this.bts_yes = bts_yes;
        this.bts_no = bts_no;
        this.asian_home_odd = asian_home_odd;
        this.asian_away_odd = asian_away_odd;
        this.goal_line_over = goal_line_over;
        this.goal_line_under = goal_line_under;
    }
}
