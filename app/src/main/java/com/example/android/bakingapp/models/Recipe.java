package com.example.android.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 10/12/2017.
 */

public class Recipe implements Parcelable {
    private boolean mIngredientVisibility = false;

    @SerializedName("id")
    @Expose
    public int mId;

    @SerializedName("name")
    @Expose
    public String mName;

    @SerializedName("ingredients")
    @Expose
    public List<Ingredient> mIngredients = new ArrayList<>();

    @SerializedName("steps")
    @Expose
    public List<Step> mSteps = new ArrayList<>();

    @SerializedName("servings")
    @Expose
    public int mServings;

    @SerializedName("image")
    @Expose
    public String mImage;

    private Recipe(Parcel source) {
        int b = source.readInt();
        if(b == 1){
            setIngredientVisibility(true);
        } else {
            setIngredientVisibility(false);
        }
        mId = source.readInt();
        mName = source.readString();
        source.readTypedList(mIngredients, Ingredient.CREATOR);
        source.readTypedList(mSteps, Step.CREATOR);
        mServings = source.readInt();
        mImage = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if(isIngredientVisible()) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeTypedList(mIngredients);
        dest.writeTypedList(mSteps);
        dest.writeInt(mServings);
        dest.writeString(mImage);
    }

    public static final Parcelable.Creator<Recipe> CREATOR =
            new Parcelable.Creator<Recipe>(){

                @Override
                public Recipe createFromParcel(Parcel source) {
                    return new Recipe(source);
                }

                @Override
                public Recipe[] newArray(int size) {
                    return new Recipe[size];
                }
            };

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public List<Step> getSteps() {
        return mSteps;
    }

    public int getServings() {
        return mServings;
    }

    public String getImage() {
        return mImage;
    }

    public boolean isIngredientVisible() {
        return mIngredientVisibility;
    }

    public void setIngredientVisibility(boolean ingredientVisibility) {
        mIngredientVisibility = ingredientVisibility;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mIngredients=" + mIngredients +
                ", mSteps=" + mSteps +
                ", mServings=" + mServings +
                ", mImage='" + mImage + '\'' +
                '}';
    }
}
