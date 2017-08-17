package com.strawbericreations.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;
import static android.R.attr.data;

/**
 * Created by redrose on 8/13/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Trailers> trailersList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;


    public TrailerAdapter(Context mContext,List<Trailers> trailersList){
        this.trailersList = trailersList;
        this.mContext = mContext;
    }
    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailer_item, null);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
    //   Trailers trailer = trailersList.get(i);
        String id = trailersList.get(position).getKey();
        String thumbnailURL = "http://img.youtube.com/vi/".concat(id).concat("/hqdefault.jpg");
        //Render image using Picasso library
        Picasso.with(mContext).load(thumbnailURL).placeholder(R.drawable.placeholder)
                .into(((TrailerViewHolder) holder).imageView);
    }

    @Override
    public int getItemCount() {
        return trailersList.size();
    }



    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

class TrailerViewHolder extends RecyclerView.ViewHolder{

        protected ImageView imageView;

        public TrailerViewHolder(View view){
            super(view);
            this.imageView = (ImageView)view.findViewById(R.id.trailerImage);
        }
    }
