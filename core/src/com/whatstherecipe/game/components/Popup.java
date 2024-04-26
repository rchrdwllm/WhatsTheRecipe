package com.whatstherecipe.game.components;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.whatstherecipe.game.ui.Colors;
import com.whatstherecipe.game.ui.CustomSkin;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Popup {
    private Stage stage;
    private Image overlay;
    private Table rootTable;
    private Table mainContainer;
    private Group mainGroup;

    public Popup(Stage stage, String title, String message, ArrayList<TextButton> buttons) {
        this.stage = stage;

        initOverlay();
        initTables();
        initLabels(title, message);
        addBtns(buttons);
    }

    private void initOverlay() {
        Pixmap brownPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        brownPixmap.setColor(Colors.brownOverlayOpacity);
        brownPixmap.fillRectangle(0, 0, 1, 1);

        this.overlay = new Image(new Texture(brownPixmap));
        this.overlay.setFillParent(true);
        this.overlay.addAction(alpha(0));

        this.overlay.addListener((EventListener) event -> {
            return false;
        });

        this.stage.addActor(overlay);
        this.overlay.addAction(fadeIn(0.5f, Interpolation.pow5));
    }

    private void initTables() {
        this.rootTable = new Table();
        this.rootTable.setFillParent(true);

        this.mainContainer = new Table();
    }

    private void initLabels(String title, String message) {
        Label titleLabel = new Label(title, CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 64));
        Label messageLabel = new Label(message, CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32));

        this.mainContainer.add(titleLabel).center().row();
        this.mainContainer.add(messageLabel).center().padTop(16).row();

        this.mainGroup = new Group();
        mainGroup.addActor(mainContainer);
        mainGroup.setSize(mainContainer.getWidth(), mainContainer.getHeight());
        mainGroup.setPosition((this.stage.getWidth() / 2) - (this.mainGroup.getWidth() / 2),
                (this.stage.getHeight() / 2) - (this.mainGroup.getHeight() / 2));
        mainGroup.setOrigin(Align.center);
        mainGroup.addAction(parallel(alpha(0), scaleTo(0.2f, 0.2f)));

        this.stage.addActor(mainGroup);
    }

    private void addBtns(ArrayList<TextButton> buttons) {
        buttons.forEach(button -> {
            this.mainContainer.add(button).center().padTop(16).padLeft(8).padRight(8);
        });
    }

    public void show() {
        this.mainGroup.addAction(parallel(scaleTo(1, 1, 0.7f, Interpolation.swingOut),
                fadeIn(0.5f, Interpolation.pow5)));
    }

    public void hide() {
        this.mainGroup.addAction(
                parallel(fadeOut(0.7f, Interpolation.pow5), scaleTo(0.2f, 0.2f, 0.5f, Interpolation.swingIn)));
    }
}
