package com.example.android.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmed on 10/12/2017.
 */

public class Ingredient implements Parcelable{
    @SerializedName("quantity")
    @Expose
    public float mQuantity;

    @SerializedName("measure")
    @Expose
    public String mMeasure;

    @SerializedName("ingredient")
    @Expose
    public String mIngredient;

    /**
     * Retrieving Ingredient data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private Ingredient(Parcel source) {
        mQuantity = source.readFloat();
        mMeasure = source.readString();
        mIngredient = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(mQuantity);
        dest.writeString(mMeasure);
        dest.writeString(mIngredient);
    }

    public static final Parcelable.Creator<Ingredient> CREATOR =
            new Parcelable.Creator<Ingredient>(){

                @Override
                public Ingredient createFromParcel(Parcel source) {
                    return new Ingredient(source);
                }

                @Override
                public Ingredient[] newArray(int size) {
                    return new Ingredient[size];
                }
            };

    @Override
    public String toString() {
        return "Ingredient{" +
                "mQuantity=" + mQuantity +
                ", mMeasure='" + mMeasure + '\'' +
                ", mIngredient='" + mIngredient + '\'' +
                '}';
    }
}
