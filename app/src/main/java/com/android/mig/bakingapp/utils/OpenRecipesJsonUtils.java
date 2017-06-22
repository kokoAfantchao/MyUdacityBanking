package com.android.mig.bakingapp.utils;

import com.android.mig.bakingapp.models.Ingredient;
import com.android.mig.bakingapp.models.Recipe;
import com.android.mig.bakingapp.models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class OpenRecipesJsonUtils {

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * This method retrieves data from a JSON string
     * and save it into an ArrayList of Recipe objects
     *
     * @param recipesJsonResponse a JSON string
     * @return an ArrayList of Recipe objects
     */
    public static ArrayList<Recipe> getRecipeArrayFromJson(String recipesJsonResponse) {

        final String RECIPE_ID = "id";
        final String RECIPE_NAME = "name";
        final String RECIPE_INGREDIENTS_ARRAY = "ingredients";
            final String INGREDIENT_QUANTITY = "quantity";
            final String INGREDIENT_MEASURE = "measure";
            final String INGREDIENT = "ingredient";
        final String RECIPE_STEPS_ARRAY = "steps";
            final String STEP_ID = "id";
            final String STEP_SHORT_DESCRIPTION = "shortDescription";
            final String STEP_DESCRIPTION = "description";
            final String STEP_VIDEO_URL = "videoURL";
            final String STEP_THUMBNAIL_URL = "thumbnailURL";
        final String RECIPE_SERVINGS = "servings";
        final String RECIPE_IMAGE = "image";

        ArrayList<Recipe> mRecipeArrayList = new ArrayList<>();
        Recipe mRecipeObject = null;

        try {
            JSONArray recipesJsonArray = new JSONArray(recipesJsonResponse);

            for (int i = 0; i < recipesJsonArray.length(); i++){

                Ingredient mIngredientObject = null;
                Step mStepObject = null;

                JSONObject resultJsonObject = recipesJsonArray.getJSONObject(i);
                int id = resultJsonObject.getInt(RECIPE_ID);
                String name = resultJsonObject.getString(RECIPE_NAME);
                int servings = resultJsonObject.getInt(RECIPE_SERVINGS);
                String image = resultJsonObject.getString(RECIPE_IMAGE);

                // retrieves ingredients array from json file and save its data into an object
                JSONArray ingredientsJsonArray = resultJsonObject.getJSONArray(RECIPE_INGREDIENTS_ARRAY);
                ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
                for (int x = 0; x < ingredientsJsonArray.length(); x++){
                    JSONObject ingredientJsonObject = ingredientsJsonArray.getJSONObject(x);
                    double quantity = ingredientJsonObject.getDouble(INGREDIENT_QUANTITY);
                    String measure = ingredientJsonObject.getString(INGREDIENT_MEASURE);
                    String ingredient = ingredientJsonObject.getString(INGREDIENT);
                    mIngredientObject = new Ingredient(quantity, measure, ingredient);
                    ingredientArrayList.add(mIngredientObject);
                }

                // retrieves steps array from json file and save its data into an object
                JSONArray stepsJsonArray = resultJsonObject.getJSONArray(RECIPE_STEPS_ARRAY);
                ArrayList<Step> stepsArrayList = new ArrayList<>();
                for (int y = 0; y < stepsJsonArray.length(); y++){
                    JSONObject stepJsonObject = stepsJsonArray.getJSONObject(y);
                    int stepID = stepJsonObject.getInt(STEP_ID);
                    String shortDescription = stepJsonObject.getString(STEP_SHORT_DESCRIPTION);
                    String description = stepJsonObject.getString(STEP_DESCRIPTION);
                    String videoURL = stepJsonObject.getString(STEP_VIDEO_URL);
                    String thumbnailURL = stepJsonObject.getString(STEP_THUMBNAIL_URL);
                    mStepObject = new Step(stepID, shortDescription, description, videoURL, thumbnailURL);
                    stepsArrayList.add(mStepObject);
                }

                // adds a new recipe item into array list
                mRecipeObject = new Recipe(id, name, ingredientArrayList, stepsArrayList, servings, image);
                mRecipeArrayList.add(mRecipeObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mRecipeArrayList;
    }
}
