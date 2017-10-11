package com.example.android.beinformed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>> {

    private NewsAdapter mAdapter;
    private final int NEWS_LOADER_ID = 1;
    private TextView ifDataDoesntLoad;
    private ProgressBar mProgress;
    private static final String REQUEST_URL =
            "https://content.guardianapis.com/search?q=debate%20AND%20" +
                    "(economy%20OR%20immigration%20education)&tag=politics/politics" +
                    "&from-date=2017-01-01&api-key=test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<News> news = new ArrayList<News>();
        mAdapter = new NewsAdapter(this,news);
        ListView newsList = (ListView) findViewById(R.id.list);

        newsList.setAdapter(mAdapter);
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News currentNews = mAdapter.getItem(i);
                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent bookIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(bookIntent);
            }
        });

        if(checkConnection()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }


    }
    public boolean checkConnection(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        return  new NewsLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        mProgress = (ProgressBar) findViewById(R.id.loading_indicator);
        mProgress.setVisibility(View.GONE);
        ifDataDoesntLoad = (TextView) findViewById(R.id.empty_view);
        mAdapter.clear();
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }else{
            ifDataDoesntLoad.setText("No news found");
            ifDataDoesntLoad.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }
}
