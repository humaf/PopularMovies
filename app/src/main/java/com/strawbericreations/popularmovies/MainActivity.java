package com.strawbericreations.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridView mGridView;
    private ArrayList<Movie> mMovieItemList;
    private MovieAdapter mMovieAdapter;
    private String movieUrl = "http://api.themoviedb.org/3/movie/popular?api_key=34de1fb55076a771087c2c04d80637f2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridView = (GridView)findViewById(R.id.grid_view);
        mMovieItemList = new ArrayList<>();
        mMovieAdapter = new MovieAdapter(this,R.layout.grid_item,mMovieItemList);
        mGridView.setAdapter(mMovieAdapter);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MovieDownloads extends AsyncTask<String,Void,List<Movie>> {

        private ArrayList<Movie> movieList;
        private  final String firstPartOfUrl =" http://image.tmdb.org/t/p/w185/";

        @Override
        protected List<Movie> doInBackground(String... urls) {
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
                    String s = firstPartOfUrl + (res.optString("poster_path"));
                    String movietitle = (res.optString("title"));
                    Log.i("Title",movietitle);
                    item.setTitle(movietitle);
                    Log.i("Image url", s);
                    item.setImage(s);
                    movieList.add(item);
                    Log.i("movie", movieList.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return movieList;
        }
        @Override
        protected void onPostExecute(List<Movie> result) {
            mMovieAdapter.setGridData(movieList);
        }
    }
}
