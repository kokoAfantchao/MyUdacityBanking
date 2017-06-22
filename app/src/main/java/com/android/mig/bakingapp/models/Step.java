package com.android.mig.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable{
    private int mStepID;
    private String mStepShortDescription;
    private String mStepDescription;
    private String mStepVideoURL;
    private String mStepThumbnailURL;

    public Step(int id, String shortDescription, String description, String videoURL, String thumbnailURL){
        mStepID = id;
        mStepShortDescription = shortDescription;
        mStepDescription = description;
        mStepVideoURL = videoURL;
        mStepThumbnailURL = thumbnailURL;
    }

    protected Step(Parcel in) {
        mStepID = in.readInt();
        mStepShortDescription = in.readString();
        mStepDescription = in.readString();
        mStepVideoURL = in.readString();
        mStepThumbnailURL = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public void setStepID(int id){
        mStepID = id;
    }

    public void setStepShortDescription(String shortDescription){
        mStepShortDescription = shortDescription;
    }

    public void setStepDescription(String description){
        mStepDescription = description;
    }

    public void setStepVideoURL(String videoURL){
        mStepVideoURL = videoURL;
    }

    public void setmStepThumbnailURL(String thumbnailURL){
        mStepThumbnailURL = thumbnailURL;
    }

    public int getStepID(){return mStepID;}
    public String getStepShortDescription(){return mStepShortDescription;}
    public String getStepDescription(){return mStepDescription;}
    public String getStepVideoURL(){return mStepVideoURL;}
    public String getStepThumbnailURL(){return mStepThumbnailURL;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mStepID);
        parcel.writeString(mStepShortDescription);
        parcel.writeString(mStepDescription);
        parcel.writeString(mStepVideoURL);
        parcel.writeString(mStepThumbnailURL);
    }
}
