package me.danieleorlando.bakingapp.api;

import java.util.ArrayList;

import me.danieleorlando.bakingapp.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipes();
}
