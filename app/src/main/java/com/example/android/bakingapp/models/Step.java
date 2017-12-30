package com.example.android.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed on 10/12/2017.
 */

public class Step implements Parcelable{
    @SerializedName("id")
    @Expose
    public int mId;

    @SerializedName("shortDescription")
    @Expose
    public String mShortDescription;

    @SerializedName("description")
    @Expose
    public String mDescription;

    @SerializedName("videoURL")
    @Expose
    public String mVideoURL;

    @SerializedName("thumbnailURL")
    @Expose
    public String mThumbnailURL;

    private Step(Parcel source) {
        mId = source.readInt();
        mShortDescription = source.readString();
        mDescription = source.readString();
        mVideoURL = source.readString();
        mThumbnailURL = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mShortDescription);
        dest.writeString(mDescription);
        dest.writeString(mVideoURL);
        dest.writeString(mThumbnailURL);
    }

    public static final Parcelable.Creator<Step> CREATOR =
            new Parcelable.Creator<Step>(){

                @Override
                public Step createFromParcel(Parcel source) {
                    return new Step(source);
                }

                @Override
                public Step[] newArray(int size) {
                    return new Step[size];
                }
            };

    @Override
    public String toString() {
        return "Step{" +
                "mId=" + mId +
                ", mShortDescription='" + mShortDescription + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mVideoURL='" + mVideoURL + '\'' +
                ", mThumbnailURL='" + mThumbnailURL + '\'' +
                '}';
    }
}
