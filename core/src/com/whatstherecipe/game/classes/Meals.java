package com.whatstherecipe.game.classes;

import java.util.ArrayList;

public class Meals {
    public static ArrayList<Meal> mealsList = new ArrayList<Meal>();

    public Meals() {
        String[] adoboIngredients = { "pork-ribs", "toyo", "suka" };
        mealsList.add(new Meal("adobo", adoboIngredients));

        String[] sinigangIngredients = { "pork-ribs", "tamarind", "tomatoes", "onion" };
        mealsList.add(new Meal("sinigang", sinigangIngredients));
    }

    public Meal getRandomMeal() {
        int randomIndex = (int) (Math.random() * mealsList.size());

        return mealsList.get(randomIndex);
    }
}
