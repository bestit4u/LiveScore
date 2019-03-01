package com.diretta.livescore.model;

/**
 * Created by shevchenko on 2015-11-29.
 */
public class CorrectScoreInfo {

    public String name;
    public String odd;

    public CorrectScoreInfo()
    {
        this.name = "";
        this.odd = "";
    }
    public CorrectScoreInfo(String name, String odd)
    {
        this.name = name;
        this.odd = odd;
    }
}
