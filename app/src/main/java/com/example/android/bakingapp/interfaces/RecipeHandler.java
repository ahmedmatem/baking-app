package com.example.android.bakingapp.interfaces;

import com.example.android.bakingapp.models.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 10/12/2017.
 */

public interface RecipeHandler {
    void onRecipeListLoaded(ArrayList<Recipe> recipes);
    void onExploreClicked(int position);
}
