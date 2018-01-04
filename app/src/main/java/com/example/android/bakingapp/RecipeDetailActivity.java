package com.example.android.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.fragments.RecipeDetailFragment;
import com.example.android.bakingapp.fragments.StepDetailFragment;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;

import static com.example.android.bakingapp.utilities.KeyUtils.RECIPE_DETAIL;
import static com.example.android.bakingapp.utilities.KeyUtils.STEP_POSITION;

public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeDetailFragment.OnFragmentInteractionListener,
        StepDetailFragment.OnFragmentInteractionListener {

    private static Recipe mRecipe;

    private boolean mTwoPane = false;
    private int mStepPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(RECIPE_DETAIL)) {
            mRecipe = bundle.getParcelable(RECIPE_DETAIL);
        }

        if(mRecipe !=null) {
            setTitle(mRecipe.getName());
        }

        if (findViewById(R.id.recipe_step_detail_container) != null) {
            mTwoPane = true;
        }

        if (savedInstanceState == null) {
            RecipeDetailFragment recipeDetailFragment =
                    RecipeDetailFragment.newInstance(mRecipe);

            FragmentManager fragmentManager = getSupportFragmentManager();

            if (mTwoPane) {
                // tablet
                StepDetailFragment stepDetailFragment =
                        StepDetailFragment.newInstance(mRecipe.getSteps().get(0),
                                0, mRecipe.getSteps().size());

                if(findViewById(R.id.recipe_detail_container) == null){
                    // landscape mode
                    fragmentManager.beginTransaction()
                            .add(R.id.recipe_step_detail_container, stepDetailFragment)
                            .commit();
                } else {
                    // portrait mode
                    fragmentManager.beginTransaction()
                            .add(R.id.recipe_detail_container, recipeDetailFragment)
                            .add(R.id.recipe_step_detail_container, stepDetailFragment)
                            .commit();
                }
            } else {
                // mobile
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_detail_container, recipeDetailFragment)
                        .commit();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Recipe recipe, int stepPosition) {
        if (mTwoPane) {
            mStepPosition = stepPosition;
            Step step = recipe.getSteps().get(stepPosition);
            int stepCount = recipe.getSteps().size();
            StepDetailFragment stepDetailFragment =
                    StepDetailFragment.newInstance(step, stepPosition, stepCount);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_detail_container, stepDetailFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(RECIPE_DETAIL, recipe);
            bundle.putInt(STEP_POSITION, stepPosition);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onFragmentInteraction(int nextStep) {
        if(mTwoPane) {
            mStepPosition += nextStep;
            Step step = mRecipe.getSteps().get(mStepPosition);
            FragmentManager fragmentManager = getSupportFragmentManager();
            // replace fragment with next step details
            StepDetailFragment stepDetailFragment =
                    StepDetailFragment.newInstance(step, mStepPosition,
                            mRecipe.getSteps().size());
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_detail_container, stepDetailFragment)
                    .commit();
        }
    }
}
