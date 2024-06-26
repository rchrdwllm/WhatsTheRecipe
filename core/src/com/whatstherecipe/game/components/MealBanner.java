package com.whatstherecipe.game.components;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.whatstherecipe.game.classes.Meal;
import com.whatstherecipe.game.screens.KitchenScreen;
import com.whatstherecipe.game.ui.Colors;
import com.whatstherecipe.game.ui.CustomSkin;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class MealBanner {
    private KitchenScreen kitchenScreen;
    private Stage stage;
    private Meal meal;
    private int roundNumber;
    private int selectionTime;
    private String selectionTimeString;
    private Image brownOverlay;
    private Image mealImage;

    public MealBanner(KitchenScreen kitchenScreen, Stage stage, Meal meal, int roundNumber) {
        this.kitchenScreen = kitchenScreen;
        this.stage = stage;
        this.meal = meal;
        this.roundNumber = roundNumber;
        this.selectionTime = kitchenScreen.selectionTime;

        determineTimeString();
        renderOverlay();
        renderMealImage();
        renderLabels();
    }

    private void determineTimeString() {
        int minutes = this.selectionTime / 60;
        int seconds = this.selectionTime % 60;

        if (minutes == 0) {
            this.selectionTimeString = seconds + " seconds";
        } else if (seconds == 0) {
            this.selectionTimeString = minutes + " minute";
        }
    }

    private void renderOverlay() {
        Pixmap brownPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        brownPixmap.setColor(Colors.darkBrown);
        brownPixmap.fillRectangle(0, 0, 1, 1);

        this.brownOverlay = new Image(new Texture(brownPixmap));
        this.brownOverlay.setFillParent(true);

        this.stage.addActor(brownOverlay);

        if (this.roundNumber > 1) {
            this.brownOverlay.addAction(sequence(
                    alpha(0),
                    delay(0.05f),
                    fadeIn(0.5f, Interpolation.pow5),
                    delay(5f),
                    fadeOut(0.5f, Interpolation.pow5), run(() -> {
                        this.brownOverlay.remove();
                        kitchenScreen.startGame();
                    })));
        } else {
            this.brownOverlay.addAction(sequence(
                    alpha(0),
                    delay(0.05f),
                    fadeIn(0.5f, Interpolation.pow5),
                    delay(8.5f),
                    fadeOut(0.5f, Interpolation.pow5), run(() -> {
                        this.brownOverlay.remove();
                        kitchenScreen.startGame();
                    })));
        }
    }

    private void renderMealImage() {
        String mealString = "meals/" + this.meal.name.toLowerCase() + ".png";

        if (this.kitchenScreen.game.assets.isLoaded(mealString)) {
            Texture mealTexture = this.kitchenScreen.game.assets.get(mealString,
                    Texture.class);

            this.mealImage = new Image(mealTexture);
            this.mealImage.setOrigin(Align.center);
        }
    }

    private void renderLabels() {
        Group group = new Group();

        Label roundNumber = new Label("Round " + this.roundNumber,
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32));

        Group yourMealGroup = new Group();
        Label yourMealLabel = new Label("Your meal...",
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 80));
        Group mealGroup = new Group();
        Label mealLabel = new Label(meal.name, CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 128));
        Group mealDifficultyGroup = new Group();
        Label mealDifficulty = new Label("Difficulty: " + meal.difficulty,
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32));
        Label instructionsLabel = new Label(
                "You've got " + this.selectionTimeString
                        + " to collect the required ingredients from the kitchen cabinets before finding out the correct steps. Good luck!",
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 48));

        roundNumber.setAlignment(Align.center);
        roundNumber.setPosition((this.kitchenScreen.game.V_WIDTH / 2) - (roundNumber.getWidth() / 2),
                this.kitchenScreen.game.V_HEIGHT - 100);

        yourMealGroup.addActor(yourMealLabel);
        yourMealGroup.setSize(yourMealLabel.getWidth(), yourMealLabel.getHeight());
        yourMealGroup.setPosition((this.kitchenScreen.game.V_WIDTH / 2) - (yourMealGroup.getWidth() / 2),
                (this.kitchenScreen.game.V_HEIGHT / 2) - (yourMealGroup.getHeight() / 2));

        mealImage.addAction(alpha(0));
        mealImage.addAction(scaleTo(0.5f, 0.5f));
        mealImage.setPosition((this.kitchenScreen.game.V_WIDTH / 2) -
                (mealImage.getWidth() / 2),
                (this.kitchenScreen.game.V_HEIGHT / 2) - (mealImage.getHeight() / 2) -
                        100);

        mealGroup.addActor(mealLabel);
        mealGroup.addAction(alpha(0));
        mealGroup.addAction(scaleTo(0.5f, 0.5f));
        mealGroup.setSize(mealLabel.getWidth(), mealLabel.getHeight());
        mealGroup.setPosition((this.kitchenScreen.game.V_WIDTH / 2) - (mealGroup.getWidth() / 2),
                (this.kitchenScreen.game.V_HEIGHT / 2) - (mealGroup.getHeight() / 2) - 375);

        mealDifficultyGroup.addActor(mealDifficulty);
        mealDifficultyGroup.addAction(alpha(0));
        mealDifficultyGroup.setSize(mealDifficulty.getWidth(), mealDifficulty.getHeight());
        mealDifficultyGroup.setPosition(
                (this.kitchenScreen.game.V_WIDTH / 2) - (mealDifficultyGroup.getWidth() / 2),
                (this.kitchenScreen.game.V_HEIGHT / 2) - (mealDifficultyGroup.getHeight() / 2) - 475);

        instructionsLabel.setAlignment(Align.center);
        instructionsLabel.setWrap(true);
        instructionsLabel.setWidth(this.kitchenScreen.game.V_WIDTH - 400);
        instructionsLabel.setPosition(
                (this.kitchenScreen.game.V_WIDTH / 2) - (instructionsLabel.getWidth() / 2),
                (this.kitchenScreen.game.V_HEIGHT / 2) - (instructionsLabel.getHeight() / 2));
        instructionsLabel.addAction(alpha(0));

        group.setOrigin(Align.center);
        group.addActor(roundNumber);
        group.addActor(mealImage);
        group.addActor(yourMealGroup);
        group.addActor(mealGroup);
        group.addActor(mealDifficultyGroup);

        if (this.roundNumber <= 1) {
            group.addActor(instructionsLabel);
        }

        group.addAction(sequence(alpha(0), fadeIn(0.5f, Interpolation.pow5)));

        yourMealGroup.setOrigin(Align.center);
        yourMealGroup.addAction(sequence(delay(2.5f),
                parallel(scaleTo(0.5f, 0.5f, 1f, Interpolation.pow5),
                        moveBy(0, 300, 1f, Interpolation.pow5))));

        mealImage.setOrigin(Align.center);
        mealImage.addAction(sequence(delay(2.5f),
                parallel(fadeIn(1f, Interpolation.pow5), moveBy(0, 125, 1f, Interpolation.pow5),
                        scaleTo(1, 1, 1f, Interpolation.pow5))));

        mealGroup.setOrigin(Align.center);
        mealGroup.addAction(sequence(delay(2.5f),
                parallel(fadeIn(1f, Interpolation.pow5), moveBy(0, 125, 1f, Interpolation.pow5),
                        scaleTo(1, 1, 1f, Interpolation.pow5))));

        mealDifficultyGroup.setOrigin(Align.center);
        mealDifficultyGroup.addAction(sequence(delay(2.5f),
                parallel(fadeIn(1f, Interpolation.pow5), moveBy(0, 125, 1f, Interpolation.pow5))));

        this.stage.addActor(group);

        group.addAction(sequence(delay(5f), run(() -> {
            roundNumber.addAction(fadeOut(0.5f, Interpolation.pow5));
            yourMealGroup.addAction(fadeOut(0.5f, Interpolation.pow5));
            mealGroup.addAction(fadeOut(0.5f, Interpolation.pow5));
            mealImage.addAction(fadeOut(0.5f, Interpolation.pow5));
            mealDifficultyGroup.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
                roundNumber.remove();
                yourMealGroup.remove();
                mealGroup.remove();
                mealImage.remove();
                mealDifficultyGroup.remove();

                if (this.roundNumber <= 1) {
                    instructionsLabel
                            .addAction(sequence(fadeIn(0.5f, Interpolation.pow5), delay(3f),
                                    fadeOut(0.5f, Interpolation.pow5), run(() -> {
                                        group.remove();
                                    })));
                }
            })));
        })));
    }
}
