package com.example.android.beinformed;

public class News {

    private String mTitle;
    private String mUrl;
    private String mDate;
    private String mTopic;

    public News(String title,String url,String date,String topic){
        mTitle=title;
        mUrl=url;
        mDate=date;
        mTopic=topic;
    }

    public String getTitle(){return mTitle;}

    public String getUrl(){return mUrl;}

    public String getDate(){return mDate;}

    public String getTopic(){return mTopic;}

}
