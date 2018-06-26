package com.example.android.popularmovies.Utilities;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.popularmovies.Movies;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by navas on 6/18/2018.
 * <p>
 * These Utilities are used to,
 * 1, Extract movie information from the JSON response and populate it to Movies instance.
 * 2, Load the image into the UI.
 */

public class PopularMoviesUtils {
    private static String TAG = PopularMoviesUtils.class.getSimpleName();


    /**
     * This Method will get the json response, parse and populate the movies details into the Movies Object Instance.
     *
     * @param context
     * @param responseJsonStr The response from the weburl
     * @return ArrayList of Movies object, where Movies object holds the details about the movie.
     * @throws JSONException related to  parsing JSON string
     */
    public static ArrayList<Movies> getMovieslistfromJSON(Context context, String responseJsonStr)
            throws JSONException {

        ArrayList<Movies> parsedMovieList = new ArrayList<>();

        if (responseJsonStr != null) {
            try {
                JSONObject jsonResponse = new JSONObject(responseJsonStr);

                //Getting JSONArray of results
                JSONArray movieslist = jsonResponse.getJSONArray(context.getString(R.string.results));

                //Get details about the movies
                for (int i = 0; i < movieslist.length(); i++) {
                    JSONObject movie = movieslist.getJSONObject(i);
                    String title = movie.optString(context.getString(R.string.title));
                    String posterPath = movie.optString(context.getString(R.string.thumbnail));
                    String overview = movie.optString(context.getString(R.string.plot_synopsis));
                    int rating = movie.optInt(context.getString(R.string.user_rating));
                    String releaseDate = movie.optString(context.getString(R.string.releasedate));

                    //Populate the Movies object with movie details and stack up in arrayList.
                    parsedMovieList.add(new Movies(title, posterPath, overview, rating, releaseDate));
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }
        return parsedMovieList;
    }

    /**
     * This Method is used to dynamically populate the UI with movie poster from the image URL.
     *
     * @param imgview UI to populate with poster image.
     * @param context
     * @param url     Image URL to load from Web.
     */
    public static void fillPicaso(final ImageView imgview, Context context, final String url) {

        //Load Image using picasso
        Picasso.with(imgview.getContext())
                .load(url)
                .into(imgview, new Callback() {

                            @Override
                            public void onSuccess() {
                                //Success image already loaded into the view so do nothing
                            }

                            @Override
                            public void onError() {
                                //Error, do further handling of this situation here
                                imgview.setVisibility(View.GONE);
                                Toast.makeText(imgview.getContext(), "Error Loading Image link : " + url, Toast.LENGTH_LONG).show();

                            }
                        }
                );
    }
}