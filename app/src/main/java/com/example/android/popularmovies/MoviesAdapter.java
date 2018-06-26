package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.Utilities.PopularMoviesUtils;


import com.example.android.popularmovies.Utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by navas on 6/18/2018.
 * {@link MoviesAdapter}  exoposes the list of Movies to  a recyclerView.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private ArrayList<Movies> movieData;

    private final MoviesAdapterOnClickHandler mClickHandler;

    public interface MoviesAdapterOnClickHandler {
        void onClick(Movies movie);
    }

    public MoviesAdapter(MoviesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    //Handle ViewHolder
    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //public final ImageView mThumbnail;
        @BindView(R.id.tv_grid_view) ImageView mThumbnail;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            //mThumbnail = (ImageView) itemView.findViewById(R.id.tv_grid_view);

            // binding view
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movies movieSelected = movieData.get(adapterPosition);
            mClickHandler.onClick(movieSelected);
        }

    }


    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutItemforGridView = R.layout.grid_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachtoParentImmediately = false;

        View view = inflater.inflate(layoutItemforGridView, parent, shouldAttachtoParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesAdapterViewHolder holder, int position) {

        final Movies currentMovie = movieData.get(position);
        Log.i("MoviesAdapter", "#" + position + currentMovie.getmTitle());

        //Bind the Movie Poster using picasso util.
        URL posterPathUrl;
        posterPathUrl = NetworkUtils.buildIMGUrl(currentMovie.getmThumbnail());
        PopularMoviesUtils.fillPicaso(holder.mThumbnail, holder.mThumbnail.getContext(), posterPathUrl.toString());

    }

    @Override
    public int getItemCount() {
        if (null == movieData) return 0;
        return movieData.size();
    }

    void setMovieData(ArrayList<Movies> movieList) {
        movieData = movieList;
        notifyDataSetChanged();
    }

}

