package me.danieleorlando.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.danieleorlando.bakingapp.R;
import me.danieleorlando.bakingapp.config.Constants;
import me.danieleorlando.bakingapp.model.Ingredient;

public class RecipeWidgetProvider extends AppWidgetProvider {

    private static String recipeName;
    private static String recipeIngredients;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeName, recipeIngredients);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Bundle extras = intent.getExtras();
        if (extras.containsKey(Constants.RECIPE_NAME)) {
            recipeName = intent.getStringExtra(Constants.RECIPE_NAME);
            recipeIngredients = intent.getStringExtra(Constants.RECIPE_INGREDIENTS);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            updateAppWidget(context, appWidgetManager, extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID), recipeName, recipeIngredients);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    static void updateAppWidget (Context context, AppWidgetManager appWidgetManager, int appWidgetId, String recipeName, String recipeIngredients) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setTextViewText(R.id.recipeNameTv, recipeName);

        Type listType = new TypeToken<ArrayList<Ingredient>>(){}.getType();
        List<Ingredient> ingredientList = new Gson().fromJson(recipeIngredients, listType);

        if (ingredientList!=null) {
            StringBuilder sb = new StringBuilder();
            for (Ingredient ingredient : ingredientList) {
                sb.append(ingredient.getIngredient()).append("\n");
            }
            views.setTextViewText(R.id.recipeIngredientsTv, sb.toString());
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
