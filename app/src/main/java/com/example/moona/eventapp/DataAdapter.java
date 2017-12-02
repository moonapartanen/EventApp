package com.example.moona.eventapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by moona on 28.11.2017.
 */

public class DataAdapter extends ArrayAdapter<Data> {

    private Context mContext;
    private List<Data> dataList = new ArrayList<>();



    public DataAdapter(Context context, ArrayList<Data> list) {
        super(context, 0 , list);
        mContext = context;
        dataList = list;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

        Data currentData = dataList.get(position);


        TextView name = listItem.findViewById(R.id.tvName);
        name.setText(currentData.getmName());

        TextView date = listItem.findViewById(R.id.tvDate);
        date.setText(currentData.getmDate());

        return listItem;
    }

}

