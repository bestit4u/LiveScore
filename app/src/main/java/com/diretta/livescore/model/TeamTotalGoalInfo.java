package com.diretta.livescore.model;

/**
 * Created by shevchenko on 2015-11-29.
 */
public class TeamTotalGoalInfo {

    public String name;
    public String odd;
    public String handicap;

    public TeamTotalGoalInfo()
    {
        this.name = "";
        this.odd = "";
        this.handicap = "";
    }
    public TeamTotalGoalInfo(String name, String odd, String handicap)
    {
        this.name = name;
        this.odd = odd;
        this.handicap = handicap;
    }
}
