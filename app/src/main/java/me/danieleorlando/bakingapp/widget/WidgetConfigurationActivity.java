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
import android.support.v7.widget.Toolbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        setContentView(R.layout.activity_widget_configuration);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.select_recipe));
        setSupportActionBar(toolbar);

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

        Gson gson = new Gson();
        String jsonIngredients = gson.toJson(adapter.getRecipe((int)v.getTag()).getIngredients());

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(WidgetConfigurationActivity.this);
        RecipeWidgetProvider.updateAppWidget(WidgetConfigurationActivity.this,
                appWidgetManager,
                mAppWidgetId,
                adapter.getRecipe((int)v.getTag()).getName(),
                jsonIngredients);

        Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        intent.putExtra(Constants.RECIPE_NAME, adapter.getRecipe((int)v.getTag()).getName());
        intent.putExtra(Constants.RECIPE_INGREDIENTS, jsonIngredients);
        setResult(RESULT_OK, intent);
        finish();

    }

}
