package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.Utilities.NetworkUtils;
import com.example.android.popularmovies.Utilities.PopularMoviesUtils;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by navas on 6/18/2018.
 * Used to Display details about Movies
 */

public class DetailActivity extends AppCompatActivity {

    /*private TextView mtvTitle;
    private ImageView mtvThumbnail;
    private TextView mtvPlotSynopsis;
    private TextView mtvrating;
    private TextView mtvReleaseDate;*/

    @BindView(R.id.tv_detail_title) TextView mtvTitle;
    @BindView(R.id.tv_detail_thumbnail) ImageView mtvThumbnail;
    @BindView(R.id.tv_detail_plotsynopsis) TextView mtvPlotSynopsis;
    @BindView(R.id.tv_detail_rating) TextView mtvrating;
    @BindView(R.id.tv_detail_releasedate) TextView mtvReleaseDate;

    String movie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // binding view
        ButterKnife.bind(this);


        /*mtvTitle = (TextView) findViewById(R.id.tv_detail_title);
        mtvThumbnail = (ImageView) findViewById(R.id.tv_detail_thumbnail);
        mtvPlotSynopsis = (TextView) findViewById(R.id.tv_detail_plotsynopsis);
        mtvrating = (TextView) findViewById(R.id.tv_detail_rating);
        mtvReleaseDate = (TextView) findViewById(R.id.tv_detail_releasedate);*/

        Intent intentThatStartedThisActivity = getIntent();

        // Get the Movie object and display the details to UI.
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("parcel_data")) {
                Movies currentMovie = intentThatStartedThisActivity.getParcelableExtra("parcel_data");

                //Movie Title
                mtvTitle.setText(currentMovie.getmTitle());

                //Movie Poster Path
                URL posterPathUrl;
                posterPathUrl = NetworkUtils.buildIMGUrl(currentMovie.getmThumbnail());
                PopularMoviesUtils.fillPicaso(mtvThumbnail, this, posterPathUrl.toString());

                //Movie Overview
                mtvPlotSynopsis.setText(currentMovie.getmPlotSynopsis());

                //Movie rating
                int rating = currentMovie.getmUserRating();
                String ratingValue = Integer.toString(rating) + "/10";
                mtvrating.setText(ratingValue);

                //Movie release Date
                mtvReleaseDate.setText(currentMovie.getmReleaseData());
            }
        }
    }
}
