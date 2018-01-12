package com.example.android.bakingapp.services;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.android.bakingapp.BakingAppWidgetProvider;
import com.example.android.bakingapp.interfaces.RecipeHandler;
import com.example.android.bakingapp.models.Recipe;

import java.util.ArrayList;

import static com.example.android.bakingapp.BakingAppWidgetProvider.RECIPE_LIST;

/**
 * Created by ahmed on 10/01/2018.
 */

public class RecipesLoader extends Service implements RecipeHandler {

    public static ArrayList<Recipe> sRecipes;

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID)){
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        loadRecipes();
        return super.onStartCommand(intent, flags, startId);
    }

    private void loadRecipes() {
        RetrofitAPIClient client = new RetrofitAPIClient(this);
        client.getAllRecipes();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onRecipeListLoaded(ArrayList<Recipe> recipes) {
        sRecipes = recipes;
        populateWidget();
    }

    /**
     * Method which sends broadcast to BakingAppWidgetProvider
     * so that widget is notified to do necessary action
     * and here action == BakingAppWidgetProvider.DATA_FETCHED
     */
    private void populateWidget() {
        if(sRecipes != null){
            Intent widgetUpdateIntent = new Intent();
            widgetUpdateIntent.setAction(BakingAppWidgetProvider.RECIPES_LOADED_ACTION);
            widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    appWidgetId);
            // out recipes in intent
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(RECIPE_LIST, sRecipes);
            widgetUpdateIntent.putExtras(bundle);

            sendBroadcast(widgetUpdateIntent);

            this.stopSelf();
        }
    }

    @Override
    public void onExploreClicked(int position) {

    }
}
