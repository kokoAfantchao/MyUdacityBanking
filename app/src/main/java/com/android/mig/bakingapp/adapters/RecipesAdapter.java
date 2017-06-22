package com.android.mig.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.mig.bakingapp.R;
import com.android.mig.bakingapp.models.Ingredient;
import com.android.mig.bakingapp.models.Recipe;
import com.android.mig.bakingapp.models.Step;

import java.util.ArrayList;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder>{

    ArrayList<Recipe> mRecipeArray;
    final private OnClickHandler mOnClickHandler;

    public RecipesAdapter(OnClickHandler clickHandler){
        mRecipeArray = new ArrayList<>();
        mOnClickHandler = clickHandler;
    }

    public void setRecipesAdapter(ArrayList<Recipe> recipeArray){
        mRecipeArray = recipeArray;
        notifyDataSetChanged();
    }

    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recipe, parent, false);

        return new RecipesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesAdapterViewHolder holder, int position) {
        String recipeName = mRecipeArray.get(position).getRecipeName();
        int servings = mRecipeArray.get(position).getRecipeServings();
        holder.mTextViewRecipe.setText(recipeName);
        holder.mTextViewServings.append(String.valueOf(servings));
    }

    @Override
    public int getItemCount() {
        return mRecipeArray.size();
    }

    public class RecipesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mTextViewRecipe;
        TextView mTextViewServings;
        Button mButtonIngredient;
        Button mButtonStep;

        public RecipesAdapterViewHolder(View itemView) {
            super(itemView);
            mTextViewRecipe = (TextView)itemView.findViewById(R.id.text_view_recipe_item);
            mTextViewServings = (TextView)itemView.findViewById(R.id.text_view_servings);
            mButtonIngredient = (Button) itemView.findViewById(R.id.button_ingredients);
            mButtonIngredient.setOnClickListener(this);
            mButtonStep = (Button) itemView.findViewById(R.id.button_steps);
            mButtonStep.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == mButtonIngredient.getId())
                mOnClickHandler.OnClickIngredient(mRecipeArray.get(getAdapterPosition()).getRecipeName(), mRecipeArray.get(getAdapterPosition()).getRecipeIngredient());
            else if (view.getId() == mButtonStep.getId()){
                mOnClickHandler.OnClickStep(mRecipeArray.get(getAdapterPosition()).getRecipeStep());
            }
        }
    }

    public interface OnClickHandler{
        void OnClickIngredient(String recipe, ArrayList<Ingredient> ingredients);
        void OnClickStep(ArrayList<Step> steps);
    }

}
