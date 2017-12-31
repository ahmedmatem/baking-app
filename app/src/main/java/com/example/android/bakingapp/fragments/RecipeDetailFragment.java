package com.example.android.bakingapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapters.RecipeStepListAdapter;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Step;
import com.example.android.bakingapp.utilities.RecipeTextUtils;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment
        implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static final String RECIPE_DETAIL = "recipe_detail";
    public static final String INGREDIENTS_VISIBILITY_STATE = "ingredients-visibility-state";

    private static Recipe mRecipe;
    private List<Step> mSteps;

    private TextView mIngredients;
    private ListView mRecipeStepListView;
    private ImageView mShowButton;
    private ImageView mHideButton;

    private boolean mIngredientsVisibilityState = true;

    private OnFragmentInteractionListener mListener;


    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailFragment newInstance(Recipe recipe) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(RECIPE_DETAIL, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(RECIPE_DETAIL);
            mSteps = mRecipe.mSteps;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        mIngredients = (TextView) view.findViewById(R.id.tv_ingredients);
        mIngredients.setText(RecipeTextUtils.friendlyLookIngredients(
                mRecipe.getIngredients()));

        mShowButton = (ImageView) view.findViewById(R.id.iv_show);
        mShowButton.setOnClickListener(this);
        mHideButton = (ImageView) view.findViewById(R.id.iv_hide);
        mHideButton.setOnClickListener(this);

        mRecipeStepListView = (ListView) view.findViewById(R.id.lv_recipe_step_list);
        RecipeStepListAdapter mAdapter = new RecipeStepListAdapter(container.getContext(),
                getLayoutInflater(), mSteps);
        mRecipeStepListView.setAdapter(mAdapter);
        mRecipeStepListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(INGREDIENTS_VISIBILITY_STATE, mIngredientsVisibilityState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setShowHideButton(mIngredientsVisibilityState);
    }

    private void setShowHideButton(boolean isVisible) {
        if(isVisible){
            mIngredients.setVisibility(View.VISIBLE);
            mShowButton.setVisibility(View.GONE);
            mHideButton.setVisibility(View.VISIBLE);
        } else {
            mIngredients.setVisibility(View.GONE);
            mShowButton.setVisibility(View.VISIBLE);
            mHideButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null &&
                savedInstanceState.containsKey(INGREDIENTS_VISIBILITY_STATE))
        mIngredientsVisibilityState =
                savedInstanceState.getBoolean(INGREDIENTS_VISIBILITY_STATE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mListener != null) {
            mListener.onFragmentInteraction(mRecipe, position);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_show:
                mIngredientsVisibilityState = true;
                setShowHideButton(true);
                break;
            case R.id.iv_hide:
                mIngredientsVisibilityState = false;
                setShowHideButton(false);
                break;
            default:
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Recipe recipe, int stepPosition);
    }

}
