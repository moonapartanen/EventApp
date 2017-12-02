package com.example.moona.eventapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

        TextView name = listItem.findViewById(R.id.txtName);
        name.setText(currentData.getmName());

        TextView date = listItem.findViewById(R.id.txtDate);
        date.setText(currentData.getmDate());

        TextView description = listItem.findViewById(R.id.txtDescription);
        description.setText(currentData.getmDescription());

        return listItem;
    }

}

