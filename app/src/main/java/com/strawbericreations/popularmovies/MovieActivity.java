package com.strawbericreations.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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
import android.widget.ImageView;

public class MovieActivity extends AppCompatActivity {

    private GridView mGridView;
    private ArrayList<Movie> mMovieItemList;
    private MovieAdapter mMovieAdapter;
    private String movieUrl = "http://api.themoviedb.org/3/movie/popular?api_key=your api key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridView = (GridView)findViewById(R.id.grid_view);
        mMovieItemList = new ArrayList<>();
     //   mMovieAdapter = new MovieAdapter(this,R.layout.grid_item,mMovieItemList);
      //  mGridView.setAdapter(mMovieAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                Movie item = (Movie) parent.getItemAtPosition(position);

                Intent intent = new Intent(MovieActivity.this, DetailsActivity.class);

                //Pass the image title and url to DetailsActivity
                intent.putExtra("title", item.getTitle())
                       .putExtra("image", item.getImage())
                        .putExtra("release_date",item.getRelease_date())
                        .putExtra("vote_average",item.getVote_average())
                        .putExtra("overview",item.getOverview());
                //Start details activity
                startActivity(intent);
            }
        });


        MovieDownloads task = new MovieDownloads();
        task.execute(movieUrl);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    public class MovieDownloads extends AsyncTask<String,Void,ArrayList<Movie>> {

        private ArrayList<Movie> movieList;

        @Override
        protected ArrayList<Movie> doInBackground(String... urls) {
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
                movieList = new ArrayList<Movie>();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject res = results.optJSONObject(i);
                    Movie item = new Movie();
                    String movietitle = (res.optString("title"));
                    item.setTitle(movietitle);
                    item.setImage(res.optString("poster_path"));
                    item.setRelease_date(res.optString("release_date"));
                    item.setOriginal_title(res.optString("title"));
                    item.setOverview(res.optString("overview"));
                    item.setVote_average(res.getInt("vote_average"));
                    Log.i("Title",movietitle);
              //      Log.i("Image url", movieimage);
                    //   Log.i("fetch",item.setImage(s));
                    Log.i("setting here", item.getImage());
                    movieList.add(item);
                    Log.i("movie", movieList.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movieList;
        }
        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            mMovieAdapter = new MovieAdapter(getApplicationContext(),R.layout.grid_item,result);
            mGridView.setAdapter(mMovieAdapter);
            Log.i("checking data", result.toString());
        }
    }

}
