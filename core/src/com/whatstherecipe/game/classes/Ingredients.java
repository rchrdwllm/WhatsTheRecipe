package com.whatstherecipe.game.classes;

import java.util.ArrayList;

public class Ingredients {
    public static String[][] ingredientsList = {
            { "bile", "garlic", "onion", "chicharon", "chili" }, // 0
            { "calamansi", "tomatoes", "ginger", "tamarind", "shrimps", "peppercorn" }, // 1
            { "ketchup", "suka", "mayonnaise", "patis", "toyo" }, // 2
            { "rice-noodles", "pork-ribs", "sisig-meat", "chicken", "goat-meat" } // 3
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
