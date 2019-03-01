package com.diretta.livescore.model;

/**
 * Created by shevchenko on 2015-11-29.
 */
public class AlternativeTotalGoalInfo {

    public String handicap;
    public String over;
    public String under;

    public AlternativeTotalGoalInfo()
    {
        this.handicap = "";
        this.over = "";
        this.under = "";
    }
    public AlternativeTotalGoalInfo(String handicap, String over, String under)
    {
        this.handicap = handicap;
        this.over = over;
        this.under = under;
    }
}
