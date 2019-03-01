package com.diretta.livescore.model;

/**
 * Created by suc on 5/15/2018.
 */

public class RankInfo {
    public int position;
    public String user_id;
    public String name;
    public String profile_img;
    public String point;
    public String percent;
    public String placed_tip_count;
    public String loses_count;

    public RankInfo(){
        this.position = 0;
        this.user_id = "";
        this.name = "";
        this.profile_img = "";
        this.point = "";
        this.percent = "";
        this.placed_tip_count = "";
        this.loses_count = "";
    }
    public RankInfo(int position, String user_id, String name, String profile_img, String point, String percent, String placed_tip_count, String loses_count){
        this.position = position;
        this.user_id = user_id;
        this.name = name;
        this.profile_img = profile_img;
        this.point = point;
        this.percent = percent;
        this.placed_tip_count = placed_tip_count;
        this.loses_count = loses_count;
    }
}
