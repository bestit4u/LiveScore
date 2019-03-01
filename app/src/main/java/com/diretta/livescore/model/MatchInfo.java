package com.diretta.livescore.model;

/**
 * Created by shevchenko on 2015-11-29.
 */
public class MatchInfo {

    public String league_id;
    public String home;
    public String home_image;
    public String away;
    public String away_image;
    public String time;
    public String event_id;
    public MatchInfo()
    {
        this.league_id = "";
        this.home = "";
        this.away = "";
        this.home_image = "";
        this.away_image = "";
        this.time = "";
        this.event_id = "";
    }
    public MatchInfo(String league_id, String home, String home_image, String away, String away_image, String time, String event_id)
    {
        this.league_id = league_id;
        this.home = home;
        this.home_image = home_image;
        this.away = away;
        this.away_image = away_image;
        this.time = time;
        this.event_id = event_id;
    }
}
