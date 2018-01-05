package com.example.android.bakingapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.fragments.StepDetailFragment;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;

import static com.example.android.bakingapp.utilities.KeyUtils.RECIPE_DETAIL;
import static com.example.android.bakingapp.utilities.KeyUtils.STEP_POSITION;

public class StepDetailActivity extends AppCompatActivity
        implements StepDetailFragment.OnFragmentInteractionListener {

    private static final String TAG = "StepDetailActivity";

    private static Recipe mRecipe;
    private static Step mStep = null;
    private int mStepPosition;

    private FragmentManager fragmentManager;
    private StepDetailFragment stepDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(STEP_POSITION)) {
                mStepPosition = bundle.getInt(STEP_POSITION);
                if (bundle.containsKey(RECIPE_DETAIL)) {
                    mRecipe = (Recipe) bundle.getParcelable(RECIPE_DETAIL);
                    mStep = mRecipe.getSteps().get(mStepPosition);
                }
            }
        }

        if (savedInstanceState == null) {
            fragmentManager = getSupportFragmentManager();
            if(mRecipe != null) {
                stepDetailFragment =
                        StepDetailFragment.newInstance(mStep, mStepPosition,
                                mRecipe.getSteps().size());
            } else {
                stepDetailFragment =
                StepDetailFragment.newInstance(null, mStepPosition,0);
            }
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_container, stepDetailFragment)
                    .commit();
        } else {
            mStepPosition = savedInstanceState.getInt(STEP_POSITION);
        }

        setTitle();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STEP_POSITION, mStepPosition);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(int nextStep) {
        mStepPosition += nextStep;
        // set activity title for new step
        setTitle();
        mStep = mRecipe.getSteps().get(mStepPosition);
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        // replace fragment with next step details
        stepDetailFragment = StepDetailFragment.newInstance(mStep, mStepPosition,
                mRecipe.getSteps().size());
        fragmentManager.beginTransaction()
                .replace(R.id.step_detail_container, stepDetailFragment)
                .commit();
    }

    private void setTitle() {
        if(mRecipe == null)
            return;

        if (mStepPosition == 0) {
            setTitle(mRecipe.getName() + " - " + "Introduction");
            return;
        }

        setTitle(String.format(
                mRecipe.getName() + " - " + getString(R.string.step_number_text),
                mStepPosition));
    }
}
