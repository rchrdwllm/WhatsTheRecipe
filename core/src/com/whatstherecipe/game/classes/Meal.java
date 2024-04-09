package com.whatstherecipe.game.classes;

import java.util.ArrayList;
import java.util.Collections;

public class Meal {
    public String name;
    public String difficulty;
    public ArrayList<String> ingredients;
    public ArrayList<Step> steps;

    public Meal(String name, String difficulty, String[] ingredients, String[] steps) {
        this.name = name;
        this.difficulty = difficulty;
        this.ingredients = new ArrayList<String>();
        this.steps = new ArrayList<Step>();

        for (String ingredient : ingredients) {
            this.ingredients.add(ingredient);
        }

        for (int i = 0; i < steps.length; i++) {
            String label = steps[i];
            int stepNumber = i;

            Step step = new Step(label, stepNumber);

            this.steps.add(step);
        }
    }

    public void shuffleMealSteps() {
        ArrayList<Step> originalSteps = new ArrayList<Step>(this.steps);

        do {
            Collections.shuffle(this.steps);
        } while (originalSteps.equals(this.steps));
    }
}
