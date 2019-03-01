package com.diretta.livescore.net;

import android.util.Log;

import com.diretta.livescore.common.Common;
import com.diretta.livescore.model.AlternativeTotalGoalInfo;
import com.diretta.livescore.model.CorrectScoreInfo;
import com.diretta.livescore.model.GameDetailInfo;
import com.diretta.livescore.model.GameListInfo;
import com.diretta.livescore.model.LeagueInfo;
import com.diretta.livescore.model.LiveGameListInfo;
import com.diretta.livescore.model.LivescoreEventInfo;
import com.diretta.livescore.model.MatchInfo;
import com.diretta.livescore.model.PlacedTipInfo;
import com.diretta.livescore.model.RankInfo;
import com.diretta.livescore.model.TeamTotalGoalInfo;
import com.diretta.livescore.model.UserInfo;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NetworkManager {
	protected final static String UTF8 = "utf-8";
	protected Map<String, Object> _reqParams = null;
	
	private NetworkManager() {
		_reqParams = new LinkedHashMap<String, Object>();
	}
	private static NetworkManager s_instance = null;

	public static NetworkManager getManager() {
		if (s_instance == null) {
			s_instance = new NetworkManager();
		}
		synchronized (s_instance) {
			return s_instance;
		}
	}

    private final static String SERVER_URL = "http://80.211.81.236/webservice/";
    private final static String OLD_SERVER_URL = "https://tipyaapp.com/webservice/";
    protected final static String URL_PREDICTION 		    = "prediction.php";
    protected final static String URL_SIGNUP 		    = "user_register.php";
    protected final static String URL_ACTIVATE 		    = "activation_account.php";
    protected final static String URL_LOGIN 		    = "user_login.php";
    protected final static String URL_DELETE_PROFILE    = "delete_profile.php";
    protected final static String URL_GET_PROFILE    = "user_profile.php";
    protected final static String URL_GET_RANK    = "rank.php";
    protected final static String URL_GAME_LIST    = "gamelist.php";
    protected final static String URL_GAME_DETAIL = "game_detail.php";
    protected final static String URL_PLACE_TIP = "place_tip.php";
    protected final static String URL_PLACE_TIP_OF_DAY = "place_tip_of_day.php";
    protected final static String URL_TIP_OF_DAY = "tip_of_day.php";
    protected final static String URL_FEED = "feed.php";
    protected final static String URL_DISCOVER = "discover_search.php";
    protected final static String URL_FOLLOW = "follow.php";
    protected final static String URL_FOLLOW_MANAGEMENT = "follow_management.php";
    protected final static String URL_GET_OTHER_PROFILE    = "user_profile_other.php";
    protected final static String URL_PROFILE_SETTING    = "user_profile_setting.php";
    protected final static String URL_PLACE_PROFILE_SETTING    = "place_profile_setting1.php";
    protected final static String URL_PLACE_PROFILE_SETTING_IMAGE    = "place_profile_image.php";
    protected final static String URL_CHANGE_PASSWORD    = "change_password.php";
    protected final static String URL_NOTIFICATION    = "notification.php";
    protected final static String URL_FORGET_PASSWORD    = "forgot_password.php";
    protected final static String URL_SOCIAL    = "signup_social_android.php";
    protected final static String URL_STAR    = "star.php";
    protected final static String URL_LIVESCORE 		    = "livescore.php";
    protected final static String URL_LIVESCORE_Detail 		    = "livescore_detail.php";

    public int star(String user_id, String tip_id, String status){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(OLD_SERVER_URL + URL_STAR);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "user_id", user_id);  // Make your own key value pair
        BasicNameValuePair searchBasicNameValuePair = new BasicNameValuePair(
                "tip_id", tip_id);  // Make your own key value pair
        BasicNameValuePair statusBasicNameValuePair = new BasicNameValuePair(
                "status", status);  // Make your own key value pair
        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        nameValuePairList.add(searchBasicNameValuePair);
        nameValuePairList.add(statusBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int socialFacebook(String full_name, String email, String subUrl, String id ){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(OLD_SERVER_URL + URL_SOCIAL);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "full_name", full_name);  // Make your own key value pair
        BasicNameValuePair emailBasicNameValuePair = new BasicNameValuePair(
                "email", email);  // Make your own key value pair
        BasicNameValuePair subUrlBasicNameValuePair = new BasicNameValuePair(
                "subUrl", subUrl);  // Make your own key value pair
        BasicNameValuePair idBasicNameValuePair = new BasicNameValuePair(
                "id", id);  // Make your own key value pair
        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        nameValuePairList.add(emailBasicNameValuePair);
        nameValuePairList.add(subUrlBasicNameValuePair);
        nameValuePairList.add(idBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    Common.getInstance().user_id = obj1.getString("id");
                    Common.getInstance().is_expert = obj1.getString("is_expert");
                    Common.getInstance().social = "1";
                    Common.getInstance().newSocial = obj1.getString("new");
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int resetPassword(String email){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(SERVER_URL + URL_FORGET_PASSWORD);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "email", email);  // Make your own key value pair

        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int loadLivescore(String date){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(OLD_SERVER_URL + URL_LIVESCORE);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "current_date", date);  // Make your own key value pair

        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);

        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    JSONArray arrJson = obj1.getJSONArray("game_list");
                    JSONObject obj;
                    Common.getInstance().arrLiveGameListInfo.clear();
                    for(int i = 0; i < arrJson.length(); i++){
                        JSONObject objItem = arrJson.getJSONObject(i);
                        LiveGameListInfo info = new LiveGameListInfo(objItem.getString("league_id"), objItem.getString("home"), objItem.getString("home_image"), objItem.getString("away"), objItem.getString("away_image"), objItem.getString("time"), objItem.getString("event_id"), objItem.getString("static_id"), objItem.getString("status"), objItem.getString("stadium"), objItem.getString("home_odd"), objItem.getString("draw_odd"), objItem.getString("away_odd"), objItem.getString("score"));
                        Common.getInstance().arrLiveGameListInfo.add(info);
                    }
                    Common.getInstance().arrLiveLeagues.clear();
                    arrJson = obj1.getJSONArray("league_list");
                    for (int i = 0; i < arrJson.length(); i++) {
                        obj = arrJson.getJSONObject(i);

                        LeagueInfo info = new LeagueInfo(obj.getString("league_name"), obj.getString("league_id"), "");
                        Common.getInstance().arrLiveLeagues.add(info);
                    }
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public int loadLivescoreDetail(String static_id){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(OLD_SERVER_URL + URL_LIVESCORE_Detail);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "static_id", static_id);  // Make your own key value pair

        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);

        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    JSONArray arrJson = obj1.getJSONArray("event_array");
                    JSONObject obj;
                    Common.getInstance().arrLiveEventInfo.clear();
                    for(int i = 0; i < arrJson.length(); i++){
                        JSONObject objItem = arrJson.getJSONObject(i);
                        LivescoreEventInfo info = new LivescoreEventInfo(objItem.getString("event_min"), objItem.getString("event_player"), objItem.getString("type"), objItem.getString("event_team"), objItem.getString("event_assist"));
                        Common.getInstance().arrLiveEventInfo.add(info);
                    }
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }

    public int loadPrediction(String date){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(OLD_SERVER_URL + URL_PREDICTION);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "current_date", date);  // Make your own key value pair

        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);

        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    JSONArray arrJson = obj1.getJSONArray("game_list");
                    JSONObject obj;
                    Common.getInstance().arrPopularGameListInfo.clear();
                    for(int i = 0; i < arrJson.length(); i++){
                        JSONObject objItem = arrJson.getJSONObject(i);
                        GameListInfo info = new GameListInfo(objItem.getString("league_id"), objItem.getString("home"), objItem.getString("home_image"), objItem.getString("away"), objItem.getString("away_image"), objItem.getString("time"), objItem.getString("event_id"), objItem.getString("start_date_time"), objItem.getString("home_odd"), objItem.getString("draw_odd"), objItem.getString("away_odd"));
                        Common.getInstance().arrPopularGameListInfo.add(info);
                    }

                    arrJson = obj1.getJSONArray("league_list");
                    for (int i = 0; i < arrJson.length(); i++) {
                        obj = arrJson.getJSONObject(i);

                        LeagueInfo info = new LeagueInfo(obj.getString("league_name"), obj.getString("league_id"), obj.getString("country_image"));
                        Common.getInstance().arrLeagues.add(info);
                    }
                    Common.getInstance().popular_league_name = obj1.getString("popular_league_name");
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int signup(String full_name, String email, String password){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(SERVER_URL + URL_SIGNUP);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "full_name", full_name);  // Make your own key value pair
        BasicNameValuePair emailBasicNameValuePair = new BasicNameValuePair(
                "email", email);  // Make your own key value pair
        BasicNameValuePair passwordBasicNameValuePair = new BasicNameValuePair(
                "password", password);  // Make your own key value pair

        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        nameValuePairList.add(emailBasicNameValuePair);
        nameValuePairList.add(passwordBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    Common.getInstance().user_id = obj1.getString("id");
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int placeTip(String user_id, String event_id, String game_style, String market_style, String odd_style, String odd, String tip_amount, String home_name, String away_name, String start_date_time, String league_name){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(OLD_SERVER_URL + URL_PLACE_TIP);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "user_id", user_id);  // Make your own key value pair
        BasicNameValuePair eventIdBasicNameValuePair = new BasicNameValuePair(
                "event_id", event_id);  // Make your own key value pair
        BasicNameValuePair gameStyleBasicNameValuePair = new BasicNameValuePair(
                "game_style", game_style);  // Make your own key value pair
        BasicNameValuePair marketStyleBasicNameValuePair = new BasicNameValuePair(
                "market_style", market_style);
        BasicNameValuePair oddStyleBasicNameValuePair = new BasicNameValuePair(
                "odd_style", odd_style);
        BasicNameValuePair oddBasicNameValuePair = new BasicNameValuePair(
                "odd", odd);
        BasicNameValuePair tipAmountBasicNameValuePair = new BasicNameValuePair(
                "tip_amount", tip_amount);
        BasicNameValuePair homeNameBasicNameValuePair = new BasicNameValuePair(
                "home_name", home_name);
        BasicNameValuePair awayNameBasicNameValuePair = new BasicNameValuePair(
                "away_name", away_name);
        BasicNameValuePair timeBasicNameValuePair = new BasicNameValuePair(
                "start_date_time", start_date_time);
        BasicNameValuePair leagueNameBasicNameValuePair = new BasicNameValuePair(
                "league_name", league_name);
        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        nameValuePairList.add(eventIdBasicNameValuePair);
        nameValuePairList.add(gameStyleBasicNameValuePair);
        nameValuePairList.add(marketStyleBasicNameValuePair);
        nameValuePairList.add(oddStyleBasicNameValuePair);
        nameValuePairList.add(oddBasicNameValuePair);
        nameValuePairList.add(tipAmountBasicNameValuePair);
        nameValuePairList.add(homeNameBasicNameValuePair);
        nameValuePairList.add(awayNameBasicNameValuePair);
        nameValuePairList.add(timeBasicNameValuePair);
        nameValuePairList.add(leagueNameBasicNameValuePair);

        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    return 1;
                }else if(strRet.equals("started")){
                    return 2;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int placeTipOfDay(String user_id, String event_id, String game_style, String market_style, String odd_style, String odd, String tip_amount, String home_name, String away_name, String start_date_time, String league_name, String description){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(OLD_SERVER_URL + URL_PLACE_TIP_OF_DAY);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "user_id", user_id);  // Make your own key value pair
        BasicNameValuePair eventIdBasicNameValuePair = new BasicNameValuePair(
                "event_id", event_id);  // Make your own key value pair
        BasicNameValuePair gameStyleBasicNameValuePair = new BasicNameValuePair(
                "game_style", game_style);  // Make your own key value pair
        BasicNameValuePair marketStyleBasicNameValuePair = new BasicNameValuePair(
                "market_style", market_style);
        BasicNameValuePair oddStyleBasicNameValuePair = new BasicNameValuePair(
                "odd_style", odd_style);
        BasicNameValuePair oddBasicNameValuePair = new BasicNameValuePair(
                "odd", odd);
        BasicNameValuePair tipAmountBasicNameValuePair = new BasicNameValuePair(
                "tip_amount", tip_amount);
        BasicNameValuePair homeNameBasicNameValuePair = new BasicNameValuePair(
                "home_name", home_name);
        BasicNameValuePair awayNameBasicNameValuePair = new BasicNameValuePair(
                "away_name", away_name);
        BasicNameValuePair timeBasicNameValuePair = new BasicNameValuePair(
                "start_date_time", start_date_time);
        BasicNameValuePair leagueNameBasicNameValuePair = new BasicNameValuePair(
                "league_name", league_name);
        BasicNameValuePair descriptionBasicNameValuePair = new BasicNameValuePair(
                "description", description);
        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        nameValuePairList.add(eventIdBasicNameValuePair);
        nameValuePairList.add(gameStyleBasicNameValuePair);
        nameValuePairList.add(marketStyleBasicNameValuePair);
        nameValuePairList.add(oddStyleBasicNameValuePair);
        nameValuePairList.add(oddBasicNameValuePair);
        nameValuePairList.add(tipAmountBasicNameValuePair);
        nameValuePairList.add(homeNameBasicNameValuePair);
        nameValuePairList.add(awayNameBasicNameValuePair);
        nameValuePairList.add(timeBasicNameValuePair);
        nameValuePairList.add(leagueNameBasicNameValuePair);
        nameValuePairList.add(descriptionBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int rank(String user_id){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(OLD_SERVER_URL + URL_GET_RANK);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "user_id", user_id);  // Make your own key value pair

        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    JSONArray arrThisMonth = obj1.getJSONArray("this_month_rank");
                    Common.getInstance().arrThisMonthRank.clear();
                    for(int i = 0; i < arrThisMonth.length(); i++){
                        JSONObject objItem = arrThisMonth.getJSONObject(i);
                        RankInfo info = new RankInfo(i+1, objItem.getString("user_id"), objItem.getString("name"), objItem.getString("profile_img"), objItem.getString("point"), objItem.getString("percent"), objItem.getString("placed_tip_count"), objItem.getString("loses_count"));
                        Common.getInstance().arrThisMonthRank.add(info);
                    }

                    JSONArray arrLastMonth = obj1.getJSONArray("last_month_rank");
                    Common.getInstance().arrLastMonthRank.clear();
                    for(int i = 0; i < arrLastMonth.length(); i++){
                        JSONObject objItem = arrLastMonth.getJSONObject(i);
                        RankInfo info = new RankInfo(i+1, objItem.getString("user_id"), objItem.getString("name"), objItem.getString("profile_img"), objItem.getString("point"), objItem.getString("percent"), objItem.getString("placed_tip_count"), objItem.getString("loses_count"));
                        Common.getInstance().arrLastMonthRank.add(info);
                    }
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public String upload(String filePath){
        String charset = "UTF-8";
        File uploadFile1 = new File(filePath);

        String requestURL = "https://tipyaapp.com/uploads/uploadImage.php";

        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);

            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");

            try {
                multipart.addFilePart("userfile", uploadFile1);
            }catch (IOException ex){

            }

            List<String> response = multipart.finish();
            String result = response.get(0);
            Log.d("server", result);
            return result;
        } catch (IOException ex) {
            System.err.println(ex);
        } catch(Exception e){

        }
        return "";
    }
    public int placeProfileSetting(String user_id, String name, String email, String phone, String bio){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(SERVER_URL + URL_PLACE_PROFILE_SETTING);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "user_id", user_id);  // Make your own key value pair
        BasicNameValuePair nameBasicNameValuePair = new BasicNameValuePair(
                "name", name);  // Make your own key value pair
        BasicNameValuePair emailBasicNameValuePair = new BasicNameValuePair(
                "email", email);  // Make your own key value pair
        BasicNameValuePair phoneBasicNameValuePair = new BasicNameValuePair(
                "phone", phone);  // Make your own key value pair
        BasicNameValuePair bioBasicNameValuePair = new BasicNameValuePair(
                "bio", bio);  // Make your own key value pair
        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        nameValuePairList.add(nameBasicNameValuePair);
        nameValuePairList.add(emailBasicNameValuePair);
        nameValuePairList.add(phoneBasicNameValuePair);
        nameValuePairList.add(bioBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int placeProfileSettingImage(String user_id ,String avatar){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(SERVER_URL + URL_PLACE_PROFILE_SETTING_IMAGE);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "user_id", user_id);  // Make your own key value pair
        BasicNameValuePair avatarBasicNameValuePair = new BasicNameValuePair(
                "avatar", avatar);  // Make your own key value pair
        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        nameValuePairList.add(avatarBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int changePassword(String id, String current_password, String new_password){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(SERVER_URL + URL_CHANGE_PASSWORD);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "id", id);  // Make your own key value pair
        BasicNameValuePair currentBasicNameValuePair = new BasicNameValuePair(
                "current_password", current_password);  // Make your own key value pair
        BasicNameValuePair newBasicNameValuePair = new BasicNameValuePair(
                "new_password", new_password);  // Make your own key value pair
        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        nameValuePairList.add(currentBasicNameValuePair);
        nameValuePairList.add(newBasicNameValuePair);

        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int notification(String user_id){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(SERVER_URL + URL_NOTIFICATION);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "user_id", user_id);  // Make your own key value pair

        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    Common.getInstance().notificationJson = obj1;
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int profileSetting(String user_id){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(SERVER_URL + URL_PROFILE_SETTING);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "user_id", user_id);  // Make your own key value pair

        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    Common.getInstance().settingProfileJson = obj1;
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int gameList(String league_name){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(OLD_SERVER_URL + URL_GAME_LIST);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "league_name", league_name);  // Make your own key value pair

        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    JSONArray arrDate = obj1.getJSONArray("date_list");
                    Common.getInstance().arrDateList.clear();
                    for(int i = 0; i < arrDate.length(); i++){
                        String objItem = arrDate.getString(i);
                        Common.getInstance().arrDateList.add(objItem);
                    }

                    JSONArray arrGameList = obj1.getJSONArray("game_list");
                    Common.getInstance().arrGameListInfo.clear();
                    for(int i = 0; i < arrGameList.length(); i++){
                        JSONObject objItem = arrGameList.getJSONObject(i);
                        GameListInfo info = new GameListInfo(objItem.getString("league_id"), objItem.getString("home"), objItem.getString("home_image"), objItem.getString("away"), objItem.getString("away_image"), objItem.getString("time"), objItem.getString("event_id"), objItem.getString("start_date_time"), objItem.getString("home_odd"), objItem.getString("draw_odd"), objItem.getString("away_odd"));
                        Common.getInstance().arrGameListInfo.add(info);
                    }
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int gameDetail(String event_id){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(OLD_SERVER_URL + URL_GAME_DETAIL);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "event_id", event_id);  // Make your own key value pair

        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    JSONObject detailJson = obj1.getJSONObject("details");
                    GameDetailInfo info = new GameDetailInfo(detailJson.getString("league_id"), detailJson.getString("league_name"), detailJson.getString("home"), detailJson.getString("home_image"), detailJson.getString("away"), detailJson.getString("away_image"), detailJson.getString("time"), detailJson.getString("event_id"), detailJson.getString("start_date_time"), detailJson.getString("home_odd"), detailJson.getString("draw_odd"), detailJson.getString("away_odd"), detailJson.getString("double_chance_1X"), detailJson.getString("double_chance_X2"), detailJson.getString("double_chance_12"), detailJson.getString("total_goal_over"), detailJson.getString("total_goal_under"), detailJson.getString("bts_yes"), detailJson.getString("bts_no"), detailJson.getString("asian_home_odd"), detailJson.getString("asian_away_odd"), detailJson.getString("goal_line_over"), detailJson.getString("goal_line_under"));
                    Common.getInstance().gameDetailInfo = info;

                    JSONArray arrCorrectScores = obj1.getJSONArray("correct_score");
                    Common.getInstance().arrCorrectScores.clear();
                    for(int i = 0; i < arrCorrectScores.length(); i++){
                        JSONObject objItem = arrCorrectScores.getJSONObject(i);
                        CorrectScoreInfo info1 = new CorrectScoreInfo(objItem.getString("name"), objItem.getString("odd"));
                        Common.getInstance().arrCorrectScores.add(info1);
                    }
                    JSONArray arrAlterTotalGoals = obj1.getJSONArray("alter_total_goal_score_array");
                    Common.getInstance().arrAlternativeTotalGoals.clear();
                    for(int i = 0; i < arrAlterTotalGoals.length(); i++){
                        JSONObject objItem = arrAlterTotalGoals.getJSONObject(i);
                        AlternativeTotalGoalInfo info2 = new AlternativeTotalGoalInfo(objItem.getString("handicap"), objItem.getString("over"), objItem.getString("under"));
                        Common.getInstance().arrAlternativeTotalGoals.add(info2);
                    }

                    JSONArray arrExactTotalGoals = obj1.getJSONArray("exact_total_goals_array");
                    Common.getInstance().arrExactTotalGoals.clear();
                    for(int i = 0; i < arrExactTotalGoals.length(); i++){
                        JSONObject objItem = arrExactTotalGoals.getJSONObject(i);
                        CorrectScoreInfo info3 = new CorrectScoreInfo(objItem.getString("name"), objItem.getString("odd"));
                        Common.getInstance().arrExactTotalGoals.add(info3);
                    }

                    JSONArray arrTeamTotalGoals = obj1.getJSONArray("team_total_goals_array");
                    Common.getInstance().arrTeamTotalGoals.clear();
                    for(int i = 0; i < arrTeamTotalGoals.length(); i++){
                        JSONObject objItem = arrTeamTotalGoals.getJSONObject(i);
                        TeamTotalGoalInfo info4 = new TeamTotalGoalInfo(objItem.getString("name"), objItem.getString("odd"), objItem.getString("handicap"));
                        Common.getInstance().arrTeamTotalGoals.add(info4);
                    }

                    JSONArray arrMatchTotalGoals = obj1.getJSONArray("result_total_goals_array");
                    Common.getInstance().arrMatchTotalGoals.clear();
                    for(int i = 0; i < arrMatchTotalGoals.length(); i++){
                        JSONObject objItem = arrMatchTotalGoals.getJSONObject(i);
                        TeamTotalGoalInfo info5 = new TeamTotalGoalInfo(objItem.getString("name"), objItem.getString("odd"), objItem.getString("handicap"));
                        Common.getInstance().arrMatchTotalGoals.add(info5);
                    }

                    JSONArray arrTotalGoalsBTS = obj1.getJSONArray("total_goal_bts_array");
                    Common.getInstance().arrTotalGoalsBTS.clear();
                    for(int i = 0; i < arrTotalGoalsBTS.length(); i++){
                        JSONObject objItem = arrTotalGoalsBTS.getJSONObject(i);
                        TeamTotalGoalInfo info6 = new TeamTotalGoalInfo(objItem.getString("name"), objItem.getString("odd"), objItem.getString("handicap"));
                        Common.getInstance().arrTotalGoalsBTS.add(info6);
                    }
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int follow(String user_id, String follow_id, String status){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(SERVER_URL + URL_FOLLOW);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "user_id", user_id);  // Make your own key value pair
        BasicNameValuePair searchBasicNameValuePair = new BasicNameValuePair(
                "follow_id", follow_id);  // Make your own key value pair
        BasicNameValuePair statusBasicNameValuePair = new BasicNameValuePair(
                "status", status);  // Make your own key value pair
        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        nameValuePairList.add(searchBasicNameValuePair);
        nameValuePairList.add(statusBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int followManagement(String user_id, String strSearch){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(SERVER_URL + URL_FOLLOW_MANAGEMENT);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "user_id", user_id);  // Make your own key value pair
        BasicNameValuePair searchBasicNameValuePair = new BasicNameValuePair(
                "search", strSearch);  // Make your own key value pair
        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        nameValuePairList.add(searchBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    Common.getInstance().followManageJson = obj1;
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int discoverUser(String user_id, String strSearch){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(SERVER_URL + URL_DISCOVER);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "user_id", user_id);  // Make your own key value pair
        BasicNameValuePair searchBasicNameValuePair = new BasicNameValuePair(
                "search", strSearch);  // Make your own key value pair
        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        nameValuePairList.add(searchBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    Common.getInstance().searchJson = obj1;
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int feed(String user_id){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(SERVER_URL + URL_FEED);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "user_id", user_id);  // Make your own key value pair

        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    Common.getInstance().feedJson = obj1;
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int tipOfDay(String user_id){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(SERVER_URL + URL_TIP_OF_DAY);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "user_id", user_id);  // Make your own key value pair

        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    JSONObject detailJson = obj1.getJSONObject("tip_of_day");
                    Common.getInstance().tipOfDayJson = detailJson;
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int getProfile(String user_id){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(SERVER_URL + URL_GET_PROFILE);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "user_id", user_id);  // Make your own key value pair

        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    JSONObject jsonUser = obj1.getJSONObject("profile");
                    UserInfo userInfo = new UserInfo(jsonUser.getString("id"), jsonUser.getString("full_name"), jsonUser.getString("email"), jsonUser.getString("profile_img"), jsonUser.getString("percent"), jsonUser.getString("point"), jsonUser.getString("is_expert"), jsonUser.getString("bio"), jsonUser.getString("followers_count"), jsonUser.getString("following_count"));
                    Common.getInstance().userInfo = userInfo;
                    JSONArray arrCurrentTips = obj1.getJSONArray("current_tips");
                    Common.getInstance().arrCurrentTips.clear();
                    for(int i = 0; i < arrCurrentTips.length(); i++){
                        JSONObject objItem = arrCurrentTips.getJSONObject(i);
                        PlacedTipInfo info = new PlacedTipInfo(objItem.getString("event_id"), objItem.getString("home_name"), objItem.getString("away_name"), objItem.getString("start_date_time"), objItem.getString("league_name"), objItem.getString("market_style"), objItem.getString("odd_style"), objItem.getString("odd"), objItem.getString("tip_amount"), objItem.getString("home_image"), objItem.getString("away_image"), "");
                        Common.getInstance().arrCurrentTips.add(info);
                    }

                    JSONArray arrEndTips = obj1.getJSONArray("end_tips");
                    Common.getInstance().arrEndTips.clear();
                    for(int i = 0; i < arrEndTips.length(); i++){
                        JSONObject objItem = arrEndTips.getJSONObject(i);
                        PlacedTipInfo info = new PlacedTipInfo(objItem.getString("event_id"), objItem.getString("home_name"), objItem.getString("away_name"), objItem.getString("start_date_time"), objItem.getString("league_name"), objItem.getString("market_style"), objItem.getString("odd_style"), objItem.getString("odd"), objItem.getString("tip_amount"), objItem.getString("home_image"), objItem.getString("away_image"), objItem.getString("result_status"));
                        Common.getInstance().arrEndTips.add(info);
                    }
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int getOtherProfile(String owner_id, String user_id){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(SERVER_URL + URL_GET_OTHER_PROFILE);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair ownerBasicNameValuePair = new BasicNameValuePair(
                "owner_id", owner_id);  // Make your own key value pair
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "user_id", user_id);  // Make your own key value pair

        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(ownerBasicNameValuePair);
        nameValuePairList.add(usernameBasicNameValuePair);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    Common.getInstance().otherProfileJson = obj1;
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int activate(String user_id, String code){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(SERVER_URL + URL_ACTIVATE);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "id", user_id);  // Make your own key value pair
        BasicNameValuePair emailBasicNameValuePair = new BasicNameValuePair(
                "code", code);  // Make your own key value pair

        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        nameValuePairList.add(emailBasicNameValuePair);

        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    Common.getInstance().user_id = obj1.getString("id");
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int postData(String email, String password) {
        // Create a new HttpClient and Post Header
        org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://tipyaapp.com/webservice/user_login.php");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("email", "dev2.hightech@hotmail.com"));
            nameValuePairs.add(new BasicNameValuePair("password", "111111"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            return 1;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            System.out
                    .println("First Exception coz of HttpResponese :"
                            + e);
            e.printStackTrace();
        } catch (IOException e) {
            System.out
                    .println("First Exception coz of HttpResponese :"
                            + e);
            e.printStackTrace();
            // TODO Auto-generated catch block
        }
        return 1;
    }
    public int oldlogin(String email, String password){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(OLD_SERVER_URL + URL_LOGIN);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "email", email);  // Make your own key value pair
        BasicNameValuePair emailBasicNameValuePair = new BasicNameValuePair(
                "password", password);  // Make your own key value pair

        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        nameValuePairList.add(emailBasicNameValuePair);

        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    Common.getInstance().user_id = obj1.getString("id");
                    Common.getInstance().is_expert = obj1.getString("is_expert");
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int login(String email, String password){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(SERVER_URL + URL_LOGIN);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "email", email);  // Make your own key value pair
        BasicNameValuePair emailBasicNameValuePair = new BasicNameValuePair(
                "password", password);  // Make your own key value pair

        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);
        nameValuePairList.add(emailBasicNameValuePair);

        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    Common.getInstance().user_id = obj1.getString("id");
                    Common.getInstance().is_expert = obj1.getString("is_expert");
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int deleteProfile(String user_id){
        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        //	JSONObject json = new JSONObject();
        HttpPost httpPost = new HttpPost(SERVER_URL + URL_DELETE_PROFILE);// replace with your url
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
        BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair(
                "user_id", user_id);  // Make your own key value pair
        // You can add more parameters like above

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(usernameBasicNameValuePair);

        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                HttpResponse httpResponse = client
                        .execute(httpPost);

                InputStream in = httpResponse.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                JSONObject obj1 =  new JSONObject(total.toString());
                Log.i("server response: ", total.toString());
                String strRet = obj1.getString("result");
                if(strRet.equals("success")){
                    Common.getInstance().user_id = obj1.getString("id");
                    return 1;
                }
                else
                    return 0;
            }catch (ClientProtocolException cpe) {
                System.out
                        .println("First Exception coz of HttpResponese :"
                                + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out
                        .println("Second Exception coz of HttpResponse :"
                                + ioe);
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
    private String urlEncode(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, UTF8);
    }
    protected JSONObject getResponseData(String strUrl, Map<String, Object> params, boolean bIsAbsoluteUri) {
        try {
            return new JSONObject(getServerResponse(strUrl, params, bIsAbsoluteUri));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    protected JSONObject getResponseData(String strUrl, Map<String, Object> params) {
        return getResponseData(strUrl, params, false);
    }
    protected JSONArray getResponseArray(String strUrl, Map<String, Object> params) {
        try {
            return new JSONArray(getServerResponse(strUrl, params));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    protected String getServerResponse(String strUrl, Map<String, Object> params, boolean bIsAbsoluteUri) {
        try {
            URL url;
            if(bIsAbsoluteUri) {
                url = new URL(strUrl);
            } else {
                url = new URL(SERVER_URL + strUrl);
            }

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String inputLine;
            String repString = "";

            while ((inputLine = in.readLine()) != null) {
                repString = repString + inputLine;
            }
            in.close();
            return repString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    protected String getServerResponse(String strUrl, Map<String, Object> params) {
        return getServerResponse(strUrl, params, false);
    }

}
