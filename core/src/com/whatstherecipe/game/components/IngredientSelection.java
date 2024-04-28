package com.whatstherecipe.game.components;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import java.util.ArrayList;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.classes.Meal;
import com.whatstherecipe.game.screens.KitchenScreen;
import com.whatstherecipe.game.ui.Colors;
import com.whatstherecipe.game.ui.CustomSkin;

public class IngredientSelection {
    public final WhatsTheRecipe game;
    public Stage stage;
    public Meal meal;
    private Table table;
    private Table leftTable;
    private Table centerTable;
    private Table rightTable;
    private Table backBtnTable;
    private RecipePaperView recipePaperView;
    private StepSorting stepSorting;
    private ArrayList<String> selectedIngredients;
    private KitchenScreen kitchenScreen;
    private String stepSortingTimeString;

    public IngredientSelection(RecipePaperView recipePaperView) {
        this.game = recipePaperView.game;
        this.stage = recipePaperView.stage;
        this.meal = recipePaperView.meal;
        this.stepSorting = recipePaperView.stepSorting;
        this.kitchenScreen = recipePaperView.kitchenScreen;
        this.recipePaperView = recipePaperView;
        this.selectedIngredients = new ArrayList<String>();

        determineStepSortingTimeString();
        initTables();
        renderLeft();
        renderCenter();
        renderRight();
        renderBtns();
    }

    private void determineStepSortingTimeString() {
        int minutes = stepSorting.time / 60;
        int seconds = stepSorting.time % 60;

        if (minutes == 0) {
            this.stepSortingTimeString = seconds + " seconds";
        } else if (seconds == 0) {
            if (minutes > 1) {
                this.stepSortingTimeString = minutes + " minutes";
            } else {
                this.stepSortingTimeString = minutes + " minute";
            }
        }
    }

    private void initTables() {
        this.table = new Table();
        this.leftTable = new Table();
        this.centerTable = new Table();
        this.rightTable = new Table();
        this.backBtnTable = new Table();

        this.table.setFillParent(true);
        this.table.add(leftTable).expand().left().padLeft(100);
        this.table.add(centerTable).expand().center();
        this.table.add(rightTable).expand().right().padRight(100);

        this.backBtnTable.setFillParent(true);
        this.backBtnTable.addAction(alpha(0));
    }

    public void show() {
        this.stage.addActor(table);
        this.table.toFront();
        this.table.addAction(alpha(0f));
        this.table.addAction(sequence(delay(1f, fadeIn(0.5f, Interpolation.pow5))));
        this.stage.addActor(backBtnTable);
        this.backBtnTable.addAction(fadeIn(0.5f, Interpolation.pow5));
        this.backBtnTable.toFront();
    }

