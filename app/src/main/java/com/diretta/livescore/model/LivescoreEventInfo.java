package com.diretta.livescore.model;

/**
 * Created by shevchenko on 2015-11-29.
 */
public class LivescoreEventInfo {

    public String event_min;
    public String event_player;
    public String type;
    public String event_team;
    public String event_assist;

    public LivescoreEventInfo()
    {
        this.event_min = "";
        this.event_player = "";
        this.type = "";
        this.event_team = "";
        this.event_assist = "";
    }
    public LivescoreEventInfo(String event_min, String event_player, String type, String event_team, String event_assist)
    {
        this.event_min = event_min;
        this.event_player = event_player;
        this.type = type;
        this.event_team = event_team;
        this.event_assist = event_assist;
    }
}
