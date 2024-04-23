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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.classes.Ingredients;
import com.whatstherecipe.game.classes.Meal;
import com.whatstherecipe.game.components.Ingredient;
import com.whatstherecipe.game.components.MealBanner;
import com.whatstherecipe.game.components.RecipePaperView;
import com.whatstherecipe.game.ui.Colors;

public class KitchenScreen implements Screen {
    public final WhatsTheRecipe game;
    public Stage stage;
    private Table tableRoot;
    private OrthographicCamera camera;
    private Image kitchenBg;
    private TextButton quitBtn;
    private int screenShows = 0;
    private ArrayList<Image> cabinetTriggers;
    private ArrayList<Image> cabinetImgs;
    private RecipePaperView recipePaperView;
    private ArrayList<String> ingredientsWithRandom;
    private ArrayList<ArrayList<Ingredient>> ingredients;
    public ArrayList<Meal> mealPlan;
    public Meal meal;
    private int maxRoundCount = 5;
    public int roundCount = 1;
    public boolean isEndGame = false;
    public String phase = "ingredient-selection";

    public KitchenScreen(final WhatsTheRecipe game, ArrayList<Meal> mealPlan) {
        this.game = game;
        this.stage = new Stage(new StretchViewport(game.V_WIDTH, game.V_HEIGHT));
        this.camera = game.camera;
        this.mealPlan = mealPlan;
        this.meal = mealPlan.get(0);
        this.ingredientsWithRandom = new ArrayList<String>();
        this.ingredients = new ArrayList<ArrayList<Ingredient>>();

        System.out.println("Meal plan for this game:");

        mealPlan.forEach(meal -> {
            System.out.println(meal.name + " - " + meal.difficulty);
        });

        System.out.println("\nRound: " + roundCount);
        System.out.println("Meal: " + meal.name);
        System.out.println("Difficulty: " + meal.difficulty + "\n");

        randomizeIngredients();
        initComponents();
    }

    public KitchenScreen(final WhatsTheRecipe game, ArrayList<Meal> mealPlan, int roundCount) {
        this.game = game;
        this.stage = new Stage(new StretchViewport(game.V_WIDTH, game.V_HEIGHT));
        this.camera = game.camera;
        this.mealPlan = mealPlan;
        this.meal = mealPlan.get(roundCount - 1);
        this.roundCount = roundCount;
        this.ingredientsWithRandom = new ArrayList<String>();
        this.ingredients = new ArrayList<ArrayList<Ingredient>>();

        System.out.println("\nRound: " + roundCount);
        System.out.println("Meal: " + meal.name);
        System.out.println("Difficulty: " + meal.difficulty + "\n");

        randomizeIngredients();
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

        this.recipePaperView = new RecipePaperView(this);

        new MealBanner(this, stage, meal, roundCount);

        if (this.screenShows > 0) {
            resetState();
        } else {
            renderKitchenBg();
            renderButtons();
            prepareIngredients();
            positionIngredients();
            prepareCabinetImgs();
            renderCabinetTriggers();
        }

        this.screenShows += 1;
    }

    private void randomizeIngredients() {
        this.ingredientsWithRandom.ensureCapacity(8);

        for (int i = 0; i < 8; i++) {
            if (i > this.meal.ingredients.size() - 1) {
                this.ingredientsWithRandom.add(Ingredients.getRandomIngredient(ingredientsWithRandom));
            } else {
                this.ingredientsWithRandom.add(this.meal.ingredients.get(i));
            }
        }
    }

