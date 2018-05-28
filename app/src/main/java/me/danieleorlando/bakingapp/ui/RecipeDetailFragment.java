package me.danieleorlando.bakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.danieleorlando.bakingapp.R;
import me.danieleorlando.bakingapp.adapter.StepAdapter;
import me.danieleorlando.bakingapp.config.Constants;
import me.danieleorlando.bakingapp.model.Ingredient;
import me.danieleorlando.bakingapp.model.Recipe;
import me.danieleorlando.bakingapp.model.Step;

public class RecipeDetailFragment extends Fragment implements View.OnClickListener {

    private Recipe mRecipe;

    private StepAdapter adapter;
    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        if(savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(Constants.RECIPE);
        }

        TextView ingredientsTv = view.findViewById(R.id.ingredientsTv);

        StringBuilder sb = new StringBuilder();
        for (Ingredient ingredient: mRecipe.getIngredients()) {
            sb.append(ingredient.getIngredient()).append("\n");
        }

        ingredientsTv.setText(sb.toString());

        recyclerView = view.findViewById(R.id.recyclerViewSteps);

        linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.hasFixedSize();

        adapter = new StepAdapter(getLayoutInflater(), this);
        recyclerView.setAdapter(adapter);
        adapter.clearSteps();
        adapter.addStep(mRecipe.getSteps());

        return view;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }


    @Override
    public void onClick(View v) {
        ((OnStepItemSelectedListener)getActivity()).onStepPicked((int)v.getTag());
    }

    public interface OnStepItemSelectedListener {
        void onStepPicked(int position);
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelable(Constants.RECIPE, mRecipe);
    }
}
