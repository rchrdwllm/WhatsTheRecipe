package com.whatstherecipe.game.components;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.whatstherecipe.game.WhatsTheRecipe;

public class InstructionsView {
    private final WhatsTheRecipe game;
    private Stage stage;
    private Image paper;
    private Image brownOverlay;
    private Table table;
    private boolean instructionsVisible = false;

    public InstructionsView(final WhatsTheRecipe game, Stage stage) {
        this.game = game;
        this.stage = stage;

        renderOverlay();
        initPaper();
        initTable();
        initBtns();
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
        this.game.sounds.bookSound.play();

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
            this.table.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
                this.table.remove();
            })));

            instructionsVisible = false;
        } else {
            this.stage.addActor(this.brownOverlay);
            this.paper.toFront();
            this.brownOverlay.addAction(fadeIn(0.5f, Interpolation.pow5));
            this.paper.addAction(sequence(
                    delay(0.5f),
                    moveTo((this.game.V_WIDTH / 2) - (paper.getWidth() / 2),
                            (this.game.V_HEIGHT / 2) - (paper.getHeight() / 2), 0.75f, Interpolation.swingOut)));
            this.table.addAction(fadeIn(0.5f, Interpolation.pow5));
            this.stage.addActor(this.table);
            this.table.toFront();

            instructionsVisible = true;
        }
    }

    private void initTable() {
        this.table = new Table();

        this.table.setFillParent(true);
        this.table.addAction(alpha(0));
    }

    private void initBtns() {
        TextButton backBtn = new TextButton("Back", this.game.skin.get("text-button-default", TextButtonStyle.class));

        backBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer,
                    int button) {
                toggleInstructions();
                return true;
            }
        });

        this.table.add(backBtn).expand().top().left().pad(48, 48, 0, 0);
    }
}
