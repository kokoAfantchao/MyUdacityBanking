package com.android.mig.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mig.bakingapp.R;
import com.android.mig.bakingapp.models.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder>{

    ArrayList<Ingredient> mIngredientArray;

    public IngredientsAdapter(){
        mIngredientArray = new ArrayList<Ingredient>();
    }

    public void setIngredientAdapter(ArrayList<Ingredient> ingredientArray){
        mIngredientArray = ingredientArray;
        notifyDataSetChanged();
    }

    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_ingredient, parent, false);
        return new IngredientsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsAdapterViewHolder holder, int position) {
        String ingredient = mIngredientArray.get(position).getIngredient();
        double qty = mIngredientArray.get(position).getIngredientQuantity();
        String measure = mIngredientArray.get(position).getIngredientMeasure();
        holder.mTextViewIngredient.setText(ingredient);
        holder.mTextViewQuantityMeasure.setText(String.valueOf(qty) + " " + measure);
        holder.mTextViewIngredient.setBackgroundResource(position % 2 == 0 ? R.color.green100 : R.color.green50);
        holder.mTextViewQuantityMeasure.setBackgroundResource(position % 2 == 0 ? R.color.green100 : R.color.green50);
    }

    @Override
    public int getItemCount() {
        if (mIngredientArray != null)
            return mIngredientArray.size();
        return 0;
    }

    public class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_ingredient) TextView mTextViewIngredient;
        @BindView(R.id.text_view_quantity_measure) TextView mTextViewQuantityMeasure;

        public IngredientsAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
