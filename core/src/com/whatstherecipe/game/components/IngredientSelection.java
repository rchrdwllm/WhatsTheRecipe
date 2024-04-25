package com.whatstherecipe.game.components;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.classes.Meal;
import com.whatstherecipe.game.ui.Colors;
import com.whatstherecipe.game.ui.CustomSkin;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import java.util.ArrayList;

import com.badlogic.gdx.math.Interpolation;

public class IngredientSelection {
    public final WhatsTheRecipe game;
    public Stage stage;
    public Meal meal;
    private Table table;
    private Table leftTable;
    private Table centerTable;
    private Table rightTable;
    private StepSorting stepSorting;
    private RecipePaperView recipePaperView;
    private ArrayList<Ingredient> selectedIngredients;
    private ArrayList<String> selectedIngredientsString;

    public IngredientSelection(RecipePaperView recipePaperView) {
        this.game = recipePaperView.game;
        this.stage = recipePaperView.stage;
        this.meal = recipePaperView.meal;
        this.stepSorting = recipePaperView.stepSorting;
        this.recipePaperView = recipePaperView;
        this.selectedIngredients = new ArrayList<Ingredient>();
        this.selectedIngredientsString = new ArrayList<String>();

        initTables();
        renderLeft();
        renderCenter();
        renderRight();
    }

    private void initTables() {
        this.table = new Table();
        this.leftTable = new Table();
        this.centerTable = new Table();
        this.rightTable = new Table();

        this.table.setFillParent(true);
        this.table.add(leftTable).expand().left().padLeft(100);
        this.table.add(centerTable).expand().center();
        this.table.add(rightTable).expand().right().padRight(100);
    }

    public void show() {
        this.stage.addActor(table);
        this.table.toFront();
        this.table.addAction(alpha(0f));
        this.table.addAction(sequence(delay(1f, fadeIn(0.5f, Interpolation.pow5))));

        this.selectedIngredients.addAll(this.recipePaperView.kitchenScreen.ingredientsInBasket);
        this.selectedIngredients.forEach(ingredient -> {
            this.selectedIngredientsString.add(ingredient.name);
        });
    }

    public void hide() {
        this.table.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
            this.table.remove();
        })));
    }

    private void renderLeft() {
        Label mealName = new Label(meal.name, CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 64));
        ArrayList<Label> instructions = new ArrayList<Label>();

        this.leftTable.add(mealName).left().padBottom(15).row();

        instructions.add(
                new Label("Listed here on the paper are the ingredients for cooking " + meal.name
                        + ". Find and collect them first from the 4 labeled cabinets back in the kitchen to proceed to the next phase: finding out the correct steps for cooking "
                        + meal.name + ".",
                        CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32)));
        instructions.add(new Label(
                "Use your basket at the bottom right for collecting ingredients, viewing them, as well as removing those that you don't want anymore!",
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32)));

        instructions.forEach(instruction -> {
            instruction.setWrap(true);

            this.leftTable.add(instruction).width(400).padBottom(15).row();
        });
    }

    private void renderCenter() {
        for (int i = 1; i <= this.meal.ingredients.size(); i++) {
            String ingredient = this.meal.ingredients.get(i - 1);

            ingredient = ingredient.substring(0, 1).toUpperCase() + ingredient.substring(1);

            Label ingredientLabel = new Label(i + ". " + ingredient,
                    CustomSkin.generateCustomLilitaOneFont(Colors.brown, 32));

            this.centerTable.add(ingredientLabel).width(300).padBottom(15).row();
        }
    }

    private void renderRight() {
        Table instructionsContainer = new Table();
        ArrayList<Label> instructions = new ArrayList<Label>();

        instructions.add(new Label("Click on the button below to check if your basket contains the right ingredients!",
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32)));
        instructions.add(new Label(
                "Remember: Having the wrong collection of ingredients will cost you a star, and the right collection will give you one star (if you don't already have 3 stars)!",
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32)));

        instructions.forEach(instruction -> {
            instruction.setWrap(true);

            instructionsContainer.add(instruction).width(400).padBottom(15).row();
        });

        this.rightTable.add(instructionsContainer).row();

        TextButton checkBtn = new TextButton(
                "Check ingredients",
                this.game.skin.get("text-button-default", TextButtonStyle.class));

        checkBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                selectedIngredientsString.forEach(ingredient -> {
                    System.out.println(ingredient);
                });

                meal.ingredients.forEach(ingredient -> {
                    System.out.println(ingredient);
                });

                if (selectedIngredientsString.equals(meal.ingredients)) {
                    System.out.println("All ingredients are correct!");
                }

                return true;
            }

        });

        this.rightTable.add(checkBtn).padTop(50);
    }
}
