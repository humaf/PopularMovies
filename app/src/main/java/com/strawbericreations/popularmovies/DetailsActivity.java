package com.strawbericreations.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    private TextView titleText;
    private ImageView imageView;
    private TextView releasedateText;
    private TextView release_date;
    private TextView voteaverageText;
    private TextView voteaverage;
    private TextView overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Movie movieIntent = (Movie) intent.getSerializableExtra("Movie");

        titleText = (TextView) findViewById(R.id.title);
        imageView = (ImageView) findViewById(R.id.grid_item_image);
        releasedateText = (TextView)findViewById(R.id.releaseTitle);
        release_date = (TextView)findViewById(R.id.releaseDate);
        voteaverageText = (TextView)findViewById(R.id.voteaverageTitle);
        voteaverage = (TextView)findViewById(R.id.voteaverage);
        overview = (TextView)findViewById(R.id.overview);
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
    }
}
