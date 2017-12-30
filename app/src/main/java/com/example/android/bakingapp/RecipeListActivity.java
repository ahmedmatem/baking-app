package com.example.android.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.bakingapp.adapters.RecipeListAdapter;
import com.example.android.bakingapp.interfaces.RecipeHandler;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.services.RetrofitAPIClient;

import java.util.ArrayList;

import static com.example.android.bakingapp.utilities.KeyUtils.RECIPE_DETAIL;
import static com.example.android.bakingapp.utilities.KeyUtils.RECIPE_LIST;

public class RecipeListActivity extends AppCompatActivity implements RecipeHandler {

    private static RecipeListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        RecyclerView rv = (RecyclerView) findViewById(R.id.recipe_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        if(savedInstanceState == null) {
            mAdapter = new RecipeListAdapter(this, this, null);
            // get recipe list asynchronously using Retrofit API client
            RetrofitAPIClient apiClient = new RetrofitAPIClient(this);
            apiClient.getAllRecipes();
        } else {
            ArrayList<Recipe> recipes = savedInstanceState.getParcelableArrayList(RECIPE_LIST);
            mAdapter = new RecipeListAdapter(this, this, recipes);
        }

        rv.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(RECIPE_LIST, mAdapter.getRecipes());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRecipeListLoaded(ArrayList<Recipe> recipes) {
        mAdapter.setRecipes(recipes);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onExploreClicked(int position) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_DETAIL, mAdapter.getRecipes().get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
