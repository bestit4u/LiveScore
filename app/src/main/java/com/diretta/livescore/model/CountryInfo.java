package com.diretta.livescore.model;

/**
 * Created by shevchenko on 2015-11-29.
 */
public class CountryInfo {

    public String country;
    public boolean bShow;

    public CountryInfo()
    {
        this.country = "";
        this.bShow = false;
    }
    public CountryInfo(String country, boolean bShow)
    {
        this.country = country;
        this.bShow = bShow;
    }
}
