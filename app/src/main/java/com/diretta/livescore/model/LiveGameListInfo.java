package com.diretta.livescore.model;

import java.io.Serializable;

/**
 * Created by suc on 5/15/2018.
 */
@SuppressWarnings("serial")

public class LiveGameListInfo  implements Serializable{
    public String league_id;
    public String home;
    public String home_image;
    public String away;
    public String away_image;
    public String time;
    public String event_id;
    public String static_id;
    public String status;
    public String score;
    public String stadium;
    public String home_odd;
    public String draw_odd;
    public String away_odd;

    public LiveGameListInfo(){
        this.league_id = "";
        this.home = "";
        this.home_image = "";
        this.away = "";
        this.away_image = "";
        this.time = "";
        this.event_id = "";
        this.static_id = "";
        this.status = "";
        this.score = "";
        this.stadium = "";
        this.home_odd = "";
        this.draw_odd = "";
        this.away_odd = "";
    }
    public LiveGameListInfo(String league_id, String home, String home_image, String away, String away_image, String time, String event_id, String static_id, String status, String stadium, String home_odd, String draw_odd, String away_odd, String score){
        this.league_id = league_id;
        this.home = home;
        this.home_image = home_image;
        this.away = away;
        this.away_image = away_image;
        this.time = time;
        this.event_id = event_id;
        this.static_id = static_id;
        this.status = status;
        this.score = score;
        this.stadium = stadium;
        this.home_odd = home_odd;
        this.draw_odd = draw_odd;
        this.away_odd = away_odd;
    }
}
