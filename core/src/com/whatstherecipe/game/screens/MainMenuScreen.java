package com.whatstherecipe.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.classes.Meal;
import com.whatstherecipe.game.classes.Meals;
import com.whatstherecipe.game.components.InstructionsView;

public class MainMenuScreen implements Screen {
    private final WhatsTheRecipe game;
    private Stage stage;
    private Table tableRoot;
    private Table labelGroup;
    private OrthographicCamera camera;
    private Image kitchenBg;
    private int screenShows = 0;
    private InstructionsView instructionsView;
    private Music backgroundMusic;

    public MainMenuScreen(final WhatsTheRecipe game) {
        this.game = game;
        this.camera = game.camera;
        this.stage = new Stage(new StretchViewport(game.V_WIDTH, game.V_HEIGHT));

        initComponents();
    }

    @Override
    public void dispose() {
        if (backgroundMusic != null) {
            backgroundMusic.dispose();
        }
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

        this.instructionsView = new InstructionsView(game, stage);

        if (this.screenShows > 0) {
            resetState();
        } else {
            this.stage.addAction(sequence(alpha(0f), fadeIn(2f, Interpolation.pow5)));

            renderHeadingsAndButtons();
            renderKitchenBg();
        }

        this.screenShows += 1;
        if (game.assets.isLoaded("background.mp3")) {
            backgroundMusic = game.assets.get("background.mp3", Music.class);
            backgroundMusic.setLooping(true); // If you want the music to loop
            backgroundMusic.play();
        } else {
            game.assets.load("background.mp3", Music.class);
            game.assets.finishLoading();
            backgroundMusic = game.assets.get("background.mp3", Music.class);
            backgroundMusic.setLooping(true); // If you want the music to loop
            backgroundMusic.play();

        }
    }

    private void initComponents() {
        this.tableRoot = new Table();

        this.tableRoot.setFillParent(true);
        this.stage.addActor(tableRoot);
        this.tableRoot.toFront();
    }

    private void renderHeadingsAndButtons() {
        this.labelGroup = new Table();

        if (this.game.assets.isLoaded("main-menu-screen-text.png")) {
            Texture mainMenuScreenText = this.game.assets.get("main-menu-screen-text.png", Texture.class);
            Image whatsTheLabel = new Image(mainMenuScreenText);

            this.labelGroup.add(whatsTheLabel).left();
        }

        TextButton playButton = new TextButton("Play",
                this.game.skin.get("text-button-default", TextButtonStyle.class));
        TextButton howToPlay = new TextButton("How to play?",
                this.game.skin.get("text-button-default", TextButtonStyle.class));
        TextButton exitButton = new TextButton("Exit",
                this.game.skin.get("text-button-default", TextButtonStyle.class));

        this.labelGroup.row();
        this.labelGroup.add(playButton).padTop(150).left();
        this.labelGroup.row();
        this.labelGroup.add(howToPlay).padTop(30).left();
        this.labelGroup.row();
        this.labelGroup.add(exitButton).padTop(30).left();

        playButton.addAction(sequence(
                alpha(0),
                moveBy(-350, 0),
                delay(1f),
                parallel(fadeIn(0.5f, Interpolation.pow5), moveBy(350, 0, 0.5f, Interpolation.swingOut))));
        howToPlay.addAction(sequence(
                alpha(0),
                moveBy(-350, 0),
                delay(1.15f),
                parallel(fadeIn(0.5f, Interpolation.pow5), moveBy(350, 0, 0.5f, Interpolation.swingOut))));
        exitButton.addAction(sequence(
                alpha(0),
                moveBy(-350, 0),
                delay(1.3f),
                parallel(fadeIn(0.5f, Interpolation.pow5), moveBy(350, 0, 0.5f, Interpolation.swingOut))));

        playButton.addListener(
                (EventListener) event -> {
                    if (event.toString().equals("touchDown")) {
                        playGame();
                    }

                    return false;
                });

        howToPlay.addListener(
                (EventListener) event -> {
                    if (event.toString().equals("touchDown")) {
                        instructionsView.toggleInstructions();
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

        this.labelGroup.setPosition(160, (float) 132.5);
        this.tableRoot.add(labelGroup).expandX().left().top().pad(0, 160, 0, 0);
    }

    private void renderKitchenBg() {
        if (this.game.assets.isLoaded("kitchen.jpg")) {
            Texture kitchenTexture = this.game.assets.get("kitchen.jpg", Texture.class);

            this.kitchenBg = new Image(kitchenTexture);

            this.kitchenBg.setOrigin(Align.center);
            this.kitchenBg.setScale(1.25f, 1.25f);
            this.kitchenBg.setWidth(this.game.V_WIDTH * 2);
            this.kitchenBg.setHeight(this.game.V_HEIGHT);
            this.stage.addActor(this.kitchenBg);
            this.kitchenBg.toBack();
        }
    }

    private void playGame() {
        ArrayList<Meal> mealPlan = Meals.createMealPlan();

        RunnableAction panKitchenBg = new RunnableAction();

        panKitchenBg.setRunnable(new Runnable() {
            @Override
            public void run() {
                RunnableAction switchToKitchenScreen = new RunnableAction();

                switchToKitchenScreen.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new KitchenScreen(game, mealPlan));
                    }
                });

                kitchenBg.addAction(sequence(
                        parallel(
                                scaleBy(-0.25f, -0.25f, 2f, Interpolation.pow5),
                                moveBy(-game.V_WIDTH, 0, 2f, Interpolation.pow5)),
                        switchToKitchenScreen));
            }
        });

        this.labelGroup.addAction(sequence(
                parallel(
                        fadeOut(0.75f, Interpolation.pow5),
                        moveBy(-350, 0, 0.5f, Interpolation.swingIn)),
                panKitchenBg));
    }

    private void resetState() {
        this.kitchenBg.clear();
        this.kitchenBg.setPosition(0, 0);
        this.kitchenBg.setScale(1.25f, 1.25f);
        this.labelGroup.addAction(parallel(fadeIn(0.35f, Interpolation.pow5),
                moveTo(160, (float) 132.5, 0.5f, Interpolation.swingOut)));
    }
}
