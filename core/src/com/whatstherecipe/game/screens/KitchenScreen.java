package com.whatstherecipe.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

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
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.whatstherecipe.game.WhatsTheRecipe;

public class KitchenScreen implements Screen {
    private final WhatsTheRecipe game;
    private Stage stage;
    private Table tableRoot;
    private Label label;
    private OrthographicCamera camera;
    private Image kitchenBg;
    private int screenShows = 0;

    public KitchenScreen(final WhatsTheRecipe game) {
        this.game = game;
        this.stage = new Stage();
        this.camera = game.camera;

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
        Gdx.input.setInputProcessor(this.stage);

        if (this.screenShows > 0) {
            resetState();
        } else {
            renderKitchenBg();
            renderLabel();
        }

        this.screenShows += 1;
    }

    private void initComponents() {
        this.tableRoot = new Table();

        this.tableRoot.setFillParent(true);
        this.stage.addActor(tableRoot);
    }

    private void renderKitchenBg() {
        if (this.game.assets.isLoaded("kitchen.jpg")) {
            Texture kitchenTexture = this.game.assets.get("kitchen.jpg", Texture.class);

            this.kitchenBg = new Image(kitchenTexture);

            this.kitchenBg.setOrigin(Align.center);
            this.kitchenBg.setSize(this.game.V_WIDTH * 2, this.game.V_HEIGHT);
            this.kitchenBg.moveBy(-this.game.V_WIDTH, 0);
            this.stage.addActor(kitchenBg);
            this.kitchenBg.toBack();
        }
    }

    private void renderLabel() {
        this.label = new Label("Kitchen", this.game.skin.get("heading-48", LabelStyle.class));
        this.tableRoot.add(this.label);

        this.label.addAction(sequence(alpha(0f), fadeIn(1.5f, Interpolation.pow5)));

        label.addListener(
                (EventListener) event -> {
                    if (event.toString().equals("touchDown")) {
                        transitionToMainMenu();
                    }

                    return false;
                });
    }

    private void transitionToMainMenu() {
        RunnableAction panKitchenBg = new RunnableAction();

        panKitchenBg.setRunnable(new Runnable() {
            @Override
            public void run() {
                RunnableAction switchToMainMenuScreen = new RunnableAction();

                switchToMainMenuScreen.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(game.mainMenuScreen);
                    }
                });

                kitchenBg.addAction(sequence(
                        parallel(
                                scaleBy(0.25f, 0.25f, 3f, Interpolation.pow5),
                                moveBy(game.V_WIDTH, 0, 3f, Interpolation.pow5)),
                        switchToMainMenuScreen));
            }
        });

        this.label.addAction(sequence(
                fadeOut(1.5f, Interpolation.pow5),
                panKitchenBg));
    }

    private void resetState() {
        this.kitchenBg.clear();
        this.kitchenBg.setPosition(-this.game.V_WIDTH, 0);
        this.kitchenBg.setScale(1f, 1f);
        this.label.addAction(fadeIn(1.5f, Interpolation.pow5));
    }
}
