package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.interfaces.RecipeHandler;
import com.example.android.bakingapp.models.Recipe;

import java.util.ArrayList;

/**
 * Created by ahmed on 12/01/2018.
 */

public class WidgetConfigRecipeListAdapter extends
        RecyclerView.Adapter<WidgetConfigRecipeListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Recipe> mRecipes;
    private boolean[] mRadioButtonsStates;

    private RecipeHandler mCallback = null;

    public WidgetConfigRecipeListAdapter(Context context,
                                         RecipeHandler callback,
                                         ArrayList<Recipe> recipes)
    {
        mContext = context;
        mCallback = callback;
        mRecipes = recipes;
        if(mRecipes != null) {
            mRadioButtonsStates = new boolean[mRecipes.size()];
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.widget_config_recipes_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mRecipes == null)
            return;

        Recipe recipe = mRecipes.get(position);

        holder.itemView.setTag(recipe.getId());
        holder.position = position;

        holder.rbRecipeName.setText(mRecipes.get(position).getName());
        if(mRadioButtonsStates != null) {
            holder.rbRecipeName.setChecked(mRadioButtonsStates[position]);
        }
    }

    @Override
    public int getItemCount() {
        if(mRecipes == null)
            return 0;
        return mRecipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int position;

        RadioButton rbRecipeName;

        public ViewHolder(View itemView) {
            super(itemView);

            rbRecipeName = (RadioButton) itemView.findViewById(R.id.rb_recipe_name);
            rbRecipeName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mCallback != null){
                mCallback.onItemClick(position);
            }
        }
    }

    public boolean[] getRadioButtonsStates() {
        return mRadioButtonsStates;
    }

    public void setRadioButtonsStates(boolean[] radioButtonsStates) {
        mRadioButtonsStates = radioButtonsStates;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
    }

    public ArrayList<Recipe> getRecipes() {
        return mRecipes;
    }
}
