package com.whatstherecipe.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.whatstherecipe.game.WhatsTheRecipe;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class MainMenuScreen implements Screen {
    private final WhatsTheRecipe game;
    private Stage stage;
    private Table tableRoot;
    private Table labelGroup;
    private OrthographicCamera camera;
    private Image kitchenBg;

    public MainMenuScreen(final WhatsTheRecipe game) {
        this.game = game;
        this.camera = game.camera;
        this.stage = new Stage();

        initComponents();
    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.valueOf("#F9ECE9"));
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.camera.update();
        this.stage.act(delta);
        this.stage.draw();
        this.game.batch.setProjectionMatrix(this.camera.combined);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        this.stage.addAction(sequence(alpha(0f), fadeIn(2f, Interpolation.pow5)));

        renderHeadingsAndButtons();
        renderKitchenBg();
    }

    private void initComponents() {
        this.tableRoot = new Table();

        this.tableRoot.setFillParent(true);
        this.stage.addActor(tableRoot);
        this.tableRoot.toFront();
    }

    private void renderHeadingsAndButtons() {
        Label whatsTheLabel = new Label("WHAT'S THE", this.game.skin.get("text-48", LabelStyle.class));
        Label recipeLabel = new Label("RECIPE?", this.game.skin.get("heading-208", LabelStyle.class));
        TextButton playButton = new TextButton("Play",
                this.game.skin.get("text-button-default", TextButtonStyle.class));
        TextButton howToPlay = new TextButton("How to play?",
                this.game.skin.get("text-button-default", TextButtonStyle.class));
        TextButton exitButton = new TextButton("Exit",
                this.game.skin.get("text-button-default", TextButtonStyle.class));

        this.labelGroup = new Table();

        this.labelGroup.add(whatsTheLabel).padTop(50).left();
        this.labelGroup.row();
        this.labelGroup.add(recipeLabel);
        this.labelGroup.row();
        this.labelGroup.add(playButton).padTop(150).left();
        this.labelGroup.row();
        this.labelGroup.add(howToPlay).padTop(30).left();
        this.labelGroup.row();
        this.labelGroup.add(exitButton).padTop(30).left();

        playButton.addAction(sequence(
                alpha(0),
                delay(1f),
                fadeIn(1f, Interpolation.pow5)));
        howToPlay.addAction(sequence(
                alpha(0),
                delay(1.25f),
                fadeIn(1f, Interpolation.pow5)));
        exitButton.addAction(sequence(
                alpha(0),
                delay(1.5f),
                fadeIn(1f, Interpolation.pow5)));

        playButton.addListener(
                (EventListener) event -> {
                    if (event.toString().equals("touchDown")) {
                        transitionToKitchen();
                    }

                    return false;
                });

        exitButton.addListener(
                (EventListener) event -> {
                    if (event.toString().equals("touchDown")) {
                        Gdx.app.exit();
                    }

                    return false;
                });

        this.tableRoot.add(labelGroup).expandX().left().expandY().top().pad(100, 160, 0, 0);
    }

    private void renderKitchenBg() {
        if (this.game.assets.isLoaded("kitchen.png")) {
            Texture kitchenTexture = this.game.assets.get("kitchen.png", Texture.class);

            this.kitchenBg = new Image(kitchenTexture);

            this.kitchenBg.setWidth(this.game.V_WIDTH * 2);
            this.kitchenBg.setHeight(this.game.V_HEIGHT);
            this.stage.addActor(this.kitchenBg);
            this.kitchenBg.toBack();
        }
    }

    private void transitionToKitchen() {
        RunnableAction panKitchenBg = new RunnableAction();

        panKitchenBg.setRunnable(new Runnable() {
            @Override
            public void run() {
                kitchenBg.addAction(moveBy(-game.V_WIDTH, 0, 3f, Interpolation.pow5));
            }
        });

        this.labelGroup.addAction(sequence(
                parallel(
                        fadeOut(1.5f, Interpolation.pow5),
                        moveBy(-750, 0, 1.5f, Interpolation.pow5)),
                panKitchenBg));
    }
}
