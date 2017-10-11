package com.example.android.beinformed;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News>{

    public NewsAdapter(Context context,List<News> news){
        super(context,0,news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView==null){
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item,parent,false);
        }

        News currentNews = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.title_id);
        title.setText(currentNews.getTitle());

        TextView date = (TextView) listItemView.findViewById(R.id.date_id);
        date.setText(currentNews.getDate());

        TextView topic = (TextView) listItemView.findViewById(R.id.topic_id);
        topic.setText(currentNews.getTopic());


        return listItemView;

    }
}
