package com.example.moona.eventapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by moona on 28.11.2017.
 */

public class KeywordAdapter extends ArrayAdapter<Keyword> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<Keyword> items;
    private final int mResource;

    public KeywordAdapter(@NonNull Context context, @LayoutRes int resource,
                          @NonNull List<Keyword> objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);

        TextView txtKeyword = view.findViewById(R.id.txtKeyWord);

        Keyword k = items.get(position);

        txtKeyword.setText(k.getmName());

        return view;
    }

}

