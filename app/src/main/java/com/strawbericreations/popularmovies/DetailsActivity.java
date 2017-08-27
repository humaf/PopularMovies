package com.strawbericreations.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class DetailsActivity extends AppCompatActivity {
    private Movie movie;
    private TextView titleText;
    private ImageView imageView;
    private TextView releasedateText;
    private TextView release_date;
    private TextView voteaverageText;
    private TextView voteaverage;
    private TextView overviews;
    private RecyclerView mRecyclerView;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter mTrailerAdapter;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager reviewlayoutManager;
    private View rootView;
    private TextView trailerText;
    private Trailers trailers;
    private ArrayList<Trailers> trailersList;
    private ArrayList<Reviews> reviewsList;
    private String trailerurl="";
    private ArrayList<String> trailerAddress;
    private RecyclerView reviewRecyclerView;
    private FavouritesProvider fav_provider;
    private ImageView favButton;
    private Movie mItem;
    private boolean isFavorite;
    private ArrayList<Integer> favouriteMovies;
    private int id;
    private String title;
    private String image;
    private String rdate;
    private String overview;
    private int averagevote;
    private Cursor cursor;

 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Movie movieIntent = (Movie) intent.getSerializableExtra("Movie");

        mRecyclerView = (RecyclerView)findViewById(R.id.trailerview);
        reviewRecyclerView = (RecyclerView)findViewById(R.id.reviview);
        titleText = (TextView) findViewById(R.id.title);
        imageView = (ImageView) findViewById(R.id.grid_item_image);
        releasedateText = (TextView)findViewById(R.id.releaseTitle);
        release_date = (TextView)findViewById(R.id.releaseDate);
        voteaverageText = (TextView)findViewById(R.id.voteaverageTitle);
        voteaverage = (TextView)findViewById(R.id.voteaverage);
        overviews = (TextView)findViewById(R.id.overview);
        trailerText = (TextView)findViewById(R.id.trailerTitle);
        favButton =(ImageView)findViewById(R.id.fav);
         id = getIntent().getIntExtra("id",0);
        String identifier = Integer.toString(id);
         title = getIntent().getStringExtra("title");
         image = getIntent().getStringExtra("image");
         rdate = getIntent().getStringExtra("release_date");
        overview = getIntent().getStringExtra("overview");
        averagevote = getIntent().getIntExtra("vote_average",0);
        String avg =Integer.toString(averagevote);
        releasedateText.setText("Release Date");
        release_date.setText(rdate);
        voteaverageText.setText("Average Vote");
        overviews.setText(overview);
        voteaverage.setText(avg);
        titleText.setText(title);

        Picasso.with(this).load("http://image.tmdb.org/t/p/w92/" + image).resize(350,350)
                .into(imageView);
    

     LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(DetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
     mRecyclerView.setLayoutManager(horizontalLayoutManager);
     mRecyclerView.addOnItemTouchListener(new RecyclerClickListener(getApplicationContext(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String url = "https://www.youtube.com/watch?v=".concat(trailersList.get(position).getKey());
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }

        }));



  DownloadTrailer tasktrailer = new DownloadTrailer();
     tasktrailer.execute(Constants.API_URL_TRAILERS + identifier  + "/videos?api_key=" + Constants.API_KEY);

     reviewlayoutManager = new LinearLayoutManager(this);
     reviewRecyclerView.setLayoutManager(reviewlayoutManager);

     DownloadReview taskreview = new DownloadReview();
     taskreview.execute(Constants.API_URL_REVIEWS + identifier  + "/reviews?api_key=" + Constants.API_KEY);

     favButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             boolean inFavorites = checkFavorites();
             if(inFavorites){
                 deleteFromFavorites();
             }else{
                 addToFavorites();
             }
             toggleFavorites();
         }
     });

 }

    private void addToFavorites() {

        Uri uri = FavouritesContract.FavouriteEntry.CONTENT_URI;
        ContentResolver resolver = getApplicationContext().getContentResolver();
        ContentValues values = new ContentValues();
        values.clear();
        values.put(FavouritesContract.FavouriteEntry.COLUMN_ID, id);
      //  values.put(PopularMovieContract.MovieEntry.MOVIE_BACKDROP_URI, movie.getTitle());
        values.put(FavouritesContract.FavouriteEntry.COLUMN_TITLE,title);
        values.put(FavouritesContract.FavouriteEntry.COLUMN_IMAGE, image);
        values.put(FavouritesContract.FavouriteEntry.COLUMN_OVERVIEW, overview);
        values.put(FavouritesContract.FavouriteEntry.COLUMN_Average_VOTE, averagevote);
        values.put(FavouritesContract.FavouriteEntry.COLUMN_RELEASE_DATE, rdate);
      //  values.put(FavouritesContract.FavouriteEntry.COLUMN_REVIEWS, rev);
       // values.put(PopularMovieContract.MovieEntry.MOVIE_TRAILERS, movie.getMoviePreviews());
        Uri check = resolver.insert(uri, values);
        Toast  toast = Toast.makeText(getApplicationContext(),"Added to Favourites",Toast.LENGTH_LONG);
        toast.show();
    }

    private void deleteFromFavorites() {

        Uri uri = FavouritesContract.FavouriteEntry.CONTENT_URI;
        ContentResolver resolver = getApplicationContext().getContentResolver();

        long noDeleted = resolver.delete(uri,
                FavouritesContract.FavouriteEntry.COLUMN_ID+ " = ? ",
                new String[]{ movie.getId() + "" });

    }
   /*
    * query DB to see if the movie is already there.
    * */
    private boolean checkFavorites() {
        Uri uri = FavouritesContract.FavouriteEntry.buildFavouritesUri(id);
        String[] projection = new String[]{FavouritesContract.FavouriteEntry.COLUMN_ID};
        Log.i("checking if its null",uri.toString());
        ContentResolver resolver = getApplicationContext().getContentResolver();
      Cursor cursor = null;
       try {

           cursor = resolver.query(uri, null, null, null, null);

           if (cursor.moveToFirst())
               return true;
       }
        finally {

            if(cursor != null )
                cursor.close();
        }
        return false;
    }

    private void toggleFavorites(){
        boolean inFavorites = checkFavorites();
      //  ImageButton addToFav = (ImageButton) rootView.findViewById(R.id.add_to_fav_view);

        if(inFavorites){
            favButton.setImageResource(R.drawable.yellow_star);
        }else{
            favButton.setImageResource(R.drawable.gray_star);
        }
    }

    public class DownloadTrailer extends AsyncTask<String,Void,ArrayList<Trailers>> {

        private ArrayList<String> u;
        protected ArrayList<Trailers> doInBackground(String ...urls){
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject response = new JSONObject(result);
                JSONArray results = response.optJSONArray("results");
               trailersList = new ArrayList<Trailers>();
                trailerAddress = new ArrayList<String>();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject res = results.optJSONObject(i);
                    Uri trailerUri = Uri.parse(new String("http://www.youtube.com/watch?v="+res.getString("key")));
                    Log.i("seeing value",trailerUri.toString());
                    trailerurl = trailerUri.toString();
                    trailerAddress.add(trailerurl);
                    Trailers item = new Trailers(trailerUri,res.optString("name"),res.optString("id"),res.optString("key"));
                    trailersList.add(item);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return trailersList;
        }
        @Override
        protected void onPostExecute(ArrayList<Trailers> result) {

           mTrailerAdapter = new TrailerAdapter(DetailsActivity.this, result)  ;
            mRecyclerView.setAdapter(mTrailerAdapter);

        }
    }

    public class DownloadReview extends AsyncTask<String,Void,ArrayList<Reviews>> {

        private ArrayList<String> u;
        protected ArrayList<Reviews> doInBackground(String ...urls){
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject response = new JSONObject(result);
                JSONArray results = response.optJSONArray("results");
                reviewsList = new ArrayList<Reviews>();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject res = results.optJSONObject(i);
                    Reviews item = new Reviews();
                    item.setAuthor(res.optString("author"));
                    item.setContent(res.optString("content"));
                    reviewsList.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return reviewsList;
        }
        @Override
        protected void onPostExecute(ArrayList<Reviews> result) {
            reviewAdapter = new ReviewAdapter(DetailsActivity.this, result)  ;
          reviewRecyclerView.setAdapter(reviewAdapter);
        }
    }


    }
