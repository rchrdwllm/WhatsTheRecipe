package com.whatstherecipe.game.components;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.ui.Colors;
import com.whatstherecipe.game.ui.CustomSkin;
import java.lang.Math;

public class Scoring {
    private final WhatsTheRecipe game;
    private Stage stage;
    private Image overlay;
    private Label scoreLabel;
    private Group scoreGroup;
    private int origScore;
    private int currentScore;
    private int scoreToAdd;
    private int step;

    public Scoring(WhatsTheRecipe game, Stage stage, int currentScore, int scoreToAdd, int step) {
        this.game = game;
        this.stage = stage;
        this.origScore = currentScore;
        this.currentScore = currentScore;
        this.scoreToAdd = scoreToAdd;
        this.step = step;

        initOverlay();
        initLabels();
    }

    private void initOverlay() {
        Pixmap brownPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        brownPixmap.setColor(Colors.brownOverlayOpacity);
        brownPixmap.fillRectangle(0, 0, 1, 1);

        this.overlay = new Image(new Texture(brownPixmap));
        this.overlay.setFillParent(true);
        this.overlay.addAction(alpha(0));
    }

    private void initLabels() {
        if (this.scoreToAdd > 0) {
            this.scoreLabel = new Label(currentScore + " pts",
                    CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 128));
        } else {
            this.scoreLabel = new Label(currentScore + " pts",
                    CustomSkin.generateCustomLilitaOneFont(Colors.red, 128));
        }

        this.scoreLabel.setAlignment(Align.center);

        this.scoreGroup = new Group();
        this.scoreGroup.addActor(this.scoreLabel);
        this.scoreGroup.setSize(scoreLabel.getWidth(), scoreLabel.getHeight());
        this.scoreGroup.setPosition((stage.getWidth() / 2) - (scoreGroup.getWidth() / 2),
                (stage.getHeight() / 2) - (scoreGroup.getHeight() / 2));
        this.scoreGroup.setOrigin(Align.center);
        this.scoreGroup.addAction(alpha(0));
        this.scoreGroup.addAction(parallel(alpha(0), scaleTo(0.2f, 0.2f)));
    }

    public void show(Runnable runnable) {
        RunnableAction runnableAction = new RunnableAction();

        runnableAction.setRunnable(runnable);

        this.stage.addActor(overlay);
        this.stage.addActor(scoreGroup);

        int limit = origScore + scoreToAdd;

        Action scoreAction = sequence(run(() -> {
            if (scoreToAdd > 0) {
                if (currentScore + step < limit) {
                    currentScore += step;
                    scoreLabel.setText(currentScore + " pts");
                } else {
                    scoreLabel.setText(limit + " pts");
                }
            } else if (scoreToAdd < 0) {
                if (currentScore - step > limit) {
                    currentScore -= step;
                    scoreLabel.setText(currentScore + " pts");
                } else {
                    scoreLabel.setText(limit + " pts");
                }
            }

            this.game.sounds.popSound.play();
        }), delay(0.00001f));

        int repeats = 0;

        if (Math.abs(scoreToAdd) % step == 0) {
            repeats = Math.abs(scoreToAdd) / step;
        } else {
            repeats = Math.abs(scoreToAdd) / step + 1;
        }

        overlay.addAction(fadeIn(0.5f, Interpolation.pow5));
        scoreGroup.addAction(sequence(parallel(scaleTo(1, 1, 0.5f, Interpolation.swingOut),
                fadeIn(0.5f, Interpolation.pow5)), repeat(repeats, scoreAction), delay(0.5f),
                run(() -> {
                    scoreToAdd = 0;
                    hide();

                    runnableAction.run();
                })));
    }

    public void hide() {
        this.overlay.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
            this.overlay.remove();
        })));
        this.scoreGroup.addAction(sequence(parallel(scaleTo(0.2f, 0.2f, 0.5f, Interpolation.swingIn),
                fadeOut(0.5f, Interpolation.pow5)), run(() -> {
                    this.scoreGroup.remove();
                })));
    }
}
