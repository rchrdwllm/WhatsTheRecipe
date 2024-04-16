package com.whatstherecipe.game.components;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.classes.Meal;
import com.whatstherecipe.game.classes.Step;
import com.whatstherecipe.game.ui.Colors;
import com.whatstherecipe.game.ui.CustomSkin;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import java.util.ArrayList;

public class RecipePaperView {
    private final WhatsTheRecipe game;
    private Stage stage;
    private Meal meal;
    private ArrayList<Step> shuffledSteps;
    private ArrayList<Step> arrangedSteps;
    private ArrayList<Step> sortedSteps;
    private Image brownOverlay;
    private Table table;
    private Table leftTable;
    private Table centerTable;
    private Table rightTable;
    public Image recipeRef;
    public Image recipe;
    private boolean recipePaperVisible = false;

    public RecipePaperView(final WhatsTheRecipe game, Stage stage, Meal meal) {
        this.game = game;
        this.stage = stage;
        this.meal = meal;
        this.shuffledSteps = new ArrayList<Step>(meal.steps);
        this.arrangedSteps = new ArrayList<Step>();
        this.sortedSteps = new ArrayList<Step>();

        randomizeSteps();
        renderOverlay();
        initTable();
        renderLeft();
        renderRight();
        initRecipePaper();
    }

    private void randomizeSteps() {
        Step.shuffle(shuffledSteps);

        System.out.println("Shuffled steps");

        for (Step step : this.shuffledSteps) {
            System.out.println(step.stepNumber + 1 + ". " + step.label);
        }
    }

    private void checkSteps() {
        this.sortedSteps = new ArrayList<Step>(this.arrangedSteps);

        Step.sort(sortedSteps);

        System.out.println("Sorted steps");

        for (Step step : this.sortedSteps) {
            System.out.println(step.stepNumber + 1 + ". " + step.label);
        }

        if (arrangedSteps.equals(sortedSteps)) {
            System.out.println("Correct steps! Cooking the meal...");
        } else {
            System.out.println("Incorrect steps! Please arrange the steps correctly.");
        }
    }

    private void renderOverlay() {
        Pixmap brownPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        brownPixmap.setColor(Colors.darkBrown);
        brownPixmap.fillRectangle(0, 0, 1, 1);

        this.brownOverlay = new Image(new Texture(brownPixmap));
        this.brownOverlay.setFillParent(true);
        this.brownOverlay.addAction(alpha(0));

        this.brownOverlay.addListener((EventListener) event -> {
            if (event.toString().contains("touchDown")) {
                toggleRecipePaper();
            }

            return false;
        });
    }

    private void initTable() {
        this.table = new Table();
        this.leftTable = new Table();
        this.centerTable = new Table();
        this.rightTable = new Table();

        this.table.setFillParent(true);
        this.table.add(leftTable).expand().left().padLeft(100);
        this.table.add(centerTable).expand().center();
        this.table.add(rightTable).expand().right().padRight(100);
    }

    private void initRecipePaper() {
        if (this.game.assets.isLoaded("recipe-ref.png")) {
            Texture recipeRefTexture = this.game.assets.get("recipe-ref.png", Texture.class);
            Texture recipeTexture = this.game.assets.get("paper.png", Texture.class);

            this.recipeRef = new Image(recipeRefTexture);
            this.recipeRef.setWidth(recipeRef.getWidth() / 4);
            this.recipeRef.setHeight(recipeRef.getHeight() / 4);
            this.recipeRef.setPosition((this.game.V_WIDTH - recipeRef.getWidth()) - 240,
                    (this.game.V_HEIGHT - recipeRef.getHeight()) - 160);
            this.recipeRef.addAction(sequence(alpha(0f), fadeIn(1.5f, Interpolation.pow5)));
            this.stage.addActor(recipeRef);

            this.recipe = new Image(recipeTexture);
            this.recipe.setWidth((float) (recipe.getWidth() / 1.25));
            this.recipe.setHeight((float) (recipe.getHeight() / 1.25));
            this.recipe.setPosition((this.game.V_WIDTH / 2) - (recipe.getWidth() / 2), -recipe.getHeight());
            this.stage.addActor(recipe);

            this.recipeRef.addListener((EventListener) event -> {
                if (event.toString().equals("touchDown")) {
                    toggleRecipePaper();
                }

                return false;
            });
        }
    }

