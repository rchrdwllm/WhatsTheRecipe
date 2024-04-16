package com.whatstherecipe.game.components;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.whatstherecipe.game.classes.Meal;
import com.whatstherecipe.game.ui.Colors;
import com.whatstherecipe.game.ui.CustomSkin;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class MealBanner {
    Stage stage;
    Meal meal;
    int roundNumber;
    Image brownOverlay;
    Table table;

    public MealBanner(Stage stage, Meal meal, int roundNumber) {
        this.stage = stage;
        this.meal = meal;
        this.roundNumber = roundNumber;

        renderOverlay();
        renderTable();
        renderLabels();
    }

    private void renderOverlay() {
        Pixmap brownPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        brownPixmap.setColor(Colors.darkBrown);
        brownPixmap.fillRectangle(0, 0, 1, 1);

        this.brownOverlay = new Image(new Texture(brownPixmap));
        this.brownOverlay.setFillParent(true);
        this.brownOverlay.addAction(alpha(0));

        this.stage.addActor(brownOverlay);

        this.brownOverlay.addAction(sequence(
                fadeIn(0.5f, Interpolation.pow5),
                delay(2.5f),
                fadeOut(0.5f, Interpolation.pow5), run(() -> {
                    this.brownOverlay.remove();
                    this.table.remove();
                })));
    }

    private void renderTable() {
        this.table = new Table();

        this.table.setFillParent(true);
        this.stage.addActor(table);
    }

    private void renderLabels() {
        Label roundLabel = new Label("Round " + this.roundNumber,
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32));
        Table mealNameTable = new Table();
        Label yourMealLabel = new Label("Your meal:",
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 48));
        mealNameTable.add(yourMealLabel).center();
        mealNameTable.row();
        Label mealLabel = new Label(meal.name,
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 120));
        mealNameTable.add(mealLabel).center();
        mealNameTable.row();
        Label mealDifficulty = new Label("Difficulty: " + meal.difficulty,
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32));
        mealNameTable.add(mealDifficulty).center();

        this.table.add(roundLabel).center().padTop(50);
        this.table.row();
        this.table.add(mealNameTable).expand().center();
        this.table.addAction(alpha(0f));
        this.table.addAction(sequence(fadeIn(0.5f, Interpolation.pow5),
                delay(2.5f),
                fadeOut(0.5f, Interpolation.pow5)));
    }
}
