package com.example.android.bakingapp.services;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.data.WidgetDataProvider;

/**
 * Created by ahmed on 10/01/2018.
 */

@SuppressLint("NewApi")
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        WidgetDataProvider dataProvider =
                new WidgetDataProvider(getApplicationContext(), intent);
        return dataProvider;
    }
}
