package com.android.mig.bakingapp.services;

import com.android.mig.bakingapp.models.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nestorkokoafantchao on 9/2/17.
 */

public class RecipesService {

    final private static String RECIPES_ADDRESS = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();

    public RecipesService() {
        if (client == null) {
            client = new OkHttpClient();
        }
    }

    public static List<Recipe> getRecipesFromUrl() throws IOException {
        Request request = new Request.Builder()
                .url(RECIPES_ADDRESS)
                .build();
        Response response = client.newCall(request).execute();
        String stringJson = response.body().string();
        List<Recipe> recipes = jsonToRecipList(stringJson);
        return recipes;
    }

    public static List<Recipe> jsonToRecipList(String json) {
        Gson gson = new Gson();
        List<Recipe> recipesList;
        Type collectionType = new TypeToken<List<Recipe>>() {
        }.getType();
        recipesList = gson.fromJson(json, collectionType);
        return recipesList;
    }
}
