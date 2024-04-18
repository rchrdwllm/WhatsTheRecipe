package com.whatstherecipe.game.components;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.classes.Meal;
import com.whatstherecipe.game.ui.Colors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class RecipePaperView {
    private final WhatsTheRecipe game;
    private Stage stage;
    private Meal meal;
    private String phase;
    public Image recipeRef;
    public Image recipe;
    private Image brownOverlay;
    private boolean recipePaperVisible = false;
    IngredientSelection ingredientSelection;
    StepSorting stepSorting;

    public RecipePaperView(final WhatsTheRecipe game, Stage stage, Meal meal, String phase) {
        this.game = game;
        this.stage = stage;
        this.meal = meal;
        this.phase = phase;

        this.ingredientSelection = new IngredientSelection(game, stage, meal);
        this.stepSorting = new StepSorting(game, stage, meal);

        renderOverlay();
        initRecipePaper();
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

        if (this.phase.equals("ingredient-selection")) {
            if (recipePaperVisible) {
                this.recipe.addAction(
                        sequence(delay(0.5f), moveTo((this.game.V_WIDTH / 2) - (recipe.getWidth() / 2),
                                -recipe.getHeight(), 0.75f, Interpolation.swingIn)));
                this.brownOverlay.addAction(sequence(delay(1.25f), fadeOut(0.5f, Interpolation.pow5), removeOverlay));

                this.ingredientSelection.hide();

                recipePaperVisible = false;
            } else {
                this.stage.addActor(this.brownOverlay);
                this.recipe.toFront();
                this.brownOverlay.addAction(fadeIn(0.5f, Interpolation.pow5));
                this.recipe.addAction(sequence(
                        delay(0.5f),
                        moveTo((this.game.V_WIDTH / 2) - (recipe.getWidth() / 2),
                                (this.game.V_HEIGHT / 2) - (recipe.getHeight() / 2), 0.75f, Interpolation.swingOut)));

                this.ingredientSelection.show();

                recipePaperVisible = true;
            }
        } else if (this.phase.equals("step-sorting")) {
            if (recipePaperVisible) {
                this.recipe.addAction(
                        sequence(delay(0.5f), moveTo((this.game.V_WIDTH / 2) - (recipe.getWidth() / 2),
                                -recipe.getHeight(), 0.75f, Interpolation.swingIn)));
                this.brownOverlay.addAction(sequence(delay(1.25f), fadeOut(0.5f, Interpolation.pow5), removeOverlay));

                this.stepSorting.hide();

                recipePaperVisible = false;
            } else {
                this.stage.addActor(this.brownOverlay);
                this.recipe.toFront();
                this.brownOverlay.addAction(fadeIn(0.5f, Interpolation.pow5));
                this.recipe.addAction(sequence(
                        delay(0.5f),
                        moveTo((this.game.V_WIDTH / 2) - (recipe.getWidth() / 2),
                                (this.game.V_HEIGHT / 2) - (recipe.getHeight() / 2), 0.75f, Interpolation.swingOut)));

                this.stepSorting.show();

                recipePaperVisible = true;
            }
        }
    }
}
