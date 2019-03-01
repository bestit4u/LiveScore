package com.diretta.livescore.model;

/**
 * Created by suc on 5/15/2018.
 */

public class GameListInfo {
    public String league_id;
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

    public GameListInfo(){
        this.league_id = "";
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
    }
    public GameListInfo(String league_id, String home, String home_image, String away, String away_image, String time, String event_id, String start_date_time, String home_odd, String draw_odd, String away_odd){
        this.league_id = league_id;
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
    }
}
