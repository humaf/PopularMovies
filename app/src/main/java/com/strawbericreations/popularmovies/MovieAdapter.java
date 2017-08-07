package com.strawbericreations.popularmovies;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import javax.security.auth.callback.Callback;

/**
 * Created by redrose on 7/25/17.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    private ArrayList<Movie> movieItemList = new ArrayList<Movie>();
    private Context mContext;
    private int layoutResourceId;
    private LayoutInflater inflater;

    public MovieAdapter(Context mContext, int layoutResourceId, ArrayList<Movie> movieItemList) {
        super(mContext, layoutResourceId, movieItemList);
        this.movieItemList = movieItemList;
        this.mContext = mContext;
        this.layoutResourceId = layoutResourceId;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return movieItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        Log.i("getview", "getview");
        if (row == null) {
            //  inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleView = (TextView) row.findViewById(R.id.text_view);
            holder.imageView = (ImageView) row.findViewById(R.id.image_item);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Movie item = movieItemList.get(position);
        String iurl = item.getImage();
        Log.i("Imageis", iurl);
        Log.i("Movie in Adapter", item.toString());



        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w92/" + movieItemList.get(position)
                .getImage()).resize(320, 500).into(holder.imageView);


        return row;
    }

    private class ViewHolder {
        TextView titleView;
        ImageView imageView;
    }


}