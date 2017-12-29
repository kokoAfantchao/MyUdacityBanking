package com.android.mig.bakingapp;

import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.mig.bakingapp.models.Ingredient;
import com.android.mig.bakingapp.models.Recipe;
import com.android.mig.bakingapp.services.RecipesService;
import com.android.mig.bakingapp.utils.AppConnectivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {


    private List<Ingredient> mCollection = new ArrayList<Ingredient>();
    private Context mContext;

    public WidgetDataProvider(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
    }

    // called when RemoteViewsFactory is first created and when notifyWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        getCollection();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mCollection.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);
        remoteViews.setTextViewText(android.R.id.text1, mCollection.get(position).getIngredient());
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // true if items in list won't change
    @Override
    public boolean hasStableIds() {
        return false;
    }

    public void getCollection() {
        mCollection.clear();
        if (AppConnectivity.isOnline(mContext)) {
            try {
                List<Recipe> recipesFromUrl = RecipesService.getRecipesFromUrl();
                if (recipesFromUrl.size() >= 1) {
                    mCollection = recipesFromUrl.get(1).getRecipeIngredient();
                    Log.d(this.getClass().getName(), mCollection.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
