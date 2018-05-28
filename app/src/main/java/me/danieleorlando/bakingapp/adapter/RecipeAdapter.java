package me.danieleorlando.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.danieleorlando.bakingapp.R;
import me.danieleorlando.bakingapp.model.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.Holder> {

    private View.OnClickListener onClickListener;
    private final LayoutInflater inflater;
    private List<Recipe> recipeList;

    public RecipeAdapter(LayoutInflater inflater, View.OnClickListener onClickListener) {
        this.inflater = inflater;
        this.onClickListener = onClickListener;
        recipeList = new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        if (recipeList.get(position).getImage().equals("")) {
            Picasso.with(holder.recipeIv.getContext())
                    .load(R.drawable.ic_cake_black_24dp)
                    .placeholder(R.drawable.ic_cake_black_24dp)
                    .into(holder.recipeIv);
        } else {
            Picasso.with(holder.recipeIv.getContext())
                    .load(recipeList.get(position).getImage())
                    .placeholder(R.drawable.ic_cake_black_24dp)
                    .into(holder.recipeIv);
        }
        holder.nameTv.setText(recipeList.get(position).getName());
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(onClickListener);
    }

    public void addRecipe(List<Recipe> recipes) {
        recipeList.clear();
        recipeList.addAll(recipes);
        notifyDataSetChanged();
    }

    public void clearRecipes() {
        recipeList.clear();
        notifyDataSetChanged();
    }

    public Recipe getRecipe(int position) {
        return recipeList.get(position);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView recipeIv;
        TextView nameTv;

        public Holder(View v) {
            super(v);
            recipeIv = v.findViewById(R.id.recipeIv);
            nameTv = v.findViewById(R.id.nameTv);
        }
    }
}