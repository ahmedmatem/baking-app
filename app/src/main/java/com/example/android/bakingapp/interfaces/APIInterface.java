package com.example.android.bakingapp.interfaces;

import com.example.android.bakingapp.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ahmed on 10/12/2017.
 */

public interface APIInterface {
    @GET("baking.json")
    Call<List<Recipe>> getAllRecipes();
}
