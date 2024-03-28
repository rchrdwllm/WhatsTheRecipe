package com.whatstherecipe.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.ui.Colors;

public class HowToPlayScreen implements Screen {
    private final WhatsTheRecipe game;
    private OrthographicCamera camera;
    private Stage stage;
    private Image paper;

    public HowToPlayScreen(final WhatsTheRecipe game) {
        this.game = game;
        this.camera = game.camera;
        this.stage = new Stage();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Colors.brown);
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

        this.stage.addListener((EventListener) event -> {
            if (event.toString().contains("touchDown")) {
                this.game.setScreen(this.game.mainMenuScreen);
            }

            return false;
        });

        renderHowTo();
    }

    private void renderHowTo() {
        if (this.game.assets.isLoaded("paper.png")) {
            Texture paperTexture = this.game.assets.get("paper.png", Texture.class);

            this.paper = new Image(paperTexture);
            this.paper.setOrigin(Align.center);
            this.paper.setWidth((float) (paper.getWidth() / 1.25));
            this.paper.setHeight((float) (paper.getHeight() / 1.25));
            this.paper.setPosition((this.game.V_WIDTH / 2) - (paper.getWidth() / 2),
                    (this.game.V_HEIGHT / 2) - (paper.getHeight() / 2));
            this.stage.addActor(this.paper);
            this.paper.toFront();
        }
    }
}
