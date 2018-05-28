package me.danieleorlando.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import me.danieleorlando.bakingapp.R;
import me.danieleorlando.bakingapp.config.Constants;

public class RecipeWidgetProvider extends AppWidgetProvider {

    private static String recipeName;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        /*for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }*/
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    static void updateAppWidget (Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        //appWidgetManager.updateAppWidget(appWidgetId, views);*/
    }
}
