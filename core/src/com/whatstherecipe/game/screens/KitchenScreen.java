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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.whatstherecipe.game.WhatsTheRecipe;

public class KitchenScreen implements Screen {
    private final WhatsTheRecipe game;
    private Stage stage;
    private Table tableRoot;
    private OrthographicCamera camera;
    private Image kitchenBg;
    private Image recipeRef;
    private TextButton backBtn;
    private int screenShows = 0;
    private boolean isRecipePaperShown = false;

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
            renderButtons();
            renderRecipeRef();
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

    private void renderButtons() {
        this.backBtn = new TextButton("Back", this.game.skin.get("text-button-default", TextButtonStyle.class));
        this.tableRoot.add(this.backBtn).expandY().top().expandX().left().pad(100, 100, 0, 0);

        this.backBtn.addAction(sequence(alpha(0f), fadeIn(1.5f, Interpolation.pow5)));
        this.backBtn.addListener(
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

                recipeRef.addAction(sequence(parallel(
                        scaleBy(0.25f, 0.25f, 2f, Interpolation.pow5),
                        moveBy(game.V_WIDTH, 0, 2f, Interpolation.pow5)),
                        alpha(0f)));
                kitchenBg.addAction(sequence(
                        parallel(
                                scaleBy(0.25f, 0.25f, 2f, Interpolation.pow5),
                                moveBy(game.V_WIDTH, 0, 2f, Interpolation.pow5)),
                        switchToMainMenuScreen));
            }
        });

        this.backBtn.addAction(sequence(
                fadeOut(1.5f, Interpolation.pow5),
                panKitchenBg));
    }

    private void renderRecipeRef() {
        if (this.game.assets.isLoaded("recipe-ref.png")) {
            Texture recipeRefTexture = this.game.assets.get("recipe-ref.png", Texture.class);

            this.recipeRef = new Image(recipeRefTexture);
            this.recipeRef.setWidth(recipeRef.getWidth() / 4);
            this.recipeRef.setHeight(recipeRef.getHeight() / 4);
            this.recipeRef.setPosition((this.game.V_WIDTH - recipeRef.getWidth()) - 240,
                    (this.game.V_HEIGHT - recipeRef.getHeight()) - 160);
            this.recipeRef.addAction(sequence(alpha(0f), fadeIn(1.5f, Interpolation.pow5)));
            this.stage.addActor(recipeRef);

            this.recipeRef.addListener((EventListener) event -> {
                if (event.toString().equals("touchDown")) {
                    toggleRecipePaper();
                }

                return false;
            });
        }
    }

    private void toggleRecipePaper() {
        if (isRecipePaperShown) {
            isRecipePaperShown = false;

            this.recipeRef.addAction(
                    parallel(
                            moveTo(
                                    (this.game.V_WIDTH - recipeRef.getWidth()) - 240,
                                    (this.game.V_HEIGHT - recipeRef.getHeight()) - 160,
                                    2f,
                                    Interpolation.pow5),
                            scaleBy(-3f, -3f, 2f, Interpolation.pow5)));
        } else {
            isRecipePaperShown = true;

            this.recipeRef.setOrigin(Align.center);
            this.recipeRef.addAction(
                    parallel(
                            moveTo(
                                    (this.game.V_WIDTH / 2) - (recipeRef.getWidth() / 2),
                                    (this.game.V_HEIGHT / 2) - (recipeRef.getHeight() / 2),
                                    2f,
                                    Interpolation.pow5),
                            scaleBy(3f, 3f, 2f, Interpolation.pow5)));
        }
    }

    private void resetState() {
        this.kitchenBg.clear();
        this.kitchenBg.setPosition(-this.game.V_WIDTH, 0);
        this.kitchenBg.setScale(1f, 1f);
        this.recipeRef.setScale(1f, 1f);
        this.recipeRef.setPosition((this.game.V_WIDTH - recipeRef.getWidth()) - 240,
                (this.game.V_HEIGHT - recipeRef.getHeight()) - 160);
        this.recipeRef.addAction(fadeIn(1.5f, Interpolation.pow5));
        this.backBtn.addAction(fadeIn(1.5f, Interpolation.pow5));
    }
}
