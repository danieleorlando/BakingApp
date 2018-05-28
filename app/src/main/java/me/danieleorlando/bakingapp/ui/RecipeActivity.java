package me.danieleorlando.bakingapp.ui;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import me.danieleorlando.bakingapp.R;
import me.danieleorlando.bakingapp.config.Constants;
import me.danieleorlando.bakingapp.model.Recipe;

public class RecipeActivity extends AppCompatActivity implements RecipeDetailFragment.OnStepItemSelectedListener {

    private FragmentTransaction transaction;

    private Recipe recipe;
    private int step_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recipe = (Recipe)getIntent().getExtras().get(Constants.RECIPE);

        if(savedInstanceState == null) {

            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle(recipe.getName());
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setRecipe(recipe);
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.containerRecipe, recipeDetailFragment);
            transaction.commit();
        }

    }

    public void selectStep(int position) {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setStep(recipe.getSteps().get(position));

        transaction = getSupportFragmentManager().beginTransaction();
        if (tabletSize) {
            transaction.replace(R.id.containerStep, stepDetailFragment);
        } else {
            transaction.replace(R.id.containerRecipe, stepDetailFragment);
            transaction.addToBackStack(recipe.getSteps().get(position).getShortDescription());
        }
        transaction.commit();
    }

    public void onStepPicked(int position) {
        step_index = position;
        selectStep(position);
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putInt(Constants.STEP_INDEX,step_index);
    }
}
