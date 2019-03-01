package com.diretta.livescore.model;

/**
 * Created by suc on 5/15/2018.
 */

public class UserInfo {
    public String id;
    public String full_name;
    public String email;
    public String profile_img;
    public String percent;
    public String point;
    public String is_expert;
    public String bio;
    public String followers_count;
    public String following_count;

    public UserInfo(){
        this.id = "";
        this.full_name = "";
        this.email = "";
        this.profile_img = "";
        this.percent = "";
        this.point = "";
        this.is_expert = "";
        this.bio = "";
        this.followers_count = "";
        this.following_count = "";
    }
    public UserInfo(String id, String full_name, String email, String profile_img, String percent, String point, String is_expert, String bio, String followers_count, String following_count){
        this.id = id;
        this.full_name = full_name;
        this.email = email;
        this.profile_img = profile_img;
        this.percent = percent;
        this.point = point;
        this.is_expert = is_expert;
        this.bio = bio;
        this.followers_count = followers_count;
        this.following_count = following_count;
    }
}
