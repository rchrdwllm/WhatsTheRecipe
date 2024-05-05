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
import com.badlogic.gdx.utils.Align;
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
    private ArrayList<Label> stepLabels;
    private ArrayList<Label> arrangedStepLabels;
    private ArrayList<String> selectedSteps;
    private int maxTries = 0;
    private int tries = 0;
    public int time = 0;
    private CookingAnimation cookingAnimation;
    private RecipePaperView recipePaperView;
    private Label timerLabel;
    private Label scoreLabel;
    private Table timerTable;
    private Table scoreTable;
    public float sortingTimeElapsed = 0;
    private boolean isSortingTimeUp = false;
    private int plusPoints = 0;
    private int deduction = 0;
    private int totalDeductions = 0;
    private int currentPoints = 0;
    private boolean isCountdownPaused = false;

    public StepSorting(RecipePaperView recipePaperView) {
        this.game = recipePaperView.game;
        this.stage = recipePaperView.stage;
        this.meal = recipePaperView.meal;
        this.currentPoints = recipePaperView.kitchenScreen.currentPoints;
        this.recipePaperView = recipePaperView;
        this.shuffledSteps = new ArrayList<Step>(meal.steps);
        this.arrangedSteps = new ArrayList<Step>();
        this.sortedSteps = new ArrayList<Step>();
        this.stepLabels = new ArrayList<Label>();
        this.arrangedStepLabels = new ArrayList<Label>();
        this.selectedSteps = new ArrayList<String>();

        resetState();
        determinePlusPoints();
        determineDeduction();
        determineMaxTries();
        determineTime();
        initTables();
        randomizeSteps();
        renderLeft();
        renderRight();
        prepareCookingAnimation();
        renderTimerScore();
    }

    private void resetState() {
        meal.steps.forEach(step -> {
            step.isSelected = false;
        });
    }

    private void determinePlusPoints() {
        switch (meal.difficulty) {
            case "easy":
                this.plusPoints = 200;
                break;
            case "medium":
                this.plusPoints = 500;
                break;
            case "hard":
                this.plusPoints = recipePaperView.kitchenScreen.currentPoints;
                break;
            default:
                this.plusPoints = 200;
                break;
        }
    }

    private void determineDeduction() {
        switch (meal.difficulty) {
            case "easy":
                this.deduction = 0;
                break;
            case "medium":
                this.deduction = (int) ((1.0 / 3.0) * recipePaperView.kitchenScreen.currentPoints);
                break;
            case "hard":
                this.deduction = recipePaperView.kitchenScreen.currentPoints;
                break;
            default:
                this.deduction = 0;
                break;
        }
    }

    private void determineMaxTries() {
        switch (this.meal.difficulty) {
            case "easy":
                this.maxTries = 3;
                break;
            case "medium":
                this.maxTries = 3;
                break;
            case "hard":
                this.maxTries = 1;
                break;
            default:
                this.maxTries = 3;
                break;
        }
    }

    private void determineTime() {
        switch (this.meal.difficulty) {
            case "easy":
                this.time = 120;
                break;
            case "medium":
                this.time = 60;
                break;
            case "hard":
                this.time = 30;
                break;
            default:
                this.time = 60;
                break;
        }
    }

    public void show() {
        this.stage.addActor(table);
        this.table.addAction(alpha(0f));
        this.table.toFront();
        this.table.addAction(sequence(delay(1f, fadeIn(0.5f, Interpolation.pow5))));

        this.stage.addActor(timerTable);
        this.timerTable.addAction(alpha(0f));
        this.timerTable.toFront();
        this.timerTable.addAction(sequence(delay(1f, fadeIn(0.5f, Interpolation.pow5))));

        this.stage.addActor(scoreTable);
        this.scoreTable.addAction(alpha(0f));
        this.scoreTable.toFront();
        this.scoreTable.addAction(sequence(delay(1f, fadeIn(0.5f, Interpolation.pow5)), run(() -> {
            startPhase();
        })));
    }

    public void hide() {
        this.table.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
            this.table.remove();
        })));
        this.timerTable.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
            this.timerTable.remove();
        })));
        this.scoreTable.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
            this.scoreTable.remove();
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

        this.timerTable = new Table();
        this.scoreTable = new Table();

        timerTable.setFillParent(true);
        scoreTable.setFillParent(true);
    }

    private void randomizeSteps() {
        Step.shuffle(shuffledSteps);
    }

    private void checkSteps() {
        determineDeduction();

        System.out.println("Current points: " + recipePaperView.kitchenScreen.currentPoints);
        System.out.println("Deduction points: " + this.deduction);

        this.tries += 1;
        this.sortedSteps = new ArrayList<Step>(this.arrangedSteps);

        if (!this.sortedSteps.isEmpty()) {
            this.cookingAnimation.toggleAnimation();

            this.stage.addAction(sequence(delay(2f), run(() -> {
                if (this.sortedSteps.size() == this.meal.steps.size()) {
                    System.out.println("\nCooking the meal...");

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
                        System.out.println("Correct steps! Proceeding to next meal...");

                        this.isCountdownPaused = true;

                        if (meal.difficulty.equals("hard")) {
                            Scoring scoring = new Scoring(game, stage, currentPoints, plusPoints, 51);

                            recipePaperView.kitchenScreen.updatePoints(this.plusPoints);
                            this.currentPoints = recipePaperView.kitchenScreen.currentPoints;
                            this.scoreLabel.setText(this.currentPoints + " pts");

                            ArrayList<TextButton> masterChefBtns = new ArrayList<TextButton>();
                            TextButton masterChefBtn = new TextButton("Hooray!",
                                    this.game.skin.get("text-button-default", TextButtonStyle.class));

                            masterChefBtns.add(masterChefBtn);

                            String masterChefTitle;
                            String masterChefMessage;

                            if (currentPoints < 2800) {
                                masterChefTitle = "Master chef!";
                                masterChefMessage = "Your cooking skills are sizzling! Imagine how amazing your dishes could be with the perfect score touch. Are you up for the challenge?";
                            } else {
                                masterChefTitle = "You're a culinary legend!";
                                masterChefMessage = "Perfect scores on every dish! Your skills are unmatched. You've mastered the art of cooking!";
                            }

                            Popup masterChefPopup = new Popup(this.game, this.stage, masterChefTitle, masterChefMessage,
                                    masterChefBtns);

                            ArrayList<TextButton> buttons = new ArrayList<TextButton>();
                            TextButton okayBtn = new TextButton("Okay",
                                    this.game.skin.get("text-button-default", TextButtonStyle.class));

                            buttons.add(okayBtn);

                            Popup popup = new Popup(this.game, this.stage, "Congratulations!",
                                    "You've successfully cooked the meal and successfully finished the full course! You earned "
                                            + this.plusPoints + " points, having a total of " + this.currentPoints
                                            + " points.",
                                    buttons,
                                    this.game.sounds.successSound);

                            if (this.currentPoints > this.game.currentHighScore
                                    && recipePaperView.kitchenScreen.roundCount == recipePaperView.kitchenScreen.maxRoundCount) {
                                this.game.currentHighScore = this.currentPoints;
                            }

                            okayBtn.addListener(new InputListener() {
                                @Override
                                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                    game.sounds.clickSound.play();
                                    popup.hide(() -> {
                                        masterChefPopup.show();

                                        masterChefBtn.addListener(new InputListener() {
                                            @Override
                                            public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                                    int button) {
                                                game.sounds.clickSound.play();

                                                masterChefPopup.hide(() -> {
                                                    scoring.show(() -> {
                                                        timerTable.addAction(fadeOut(0.5f, Interpolation.pow5));
                                                        scoreTable.addAction(fadeOut(0.5f, Interpolation.pow5));
                                                        table.addAction(
                                                                sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
                                                                    recipePaperView.brownOverlay.addAction(sequence(
                                                                            fadeOut(0.5f, Interpolation.pow5)));
                                                                    recipePaperView.recipe.addAction(sequence(
                                                                            delay(0.5f), moveTo(
                                                                                    (game.V_WIDTH / 2)
                                                                                            - (recipePaperView.recipe
                                                                                                    .getWidth() / 2),
                                                                                    -recipePaperView.recipe.getHeight(),
                                                                                    0.75f, Interpolation.swingIn),
                                                                            run(() -> {
                                                                                recipePaperView.brownOverlay.remove();
                                                                                recipePaperView.kitchenScreen
                                                                                        .transitionToMainMenu();
                                                                            })));
                                                                })));
                                                    });
                                                });

                                                return true;
                                            }
                                        });
                                    });

                                    return true;
                                }
                            });

                            popup.show();
                        } else {
                            int step = 0;

                            switch (meal.difficulty) {
                                case "easy":
                                    step = 2;
                                    break;
                                case "medium":
                                    step = 5;
                                    break;
                                default:
                                    step = 2;
                                    break;
                            }

                            Scoring scoring = new Scoring(game, stage, currentPoints, plusPoints, step);

                            recipePaperView.kitchenScreen.updatePoints(this.plusPoints);
                            this.currentPoints = recipePaperView.kitchenScreen.currentPoints;
                            this.scoreLabel.setText(this.currentPoints + " pts");

                            ArrayList<TextButton> buttons = new ArrayList<TextButton>();
                            TextButton nextBtn = new TextButton("Next meal",
                                    this.game.skin.get("text-button-default", TextButtonStyle.class));

                            buttons.add(nextBtn);

                            Popup popup = new Popup(this.game, this.stage, "Correct steps!",
                                    "You've successfully cooked the meal! You earned " + this.plusPoints + " points!",
                                    buttons,
                                    this.game.sounds.successSound);

                            nextBtn.addListener(new InputListener() {
                                @Override
                                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                    game.sounds.clickSound.play();
                                    popup.hide();
                                    scoring.show(() -> {
                                        timerTable.addAction(fadeOut(0.5f, Interpolation.pow5));
                                        scoreTable.addAction(fadeOut(0.5f, Interpolation.pow5));
                                        table.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
                                            recipePaperView.brownOverlay
                                                    .addAction(sequence(fadeOut(0.5f, Interpolation.pow5)));
                                            recipePaperView.recipe.addAction(
                                                    sequence(delay(0.5f),
                                                            moveTo((game.V_WIDTH / 2)
                                                                    - (recipePaperView.recipe.getWidth() / 2),
                                                                    -recipePaperView.recipe.getHeight(), 0.75f,
                                                                    Interpolation.swingIn),
                                                            run(() -> {
                                                                recipePaperView.brownOverlay.remove();
                                                                recipePaperView.kitchenScreen.nextRound();
                                                            })));
                                        })));
                                    });

                                    return true;
                                }
                            });

                            popup.show();
                        }
                    } else {
                        if (this.tries == this.maxTries) {
                            System.out.println(
                                    "\nFailed to cook the meal. " + (this.tries) + "/" + (this.maxTries)
                                            + " tries! Proceeding to next meal...");

                            this.isCountdownPaused = true;

                            if (meal.difficulty.equals("easy")) {
                                ArrayList<TextButton> buttons = new ArrayList<TextButton>();
                                TextButton nextMeal = new TextButton("Next meal",
                                        this.game.skin.get("text-button-default", TextButtonStyle.class));

                                buttons.add(nextMeal);

                                Popup popup = new Popup(this.game, this.stage, "Incorrect steps!",
                                        "You already reached the max number of steps! No deductions for this easy meal. Proceed now to the next meal.",
                                        buttons, this.game.sounds.failSound);

                                nextMeal.addListener(new InputListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                            int button) {
                                        game.sounds.clickSound.play();
                                        popup.hide();

                                        timerTable.addAction(fadeOut(0.5f, Interpolation.pow5));
                                        scoreTable.addAction(fadeOut(0.5f, Interpolation.pow5));
                                        table.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
                                            recipePaperView.brownOverlay
                                                    .addAction(sequence(fadeOut(0.5f, Interpolation.pow5)));
                                            recipePaperView.recipe.addAction(
                                                    sequence(delay(0.5f),
                                                            moveTo((game.V_WIDTH / 2)
                                                                    - (recipePaperView.recipe.getWidth() / 2),
                                                                    -recipePaperView.recipe.getHeight(), 0.75f,
                                                                    Interpolation.swingIn),
                                                            run(() -> {
                                                                recipePaperView.brownOverlay.remove();
                                                                recipePaperView.kitchenScreen.nextRound();
                                                            })));
                                        })));

                                        return true;
                                    }
                                });

                                popup.show();
                            } else if (meal.difficulty.equals("hard")) {
                                Scoring scoring = new Scoring(game, stage, currentPoints, -deduction, 51);

                                recipePaperView.kitchenScreen.updatePoints(-this.deduction);
                                this.currentPoints = recipePaperView.kitchenScreen.currentPoints;
                                this.scoreLabel.setText(this.currentPoints + " pts");

                                ArrayList<TextButton> buttons = new ArrayList<TextButton>();
                                TextButton okayBtn = new TextButton("Okay",
                                        this.game.skin.get("text-button-default", TextButtonStyle.class));

                                buttons.add(okayBtn);

                                Popup popup = new Popup(this.game, this.stage, "Goodbye!",
                                        "This is an all or nothing round, and you lost everything!",
                                        buttons, this.game.sounds.failSound);

                                if (this.currentPoints > this.game.currentHighScore
                                        && recipePaperView.kitchenScreen.roundCount == recipePaperView.kitchenScreen.maxRoundCount) {
                                    this.game.currentHighScore = this.currentPoints;
                                }

                                okayBtn.addListener(new InputListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                            int button) {
                                        game.sounds.clickSound.play();
                                        popup.hide(() -> {
                                            scoring.show(() -> {
                                                timerTable.addAction(fadeOut(0.5f, Interpolation.pow5));
                                                scoreTable.addAction(fadeOut(0.5f, Interpolation.pow5));
                                                table.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
                                                    recipePaperView.brownOverlay
                                                            .addAction(sequence(fadeOut(0.5f, Interpolation.pow5)));
                                                    recipePaperView.recipe.addAction(
                                                            sequence(delay(0.5f),
                                                                    moveTo((game.V_WIDTH / 2)
                                                                            - (recipePaperView.recipe.getWidth() / 2),
                                                                            -recipePaperView.recipe.getHeight(), 0.75f,
                                                                            Interpolation.swingIn),
                                                                    run(() -> {
                                                                        recipePaperView.brownOverlay.remove();
                                                                        recipePaperView.kitchenScreen
                                                                                .transitionToMainMenu();
                                                                    })));
                                                })));
                                            });
                                        });

                                        return true;
                                    }
                                });

                                popup.show();
                            } else {
                                ArrayList<TextButton> buttons = new ArrayList<TextButton>();
                                TextButton nextMeal = new TextButton("Next meal",
                                        this.game.skin.get("text-button-default", TextButtonStyle.class));

                                buttons.add(nextMeal);

                                Popup popup = new Popup(this.game, this.stage, "Incorrect steps!",
                                        "You already reached the max number of steps! The total deduction for this round is "
                                                + this.totalDeductions + " points. Proceed now to the next meal.",
                                        buttons, this.game.sounds.failSound);

                                nextMeal.addListener(new InputListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                            int button) {
                                        game.sounds.clickSound.play();
                                        popup.hide();

                                        timerTable.addAction(fadeOut(0.5f, Interpolation.pow5));
                                        scoreTable.addAction(fadeOut(0.5f, Interpolation.pow5));
                                        table.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
                                            recipePaperView.brownOverlay
                                                    .addAction(sequence(fadeOut(0.5f, Interpolation.pow5)));
                                            recipePaperView.recipe.addAction(
                                                    sequence(delay(0.5f),
                                                            moveTo((game.V_WIDTH / 2)
                                                                    - (recipePaperView.recipe.getWidth() / 2),
                                                                    -recipePaperView.recipe.getHeight(), 0.75f,
                                                                    Interpolation.swingIn),
                                                            run(() -> {
                                                                recipePaperView.brownOverlay.remove();
                                                                recipePaperView.kitchenScreen.nextRound();
                                                            })));
                                        })));

                                        return true;
                                    }
                                });

                                popup.show();
                            }
                        } else {
                            System.out.println("\nIncorrect steps! Please arrange the steps correctly.");

                            this.isCountdownPaused = true;

                            if (meal.difficulty.equals("easy")) {
                                ArrayList<TextButton> buttons = new ArrayList<TextButton>();
                                TextButton tryAgainBtn = new TextButton("Try again",
                                        this.game.skin.get("text-button-default", TextButtonStyle.class));
                                TextButton nextMeal = new TextButton("Next meal",
                                        this.game.skin.get("text-button-default", TextButtonStyle.class));

                                buttons.add(tryAgainBtn);
                                buttons.add(nextMeal);

                                Popup popup = new Popup(this.game, this.stage, "Incorrect steps!",
                                        "Failed to cook the meal! No deductions for this easy meal. You may try again or skip to the next meal already.",
                                        buttons, this.game.sounds.failSound);

                                tryAgainBtn.addListener(new InputListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                            int button) {
                                        game.sounds.clickSound.play();
                                        popup.hide(() -> {
                                            isCountdownPaused = false;
                                        });

                                        arrangedSteps.clear();
                                        selectedSteps.clear();

                                        stepLabels.forEach(step -> {
                                            step.addAction(alpha(1f, 0.25f));
                                        });

                                        shuffledSteps.forEach(step -> {
                                            step.isSelected = false;
                                        });

                                        arrangedStepLabels.forEach(arrangedStepLabel -> {
                                            arrangedStepLabel.addAction(fadeOut(0.5f, Interpolation.pow5));

                                            Cell<Label> arrangedStepLabelCell = centerTable.getCell(arrangedStepLabel);

                                            if (arrangedStepLabelCell != null) {
                                                arrangedStepLabelCell.padBottom(0);
                                                arrangedStepLabel.remove();
                                            }

                                            centerTable.invalidate();
                                        });

                                        return true;
                                    }
                                });

                                nextMeal.addListener(new InputListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                            int button) {
                                        game.sounds.clickSound.play();
                                        popup.hide();

                                        timerTable.addAction(fadeOut(0.5f, Interpolation.pow5));
                                        scoreTable.addAction(fadeOut(0.5f, Interpolation.pow5));
                                        table.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
                                            recipePaperView.brownOverlay
                                                    .addAction(sequence(fadeOut(0.5f, Interpolation.pow5)));
                                            recipePaperView.recipe.addAction(
                                                    sequence(delay(0.5f),
                                                            moveTo((game.V_WIDTH / 2)
                                                                    - (recipePaperView.recipe.getWidth() / 2),
                                                                    -recipePaperView.recipe.getHeight(), 0.75f,
                                                                    Interpolation.swingIn),
                                                            run(() -> {
                                                                recipePaperView.brownOverlay.remove();
                                                                recipePaperView.kitchenScreen.nextRound();
                                                            })));
                                        })));

                                        return true;
                                    }
                                });

                                popup.show();
                            } else {
                                Scoring scoring = new Scoring(game, stage, currentPoints, -deduction, 3);

                                recipePaperView.kitchenScreen.updatePoints(-this.deduction);

                                this.currentPoints = recipePaperView.kitchenScreen.currentPoints;
                                this.totalDeductions += this.deduction;
                                this.scoreLabel.setText(this.currentPoints + " pts");

                                ArrayList<TextButton> buttons = new ArrayList<TextButton>();
                                TextButton tryAgainBtn = new TextButton("Try again",
                                        this.game.skin.get("text-button-default", TextButtonStyle.class));
                                TextButton nextMeal = new TextButton("Next meal",
                                        this.game.skin.get("text-button-default", TextButtonStyle.class));

                                buttons.add(tryAgainBtn);
                                buttons.add(nextMeal);

                                Popup popup = new Popup(this.game, this.stage, "Incorrect steps!",
                                        "Failed to cook the meal! You got a deduction of " + this.deduction
                                                + " points. You may try again or skip to the next meal already.",
                                        buttons, this.game.sounds.failSound);

                                tryAgainBtn.addListener(new InputListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                            int button) {
                                        game.sounds.clickSound.play();
                                        popup.hide(() -> {
                                            scoring.show(() -> {
                                                arrangedSteps.clear();
                                                selectedSteps.clear();

                                                stepLabels.forEach(step -> {
                                                    step.addAction(alpha(1f, 0.25f));
                                                });

                                                shuffledSteps.forEach(step -> {
                                                    step.isSelected = false;
                                                });

                                                arrangedStepLabels.forEach(arrangedStepLabel -> {
                                                    arrangedStepLabel.addAction(fadeOut(0.5f, Interpolation.pow5));

                                                    Cell<Label> arrangedStepLabelCell = centerTable
                                                            .getCell(arrangedStepLabel);

                                                    if (arrangedStepLabelCell != null) {
                                                        arrangedStepLabelCell.padBottom(0);
                                                        arrangedStepLabel.remove();
                                                    }

                                                    centerTable.invalidate();
                                                });

                                                isCountdownPaused = false;
                                            });
                                        });

                                        return true;
                                    }
                                });

                                nextMeal.addListener(new InputListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                            int button) {
                                        game.sounds.clickSound.play();
                                        popup.hide(() -> {
                                            scoring.show(() -> {
                                                table.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
                                                    recipePaperView.brownOverlay
                                                            .addAction(sequence(fadeOut(0.5f, Interpolation.pow5)));
                                                    recipePaperView.recipe.addAction(
                                                            sequence(delay(0.5f),
                                                                    moveTo((game.V_WIDTH / 2)
                                                                            - (recipePaperView.recipe.getWidth() / 2),
                                                                            -recipePaperView.recipe.getHeight(), 0.75f,
                                                                            Interpolation.swingIn),
                                                                    run(() -> {
                                                                        recipePaperView.brownOverlay.remove();
                                                                        recipePaperView.kitchenScreen.nextRound();
                                                                    })));
                                                })));
                                            });
                                        });

                                        return true;
                                    }
                                });

                                popup.show();
                            }
                        }
                    }
                } else {
                    ArrayList<TextButton> buttons = new ArrayList<TextButton>();
                    TextButton tryAgainBtn = new TextButton("Try again",
                            this.game.skin.get("text-button-default", TextButtonStyle.class));

                    buttons.add(tryAgainBtn);

                    Popup popup = new Popup(this.game, this.stage, "Oops!",
                            "You tried to cook the meal without arranging all steps. Try again!",
                            buttons, this.game.sounds.failSound);

                    tryAgainBtn.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                int button) {
                            game.sounds.clickSound.play();
                            popup.hide();

                            return true;
                        }
                    });

                    popup.show();
                }
            })));
        }
    }

    private void renderLeft() {
        Table leftContainer = new Table();
        Table stepsContainer = new Table();

        Label mealName = new Label(meal.name, CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 64));

        mealName.setWrap(true);
        leftContainer.add(mealName).width(400).row();

        this.shuffledSteps.forEach(step -> {
            Label stepLabel = new Label(step.stepNumber + 1 + ". " + step.label,
                    CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32));

            this.stepLabels.add(stepLabel);
            stepLabel.setWrap(true);

            stepsContainer.add(stepLabel).width(400).padBottom(15).row();

            stepLabel.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    game.sounds.paperSound.play();
                    String labelText = step.stepNumber + 1 + ". " + step.label;

                    if (selectedSteps.contains(labelText)) {
                        return true;
                    }

                    Label arrangedStepLabel = new Label(labelText,
                            CustomSkin.generateCustomLilitaOneFont(Colors.brown, 32));

                    selectedSteps.add(labelText);
                    arrangedStepLabels.add(arrangedStepLabel);

                    if (step.isSelected) {
                        arrangedSteps.remove(step);

                        Cell<Label> arrangedStepLabelCell = centerTable.getCell(arrangedStepLabel);

                        if (arrangedStepLabelCell != null) {
                            arrangedStepLabelCell.padBottom(0);
                            arrangedStepLabel.remove();
                        }

                        stepLabel.addAction(alpha(1f, 0.25f));
                        centerTable.invalidate();

                        step.isSelected = false;
                    } else {
                        stepLabel.addAction(alpha(0.5f, 0.25f));
                        arrangedSteps.add(step);
                        centerTable.add(arrangedStepLabel).width(400).padBottom(15).row();

                        step.isSelected = true;
                    }

                    arrangedStepLabel.setWrap(true);
                    arrangedStepLabel.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            game.sounds.paper2Sound.play();
                            arrangedSteps.remove(step);

                            Cell<Label> arrangedStepLabelCell = centerTable.getCell(arrangedStepLabel);

                            if (arrangedStepLabelCell != null) {
                                arrangedStepLabelCell.padBottom(0);
                                arrangedStepLabel.remove();
                            }

                            stepLabel.addAction(alpha(1f, 0.25f));
                            centerTable.invalidate();

                            selectedSteps.remove(labelText);

                            step.isSelected = false;

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
                "Click on the 'cook' button if you're sure with the order of steps for this meal!",
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32)));
        instructions.add(new Label(
                "Remember: Trying to cook with the wrong steps will result in a disaster, costing you some points! Good luck!",
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32)));

        instructions.forEach(instruction -> {
            instruction.setWrap(true);
            instructionsContainer.add(instruction).width(400).padBottom(15).row();
        });

        TextButton cookBtn = new TextButton("Cook", this.game.skin.get("text-button-default", TextButtonStyle.class));

        cookBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.sounds.clickSound.play();
                checkSteps();

                return true;
            }
        });

        rightContainer.add(instructionsContainer).row();
        rightContainer.add(cookBtn).padTop(50);

        this.rightTable.add(rightContainer);
    }

    private void prepareCookingAnimation() {
        this.cookingAnimation = new CookingAnimation(this.game, this.stage);
    }

    private void renderTimerScore() {
        this.timerLabel = new Label(String.format("%02d:%02d", this.time / 60, this.time % 60),
                CustomSkin.generateCustomLilitaOneBackground(Colors.lightBrown, 32));
        this.scoreLabel = new Label(recipePaperView.kitchenScreen.currentPoints + " pts",
                CustomSkin.generateCustomLilitaOneBackground(Colors.lightBrown, 32));

        this.timerLabel.setAlignment(Align.center);
        this.scoreLabel.setAlignment(Align.center);

        this.timerTable.add(timerLabel).center().expand().top().padTop(48);
        this.scoreTable.add(scoreLabel).top().expand().right().pad(48, 0, 0, 48);
    }

    private void startPhase() {
        recipePaperView.kitchenScreen.isStepSortingStarted = true;
    }

    public void startCountdown(float delta) {
        if (recipePaperView.kitchenScreen.isStepSortingStarted && !this.isCountdownPaused) {
            this.sortingTimeElapsed += delta;

            if (this.sortingTimeElapsed >= 1) {
                this.time -= (int) this.sortingTimeElapsed;
                this.sortingTimeElapsed = 0;

                if (this.time <= 0 && !this.isSortingTimeUp) {
                    this.time = 0;
                    this.isSortingTimeUp = true;

                    alertUserFailedSorting();
                }

                int seconds = (int) this.time;

                if (seconds >= 0) {
                    this.timerLabel.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
                }
            }
        }
    }

    private void alertUserFailedSorting() {
        if (meal.difficulty.equals("easy")) {
            ArrayList<TextButton> buttons = new ArrayList<TextButton>();
            TextButton nextBtn = new TextButton("Next meal",
                    this.game.skin.get("text-button-default", TextButtonStyle.class));

            buttons.add(nextBtn);

            Popup popup = new Popup(this.game, this.stage, "Time's up!",
                    "You ran out of time to sort the steps. No deduction for this easy meal. Proceed now to next meal!",
                    buttons,
                    this.game.sounds.failSound);

            nextBtn.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    game.sounds.clickSound.play();

                    popup.hide(() -> {
                        recipePaperView.kitchenScreen.nextRound();
                    });

                    return true;
                }
            });

            popup.show();
        } else if (meal.difficulty.equals("hard")) {
            ArrayList<TextButton> buttons = new ArrayList<TextButton>();
            TextButton okayBtn = new TextButton("Okay",
                    this.game.skin.get("text-button-default", TextButtonStyle.class));

            buttons.add(okayBtn);

            Popup popup = new Popup(this.game, this.stage, "Time's up!",
                    "You ran out of time to sort the steps. Since this is the last round, you have to try again from the start!",
                    buttons,
                    this.game.sounds.failSound);

            okayBtn.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    game.sounds.clickSound.play();

                    popup.hide(() -> {
                        timerTable.addAction(fadeOut(0.5f, Interpolation.pow5));
                        scoreTable.addAction(fadeOut(0.5f, Interpolation.pow5));
                        recipePaperView.kitchenScreen.transitionToMainMenu();
                    });

                    return true;
                }
            });

            popup.show();
        } else {
            ArrayList<TextButton> buttons = new ArrayList<TextButton>();
            TextButton nextBtn = new TextButton("Next meal",
                    this.game.skin.get("text-button-default", TextButtonStyle.class));

            buttons.add(nextBtn);

            Popup popup = new Popup(this.game, this.stage, "Time's up!",
                    "You ran out of time to sort the steps. You got a total deduction of " + this.deduction
                            + " points. Proceed now to next meal!",
                    buttons,
                    this.game.sounds.failSound);

            nextBtn.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    game.sounds.clickSound.play();

                    popup.hide(() -> {
                        recipePaperView.kitchenScreen.nextRound();
                    });

                    return true;
                }
            });

            popup.show();
        }
    }
}
