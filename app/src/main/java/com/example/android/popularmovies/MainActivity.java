package com.example.android.popularmovies;

import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.Utilities.NetworkUtils;
import com.example.android.popularmovies.Utilities.PopularMoviesUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements
        MoviesAdapter.MoviesAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<Movies>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    //private RecyclerView mRecyclerView;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    private MoviesAdapter mAdapter;

    //private TextView mErrorMessageDisplay;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;

    //private ProgressBar mLoadingIndicator;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    private static final int MOVIES_LOADER_ID = 0;

    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /* Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks. */
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //If preference is updated restart the loader.
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            Log.d(TAG, "onStart: preferences were updated");
            getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // binding view
        ButterKnife.bind(this);


        // Set Grid Layout and Adapter to Recycler View
        //mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        //mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        int noOfCol = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, noOfCol);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new MoviesAdapter(this);

        mRecyclerView.setAdapter(mAdapter);

        //mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        LoaderManager.LoaderCallbacks<ArrayList<Movies>> callback = MainActivity.this;

        Bundle bundleLoader = null;
        getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, bundleLoader, callback);

        Log.d(TAG, "onCreate: registering preference changed listener");

        /* Register MainActivity as an OnPreferenceChangedListener.*/
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onClick(Movies currentMovie) {
        //Instantiate detail activity and pass the Movie object.
        Context context = this;
        Class intentClass = DetailActivity.class;
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("parcel_data", currentMovie);
        startActivity(intent);
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id     The ID whose loader is to be created.
     * @param bundle Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<ArrayList<Movies>> onCreateLoader(int id, final Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<Movies>>(this) {

            ArrayList<Movies> mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            /**
             * This method will load and parse the JSON data
             * from WebURL in the background.
             *
             * @return Movies information from Web as an array of Movies.
             *
             */
            @Override
            public ArrayList<Movies> loadInBackground() {

                URL popularMoviesUrl;
                popularMoviesUrl = NetworkUtils.buildAPIUrl(MainActivity.this);

                try {
                    String results = NetworkUtils.getResponseFromHttpUrl(popularMoviesUrl);
                    mMovieData = PopularMoviesUtils.getMovieslistfromJSON(MainActivity.this, results);

                    return mMovieData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            /**
             * Sends the result of the load to the registered listener.
             *
             * @param data The result of the load
             */
            public void deliverResult(ArrayList<Movies> data) {
                super.deliverResult(data);
            }
        };
    }

    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<ArrayList<Movies>> loader, ArrayList<Movies> data) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null && data.size() > 0) {
            mAdapter.setMovieData(data);
            showDataView();
        }else {
            showErrorMessage();
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<ArrayList<Movies>> loader) {

    }

    private void invalidateData() {
        mAdapter.setMovieData(null);
    }

    private void showDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle Settings Menu
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            invalidateData();
            getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }
}

