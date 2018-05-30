package me.danieleorlando.bakingapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.danieleorlando.bakingapp.IdlingResource.SimpleIdlingResource;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RecipeAdapter adapter;
    private RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIdlingResource();

        setupUI();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_RECIPE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        service.getRecipes().enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                getRecipes(response.body());
                mIdlingResource.setIdleState(true);
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                mIdlingResource.setIdleState(true);
            }

        });

    }

    private void setupUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            gridLayoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        recyclerView.hasFixedSize();
    }

    private void getRecipes(List<Recipe> recipeList) {
        adapter = new RecipeAdapter(getLayoutInflater(), this);
        recyclerView.setAdapter(adapter);
        adapter.clearRecipes();
        adapter.addRecipe(recipeList);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        intent.putExtra(Constants.RECIPE, adapter.getRecipe((int)v.getTag()));
        startActivity(intent);
    }
}
