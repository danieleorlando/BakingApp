package me.danieleorlando.bakingapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;

import me.danieleorlando.bakingapp.R;
import me.danieleorlando.bakingapp.adapter.RecipeAdapter;
import me.danieleorlando.bakingapp.api.ApiService;
import me.danieleorlando.bakingapp.config.Constants;
import me.danieleorlando.bakingapp.model.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WidgetConfigurationActivity extends AppCompatActivity implements View.OnClickListener {

    private RecipeAdapter adapter;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private static final String PREF_PREFIX_KEY_RECIPE_NAME = "appwidget_recipe_name";
    private static final String PREFS_NAME = "me.danieleorlando.bakingapp.widget.RecipeWidgetProvider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_widget_configuration);

        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.hasFixedSize();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_RECIPE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        service.getRecipes().enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                adapter = new RecipeAdapter(getLayoutInflater(), WidgetConfigurationActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.clearRecipes();
                adapter.addRecipe(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
            }

        });


    }

    @Override
    public void onClick(View v) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(WidgetConfigurationActivity.this);
        RecipeWidgetProvider.updateAppWidget(WidgetConfigurationActivity.this, appWidgetManager, mAppWidgetId);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();

    }

}
