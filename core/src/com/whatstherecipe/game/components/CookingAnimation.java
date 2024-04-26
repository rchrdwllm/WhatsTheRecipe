package com.whatstherecipe.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.ui.Colors;
import com.whatstherecipe.game.ui.CustomSkin;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class CookingAnimation {
    private Stage stage;
    private TextureAtlas cookingAtlas;
    private float FRAME_TIME = 0.166f;
    public Animation<TextureRegion> cookingAnimation;
    public AnimatedActor cookingActor;
    private Image brownOverlay;
    private Table table;

    public CookingAnimation(final WhatsTheRecipe game, Stage stage) {
        this.stage = stage;
        this.cookingAtlas = new TextureAtlas(Gdx.files.internal("cooking/cooking.atlas"));
        this.cookingAnimation = new Animation<TextureRegion>(FRAME_TIME, cookingAtlas.findRegions("cooking"),
                PlayMode.LOOP);
        this.cookingAnimation.setFrameDuration(FRAME_TIME);
        this.cookingAnimation.setPlayMode(Animation.PlayMode.LOOP);

        this.cookingActor = new AnimatedActor(cookingAnimation);

        this.cookingActor.setSize(300, 300);
        this.cookingActor.setPosition((game.V_WIDTH / 2) - (cookingActor.getWidth() / 2),
                (game.V_HEIGHT / 2) - (cookingActor.getHeight() / 2));

        renderTable();
        renderOverlay();
        renderAnimation();
        renderLabel();
    }

    private void renderTable() {
        this.table = new Table();
        this.table.setFillParent(true);
        this.table.addAction(alpha(0f));
    }

    private void renderOverlay() {
        Pixmap brownPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        brownPixmap.setColor(Colors.darkBrown);
        brownPixmap.fillRectangle(0, 0, 1, 1);

        this.brownOverlay = new Image(new Texture(brownPixmap));
        this.brownOverlay.setFillParent(true);
        this.brownOverlay.addAction(alpha(0));

        this.brownOverlay.addListener((EventListener) event -> {
            if (event.toString().contains("touchDown")) {
                toggleAnimation();
            }

            return false;
        });
    }

    private void renderAnimation() {
        this.table.add(this.cookingActor).center();
    }

    public void renderLabel() {
        Label label = new Label("Cooking meal...", CustomSkin.generateCustomLilitaOneFont(Colors.lightBrown, 48));

        label.setAlignment(Align.center);

        this.table.row();
        this.table.add(label).center().padTop(16);
    }

    public void toggleAnimation() {
        this.stage.addActor(this.brownOverlay);
        this.stage.addActor(this.table);

        this.brownOverlay.addAction(fadeIn(0.5f));
        this.table.addAction(sequence(fadeIn(0.5f), delay(2f), run(() -> {
            this.brownOverlay.addAction(fadeOut(0.5f, Interpolation.pow5));
            this.table.addAction(sequence(fadeOut(0.5f, Interpolation.pow5), run(() -> {
                this.table.remove();
                this.brownOverlay.remove();
            })));
        })));
    }
}
