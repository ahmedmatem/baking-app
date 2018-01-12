package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.services.RecipesLoader;
import com.example.android.bakingapp.services.WidgetService;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "BakingAppWidgetProvider";

    // String to be sent on Broadcast as soon as RecipeList is Loaded
    // should be included on BakingAppWidgetProvider manifest intent action
    // to be recognized by this BakingAppWidgetProvider to receive broadcast
    public static final String RECIPES_LOADED_ACTION =
            "com.example.android.bakingapp.action.RECIPES_LOADED";
    public static final String GOTO_POSITION_ACTION =
            "com.example.android.bakingapp.action.GOTO_POSITION_ACTION";

    public static final String RECIPE_LIST = "recipe-list";
    public static final String EXTRA_ITEM_POSITION = "extra-item";

    private static ArrayList<Recipe> sRecipes = new ArrayList<Recipe>();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Intent serviceIntent = new Intent(context, RecipesLoader.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    appWidgetId);
            context.startService(serviceIntent);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        // receive recipes loaded action
        if (intent.getAction().equals(BakingAppWidgetProvider.RECIPES_LOADED_ACTION)) {
            Log.d(TAG, "onReceive: RECIPE LIST LOADE ACTION");
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context);
            ArrayList<Recipe> recipes = new ArrayList<Recipe>();
            Bundle bundle = intent.getExtras();
            if (bundle != null && bundle.containsKey(RECIPE_LIST)) {
                sRecipes = bundle.getParcelableArrayList(RECIPE_LIST);
            }

            RemoteViews remoteViews = updateWidgetViews(context, appWidgetId);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

        // receive goto position action
        if(intent.getAction().equals(GOTO_POSITION_ACTION)){
            Log.d(TAG, "onReceive: GOTO POSITION ACTION");
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context);
            int position = intent.getIntExtra(EXTRA_ITEM_POSITION, 0);
            // launch RecipeListActivity and scroll to recipe at given position
            Intent launchIntent = new Intent(context, RecipeListActivity.class);
            launchIntent.putExtra(EXTRA_ITEM_POSITION, position);
            context.startActivity(launchIntent);
        }
    }

    private RemoteViews updateWidgetViews(Context context, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.baking_app_widget);

        Intent svcIntent = new Intent(context, WidgetService.class);
        // passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // put all recipes names in intent
        svcIntent.putStringArrayListExtra(RECIPE_LIST, listOfRecipeNames(sRecipes));
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        // setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(R.id.widgetCollectionList, svcIntent);
        // setting an empty view in case of no data
        // remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);

        // This section makes it possible for items to have individualized behavior.
        // It does this by setting up a pending intent template. Individuals items of a collection
        // cannot set up their own pending intents. Instead, the collection as a whole sets
        // up a pending intent template, and the individual items set a fillInIntent
        // to create unique behavior on an item-by-item basis.
        Intent broadcastIntent = new Intent(context, BakingAppWidgetProvider.class);
        // Set the action for the intent.
        // When the user touches a particular list view item,
        // it will have the effect of broadcasting GOTO_POSITION_ACTION.
        broadcastIntent.setAction(GOTO_POSITION_ACTION);
        broadcastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        broadcastIntent.setData(Uri.parse(broadcastIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, 0, broadcastIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widgetCollectionList, pendingIntent);

        return remoteViews;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private ArrayList<String> listOfRecipeNames(ArrayList<Recipe> recipes) {
        ArrayList<String> recipeNames = new ArrayList<>();
        if (recipes != null) {
            for (Recipe recipe : recipes) {
                recipeNames.add(recipe.getName());
            }
            return recipeNames;
        }
        return null;
    }
}

