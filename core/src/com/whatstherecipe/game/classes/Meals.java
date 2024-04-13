package com.whatstherecipe.game.classes;

import java.util.ArrayList;

public class Meals {
    public static ArrayList<Meal> easyMeals = new ArrayList<Meal>();
    public static ArrayList<Meal> mediumMeals = new ArrayList<Meal>();
    public static ArrayList<Meal> hardMeals = new ArrayList<Meal>();

    public Meals() {
        String[] adoboIngredients = { "chicken", "toyo", "suka", "garlic", }; // Pepper yung kulang
        String[] adoboSteps = {
                "Heat a little oil in a pot over medium-high heat. Add chicken and cook until lightly browned on all sides.",
                "Toss in the garlic and cook until fragrant (about 30 seconds).",
                "Pour in the soy sauce, vinegar, and peppercorns. Bring to a simmer.",
                "Reduce heat to low, cover, and simmer until the chicken is cooked through and the sauce is slightly thickened (about 30-40 minutes).",
                "Enjoy your delicious adobo with steamed rice."

        };
        easyMeals.add(new Meal("Adobo", "easy", adoboIngredients, adoboSteps));

        String[] sinigangIngredients = { "pork-ribs", "tamarind", "tomatoes", "onion" };
        String[] sinigangSteps = {
                "In a pot, cover the pork ribs with water and bring to a boil. Skim off any scum that rises to the top.",
                "Toss in the onion and tomatoes. Simmer for 30 minutes, or until the pork is partially tender.",
                "Stir in the tamarind soup base. Simmer for another 15-20 minutes, or until the pork is fully tender.",
                "Add salt to taste.",
                "Enjoy your hot and sour sinigang with rice!"
        };
        easyMeals.add(new Meal("Sinigang", "easy", sinigangIngredients, sinigangSteps));

        String[] pancitpalabokIngredients = { "rice-noodles", "shrimp", "chicharon", "patis" };
        String[] pancitpalabokSteps = {
                "Cook the noodles according to package instructions. Drain and set aside.",
                "In a pot, heat the oil over medium heat. Add the shrimp and cook until pink.",
                "Add the patis and stir to combine.",
                "Add the noodles and toss to coat in the sauce.",
                "Top with chicharon, green onions, and boiled eggs.",
                "Enjoy your delicious pancit palabok!"
        };
        easyMeals.add(new Meal("Pancit Palabok", "easy", pancitpalabokIngredients, pancitpalabokSteps));

        String[] sisigIngredients = { "sisig-meat", "calamansi", "onion", "mayonnaise", "chili" };
        String[] sisigSteps = {
                "In a pan, heat the oil over medium heat. Add the sisig meat and cook until browned.",
                "Add the onion and cook until translucent.",
                "Squeeze in the calamansi juice and stir to combine.",
                "Add the mayonnaise and stir to coat the meat.",
                "Top with chili and onions.",
                "Enjoy your sizzling sisig!"
        };
        mediumMeals.add(new Meal("Sisig", "medium", sisigIngredients, sisigSteps));

        String[] papaitanIngredients = { "bile", "ginger", "onion", "garlic" }; // Kulang Goat Meat
        String[] papaitanSteps = {
                "In a pot, combine the goat meat, ginger, and water. Bring to a boil and simmer until the meat is tender.",
                "Add the bile and stir to combine.",
                "Add the onion and garlic. Simmer for another 10 minutes.",
                "Season with salt and pepper to taste.",
                "Enjoy your hearty papaitan!"
        };
        hardMeals.add(new Meal("Papaitan", "hard", papaitanIngredients, papaitanSteps));
    }

    public static Meal getRandomEasyMeal() {
        int randomIndex = (int) (Math.random() * easyMeals.size());

        return easyMeals.get(randomIndex);
    }

    public static Meal getRandomEasyMeal(ArrayList<Meal> previousMeals) {
        int randomIndex = (int) (Math.random() * easyMeals.size());

        if (previousMeals.containsAll(easyMeals)) {
            return easyMeals.get(randomIndex);
        } else {
            while (previousMeals.contains(easyMeals.get(randomIndex))) {
                randomIndex = (int) (Math.random() * easyMeals.size());
            }
        }

        return easyMeals.get(randomIndex);
    }

    public static Meal getRandomMediumMeal(ArrayList<Meal> previousMeals) {
        int randomIndex = (int) (Math.random() * mediumMeals.size());

        if (previousMeals.containsAll(mediumMeals)) {
            return mediumMeals.get(randomIndex);
        } else {
            while (previousMeals.contains(mediumMeals.get(randomIndex))) {
                randomIndex = (int) (Math.random() * mediumMeals.size());
            }
        }

        return mediumMeals.get(randomIndex);
    }

    public static Meal getRandomHardMeal(ArrayList<Meal> previousMeals) {
        int randomIndex = (int) (Math.random() * hardMeals.size());

        if (previousMeals.containsAll(hardMeals)) {
            return hardMeals.get(randomIndex);
        } else {
            while (previousMeals.contains(hardMeals.get(randomIndex))) {
                randomIndex = (int) (Math.random() * hardMeals.size());
            }
        }

        return hardMeals.get(randomIndex);
    }
}
