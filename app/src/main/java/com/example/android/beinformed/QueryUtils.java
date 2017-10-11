package com.example.android.beinformed;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QueryUtils {

    private static Context mContext;
    private static final String LOG = QueryUtils.class.getName();


    private QueryUtils(Context context){mContext=context;}

    public static List<News> fetchNewsData(String requestUrl){
        URL url= createUrl(requestUrl);
        String jsonResponse="";
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            e.printStackTrace();
            Log.e(LOG,"Request error: ",e.getCause());
        }
        List<News> newsList = extractNews(jsonResponse);
        return newsList;
    }

    private static URL createUrl(String requestUrl){
        URL url = null;
        try{
            url = new URL(requestUrl);
        }catch (MalformedURLException ex){
            ex.printStackTrace();
            Log.e(LOG,"ERROR creation Url: ",ex.getCause());
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG, "Problem retrieving JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<News> extractNews(String jsonResponse){

        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        List<News> news = new ArrayList<News>();

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONObject results = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = results.getJSONArray("results");

            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNews = newsArray.getJSONObject(i);

                String title = currentNews.getString("webTitle");

                String url = currentNews.getString("webUrl");

                String date = currentNews.getString("webPublicationDate");
                date = formatDate(date);

                String topic = currentNews.getString("sectionName");

                news.add(new News(title,url,date,topic));
            }

        }catch (JSONException e){
            e.printStackTrace();
            Log.e(LOG,"Error parsing: "+e.getMessage());
        }
        return news;
    }

    private static String formatDate(String dateObject) {
        String getDate = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat dateFormat = new SimpleDateFormat(getDate,Locale.UK);
        try {
            Date parseJsonDate = dateFormat.parse(dateObject);
            String wishedDate = "MMM d, yyy";
            SimpleDateFormat format = new SimpleDateFormat(wishedDate,Locale.UK);
            return wishedDate.format(String.valueOf(parseJsonDate));
        }catch (ParseException ex){
            Log.e(LOG,"Couldn't parse date: ",ex.getCause());
            return "";
        }
    }

}
