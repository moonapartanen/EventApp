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

public class LocationAdapter extends ArrayAdapter<Location> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<Location> items;
    private final int mResource;

    public LocationAdapter(@NonNull Context context, @LayoutRes int resource,
                           @NonNull List<Location> objects) {
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

        TextView txtLocation = view.findViewById(R.id.txtLocation);

        Location l = items.get(position);

        txtLocation.setText(l.getmLocation());

        return view;
    }

}

