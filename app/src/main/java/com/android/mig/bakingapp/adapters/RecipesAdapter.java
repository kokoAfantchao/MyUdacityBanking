package com.android.mig.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mig.bakingapp.R;
import com.android.mig.bakingapp.models.Ingredient;
import com.android.mig.bakingapp.models.Recipe;
import com.android.mig.bakingapp.models.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder> {

    ArrayList<Recipe> mRecipeArray;
    final private OnClickHandler mOnClickHandler;

    public RecipesAdapter(OnClickHandler clickHandler) {
        mRecipeArray = new ArrayList<>();
        mOnClickHandler = clickHandler;
    }

    public void setRecipesAdapter(ArrayList<Recipe> recipeArray) {
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
        Recipe recipe = mRecipeArray.get(position);
        String recipeName = recipe.getRecipeName();
        String recipeImage = recipe.getRecipeImage();
        int servings = recipe.getRecipeServings();
        holder.mTextViewRecipe.setText(recipeName);
        holder.mTextViewServings.append(String.valueOf(servings));
        if (!TextUtils.isEmpty(recipeImage)) {
            Picasso.with(holder.itemView.getContext()).load(recipeImage)
                    .error(R.drawable.pie).into(holder.mImageViewRecip);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipeArray.size();
    }

    public class RecipesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_view_recipe_item)
        TextView mTextViewRecipe;
        @BindView(R.id.text_view_servings)
        TextView mTextViewServings;
        @BindView(R.id.button_ingredients)
        Button mButtonIngredient;
        @BindView(R.id.button_steps)
        Button mButtonStep;
        @BindView(R.id.image_view_recipe_image)
        ImageView mImageViewRecip;

        public RecipesAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mButtonIngredient.setOnClickListener(this);
            mButtonStep = (Button) itemView.findViewById(R.id.button_steps);
            mButtonStep.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == mButtonIngredient.getId())
                mOnClickHandler.OnClickIngredient(mRecipeArray.get(getAdapterPosition()).getRecipeName(), mRecipeArray.get(getAdapterPosition()).getRecipeIngredient());
            else if (view.getId() == mButtonStep.getId()) {
                mOnClickHandler.OnClickStep(mRecipeArray.get(getAdapterPosition()).getRecipeStep());
            }
        }
    }

    public interface OnClickHandler {
        void OnClickIngredient(String recipe, ArrayList<Ingredient> ingredients);

        void OnClickStep(ArrayList<Step> steps);
    }

}
