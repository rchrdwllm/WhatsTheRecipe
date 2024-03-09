package com.whatstherecipe.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.whatstherecipe.game.WhatsTheRecipe;

public class MainMenuScreen implements Screen {
    private final WhatsTheRecipe game;
    private Stage stage;
    private Table tableRoot;
    private OrthographicCamera camera;

    public MainMenuScreen(final WhatsTheRecipe game) {
        this.game = game;
        this.camera = game.camera;
        this.stage = new Stage();

        initComponents();
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
        renderLabel();
    }

    private void initComponents() {
        this.tableRoot = new Table();

        this.tableRoot.setFillParent(true);
        this.stage.addActor(tableRoot);
    }

    private void renderLabel() {
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        Label mainMenuLabel = new Label("Main menu screen", skin.get("heading", LabelStyle.class));

        this.tableRoot.add(mainMenuLabel);
    }
}
