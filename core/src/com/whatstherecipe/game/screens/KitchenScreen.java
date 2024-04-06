package com.whatstherecipe.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.components.RecipePaperView;
import com.whatstherecipe.game.ui.Colors;

public class KitchenScreen implements Screen {
    private final WhatsTheRecipe game;
    private Stage stage;
    private Table tableRoot;
    private OrthographicCamera camera;
    private Image kitchenBg;
    private TextButton backBtn;
    private int screenShows = 0;
    private ArrayList<Image> cabinetTriggers;
    private ArrayList<Image> cabinetImgs;
    private RecipePaperView recipePaperView;

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

        this.recipePaperView = new RecipePaperView(game, stage);

        if (this.screenShows > 0) {
            resetState();
        } else {
            renderKitchenBg();
            renderButtons();
            prepareCabinetImgs();
            renderCabinetTriggers();
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
            this.kitchenBg.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    System.out.println("x: " + x + ", y: " + y);

                    return true;
                }
            });
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
        System.out.println("Going back");

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
                recipePaperView.recipeRef.addAction(sequence(parallel(
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

    private void renderCabinetTriggers() {
        this.cabinetTriggers = new ArrayList<Image>();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        TextButton closeCabinetBtn = new TextButton("Close",
                this.game.skin.get("text-button-default", TextButtonStyle.class));

        closeCabinetBtn.setPosition(100, this.game.V_HEIGHT - closeCabinetBtn.getHeight() - 100);

        pixmap.setColor(Colors.transparent);
        pixmap.fillRectangle(0, 0, 1, 1);

        Image cabinetTrigger1 = new Image(new Texture(pixmap));
        cabinetTrigger1.setSize(330, 398);
        cabinetTrigger1.setPosition(0, 114);
        cabinetTriggers.add(cabinetTrigger1);

        Image cabinetTrigger2 = new Image(new Texture(pixmap));
        cabinetTrigger2.setSize(316, 406);
        cabinetTrigger2.setPosition(1097, 105);
        cabinetTriggers.add(cabinetTrigger2);

        Image cabinetTrigger3 = new Image(new Texture(pixmap));
        cabinetTrigger3.setSize(325, 223);
        cabinetTrigger3.setPosition(1087, 856);
        cabinetTriggers.add(cabinetTrigger3);

        for (int i = 0; i < this.cabinetTriggers.size(); i++) {
            Image cabinetTrigger = this.cabinetTriggers.get(i);
            int index = i;

            this.stage.addActor(cabinetTrigger);

            cabinetTrigger.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Image cabinetImg = cabinetImgs.get(index);

                    closeCabinetBtn.clear();
                    stage.addActor(cabinetImg);
                    cabinetImg.toFront();
                    cabinetImg.addAction(fadeIn(0.5f));
                    cabinetTrigger.addAction(fadeIn(0.5f));
                    closeCabinetBtn.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            RunnableAction removeItems = new RunnableAction();

                            removeItems.setRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    cabinetImg.remove();
                                    closeCabinetBtn.clear();
                                    closeCabinetBtn.remove();
                                }
                            });

                            cabinetImg.addAction(fadeOut(0.5f));
                            closeCabinetBtn.addAction(sequence(fadeOut(0.5f), removeItems));

                            return true;
                        }
                    });
                    closeCabinetBtn.addAction(sequence(fadeIn(0.5f)));
                    stage.addActor(closeCabinetBtn);
                    recipePaperView.recipeRef.toFront();

                    return true;
                }
            });
        }
    }

    private void prepareCabinetImgs() {
        String[] fileNames = { "open-cabinet-1.jpg", "open-cabinet-2.jpg", "open-cabinet-3.jpg" };

        this.cabinetImgs = new ArrayList<Image>();

        for (String fileName : fileNames) {
            if (this.game.assets.isLoaded(fileName)) {
                Texture cabinetImgTexture = this.game.assets.get(fileName, Texture.class);

                Image cabinetImg = new Image(cabinetImgTexture);
                cabinetImg.setOrigin(Align.center);
                cabinetImg.setSize(this.game.V_WIDTH * 2, this.game.V_HEIGHT);
                cabinetImg.setPosition(-this.game.V_WIDTH, 0);
                cabinetImg.addAction(alpha(0f));
                this.cabinetImgs.add(cabinetImg);
            }
        }
    }

    private void resetState() {
        this.kitchenBg.clear();
        this.kitchenBg.setPosition(-this.game.V_WIDTH, 0);
        this.kitchenBg.setScale(1f, 1f);
        this.backBtn.addAction(fadeIn(1.5f, Interpolation.pow5));
    }
}
