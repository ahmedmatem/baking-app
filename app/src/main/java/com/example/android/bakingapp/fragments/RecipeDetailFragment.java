package com.example.android.bakingapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        implements AdapterView.OnItemClickListener {

    private static final String RECIPE_DETAIL = "recipe_detail";

    private static Recipe mRecipe;
    private List<Step> mSteps;

    private TextView mIngredients;
    private ListView mRecipeStepListView;

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

        mRecipeStepListView = (ListView) view.findViewById(R.id.lv_recipe_step_list);
        RecipeStepListAdapter mAdapter = new RecipeStepListAdapter(container.getContext(),
                getLayoutInflater(), mSteps);
        mRecipeStepListView.setAdapter(mAdapter);
        mRecipeStepListView.setOnItemClickListener(this);

        return view;
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Recipe recipe, int stepPosition);
    }

}
