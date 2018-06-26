package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by navas on 6/23/2018.
 * Movies Class with Movie related Information.
 */

public class Movies implements Parcelable {

    private String mTitle;
    private String mThumbnail;
    private String mPlotSynopsis;
    private int mUserRating;
    private String mReleaseDate;

    public Movies(String title, String thumbnail, String overview, int rating, String releasedate){
        mTitle = title;
        mThumbnail = thumbnail;
        mPlotSynopsis = overview;
        mUserRating = rating;
        mReleaseDate = releasedate;
    }

    public Movies(Parcel in) {
        mTitle = in.readString();
        mThumbnail = in.readString();
        mPlotSynopsis = in.readString();
        mUserRating = in.readInt();
        mReleaseDate = in.readString();
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public int getmUserRating() {
        return mUserRating;
    }

    public String getmPlotSynopsis() {
        return mPlotSynopsis;
    }

    public String getmReleaseData() {
        return mReleaseDate;
    }

    @Override
    public String toString() {
        return "Movies{" +
                "mTitle='" + mTitle + '\'' +
                ", mThumbnail='" + mThumbnail + '\'' +
                ", mPlotSynopsis=" + mPlotSynopsis +
                ", mUserRating=" + Integer.toString(mUserRating) +
                ", mReleaseDate=" + mReleaseDate +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(mTitle);
        parcel.writeString(mThumbnail);
        parcel.writeString(mPlotSynopsis);
        parcel.writeInt(mUserRating);
        parcel.writeString(mReleaseDate);

    }

    // This is to de-serialize the object
    public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>() {
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
}
