package com.android.mig.bakingapp.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable{
    @SerializedName(value = "quantity")
    private double mIngredientQuantity;
    @SerializedName(value = "measure")
    private String mIngredientMeasure;
    @SerializedName(value = "ingredient")
    private String mIngredient;

    public Ingredient(double quantity, String measure, String ingredient){
        mIngredientQuantity = quantity;
        mIngredientMeasure = measure;
        mIngredient = ingredient;
    }

    protected Ingredient(Parcel in) {
        mIngredientQuantity = in.readDouble();
        mIngredientMeasure = in.readString();
        mIngredient = in.readString();
    }

    public void setIngredientQuantity(double quantity){
        mIngredientQuantity = quantity;
    }

    public void setIngredientMeasure(String measure){
        mIngredientMeasure = measure;
    }

    public void setIngredient(String ingredient){
        mIngredient = ingredient;
    }

    public double getIngredientQuantity(){return mIngredientQuantity;}
    public String getIngredientMeasure(){return mIngredientMeasure;}
    public String getIngredient(){return mIngredient;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(mIngredientQuantity);
        parcel.writeString(mIngredientMeasure);
        parcel.writeString(mIngredient);
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

}
