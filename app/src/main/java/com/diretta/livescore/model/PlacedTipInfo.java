package com.diretta.livescore.model;

/**
 * Created by suc on 5/15/2018.
 */

public class PlacedTipInfo {
    public String event_id;
    public String home_name;
    public String away_name;
    public String start_date_time;
    public String league_name;
    public String market_style;
    public String odd_style;
    public String odd;
    public String tip_amount;
    public String home_image;
    public String away_image;
    public String result_status;

    public PlacedTipInfo(){
        this.event_id = "";
        this.home_name = "";
        this.away_name = "";
        this.start_date_time = "";
        this.league_name = "";
        this.market_style = "";
        this.odd_style = "";
        this.odd = "";
        this.tip_amount = "";
        this.home_image = "";
        this.away_image = "";
        this.result_status = "";
    }

    public PlacedTipInfo(String event_id, String home_name, String away_name, String start_date_time, String league_name, String market_style, String odd_style, String odd, String tip_amount, String home_image, String away_image, String result_status){
        this.event_id = event_id;
        this.home_name = home_name;
        this.away_name = away_name;
        this.start_date_time = start_date_time;
        this.league_name = league_name;
        this.market_style = market_style;
        this.odd_style = odd_style;
        this.odd = odd;
        this.tip_amount = tip_amount;
        this.home_image = home_image;
        this.away_image = away_image;
        this.result_status = result_status;
    }
}