    public void toggleRecipePaper() {
        RunnableAction removeOverlay = new RunnableAction();

        removeOverlay.setRunnable(new Runnable() {
            @Override
            public void run() {
                brownOverlay.remove();
                table.remove();
            }
        });

        if (recipePaperVisible) {
            this.table.addAction(fadeOut(0.5f, Interpolation.pow5));
            this.recipe.addAction(
                    sequence(delay(0.5f), moveTo((this.game.V_WIDTH / 2) - (recipe.getWidth() / 2),
                            -recipe.getHeight(), 0.75f, Interpolation.swingIn)));
            this.brownOverlay.addAction(sequence(delay(1.25f), fadeOut(0.5f, Interpolation.pow5), removeOverlay));

            recipePaperVisible = false;
        } else {
            this.stage.addActor(this.brownOverlay);
            this.stage.addActor(table);
            this.table.addAction(alpha(0f));
            this.recipe.toFront();
            this.brownOverlay.addAction(fadeIn(0.5f, Interpolation.pow5));
            this.recipe.addAction(sequence(
                    delay(0.5f),
                    moveTo((this.game.V_WIDTH / 2) - (recipe.getWidth() / 2),
                            (this.game.V_HEIGHT / 2) - (recipe.getHeight() / 2), 0.75f, Interpolation.swingOut)));
            this.table.addAction(sequence(delay(1.25f), fadeIn(0.5f, Interpolation.pow5)));

            recipePaperVisible = true;
        }

        this.table.toFront();
    }

    private void renderLeft() {
        Table leftContainer = new Table();
        Table stepsContainer = new Table();

        Label mealName = new Label(meal.name, CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 64));

        mealName.setWrap(true);
        leftContainer.add(mealName).width(400).row();

        this.shuffledSteps.forEach(step -> {
            final boolean[] isStepSelected = { false };
            Label stepLabel = new Label(step.stepNumber + 1 + ". " + step.label,
                    CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32));

            stepLabel.setWrap(true);

            stepsContainer.add(stepLabel).width(400).padBottom(15).row();

            stepLabel.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    System.out.println(
                            "Step " + (step.stepNumber + 1) + " clicked!");

                    Label arrangedStepLabel = new Label(step.stepNumber + 1 + ". " + step.label,
                            CustomSkin.generateCustomLilitaOneFont(Colors.brown, 32));

                    arrangedStepLabel.setWrap(true);
                    arrangedStepLabel.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            Cell<Label> cell = centerTable.getCell(arrangedStepLabel);

                            arrangedStepLabel.remove();
                            centerTable.getCells().removeValue(cell, true);
                            centerTable.invalidate();
                            arrangedSteps.remove(step);
                            stepLabel.addAction(alpha(1f));

                            isStepSelected[0] = false;

                            return true;
                        }
                    });

                    if (isStepSelected[0]) {
                        Cell<Label> cell = centerTable.getCell(arrangedStepLabel);

                        arrangedStepLabel.remove();
                        centerTable.getCells().removeValue(cell, true);
                        centerTable.invalidate();
                        arrangedSteps.remove(step);
                        stepLabel.addAction(alpha(1f));

                        isStepSelected[0] = false;
                    } else {
                        stepLabel.addAction(alpha(0.5f));
                        arrangedSteps.add(step);
                        centerTable.add(arrangedStepLabel).width(400).padBottom(15).row();

                        isStepSelected[0] = true;
                    }

                    return true;
                }
            });
        });

        leftContainer.add(stepsContainer).padTop(50);

        this.leftTable.add(leftContainer);
    }

    private void renderRight() {
        Table rightContainer = new Table();
        Table instructionsContainer = new Table();
        ArrayList<Label> instructions = new ArrayList<Label>();

        instructions.add(new Label(
                "Click on the meal steps on the left side of the screen to sort them on the recipe paper! You can remove and switch up the steps as you want by clicking again on the steps on the paper",
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32)));
        instructions.add(new Label(
                "Click anywhere on the brown area to dismiss the recipe paper.",
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32)));
        instructions.add(new Label(
                "Finally, click on the 'cook' button if you're sure with the order of steps for this meal!",
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32)));

        instructions.forEach(instruction -> {
            instruction.setWrap(true);
            instructionsContainer.add(instruction).width(400).padBottom(15).row();
        });

        TextButton cookBtn = new TextButton("Cook", this.game.skin.get("text-button-default", TextButtonStyle.class));

        cookBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                checkSteps();

                return true;
            }
        });

        rightContainer.add(instructionsContainer).row();
        rightContainer.add(cookBtn).padTop(50);

        this.rightTable.add(rightContainer);
    }
}
