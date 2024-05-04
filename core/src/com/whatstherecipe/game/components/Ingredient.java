package com.whatstherecipe.game.components;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.screens.KitchenScreen;

public class Ingredient {
    private final WhatsTheRecipe game;
    private Stage stage;
    private String directory = "ingredients/";
    private String path;
    public String name;
    public String fileName;
    public Image ingredient;
    public int cabinetIndex;
    public boolean isToggled = false;
    public boolean isAdded = false;
    private KitchenScreen kitchenScreen;

    public Ingredient(final WhatsTheRecipe game, Stage stage, String name, KitchenScreen kitchenScreen) {
        this.game = game;
        this.stage = stage;
        this.name = name;
        this.fileName = name + ".png";
        this.path = directory + fileName;
        this.kitchenScreen = kitchenScreen;

        initIngredient();
    }

    private void initIngredient() {
        if (this.game.assets.isLoaded(path)) {
            Texture ingredientTexture = this.game.assets.get(path);

            this.ingredient = new Image(ingredientTexture);
            this.ingredient.setOrigin(Align.center);
            this.ingredient.setSize(ingredient.getWidth(),
                    ingredient.getHeight());
            this.ingredient.setPosition(0, 0);
            this.ingredient.addAction(alpha(0));

            this.ingredient.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    game.sounds.dropSound.play();
                    toggleToBasket();

                    return true;
                }

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    Gdx.graphics.setCursor(game.cursors.spatulaWhiskCursor);

                    ingredient.addAction(scaleTo(1.2f, 1.2f, 0.5f, Interpolation.swingOut));
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    Gdx.graphics.setCursor(game.cursors.spatulaCursor);

                    ingredient.addAction(scaleTo(1f, 1f, 0.5f, Interpolation.swingOut));
                }
            });
        }
    }

    public void toggleToBasket() {
        if (!isAdded) {
            this.ingredient.addAction(sequence(fadeOut(0.25f), run(() -> this.ingredient.remove())));
            this.kitchenScreen.ingredientsInBasket.add(this);

            this.isAdded = true;
        } else {
            this.stage.addActor(this.ingredient);
            this.kitchenScreen.ingredientsInBasket.remove(this);

            this.isAdded = false;
        }
    }

    public void toggleIngredient() {
        this.ingredient.toFront();

        if (isToggled) {
            this.ingredient.addAction(sequence(fadeOut(0.5f), run(() -> this.ingredient.remove())));

            this.isToggled = false;
        } else {
            if (!isAdded) {
                this.stage.addActor(this.ingredient);
                this.ingredient.addAction(fadeIn(0.5f));

                this.isToggled = true;
            }
        }
    }
}
