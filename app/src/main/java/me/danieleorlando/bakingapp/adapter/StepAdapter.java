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
import me.danieleorlando.bakingapp.model.Step;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.Holder> {

    private View.OnClickListener onClickListener;
    private final LayoutInflater inflater;
    private List<Step> stepList;

    public StepAdapter(LayoutInflater inflater, View.OnClickListener onClickListener) {
        this.inflater = inflater;
        this.onClickListener = onClickListener;
        stepList = new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_step, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        if (stepList.get(position).getThumbnailURL().equals("")) {
            Picasso.with(holder.thumbIv.getContext())
                    .load(R.drawable.ic_play_circle_outline_black_24dp)
                    .placeholder(R.drawable.ic_play_circle_outline_black_24dp)
                    .into(holder.thumbIv);
        } else {
            Picasso.with(holder.thumbIv.getContext())
                    .load(stepList.get(position).getThumbnailURL())
                    .placeholder(R.drawable.ic_play_circle_outline_black_24dp)
                    .into(holder.thumbIv);
        }

        holder.descriptionTv.setText(stepList.get(position).getShortDescription());
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(onClickListener);
    }

    public void addStep(List<Step> steps) {
        stepList.clear();
        stepList.addAll(steps);
        notifyDataSetChanged();
    }

    public void clearSteps() {
        stepList.clear();
        notifyDataSetChanged();
    }

    public Step getStep(int position) {
        return stepList.get(position);
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView thumbIv;
        TextView descriptionTv;

        public Holder(View v) {
            super(v);
            thumbIv = v.findViewById(R.id.thumbIv);
            descriptionTv = v.findViewById(R.id.descriptionTv);
        }
    }
}