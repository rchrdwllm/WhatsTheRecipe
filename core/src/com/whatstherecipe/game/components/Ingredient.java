package com.whatstherecipe.game.components;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.whatstherecipe.game.WhatsTheRecipe;

public class Ingredient {
    private final WhatsTheRecipe game;
    private Stage stage;
    private String directory = "ingredients/";
    private String path;
    public String name;
    public String fileName;
    public Image ingredient;
    public int cabinetIndex;
    private boolean isToggled = false;

    public Ingredient(final WhatsTheRecipe game, Stage stage, String name) {
        this.game = game;
        this.stage = stage;
        this.name = name;
        this.fileName = name + ".png";
        this.path = directory + fileName;

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
        }
    }

    public void toggleIngredient() {
        this.ingredient.toFront();

        if (isToggled) {
            this.ingredient.addAction(sequence(fadeOut(0.5f), run(() -> this.ingredient.remove())));

            this.isToggled = false;
        } else {
            this.stage.addActor(this.ingredient);
            this.ingredient.addAction(fadeIn(0.5f));

            this.isToggled = true;
        }
    }
}
