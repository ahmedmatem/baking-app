package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.android.bakingapp.adapters.WidgetConfigRecipeListAdapter;
import com.example.android.bakingapp.interfaces.RecipeHandler;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.services.RecipesLoader;
import com.example.android.bakingapp.services.RetrofitAPIClient;
import com.example.android.bakingapp.services.WidgetService;

import java.util.ArrayList;

import static com.example.android.bakingapp.BakingAppWidgetProvider.RECIPE;

public class WidgetConfigActivity extends AppCompatActivity implements RecipeHandler {
    public static WidgetConfigRecipeListAdapter mAdapter;
    public static final String IS_CONFIG_MODE = "is-config-mode";

    private int mAppWidgetId;
    private Menu mMenu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the result to CANCELED. This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_widget_config);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras
                    .getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                            AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID,
        // finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        RecyclerView rv = findViewById(R.id.rv_widget_config_recipes);
        mAdapter = new WidgetConfigRecipeListAdapter(this, this, null);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(mAdapter);

        RetrofitAPIClient apiClient = new RetrofitAPIClient(this);
        apiClient.getAllRecipes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.widget_config_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_action) {
            // update app widget
            Context context = getBaseContext();
            AppWidgetManager appWidgetManager =
                    AppWidgetManager.getInstance(context);

            int position = getSelectedPosition();
            Recipe selectedRecipe = getRecipeAt(position);

            // start recipe loader service with selected recipe
            Intent serviceIntent = new Intent(context, RecipesLoader.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    mAppWidgetId);
            serviceIntent.putExtra(RECIPE, selectedRecipe);
            serviceIntent.putExtra(IS_CONFIG_MODE, true);
            context.startService(serviceIntent);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private Recipe getRecipeAt(int position) {
        return mAdapter.getRecipes().get(position);
    }

    private int getSelectedPosition() {
        int position = 0;
        boolean[] states = mAdapter.getRadioButtonsStates();
        if(states == null){
            return position;
        }
        for(int i = 0; i < states.length; i++){
            if(states[i] == true){
                position = i;
                break;
            }
        }
        return position;
    }

    @Override
    public void onRecipeListLoaded(ArrayList<Recipe> recipes) {
        mAdapter.setRecipes(recipes);
        if (recipes != null) {
            mAdapter.setRadioButtonsStates(new boolean[recipes.size()]);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        updateRadioButtonsStates(position);
        updateSaveActionState();
    }

    private void updateSaveActionState() {
        boolean[] states = mAdapter.getRadioButtonsStates();
        if(states != null){
            if(mMenu != null){
                MenuItem menuItem = mMenu.findItem(R.id.save_action);
                if(hasSelectedRadioButton(states)){
                    menuItem.setEnabled(true);
                } else {
                    menuItem.setEnabled(false);
                }
            }
        }
    }

    private boolean hasSelectedRadioButton(boolean[] states) {
        for (boolean state : states){
            if(state == true){
                return true;
            }
        }
        return false;
    }

    private void updateRadioButtonsStates(int position) {
        if (mAdapter == null) {
            return;
        }

        boolean[] states = mAdapter.getRadioButtonsStates();
        if (states == null) {
            return;
        }

        for (int i = 0; i < states.length; i++) {
            if (i == position) {
                states[i] = !states[i];
            } else {
                states[i] = false;
            }
        }

        mAdapter.setRadioButtonsStates(states);
        mAdapter.notifyDataSetChanged();
    }
}
