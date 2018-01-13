package com.example.android.bakingapp.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.bakingapp.BakingAppWidgetProvider.EXTRA_ITEM_POSITION;
import static com.example.android.bakingapp.BakingAppWidgetProvider.RECIPE;

/**
 * Created by ahmed on 10/01/2018.
 */

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = "WidgetDataProvider";

    private Context mContext;
    List mCollections = new ArrayList();
    private static ArrayList<String> sRecipeIngredients;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if(bundle.containsKey(RECIPE)) {
                sRecipeIngredients = intent.getStringArrayListExtra(RECIPE);
            }
        }
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(sRecipeIngredients != null) {
            return sRecipeIngredients.size();
        }
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rvs = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_list_item);
        if (sRecipeIngredients != null) {
            rvs.setTextViewText(R.id.tv_widget_recipe_ingredient,
                    (CharSequence) sRecipeIngredients.get(position));
            rvs.setTextColor(R.id.tv_widget_recipe_ingredient, Color.BLACK);
        }

        Bundle extras = new Bundle();
        extras.putInt(EXTRA_ITEM_POSITION, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rvs.setOnClickFillInIntent(R.id.tv_widget_recipe_ingredient, fillInIntent);

        return rvs;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private void initData() {

    }
}
