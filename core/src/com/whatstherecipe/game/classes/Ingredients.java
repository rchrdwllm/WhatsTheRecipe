package com.whatstherecipe.game.classes;

import java.util.ArrayList;

public class Ingredients {
    public static String[][] ingredientsList = {
            { "bile", "chicken", "ginger", "onion", "sisig-meat" }, // 0
            { "calamansi", "chili", "ketchup", "patis", "shrimps" }, // 1
            { "chicharon", "garlic", "mayonnaise", "pork-ribs" }, // 2
            { "rice-noodles", "tamarind", "tomatoes", "pork-ribs", "suka", "toyo" } // 2
    };

    public static String getRandomIngredient(ArrayList<String> existingIngredients) {
        int randomCategory = (int) (Math.random() * ingredientsList.length);
        String[] category = ingredientsList[randomCategory];
        String randomIngredient = category[(int) (Math.random() * category.length)];

        while (existingIngredients.contains(randomIngredient)) {
            randomCategory = (int) (Math.random() * ingredientsList.length);
            category = ingredientsList[randomCategory];
            randomIngredient = category[(int) (Math.random() * category.length)];
        }

        return randomIngredient;
    }
}
