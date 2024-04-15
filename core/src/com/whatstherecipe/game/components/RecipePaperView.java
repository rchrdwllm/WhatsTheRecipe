package com.whatstherecipe.game.components;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.classes.Meal;
import com.whatstherecipe.game.classes.Step;
import com.whatstherecipe.game.ui.Colors;
import com.whatstherecipe.game.ui.CustomSkin;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class RecipePaperView {
    private final WhatsTheRecipe game;
    private Stage stage;
    private Meal meal;
    private Image brownOverlay;
    private Table recipeTable;
    private Table stepsTable;
    public Image recipeRef;
    public Image recipe;
    private boolean recipePaperVisible = false;

    public RecipePaperView(final WhatsTheRecipe game, Stage stage, Meal meal) {
        this.game = game;
        this.stage = stage;
        this.meal = meal;

        randomizeSteps();
        renderOverlay();
        initTable();
        renderTextSteps();
        renderInstructions();
        initRecipePaper();
    }

    private void randomizeSteps() {
        Step.shuffle(this.meal.steps);

        System.out.println("Shuffled steps");

        for (Step step : this.meal.steps) {
            System.out.println(step.stepNumber + 1 + ". " + step.label);
        }
    }

    private void sortSteps() {
        Step.sort(this.meal.steps);

        System.out.println("Sorted steps");

        for (Step step : this.meal.steps) {
            System.out.println(step.stepNumber + 1 + ". " + step.label);
        }
    }

    private void renderOverlay() {
        Pixmap brownPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        brownPixmap.setColor(Colors.brown);
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
        this.recipeTable = new Table();
        this.stepsTable = new Table();
        this.recipeTable.setFillParent(true);
        this.stepsTable.setFillParent(true);

        this.stage.addActor(this.recipeTable);
        this.recipeTable.addActor(stepsTable);

        this.recipeTable.addAction(alpha(0f));
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
            }
        });

        if (recipePaperVisible) {
            this.recipeTable.addAction(fadeOut(0.5f, Interpolation.pow5));
            this.recipe.addAction(
                    sequence(delay(0.5f), moveTo((this.game.V_WIDTH / 2) - (recipe.getWidth() / 2),
                            -recipe.getHeight(), 0.75f, Interpolation.swingIn)));
            this.brownOverlay.addAction(sequence(delay(1.25f), fadeOut(0.5f, Interpolation.pow5), removeOverlay));

            recipePaperVisible = false;
        } else {
            this.stage.addActor(this.brownOverlay);
            this.recipe.toFront();
            this.brownOverlay.addAction(fadeIn(0.5f, Interpolation.pow5));
            this.recipe.addAction(sequence(
                    delay(0.5f),
                    moveTo((this.game.V_WIDTH / 2) - (recipe.getWidth() / 2),
                            (this.game.V_HEIGHT / 2) - (recipe.getHeight() / 2), 0.75f, Interpolation.swingOut)));
            this.recipeTable.addAction(sequence(delay(1.25f), fadeIn(0.5f, Interpolation.pow5)));

            recipePaperVisible = true;
        }

        this.recipeTable.toFront();
    }

    private void renderTextSteps() {
        this.meal.steps.forEach(step -> {
            Label stepLabel = new Label(step.stepNumber + 1 + ". " + step.label,
                    CustomSkin.generateCustomLilitaOneFont(Colors.brown, 32));

            stepLabel.setWrap(true);

            this.stepsTable.add(stepLabel).width(400).padBottom(10).row();
        });
    }

    private void renderInstructions() {
        Label instructions = new Label(
                "Click on the meal steps on the left side of the screen to sort them on the recipe paper! You can remove and switch up the steps as you want by clicking again on the steps on the paper",
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32));

        instructions.setWrap(true);

        this.recipeTable.add(instructions).width(400).right().expand().padRight(160);
    }
}
