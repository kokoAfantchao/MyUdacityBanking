package com.android.mig.bakingapp.utils;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.mig.bakingapp.R;
import com.android.mig.bakingapp.RecipeWidget;
import com.android.mig.bakingapp.models.Ingredient;

import java.util.ArrayList;

public class Preferences {

    public static final String PREFERENCE_FILE_KEY = "com.mig.android.mig.bakingapp";


    /**
     * Stores the last ingredients that were last viewed into SharedPreferences
     *
     * @param context             the activity context
     * @param recipe              the recipe name
     * @param ingredientArrayList an array that contains the ingredients for one particular recipe
     */
    public static void saveIngredientList(Context context, String recipe, ArrayList<Ingredient> ingredientArrayList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < ingredientArrayList.size(); i++) {
            String ingredient = ingredientArrayList.get(i).getIngredient() + " - " +
                    ingredientArrayList.get(i).getIngredientQuantity() + " " +
                    ingredientArrayList.get(i).getIngredientMeasure();
            editor.putString(String.valueOf(i), ingredient);
        }
        editor.apply();
        updateWidget(context, recipe);
    }


    /**
     * Delete all the ingredients from SharedPreferences
     *
     * @param context the activity context
     */
    public static void deleteAllPreferences(Context context) {
        context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE).edit().clear().apply();
    }

    /**
     * Notifies the RemoteViewServices that data has changed, so it can update those changes
     *
     * @param context the activity context
     * @param recipe  the recipe name
     */
    private static void updateWidget(Context context, String recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_list);
        RecipeWidget.updateRecipeWidget(context, appWidgetManager, appWidgetIds, recipe);
    }
}
