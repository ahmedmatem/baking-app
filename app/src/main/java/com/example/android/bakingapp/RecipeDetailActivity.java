package com.example.android.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.fragments.RecipeDetailFragment;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;

import static com.example.android.bakingapp.utilities.KeyUtils.RECIPE_DETAIL;
import static com.example.android.bakingapp.utilities.KeyUtils.STEP_POSITION;

public class RecipeDetailActivity extends AppCompatActivity
                implements RecipeDetailFragment.OnFragmentInteractionListener{

    private static Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey(RECIPE_DETAIL)) {
            mRecipe = bundle.getParcelable(RECIPE_DETAIL);
        }

        setTitle(mRecipe.getName());

        if(savedInstanceState == null) {
            RecipeDetailFragment recipeDetailFragment =
                    RecipeDetailFragment.newInstance(mRecipe);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, recipeDetailFragment)
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(Recipe recipe, int stepPosition) {
        Intent intent = new Intent(this, StepDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_DETAIL, recipe);
        bundle.putInt(STEP_POSITION, stepPosition);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
