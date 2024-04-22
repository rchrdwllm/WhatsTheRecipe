package com.whatstherecipe.game.classes;

import java.util.ArrayList;

public class Ingredients {
    public static String[][] ingredientsList = {
            { "bile", "suka", "ketchup", "onion", "sisig-meat" }, // 0
            { "calamansi", "chili", "ginger", "pork-ribs", "shrimps" }, // 1
            { "chicharon", "garlic", "mayonnaise", "patis" }, // 2
            { "rice-noodles", "tamarind", "tomatoes", "chicken", "toyo" } // 3
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
