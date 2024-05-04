package com.whatstherecipe.game.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.classes.Meal;
import com.whatstherecipe.game.screens.KitchenScreen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.Gdx;

public class RecipePaperView {
    public final WhatsTheRecipe game;
    public Stage stage;
    public Meal meal;
    public Image recipeRef;
    public Image recipe;
    public Image brownOverlay;
    private boolean recipePaperVisible = false;
    public KitchenScreen kitchenScreen;
    public IngredientSelection ingredientSelection;
    public StepSorting stepSorting;

    public RecipePaperView(KitchenScreen kitchenScreen) {
        this.game = kitchenScreen.game;
        this.stage = kitchenScreen.stage;
        this.meal = kitchenScreen.meal;
        this.kitchenScreen = kitchenScreen;

        this.stepSorting = new StepSorting(this);
        this.ingredientSelection = new IngredientSelection(this);

        renderOverlay();
        initRecipePaper();
    }

    private void renderOverlay() {
        if (this.game.assets.isLoaded("wood-bg.png")) {
            this.brownOverlay = new Image(this.game.assets.get("wood-bg.png", Texture.class));
            this.brownOverlay.setFillParent(true);
            this.brownOverlay.addAction(alpha(0));
        }
    }

    private void initRecipePaper() {
        if (this.game.assets.isLoaded("recipe-ref.png")) {
            Texture recipeRefTexture = this.game.assets.get("recipe-ref.png", Texture.class);
            Texture recipeTexture = this.game.assets.get("paper.png", Texture.class);

            this.recipeRef = new Image(recipeRefTexture);
            this.recipeRef.setWidth(recipeRef.getWidth() / 4);
            this.recipeRef.setHeight(recipeRef.getHeight() / 4);
            this.recipeRef.setOrigin(Align.center);
            this.recipeRef.setPosition((this.game.V_WIDTH - recipeRef.getWidth()) - 240,
                    (this.game.V_HEIGHT - recipeRef.getHeight()) - 160);
            this.recipeRef.addAction(sequence(alpha(0f), fadeIn(1.5f, Interpolation.pow5)));
            this.stage.addActor(recipeRef);

            this.recipe = new Image(recipeTexture);
            this.recipe.setWidth((float) (recipe.getWidth() / 1.25));
            this.recipe.setHeight((float) (recipe.getHeight() / 1.25));
            this.recipe.setPosition((this.game.V_WIDTH / 2) - (recipe.getWidth() / 2), -recipe.getHeight());
            this.stage.addActor(recipe);

            this.recipeRef.addListener(new InputListener() {
                @Override
                public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y,
                        int pointer, int button) {
                    toggleRecipePaper();
                    return true;
                }

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    Gdx.graphics.setCursor(game.cursors.spatulaWhiskCursor);

                    recipeRef.addAction(scaleTo(1.2f, 1.2f, 0.5f, Interpolation.swingOut));
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    Gdx.graphics.setCursor(game.cursors.spatulaCursor);

                    recipeRef.addAction(scaleTo(1f, 1f, 0.5f, Interpolation.swingOut));
                }
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

        if (this.kitchenScreen.phase.equals("ingredient-selection")) {
            if (recipePaperVisible) {
                this.game.sounds.paperSound.play();

                this.recipe.addAction(
                        sequence(delay(0.5f), moveTo((this.game.V_WIDTH / 2) - (recipe.getWidth() / 2),
                                -recipe.getHeight(), 0.75f, Interpolation.swingIn)));
                this.brownOverlay.addAction(sequence(delay(1.25f), fadeOut(0.5f, Interpolation.pow5), removeOverlay));

                this.ingredientSelection.hide();

                recipePaperVisible = false;
            } else {
                this.game.sounds.paperSound.play();

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
        }
    }
}
