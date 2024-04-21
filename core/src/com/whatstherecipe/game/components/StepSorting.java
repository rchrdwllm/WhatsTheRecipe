package com.whatstherecipe.game.components;

import java.util.ArrayList;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
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

public class StepSorting {
    private final WhatsTheRecipe game;
    private Stage stage;
    private Meal meal;
    private ArrayList<Step> shuffledSteps;
    private ArrayList<Step> arrangedSteps;
    private ArrayList<Step> sortedSteps;
    private Table table;
    private Table leftTable;
    private Table centerTable;
    private Table rightTable;
    private String phase;

    public StepSorting(RecipePaperView recipePaperView) {
        this.game = recipePaperView.game;
        this.stage = recipePaperView.stage;
        this.meal = recipePaperView.meal;
        this.shuffledSteps = new ArrayList<Step>(meal.steps);
        this.arrangedSteps = new ArrayList<Step>();
        this.sortedSteps = new ArrayList<Step>();

        initTables();
        randomizeSteps();
        renderLeft();
        renderRight();
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

    private void randomizeSteps() {
        Step.shuffle(shuffledSteps);
    }

    private void checkSteps() {
        this.sortedSteps = new ArrayList<Step>(this.arrangedSteps);

        System.out.println("Arranged steps by player:");

        for (Step step : this.sortedSteps) {
            System.out.println(step.stepNumber + 1 + ". " + step.label);
        }

        Step.sort(sortedSteps);

        System.out.println("Sorted steps:");

        for (Step step : this.sortedSteps) {
            System.out.println(step.stepNumber + 1 + ". " + step.label);
        }

        if (arrangedSteps.equals(sortedSteps)) {
            System.out.println("Correct steps! Cooking the meal...");
        } else {
            System.out.println("Incorrect steps! Please arrange the steps correctly.");
        }
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
                    Label arrangedStepLabel = new Label(step.stepNumber + 1 + ". " + step.label,
                            CustomSkin.generateCustomLilitaOneFont(Colors.brown, 32));

                    if (isStepSelected[0]) {
                        arrangedSteps.remove(step);

                        Cell<Label> arrangedStepLabelCell = centerTable.getCell(arrangedStepLabel);

                        if (arrangedStepLabelCell != null) {
                            arrangedStepLabelCell.padBottom(0);
                            arrangedStepLabel.remove();
                        }

                        stepLabel.addAction(alpha(1f));
                        centerTable.invalidate();

                        isStepSelected[0] = false;
                    } else {
                        stepLabel.addAction(alpha(0.5f));
                        arrangedSteps.add(step);
                        centerTable.add(arrangedStepLabel).width(400).padBottom(15).row();

                        isStepSelected[0] = true;
                    }

                    arrangedStepLabel.setWrap(true);
                    arrangedStepLabel.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            arrangedSteps.remove(step);

                            Cell<Label> arrangedStepLabelCell = centerTable.getCell(arrangedStepLabel);

                            if (arrangedStepLabelCell != null) {
                                arrangedStepLabelCell.padBottom(0);
                                arrangedStepLabel.remove();
                            }

                            stepLabel.addAction(alpha(1f));
                            centerTable.invalidate();

                            isStepSelected[0] = false;

                            return true;
                        }
                    });

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
