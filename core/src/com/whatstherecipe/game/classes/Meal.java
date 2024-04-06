package com.whatstherecipe.game.classes;

import java.util.ArrayList;

public class Meal {
    public String name;
    public ArrayList<String> ingredients;

    public Meal(String name, String[] ingredients) {
        this.name = name;
        this.ingredients = new ArrayList<String>();

        for (String ingredient : ingredients) {
            this.ingredients.add(ingredient);
        }
    }
}
