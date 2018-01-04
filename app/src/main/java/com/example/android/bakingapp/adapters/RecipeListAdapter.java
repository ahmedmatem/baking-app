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
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.interfaces.RecipeHandler;
import com.example.android.bakingapp.models.Recipe;
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
        ImageView image;

        // Action buttons
        Button actionExplore;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.tv_recipe_title);
            servings = (TextView) itemView.findViewById(R.id.tv_recipe_servings);
            image = (ImageView) itemView.findViewById(R.id.iv_recipe_image);

            actionExplore = (Button) itemView.findViewById(R.id.btn_action_explore);
            actionExplore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_action_explore:
                    if(mCallback != null){
                        mCallback.onExploreClicked(position);
                    }
                    break;
                default:

            }
        }
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
    }

    public ArrayList<Recipe> getRecipes() {
        return mRecipes;
    }
}


