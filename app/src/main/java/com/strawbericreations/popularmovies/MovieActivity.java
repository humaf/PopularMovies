package com.strawbericreations.popularmovies;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.widget.Toast;

public class MovieActivity extends AppCompatActivity {

    private GridView mGridView;

    private MovieAdapter mMovieAdapter;
    final String ARRAY_OF_MOVIES = "results";

    private ArrayList<Movie> mov;
    private ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridView = (GridView) findViewById(R.id.grid_view);
        Toast networkerror = Toast.makeText(this, "please connect to the INTERNET", Toast.LENGTH_LONG);


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                Movie item = (Movie) parent.getItemAtPosition(position);

                //    mov.add(item);

                Intent intent = new Intent(MovieActivity.this, DetailsActivity.class);

                //Pass the image title and url to DetailsActivity
                intent.putExtra("title", item.getTitle())
                        .putExtra("image", item.getImage())
                        .putExtra("release_date", item.getRelease_date())
                        .putExtra("vote_average", item.getVote_average())
                        .putExtra("id", item.getId())
                        .putExtra("overview", item.getOverview());
                //Start details activity
                startActivity(intent);
            }
        });

        if (!isNetworkAvailable(this) == false) {

            MovieDownloads task = new MovieDownloads();
            task.execute(Constants.API_URL_POP + Constants.API_KEY);
        } else
            networkerror.show();

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

        switch (item.getItemId()) {
            case R.id.action_sort_by_popularity:
                MovieDownloads taskpop = new MovieDownloads();
                taskpop.execute(Constants.API_URL_POP + Constants.API_KEY);
                return true;
            case R.id.action_sort_by_rating:
                MovieDownloads tasktop = new MovieDownloads();
                tasktop.execute(Constants.API_URL_TOP + Constants.API_KEY);
                return true;
            case R.id.action_sort_by_favourite:
               // FavouriteDownloads taskfav = new FavouriteDownloads(mMovieAdapter, getContentResolver());
                //taskfav.execute();
                getFavourites();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public Boolean isNetworkAvailable(Context context) {

        Boolean resultValue = false; // Initial Value

        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            resultValue = true;
        }
        return resultValue;
    }

    private void getFavourites(){


            Uri uri =FavouritesContract.FavouriteEntry.CONTENT_URI;
            ContentResolver resolver = getApplicationContext().getContentResolver();
            Cursor cursor = null;

            try {

                cursor = resolver.query(uri, null, null, null, null);

                // clear movies
             //   movies.clear();

                if (cursor.moveToFirst()){
                    do {
                     Movie movie = new Movie(cursor.getInt(1), cursor.getString(3),
                              cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                               cursor.getString(7));
                   movie.setReviews(cursor.getString(8));
                       movie.setTrailers(cursor.getString(9));
                        movies.add(movie);
                    } while (cursor.moveToNext());
                }

            } finally {

                if(cursor != null)
                    cursor.close();

            }




        }


    public class MovieDownloads extends AsyncTask<String, Void, ArrayList<Movie>> {

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
                    int id = res.getInt("id");
                    String title = res.optString("title");
                    String image = res.optString("poster_path");
                    String overview = res.getString("overview");
                    int voteAverage = res.getInt("vote_average");
                    String releaseDate = res.optString("release_date");
                    Movie newMovie = new Movie(id, title, image, overview, voteAverage, releaseDate);

/*
                    String movietitle = (res.optString("title"));
                    item.setTitle(movietitle);
                    item.setId(res.getInt("id"));
                    item.setImage(res.optString("poster_path"));
                    item.setRelease_date(res.optString("release_date"));
                    item.setOriginal_title(res.optString("title"));
                    item.setOverview(res.optString("overview"));
                    item.setVote_average(res.getInt("vote_average"));
                    String check = Integer.toString(res.getInt("id"));
                    Log.i("ID value generated", check);
                    Log.i("Title", movietitle);
                    Log.i("setting here", item.getImage());

                    */
                    movieList.add(newMovie);
                    Log.i("movie", movieList.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movieList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            mMovieAdapter = new MovieAdapter(getApplicationContext(), R.layout.grid_item, result);
            mGridView.setAdapter(mMovieAdapter);
            Log.i("checking data", result.toString());
        }
    }

}























