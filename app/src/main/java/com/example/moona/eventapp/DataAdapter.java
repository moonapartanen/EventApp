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

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

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


        mRequestQueue = Volley.newRequestQueue(mContext);
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

        NetworkImageView avatar = listItem.findViewById(R.id.imageView);
        avatar.setImageUrl(RandomImage(), mImageLoader);

        TextView name = listItem.findViewById(R.id.tvName);
        name.setText(currentData.getmName());

        TextView date = listItem.findViewById(R.id.tvDate);
        date.setText(currentData.getmDate());

        return listItem;
    }

    public String RandomImage()
    {
        Random rnd = new Random();

        int random = rnd.nextInt(3) + 1;

        if (random == 1)
        {
            return "https://images.cdn.yle.fi/image/upload/w_1200,h_675,g_center,g_faces,c_fill/13-3-8574527.jpg";
        }
        else if (random == 2)
        {
            return "http://static.iltalehti.fi/smliiga/puljuetu190216lm_sm.jpg";
        }
        else
        {
            return "https://static.ilcdn.fi/nhl/lainekurrietu_mh0503_nh.jpg";
        }
    }
}

