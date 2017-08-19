package com.strawbericreations.popularmovies;

import android.app.LauncherActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

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

import static com.strawbericreations.popularmovies.R.attr.layoutManager;
import static java.security.AccessController.getContext;

public class DetailsActivity extends AppCompatActivity {
    private TextView titleText;
    private ImageView imageView;
    private TextView releasedateText;
    private TextView release_date;
    private TextView voteaverageText;
    private TextView voteaverage;
    private TextView overview;
    private RecyclerView mRecyclerView;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter mTrailerAdapter;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager reviewlayoutManager;
    private TextView trailerText;
    private Trailers trailers;
    private ArrayList<Trailers> trailersList;
    private ArrayList<Reviews> reviewsList;
    private String trailerurl="";
    private ArrayList<String> trailerAddress;
    private RecyclerView reviewRecyclerView;

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
        overview = (TextView)findViewById(R.id.overview);
        trailerText = (TextView)findViewById(R.id.trailerTitle);
        int id = getIntent().getIntExtra("id",0);
        String identifier = Integer.toString(id);
        String title = getIntent().getStringExtra("title");
        String image = getIntent().getStringExtra("image");
        String rdate = getIntent().getStringExtra("release_date");
        String  over_view = getIntent().getStringExtra("overview");
        int averagevote = getIntent().getIntExtra("vote_average",0);
        String avg =Integer.toString(averagevote);

        releasedateText.setText("Release Date");
        release_date.setText(rdate);
        voteaverageText.setText("Average Vote");
        overview.setText(over_view);
        voteaverage.setText(avg);
        titleText.setText(title);

        Picasso.with(this).load("http://image.tmdb.org/t/p/w92/" + image).resize(350,350)
                .into(imageView);
        layoutManager = new LinearLayoutManager(this);
     mRecyclerView.setLayoutManager(layoutManager);
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

