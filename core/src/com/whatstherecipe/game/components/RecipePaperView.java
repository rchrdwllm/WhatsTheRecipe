package com.whatstherecipe.game.components;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.ui.Colors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class RecipePaperView {
    private final WhatsTheRecipe game;
    private Stage stage;
    private Image brownOverlay;
    public Image recipeRef;
    private boolean recipePaperVisible = false;

    public RecipePaperView(final WhatsTheRecipe game, Stage stage) {
        this.game = game;
        this.stage = stage;

        renderOverlay();
        initRecipePaper();
    }

    private void renderOverlay() {
        Pixmap brownPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        brownPixmap.setColor(Colors.brown);
        brownPixmap.fillRectangle(0, 0, 1, 1);

        this.brownOverlay = new Image(new Texture(brownPixmap));
        this.brownOverlay.setFillParent(true);
        this.brownOverlay.addAction(alpha(0));

        this.brownOverlay.addListener((EventListener) event -> {
            if (event.toString().contains("touchDown")) {
                toggleRecipePaper();
            }

            return false;
        });
    }

    private void initRecipePaper() {
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

    public void toggleRecipePaper() {
        RunnableAction removeOverlay = new RunnableAction();

        removeOverlay.setRunnable(new Runnable() {
            @Override
            public void run() {
                brownOverlay.remove();
            }
        });

        if (recipePaperVisible) {
            this.recipeRef.addAction(
                    parallel(
                            moveTo(
                                    (this.game.V_WIDTH - recipeRef.getWidth()) - 240,
                                    (this.game.V_HEIGHT - recipeRef.getHeight()) - 160,
                                    1f,
                                    Interpolation.pow5),
                            scaleBy(-3f, -3f, 0.75f, Interpolation.pow5)));
            this.brownOverlay.addAction(sequence(delay(0.75f), fadeOut(0.5f, Interpolation.pow5), removeOverlay));

            recipePaperVisible = false;
        } else {
            this.recipeRef.setOrigin(Align.center);
            this.stage.addActor(this.brownOverlay);
            this.recipeRef.toFront();
            this.brownOverlay.addAction(fadeIn(0.75f, Interpolation.pow5));
            this.recipeRef.addAction(
                    parallel(
                            moveTo(
                                    (this.game.V_WIDTH / 2) - (recipeRef.getWidth() / 2),
                                    (this.game.V_HEIGHT / 2) - (recipeRef.getHeight() / 2),
                                    1f,
                                    Interpolation.pow5),
                            scaleBy(3f, 3f, 0.75f, Interpolation.swingOut)));

            recipePaperVisible = true;
        }
    }
}
