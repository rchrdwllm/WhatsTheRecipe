package com.whatstherecipe.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.whatstherecipe.game.WhatsTheRecipe;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class LoadingScreen implements Screen {
    int LOGO_WIDTH = 2916;
    int LOGO_HEIGHT = 1602;

    private final WhatsTheRecipe game;
    private OrthographicCamera camera;
    private Stage stage;
    private Image logo;
    private ShapeRenderer shapeRenderer;

    float loadProgress = 0f;
    boolean shouldLoad = false;

    public LoadingScreen(final WhatsTheRecipe game) {
        this.game = game;
        this.camera = game.camera;
        this.stage = new Stage(new FitViewport(game.V_WIDTH, game.V_HEIGHT));
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void dispose() {
        this.stage.dispose();
        this.shapeRenderer.dispose();
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

        updateProgress();
        renderLoader();
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, false);
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
        renderLogo();
    }

    private void renderLogo() {
        if (this.game.assets.update()) {
            RunnableAction load = new RunnableAction();

            load.setRunnable(new Runnable() {
                @Override
                public void run() {
                    shouldLoad = true;
                }
            });

            Texture logoTexture = this.game.assets.get("logo.png", Texture.class);

            this.logo = new Image(logoTexture);
            this.logo.setWidth(LOGO_WIDTH / 3);
            this.logo.setHeight(LOGO_HEIGHT / 3);
            this.logo.setPosition(
                    this.stage.getWidth() / 2 - this.logo.getWidth() / 2,
                    (this.stage.getHeight() / 2 - this.logo.getHeight() / 2) - 100);
            this.logo.setScaling(Scaling.fit);

            this.stage.addActor(this.logo);

            this.logo.addAction(sequence(alpha(0), delay(0.5f), parallel(
                    fadeIn(1f, Interpolation.pow4),
                    moveBy(0, 100, 1f, Interpolation.pow4)),
                    delay(0.5f),
                    load));
        }
    }

    private void updateProgress() {
        if (this.shouldLoad) {
            this.loadProgress = MathUtils.lerp(this.loadProgress, this.game.assets.getProgress(), 0.06f);
        }
    }

    private void renderLoader() {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.valueOf("#E0C7A4"));
        shapeRenderer.rect(0, 0, this.camera.viewportWidth, 15);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.valueOf("#C18F4B"));
        shapeRenderer.rect(0, 0, (this.camera.viewportWidth * loadProgress), 15);
        shapeRenderer.end();
    }
}
