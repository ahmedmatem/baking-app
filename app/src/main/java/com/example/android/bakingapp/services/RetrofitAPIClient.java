package com.example.android.bakingapp.services;

import com.example.android.bakingapp.interfaces.APIInterface;
import com.example.android.bakingapp.interfaces.RecipeHandler;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ahmed on 10/12/2017.
 */

public class RetrofitAPIClient {
    private static final String BASE_URL = NetworkUtils.BASE_URL;

    private static APIInterface mAPIInterface;
    private static RecipeHandler mCallback;

    private Retrofit mRetrofit;

    public RetrofitAPIClient(RecipeHandler callback) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mAPIInterface = mRetrofit.create(APIInterface.class);
        mCallback = callback;
    }

    public void getAllRecipes(){
        final ArrayList<Recipe> result = new ArrayList<>();

        Call<List<Recipe>> call = mAPIInterface.getAllRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(response.isSuccessful()){
                    ArrayList<Recipe> recipes = new ArrayList<>();
                    for(Recipe recipe : response.body()){
                        recipes.add(recipe);
                    }
                    mCallback.onRecipeListLoaded(recipes);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
