package com.whatstherecipe.game.components;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.EventListener;
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
        renderTable();
    }

    private void renderOverlay() {
        Pixmap brownPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        brownPixmap.setColor(Colors.darkBrown);
        brownPixmap.fillRectangle(0, 0, 1, 1);

        this.brownOverlay = new Image(new Texture(brownPixmap));
        this.brownOverlay.setFillParent(true);
        this.brownOverlay.addAction(alpha(0));

        this.brownOverlay.addListener((EventListener) event -> {
            if (event.toString().contains("touchDown")) {
                toggleBasket();
            }

            return false;
        });
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
            this.table.addAction(fadeOut(0.5f, Interpolation.pow5));
            this.brownOverlay.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
                this.brownOverlay.remove();
                this.table.remove();
                this.row1.remove();
                this.row2.remove();

                this.table.clear();
                this.row1.clear();
                this.row2.clear();
            })));

            basketVisible = false;
        } else {
            this.stage.addActor(this.brownOverlay);
            this.brownOverlay.addAction(fadeIn(0.5f, Interpolation.pow5));

            this.table.add(this.row1).padTop(50).grow().padBottom(50).row();
            this.table.add(this.row2).padTop(50).grow().padBottom(50);

            this.stage.addActor(this.table);
            this.table.addAction(fadeIn(0.5f, Interpolation.pow5));

            addIngredients();
        }
    }

    private void addIngredients() {
        ingredientsInBasket.forEach(ingredient -> {
            Table ingredientTable = new Table();
            Label label = new Label(ingredient.name, CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 48));
            TextButton removeBtn = new TextButton("Remove",
                    this.game.skin.get("text-button-default", TextButtonStyle.class));

            label.setAlignment(Align.center);

            ingredientTable.add(label).center().row();
            ingredientTable.add(removeBtn).center().padTop(16);

            if (row1.getChildren().size < 4) {
                row1.add(ingredientTable).center().expand();
            } else {
                row2.add(ingredientTable).center().expand();
            }

            removeBtn.addListener(new InputListener() {
                @Override
                public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y,
                        int pointer, int button) {
                    ingredient.toggleToBasket();

                    row1.clear();
                    row2.clear();

                    addIngredients();

                    return true;
                }
            });
        });

        basketVisible = true;
    }
}
