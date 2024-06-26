package com.whatstherecipe.game.components;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.ui.Colors;
import com.whatstherecipe.game.ui.CustomSkin;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Popup {
    private final WhatsTheRecipe game;
    private Stage stage;
    private Image overlay;
    private Table rootTable;
    private Table mainContainer;
    private Group mainGroup;
    private Image groupBg;
    private Sound sound;

    public Popup(final WhatsTheRecipe game, Stage stage, String title, String message, ArrayList<TextButton> buttons) {
        this.game = game;
        this.stage = stage;

        initOverlay();
        initTables();
        initBg();
        initLabels(title, message, buttons);
    }

    public Popup(final WhatsTheRecipe game, Stage stage, String title, String message, ArrayList<TextButton> buttons,
            Sound sound) {
        this.game = game;
        this.stage = stage;
        this.sound = sound;

        initOverlay();
        initTables();
        initBg();
        initLabels(title, message, buttons);
    }

    private void initOverlay() {
        Pixmap brownPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        brownPixmap.setColor(Colors.brownOverlayOpacity);
        brownPixmap.fillRectangle(0, 0, 1, 1);

        this.overlay = new Image(new Texture(brownPixmap));
        this.overlay.setFillParent(true);
        this.overlay.addAction(alpha(0));

        this.stage.addActor(overlay);
    }

    private void initTables() {
        this.rootTable = new Table();
        this.rootTable.setFillParent(true);

        this.mainContainer = new Table();
    }

    private void initBg() {
        if (this.game.assets.isLoaded("popup-bg.png")) {
            Texture popupTexture = this.game.assets.get("popup-bg.png", Texture.class);

            this.groupBg = new Image(popupTexture);

            this.groupBg.setOrigin(Align.center);
            this.groupBg.setSize(popupTexture.getWidth(), popupTexture.getHeight());
            this.groupBg.setPosition((this.stage.getWidth() / 2) - (this.groupBg.getWidth() / 2),
                    (this.stage.getHeight() / 2) - (this.groupBg.getHeight() / 2) - 32);
            this.groupBg.addAction(parallel(alpha(0), scaleTo(0.2f, 0.2f)));
            this.stage.addActor(groupBg);
        }
    }

    private void initLabels(String title, String message, ArrayList<TextButton> buttons) {
        Label titleLabel = new Label(title, CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 64));
        Label messageLabel = new Label(message, CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 32));
        Table buttonsRow = new Table();

        messageLabel.setWrap(true);
        messageLabel.setAlignment(Align.center);

        this.mainContainer.add(titleLabel).center().row();
        this.mainContainer.add(messageLabel).width(850).center().padTop(16).row();

        buttons.forEach(button -> {
            buttonsRow.add(button).padTop(16).padLeft(8).padRight(8);

            button.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    Gdx.graphics.setCursor(game.cursors.spatulaWhiskCursor);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    Gdx.graphics.setCursor(game.cursors.spatulaCursor);
                }
            });
        });

        this.mainContainer.add(buttonsRow).center().padTop(32);

        this.mainGroup = new Group();
        mainGroup.addActor(mainContainer);
        mainGroup.setSize(mainContainer.getWidth(), mainContainer.getHeight());
        mainGroup.setPosition((this.stage.getWidth() / 2) - (this.mainGroup.getWidth() / 2),
                (this.stage.getHeight() / 2) - (this.mainGroup.getHeight() / 2));
        mainGroup.setOrigin(Align.center);
        mainGroup.addAction(parallel(alpha(0), scaleTo(0.2f, 0.2f)));
        this.stage.addActor(mainGroup);
    }

    public void show() {
        this.overlay.addAction(fadeIn(0.5f, Interpolation.pow5));
        this.mainGroup.addAction(parallel(scaleTo(1, 1, 0.7f, Interpolation.swingOut),
                fadeIn(0.5f, Interpolation.pow5)));
        this.groupBg.addAction(parallel(scaleTo(1, 1, 0.7f, Interpolation.swingOut),
                fadeIn(0.5f, Interpolation.pow5)));

        if (this.sound != null) {
            this.sound.play();
        }
    }

    public void hide() {
        this.mainGroup.addAction(
                parallel(fadeOut(0.7f, Interpolation.pow5), scaleTo(0.2f, 0.2f, 0.5f, Interpolation.swingIn)));
        this.overlay.addAction(sequence(fadeOut(0.5f, Interpolation.pow5)));
        this.groupBg.addAction(
                sequence(parallel(fadeOut(0.7f, Interpolation.pow5), scaleTo(0.2f, 0.2f, 0.5f, Interpolation.swingIn)),
                        run(() -> {
                            this.overlay.remove();
                            this.groupBg.remove();
                            this.mainContainer.remove();
                        })));
    }

    public void hide(Runnable runnable) {
        this.mainGroup.addAction(
                parallel(fadeOut(0.7f, Interpolation.pow5), scaleTo(0.2f, 0.2f, 0.5f, Interpolation.swingIn)));
        this.overlay.addAction(sequence(fadeOut(0.5f, Interpolation.pow5)));
        this.groupBg.addAction(
                sequence(parallel(fadeOut(0.7f, Interpolation.pow5), scaleTo(0.2f, 0.2f, 0.5f, Interpolation.swingIn)),
                        run(() -> {
                            this.overlay.remove();
                            this.groupBg.remove();
                            this.mainContainer.remove();

                            RunnableAction action = action(RunnableAction.class);
                            action.setRunnable(runnable);
                            action.run();
                        })));
    }
}
