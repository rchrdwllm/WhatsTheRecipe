package com.whatstherecipe.game.components;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.screens.KitchenScreen;
import com.whatstherecipe.game.ui.Colors;
import com.whatstherecipe.game.ui.CustomSkin;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class BasketView {
    private WhatsTheRecipe game;
    private Table table;
    private Table row1;
    private Table row2;
    private Stage stage;
    private Label emptyLabel;
    private KitchenScreen kitchenScreen;
    private ArrayList<Ingredient> ingredientsInBasket;
    private Image brownOverlay;
    private boolean basketVisible = false;

    public BasketView(KitchenScreen kitchenScreen) {
        this.game = kitchenScreen.game;
        this.kitchenScreen = kitchenScreen;
        this.stage = kitchenScreen.stage;
        this.ingredientsInBasket = kitchenScreen.ingredientsInBasket;

        renderOverlay();
        renderEmptyLabel();
        renderTable();
    }

    private void renderOverlay() {
        this.brownOverlay = new Image(this.game.assets.get("abaca-bg.png", Texture.class));
        this.brownOverlay.setFillParent(true);
        this.brownOverlay.addAction(alpha(0));

        this.brownOverlay.addListener((EventListener) event -> {
            if (event.toString().contains("touchDown")) {
                toggleBasket();
            }

            return false;
        });
    }

    private void renderEmptyLabel() {
        this.emptyLabel = new Label("Your basket is empty!",
                CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 48));
        this.emptyLabel.setAlignment(Align.center);
        this.emptyLabel.addAction(alpha(0));
    }

    private void renderTable() {
        this.table = new Table();
        this.table.setFillParent(true);

        this.row1 = new Table();
        this.row2 = new Table();

        this.table.toFront();
        this.table.addAction(alpha(0));
    }

    public void toggleBasket() {
        this.ingredientsInBasket = this.kitchenScreen.ingredientsInBasket;

        if (basketVisible) {
            this.table.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
                this.table.remove();
            })));

            if (ingredientsInBasket.isEmpty()) {
                this.emptyLabel.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
                    this.emptyLabel.remove();
                })));
                this.brownOverlay.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
                    this.brownOverlay.remove();
                })));
            } else {
                this.brownOverlay.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
                    this.brownOverlay.remove();
                    this.table.remove();
                    this.row1.remove();
                    this.row2.remove();

                    this.table.clear();
                    this.row1.clear();
                    this.row2.clear();
                })));
            }

            basketVisible = false;
        } else {
            this.table.clear();

            this.stage.addActor(this.brownOverlay);
            this.brownOverlay.addAction(fadeIn(0.5f, Interpolation.pow5));

            this.stage.addActor(this.table);
            this.table.addAction(fadeIn(0.5f, Interpolation.pow5));

            if (ingredientsInBasket.isEmpty()) {
                this.table.add(emptyLabel).center();
                this.emptyLabel.addAction(alpha(0));
                this.emptyLabel.addAction(sequence(parallel(scaleTo(1, 1, 0.5f, Interpolation.swingOut),
                        fadeIn(0.5f, Interpolation.pow5))));
                this.emptyLabel.toFront();
            } else {
                this.table.add(this.row1).padTop(50).grow().padBottom(50).row();
                this.table.add(this.row2).padTop(50).grow().padBottom(50);

                addIngredients();
            }

            basketVisible = true;
        }
    }

    private void addIngredients() {
        if (ingredientsInBasket.isEmpty()) {
            this.table.clear();
            this.table.clearChildren();

            this.table.add(emptyLabel).center();
            this.emptyLabel.addAction(alpha(0));
            this.emptyLabel.addAction(fadeIn(0.5f, Interpolation.pow5));
            this.emptyLabel.toFront();

            return;
        }

        for (int i = 0; i < ingredientsInBasket.size(); i++) {
            Ingredient ingredient = ingredientsInBasket.get(i);

            Table ingredientTable = new Table();
            Image ingredientImage = new Image(
                    this.game.assets.get("ingredients/basket-view/" + ingredient.name + "-basket.png", Texture.class));
            Table ingredientImageTable = new Table();

            ingredientImageTable.add(ingredientImage).center();

            Table ingredientLabelsTable = new Table();
            Label label = new Label(ingredient.name, CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 48));
            TextButton removeBtn = new TextButton("Remove",
                    this.game.skin.get("text-button-default", TextButtonStyle.class));

            label.setAlignment(Align.center);

            ingredientLabelsTable.add(label).center().padBottom(10).row();
            ingredientLabelsTable.add(removeBtn).center();

            ingredientTable.add(ingredientImageTable).center().height(185).padBottom(10).row();
            ingredientTable.add(ingredientLabelsTable).center();

            Group ingredientGroup = new Group();

            ingredientGroup.addActor(ingredientTable);
            ingredientGroup.addAction(parallel(scaleTo(0.2f, 0.2f), alpha(0)));

            if (row1.getChildren().size < 4) {
                row1.add(ingredientGroup).center().expand();
            } else {
                row2.add(ingredientGroup).center().expand();
            }

            ingredientGroup.addAction(sequence(delay(i * 0.1f), parallel(scaleTo(1, 1, 0.5f, Interpolation.swingOut),
                    fadeIn(0.5f, Interpolation.pow5))));

            removeBtn.addListener(new InputListener() {
                @Override
                public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y,
                        int pointer, int button) {
                    game.sounds.clickSound.play();
                    ingredient.toggleToBasket();

                    row1.clear();
                    row2.clear();

                    updateIngredients();

                    return true;
                }
            });
        }
    }

    private void updateIngredients() {
        if (ingredientsInBasket.isEmpty()) {
            this.table.clear();
            this.table.clearChildren();

            this.table.add(emptyLabel).center();
            this.emptyLabel.addAction(alpha(0));
            this.emptyLabel.addAction(fadeIn(0.5f, Interpolation.pow5));
            this.emptyLabel.toFront();

            return;
        }

        for (int i = 0; i < ingredientsInBasket.size(); i++) {
            Ingredient ingredient = ingredientsInBasket.get(i);

            Table ingredientTable = new Table();
            Image ingredientImage = new Image(
                    this.game.assets.get("ingredients/basket-view/" + ingredient.name + "-basket.png", Texture.class));
            Table ingredientImageTable = new Table();

            ingredientImageTable.add(ingredientImage).center();

            Table ingredientLabelsTable = new Table();
            Label label = new Label(ingredient.name, CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 48));
            TextButton removeBtn = new TextButton("Remove",
                    this.game.skin.get("text-button-default", TextButtonStyle.class));

            label.setAlignment(Align.center);

            ingredientLabelsTable.add(label).center().padBottom(10).row();
            ingredientLabelsTable.add(removeBtn).center();

            ingredientTable.add(ingredientImageTable).center().height(185).padBottom(10).row();
            ingredientTable.add(ingredientLabelsTable).center();

            if (row1.getChildren().size < 4) {
                row1.add(ingredientTable).center().expand();
            } else {
                row2.add(ingredientTable).center().expand();
            }

            removeBtn.addListener(new InputListener() {
                @Override
                public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y,
                        int pointer, int button) {
                    game.sounds.clickSound.play();
                    ingredient.toggleToBasket();

                    row1.clear();
                    row2.clear();

                    updateIngredients();

                    return true;
                }
            });
        }
    }
}
