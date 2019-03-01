package com.diretta.livescore.model;

/**
 * Created by shevchenko on 2015-11-29.
 */
public class LeagueInfo {

    public String league_name;
    public String league_id;
    public String country_image;
    public LeagueInfo()
    {
        this.league_id = "";
        this.league_name = "";
        this.country_image = "";
    }
    public LeagueInfo(String league_name, String league_id, String country_image)
    {
        this.league_id = league_id;
        this.league_name = league_name;
        this.country_image = country_image;
    }
}
