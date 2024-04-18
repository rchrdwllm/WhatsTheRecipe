package com.whatstherecipe.game.components;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.classes.Meal;
import com.whatstherecipe.game.ui.Colors;
import com.whatstherecipe.game.ui.CustomSkin;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.math.Interpolation;

public class IngredientSelection {
    private final WhatsTheRecipe game;
    private Stage stage;
    private Meal meal;
    private Table table;

    public IngredientSelection(final WhatsTheRecipe game, Stage stage, Meal meal) {
        this.game = game;
        this.stage = stage;
        this.meal = meal;

        initTables();
        initLabels();
    }

    private void initTables() {
        this.table = new Table();
        this.table.setFillParent(true);
    }

    public void show() {
        this.stage.addActor(table);
        this.table.toFront();
        this.table.addAction(alpha(0f));
        this.table.addAction(sequence(delay(1f, fadeIn(0.5f, Interpolation.pow5))));
    }

    public void hide() {
        this.table.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
            this.table.remove();
        })));
    }

    private void initLabels() {
        Label label = new Label("Ingredient selection", CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32));

        this.table.add(label).expand().center();
    }
}