    private void initComponents() {
        this.tableRoot = new Table();

        this.tableRoot.setFillParent(true);
        this.stage.addActor(tableRoot);
        // this.stage.addListener(new InputListener() {
        // @Override
        // public boolean touchDown(InputEvent event, float x, float y, int pointer, int
        // button) {
        // System.out.print("x: " + x + ", y: " + y + "\n");

        // return true;
        // }
        // });
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
        this.quitBtn = new TextButton("Quit", this.game.skin.get("text-button-default", TextButtonStyle.class));
        this.tableRoot.add(this.quitBtn).expandY().top().expandX().left().pad(100, 100, 0, 0);

        this.quitBtn.addAction(sequence(alpha(0f), fadeIn(1.5f, Interpolation.pow5)));
        this.quitBtn.addListener(
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

        this.quitBtn.addAction(sequence(
                fadeOut(1.5f, Interpolation.pow5),
                panKitchenBg));
    }

    private void renderCabinetTriggers() {
        this.cabinetTriggers = new ArrayList<Image>();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        TextButton closeCabinetBtn = new TextButton("Close the cabinet",
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

        Image cabinetTrigger4 = new Image(new Texture(pixmap));
        cabinetTrigger4.setSize(394, 332);
        cabinetTrigger4.setPosition(1436, 108);
        cabinetTriggers.add(cabinetTrigger4);

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
                            ingredients.get(index).forEach(ingredient -> {
                                ingredient.toggleIngredient();
                            });

                            return true;
                        }
                    });
                    closeCabinetBtn.addAction(sequence(fadeIn(0.5f)));
                    closeCabinetBtn.toFront();
                    stage.addActor(closeCabinetBtn);
                    recipePaperView.recipeRef.toFront();
                    ingredients.get(index).forEach(ingredient -> {
                        ingredient.toggleIngredient();
                    });

                    return true;
                }
            });
        }
    }

    private void prepareCabinetImgs() {
        String[] fileNames = { "open-cabinet-1.jpg", "open-cabinet-2.jpg", "open-cabinet-3.jpg", "open-cabinet-4.jpg" };

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

    private void prepareIngredients() {
        for (int i = 0; i < Ingredients.ingredientsList.length; i++) {
            this.ingredients.add(new ArrayList<Ingredient>());
            int cabinetIndex = i;

            for (int j = 0; j < Ingredients.ingredientsList[i].length; j++) {
                Ingredient ingredient = new Ingredient(this.game, this.stage, Ingredients.ingredientsList[i][j]);

                ingredient.cabinetIndex = cabinetIndex;

                this.ingredientsWithRandom.forEach(ingredientName -> {
                    if (ingredientName.equals(ingredient.name)) {
                        this.ingredients.get(cabinetIndex)
                                .add(ingredient);
                    }
                });
            }
        }
    }

    private void positionIngredients() {
        try {
            this.ingredients.get(0).get(0).ingredient.setPosition(100, 362);
            this.ingredients.get(0).get(1).ingredient.setPosition(222, 362);
            this.ingredients.get(0).get(2).ingredient.setPosition(100, 174);
            this.ingredients.get(0).get(3).ingredient.setPosition(222, 174);
        } catch (IndexOutOfBoundsException e) {
        }

        try {
            this.ingredients.get(1).get(0).ingredient.setPosition(1196, 352);
            this.ingredients.get(1).get(1).ingredient.setPosition(1300, 352);
            this.ingredients.get(1).get(2).ingredient.setPosition(1196, 156);
            this.ingredients.get(1).get(3).ingredient.setPosition(1300, 156);
        } catch (IndexOutOfBoundsException e) {
        }

        try {
            this.ingredients.get(2).get(0).ingredient.setPosition(1148, 888);
            this.ingredients.get(2).get(1).ingredient.setPosition(1240, 888);
            this.ingredients.get(2).get(2).ingredient.setPosition(1203, 888);
        } catch (IndexOutOfBoundsException e) {
        }

        try {
            this.ingredients.get(3).get(0).ingredient.setPosition(1510, 320);
            this.ingredients.get(3).get(1).ingredient.setPosition(1634, 320);
            this.ingredients.get(3).get(2).ingredient.setPosition(1732, 320);
            this.ingredients.get(3).get(3).ingredient.setPosition(1576, 190);
            this.ingredients.get(3).get(4).ingredient.setPosition(1692, 190);
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void nextRound() {
        if (roundCount == this.maxRoundCount) {
            this.isEndGame = true;

            System.out.println("End game");

            return;
        }

        this.roundCount += 1;

        game.setScreen(new KitchenScreen(game,
                mealPlan,
                roundCount));
        this.dispose();
    }

    private void resetState() {
        this.kitchenBg.clear();
        this.kitchenBg.setPosition(-this.game.V_WIDTH, 0);
        this.kitchenBg.setScale(1f, 1f);
        this.quitBtn.addAction(fadeIn(1.5f, Interpolation.pow5));
    }
}
