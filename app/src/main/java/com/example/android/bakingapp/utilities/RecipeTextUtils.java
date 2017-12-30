package com.example.android.bakingapp.utilities;

import android.util.Log;

import com.example.android.bakingapp.models.Ingredient;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * Created by ahmed on 17/12/2017.
 */

public class RecipeTextUtils {

    // measures
    private static final String MEASURE_CUP = "CUP";
    private static final String MEASURE_TBLSP = "TBLSP";
    private static final String MEASURE_TSP = "TSP";
    private static final String MEASURE_K = "K";
    private static final String MEASURE_G = "G";
    private static final String MEASURE_OZ = "OZ";
    private static final String MEASURE_UNIT = "UNIT";

    /**
     *
     * @param ingredients
     * @return friendly looking text of ingredients or null if no ingredients
     */
    public static String friendlyLookIngredients(List<Ingredient> ingredients){
        if(ingredients.size() == 0){
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for(Ingredient ingredient : ingredients){
            stringBuilder.append("\n" +
                    friendlyQuantity(String.valueOf(ingredient.mQuantity)) + " " +
                    friendlyMeasure(ingredient.mMeasure) + " " +
                    ingredient.mIngredient);
        }
        return stringBuilder.toString();
    }

    /**
     *
     * @param quantity
     * @return an integer part from float quantity if fragment part is equal 0
     *  or the same quantity
     */
    private static String friendlyQuantity(String quantity){
        if(quantity.contains(".")){
            String[] splitQuantity = quantity.split("\\.");
            String fragment = splitQuantity[splitQuantity.length - 1];
            if(Integer.valueOf(fragment) == 0){
                return splitQuantity[0];
            }
        }
        return quantity;
    }

    private static String friendlyMeasure(String measure){
        switch (measure.toUpperCase()){
            case MEASURE_CUP:
                return "cup";
            case MEASURE_G:
                return "g";
            case MEASURE_K:
                return "kg";
            case MEASURE_OZ:
                return "ounce";
            case MEASURE_TBLSP:
                return "table-spoon";
            case MEASURE_TSP:
                return "tea-spoon";
            case MEASURE_UNIT:
                return "unit";
                default:
                    throw new InvalidParameterException("Invalid measure: " + measure);
        }
    }
}
