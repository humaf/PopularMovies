package com.strawbericreations.popularmovies;

/**
 * Created by redrose on 8/17/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Reviews> reviewsList;
    private Context mContext;

    public ReviewAdapter(Context mContext, List<Reviews> reviewsList) {
        this.reviewsList = reviewsList;
        this.mContext = mContext;
    }

    private Context getContext(){
        return mContext;
    }
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View reviewView = inflater.inflate(R.layout.review_item, viewGroup, false);
        ReviewAdapter.ReviewViewHolder viewHolder = new ReviewViewHolder(reviewView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ReviewViewHolder holder, int position) {

      String reviewContent = reviewsList.get(position).getContent();
      String reviewAuthor = reviewsList.get(position).getAuthor();
        ReviewViewHolder.aTextView.setText(reviewAuthor);
        ReviewViewHolder.cTextView.setText(reviewContent);
    }
    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        protected  static TextView aTextView;
        protected static  TextView cTextView;

        public ReviewViewHolder(View view) {
            super(view);
            aTextView = (TextView) view.findViewById(R.id.authorText);
            cTextView = (TextView) view.findViewById(R.id.contentText);
        }

    }
}