    public void hide() {
        this.table.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
            this.table.remove();
        })));
        this.backBtnTable.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
            this.backBtnTable.remove();
        })));
    }

    private void renderLeft() {
        Label mealName = new Label(meal.name, CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 64));
        ArrayList<Label> instructions = new ArrayList<Label>();

        this.leftTable.add(mealName).left().padBottom(15).row();

        instructions.add(
                new Label("Listed here on the paper are the ingredients for cooking " + meal.name
                        + ". Find and collect them first from the 4 labeled cabinets back in the kitchen to proceed to the next phase: finding out the correct steps for cooking "
                        + meal.name + ".",
                        CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32)));
        instructions.add(new Label(
                "Use your basket at the middle in the kitchen for collecting ingredients, viewing them, as well as removing those that you don't want anymore!",
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32)));

        instructions.forEach(instruction -> {
            instruction.setWrap(true);

            this.leftTable.add(instruction).width(400).padBottom(15).row();
        });
    }

    private void renderCenter() {
        for (int i = 1; i <= this.meal.ingredients.size(); i++) {
            String ingredient = this.meal.ingredients.get(i - 1);

            ingredient = ingredient.substring(0, 1).toUpperCase() + ingredient.substring(1);

            Label ingredientLabel = new Label(i + ". " + ingredient,
                    CustomSkin.generateCustomLilitaOneFont(Colors.brown, 32));

            this.centerTable.add(ingredientLabel).width(300).padBottom(15).row();
        }
    }

    private void renderBtns() {
        TextButton backBtn = new TextButton("Back", this.game.skin.get("text-button-alt", TextButtonStyle.class));

        backBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.sounds.clickSound.play();
                recipePaperView.toggleRecipePaper();

                return true;
            }
        });

        this.backBtnTable.add(backBtn).top().left().expand().pad(48, 48, 0, 0);
    }

    public void checkIngredients() {
        this.selectedIngredients.clear();

        System.out.println("\nSelected ingredients:");

        this.recipePaperView.kitchenScreen.ingredientsInBasket.forEach(ingredient -> {
            this.selectedIngredients.add(ingredient.name);

            System.out.println(ingredient.name);
        });

        System.out.println("Correct meal ingredients:");

        this.meal.ingredients.forEach(ingredient -> {
            System.out.println(ingredient);
        });

        if (selectedIngredients.containsAll(meal.ingredients)) {
            if (selectedIngredients.size() == meal.ingredients.size()) {
                kitchenScreen.phase = "step-sorting";

                System.out.println("Ingredients match!");

                if (meal.difficulty.equals("hard")) {
                    TextButton nextBtn = new TextButton("Next",
                            this.game.skin.get("text-button-default", TextButtonStyle.class));
                    ArrayList<TextButton> buttons = new ArrayList<TextButton>();

                    buttons.add(nextBtn);

                    TextButton nestedNextBtn = new TextButton("Next",
                            game.skin.get("text-button-default", TextButtonStyle.class));
                    ArrayList<TextButton> nextBtns = new ArrayList<TextButton>();

                    nextBtns.add(nestedNextBtn);

                    Popup nextPopup = new Popup(game, stage, "All or nothing!!",
                            "Since this is a " + meal.difficulty + " meal, you have " + stepSortingTimeString
                                    + " to sort the correct steps for cooking and you only have 1 attempt. If you win, you'll have twice your current score. If not, you'll lose everything, and may have to start all over again. Good luck!",
                            nextBtns);

                    Popup popup = new Popup(this.game, this.stage, "Ingredients match!",
                            "You can now proceed to the sorting stage of the round! Well done!", buttons,
                            this.game.sounds.successSound);

                    popup.show();

                    nextBtn.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            game.sounds.clickSound.play();

                            popup.hide(() -> {
                                nextPopup.show();

                                nestedNextBtn.addListener(new InputListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                            int button) {
                                        game.sounds.clickSound.play();
                                        nextPopup.hide();

                                        hide();

                                        stepSorting.show();

                                        return true;
                                    }
                                });
                            });

                            return true;
                        }
                    });
                } else {
                    TextButton nextBtn = new TextButton("Next",
                            this.game.skin.get("text-button-default", TextButtonStyle.class));
                    ArrayList<TextButton> buttons = new ArrayList<TextButton>();

                    buttons.add(nextBtn);

                    TextButton nestedNextBtn = new TextButton("Next",
                            game.skin.get("text-button-default", TextButtonStyle.class));
                    ArrayList<TextButton> nextBtns = new ArrayList<TextButton>();

                    nextBtns.add(nestedNextBtn);

                    Popup nextPopup = new Popup(game, stage, "Next phase!",
                            "For this " + meal.difficulty + " meal, you have " + stepSortingTimeString
                                    + " to sort the correct steps for cooking. Good luck!",
                            nextBtns);

                    Popup popup = new Popup(this.game, this.stage, "Ingredients match!",
                            "You can now proceed to the sorting stage of the round! Well done!", buttons,
                            this.game.sounds.successSound);

                    popup.show();

                    nextBtn.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            game.sounds.clickSound.play();

                            popup.hide(() -> {
                                nextPopup.show();

                                nestedNextBtn.addListener(new InputListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                            int button) {
                                        game.sounds.clickSound.play();
                                        nextPopup.hide();

                                        hide();

                                        stepSorting.show();

                                        return true;
                                    }
                                });
                            });

                            return true;
                        }
                    });
                }
            } else {
                ArrayList<TextButton> buttons = new ArrayList<TextButton>();
                TextButton tryAgainBtn = new TextButton("Try again",
                        this.game.skin.get("text-button-default", TextButtonStyle.class));

                buttons.add(tryAgainBtn);

                Popup popup = new Popup(this.game, this.stage, "Not a match!",
                        "These ingredients aren't correct. Try again!", buttons, this.game.sounds.failSound);

                popup.show();

                tryAgainBtn.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        game.sounds.clickSound.play();
                        popup.hide();

                        return true;
                    }
                });

                System.out.println("Ingredients don't match!");
            }
        } else {
            ArrayList<TextButton> buttons = new ArrayList<TextButton>();
            TextButton tryAgainBtn = new TextButton("Try again",
                    this.game.skin.get("text-button-default", TextButtonStyle.class));

            buttons.add(tryAgainBtn);

            Popup popup = new Popup(this.game, this.stage, "Not a match!",
                    "These ingredients aren't correct. Try again!", buttons, this.game.sounds.failSound);

            popup.show();

            tryAgainBtn.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    game.sounds.clickSound.play();
                    popup.hide();

                    return true;
                }
            });

            System.out.println("Ingredients don't match!");
        }
    }

    private void renderRight() {
        Table instructionsContainer = new Table();
        ArrayList<Label> instructions = new ArrayList<Label>();

        instructions.add(new Label("Click on the button below to check if your basket contains the right ingredients!",
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32)));
        instructions.add(new Label(
                "Remember: Having the wrong collection of ingredients won't allow you to proceed to the sorting stage of the round!",
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32)));

        instructions.forEach(instruction -> {
            instruction.setWrap(true);

            instructionsContainer.add(instruction).width(400).padBottom(15).row();
        });

        this.rightTable.add(instructionsContainer).row();

        TextButton checkBtn = new TextButton(
                "Check ingredients",
                this.game.skin.get("text-button-default", TextButtonStyle.class));

        checkBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.sounds.clickSound.play();
                checkIngredients();

                return true;
            }

        });

        this.rightTable.add(checkBtn).padTop(50);
    }
}
