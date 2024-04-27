package com.whatstherecipe.game.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.whatstherecipe.game.WhatsTheRecipe;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class InstructionsView {
    private final WhatsTheRecipe game;
    private Stage stage;
    private Image paper;
    private Image brownOverlay;
    private boolean instructionsVisible = false;

    public InstructionsView(final WhatsTheRecipe game, Stage stage) {
        this.game = game;
        this.stage = stage;

        renderOverlay();
        initPaper();
    }

    private void renderOverlay() {
        if (this.game.assets.isLoaded("wood-bg.png")) {
            this.brownOverlay = new Image(this.game.assets.get("wood-bg.png", Texture.class));
            this.brownOverlay.setFillParent(true);
            this.brownOverlay.addAction(alpha(0));

            this.brownOverlay.addListener((EventListener) event -> {
                if (event.toString().contains("touchDown")) {
                    toggleInstructions();
                }

                return false;
            });
        }
    }

    private void initPaper() {
        if (this.game.assets.isLoaded("how-to-play-book.png")) {
            Texture paperTexture = this.game.assets.get("how-to-play-book.png", Texture.class);

            this.paper = new Image(paperTexture);
            this.paper.setOrigin(Align.center);
            this.paper.setWidth((float) (paper.getWidth()));
            this.paper.setHeight((float) (paper.getHeight()));
            this.paper.setPosition((this.game.V_WIDTH / 2) - (paper.getWidth() / 2), -paper.getHeight());
            this.stage.addActor(this.paper);
        }
    }

    public void toggleInstructions() {
        this.game.sounds.paperSound.play();

        RunnableAction removeOverlay = new RunnableAction();

        removeOverlay.setRunnable(new Runnable() {
            @Override
            public void run() {
                brownOverlay.remove();
            }
        });

        if (instructionsVisible) {
            this.paper.addAction(
                    moveTo((this.game.V_WIDTH / 2) - (paper.getWidth() / 2),
                            -paper.getHeight(), 0.75f, Interpolation.swingIn));
            this.brownOverlay.addAction(sequence(delay(0.75f), fadeOut(0.5f, Interpolation.pow5), removeOverlay));

            instructionsVisible = false;
        } else {
            this.stage.addActor(this.brownOverlay);
            this.paper.toFront();
            this.brownOverlay.addAction(fadeIn(0.5f, Interpolation.pow5));
            this.paper.addAction(sequence(
                    delay(0.5f),
                    moveTo((this.game.V_WIDTH / 2) - (paper.getWidth() / 2),
                            (this.game.V_HEIGHT / 2) - (paper.getHeight() / 2), 0.75f, Interpolation.swingOut)));

            instructionsVisible = true;
        }
    }
}
