package com.whatstherecipe.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.whatstherecipe.game.WhatsTheRecipe;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class MainMenuScreen implements Screen {
    private final WhatsTheRecipe game;
    private Stage stage;
    private Table tableRoot;
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

        renderLabel();
        renderKitchenBg();
    }

    private void initComponents() {
        this.tableRoot = new Table();

        this.tableRoot.setFillParent(true);
        this.stage.addActor(tableRoot);
        this.tableRoot.toFront();
    }

    private void renderLabel() {
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        Label mainMenuLabel = new Label("Main menu screen", skin.get("heading", LabelStyle.class));

        this.tableRoot.add(mainMenuLabel);
    }

    private void renderKitchenBg() {
        if (this.game.assets.isLoaded("kitchen.png")) {
            Texture kitchenTexture = this.game.assets.get("kitchen.png", Texture.class);

            this.kitchenBg = new Image(kitchenTexture);

            this.kitchenBg.setWidth(this.game.V_WIDTH);
            this.kitchenBg.setHeight(this.game.V_HEIGHT);
            this.stage.addActor(this.kitchenBg);
            this.kitchenBg.toBack();
        }
    }
}
