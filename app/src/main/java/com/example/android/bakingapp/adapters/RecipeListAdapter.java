package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.interfaces.RecipeHandler;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.utilities.RecipeTextUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ahmed on 09/12/2017.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Recipe> mRecipes;

    private RecipeHandler mCallback = null;

    public RecipeListAdapter(Context context, RecipeHandler callback, ArrayList<Recipe> recipes)
    {
        mContext = context;
        mCallback = callback;
        mRecipes = recipes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recipe_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mRecipes == null)
            return;

        Recipe recipe = mRecipes.get(position);

        holder.itemView.setTag(recipe.getId());
        holder.position = position;

        holder.name.setText(recipe.getName());
        holder.servings.setText(String.format(mContext.getString(
                R.string.servings), recipe.getServings()));
        holder.ingredients.setText(RecipeTextUtils.friendlyLookIngredients(recipe.getIngredients()));
        if(recipe.isIngredientVisible()){
            holder.ingredientsContainer.setVisibility(View.VISIBLE);
            holder.actionDecrease.setVisibility(View.VISIBLE);
            holder.actionExpand.setVisibility(View.GONE);
        } else {
            holder.ingredientsContainer.setVisibility(View.GONE);
            holder.actionDecrease.setVisibility(View.GONE);
            holder.actionExpand.setVisibility(View.VISIBLE);
        }

        String imageUrl = recipe.getImage();
        Picasso.with(mContext)
                .load(Uri.parse(imageUrl))
                .placeholder(R.drawable.recipe_default_image)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        if(mRecipes == null)
            return 0;
        return mRecipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int position;

        TextView name;
        TextView servings;
        TextView ingredients;
        ImageView image;

//      Action buttons
        Button actionExplore;
        ImageButton actionFavorite;
        ImageButton actionExpand;
        ImageButton actionDecrease;

        LinearLayout ingredientsContainer;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.tv_recipe_title);
            servings = (TextView) itemView.findViewById(R.id.tv_recipe_servings);
            image = (ImageView) itemView.findViewById(R.id.iv_recipe_image);

            actionExplore = (Button) itemView.findViewById(R.id.btn_action_explore);
            actionFavorite = (ImageButton) itemView.findViewById(R.id.ib_action_favorite);
            actionExpand = (ImageButton) itemView.findViewById(R.id.ib_action_expand);
            actionDecrease = (ImageButton) itemView.findViewById(R.id.ib_action_decrease);
            actionDecrease.setVisibility(View.GONE);

            ingredientsContainer = (LinearLayout) itemView.findViewById(
                    R.id.ingredients_container);
            ingredientsContainer.setVisibility(View.GONE);
            ingredients = (TextView) itemView.findViewById(R.id.tv_recipe_ingredients);

            actionExplore.setOnClickListener(this);
            actionExpand.setOnClickListener(this);
            actionDecrease.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ib_action_expand:
                    showIngredientList();
                    break;
                case R.id.ib_action_decrease:
                    hideIngredientList();
                    break;
                case R.id.btn_action_explore:
                    if(mCallback != null){
                        mCallback.onExploreClicked(position);
                    }
                    break;
                default:

            }
        }

        private void hideIngredientList() {
            mRecipes.get(position).setIngredientVisibility(false);
            ingredientsContainer.setVisibility(View.GONE);
            actionDecrease.setVisibility(View.GONE);
            actionExpand.setVisibility(View.VISIBLE);
        }

        private void showIngredientList() {
            mRecipes.get(position).setIngredientVisibility(true);
            ingredientsContainer.setVisibility(View.VISIBLE);
            actionExpand.setVisibility(View.GONE);
            actionDecrease.setVisibility(View.VISIBLE);
        }
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
    }

    public ArrayList<Recipe> getRecipes() {
        return mRecipes;
    }
}


