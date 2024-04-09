package com.whatstherecipe.game.classes;

import java.util.ArrayList;

public class Meals {
    public static ArrayList<Meal> mealsList = new ArrayList<Meal>();

    public Meals() {
        String[] adoboIngredients = { "pork-ribs", "toyo", "suka" };
        String[] adoboSteps = {
                "first adobo step",
                "second adobo step",
                "third adobo step"
        };
        mealsList.add(new Meal("adobo", "easy", adoboIngredients, adoboSteps));

        String[] sinigangIngredients = { "pork-ribs", "tamarind", "tomatoes", "onion" };
        String[] sinigangSteps = {
                "first sinigang step",
                "second sinigang step",
                "third sinigang step"
        };
        mealsList.add(new Meal("sinigang", "easy", sinigangIngredients, sinigangSteps));
    }

    public static Meal getRandomMeal() {
        int randomIndex = (int) (Math.random() * mealsList.size());

        return mealsList.get(randomIndex);
    }
}
