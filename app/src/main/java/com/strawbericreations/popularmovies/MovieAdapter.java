package com.strawbericreations.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by redrose on 7/25/17.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    private ArrayList<Movie> movieItemList= new ArrayList<Movie>();

    private Context mContext;
    private int layoutResourceId;
    private LayoutInflater inflater;

    public MovieAdapter(Context mContext,int layoutResourceId, ArrayList<Movie> movieItemList) {
        super(mContext,layoutResourceId, movieItemList);
        this.movieItemList = movieItemList;
        this.mContext = mContext;
        this.layoutResourceId = layoutResourceId;
    }

    public void setGridData(ArrayList<Movie> movieItemList) {
        this.movieItemList = movieItemList;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if(row==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleView = row.findViewById(R.id.text_view);
            holder.imageView =  row.findViewById(R.id.image_item);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }
        Movie item = movieItemList.get(position);
        Log.i("Movie", item.toString());
        holder.titleView.setText(item.getTitle());
       // holder.titleView.setText(Html.fromHtml(item.getTitle()));
        Picasso.with(mContext).load(item.getImage()).into(holder.imageView);
    //    Picasso.with(mContext).load("https://image.tmdb.org/t/p/w185" + item.getImage()).into(holder.imageView);

        return row;
    }

    static class ViewHolder {
        TextView titleView;
        ImageView imageView;
    }
}
