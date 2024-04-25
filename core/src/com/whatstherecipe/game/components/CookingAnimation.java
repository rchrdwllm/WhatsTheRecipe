package com.whatstherecipe.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.whatstherecipe.game.WhatsTheRecipe;
import com.whatstherecipe.game.ui.Colors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class CookingAnimation {
    private Stage stage;
    private TextureAtlas cookingAtlas;
    private float FRAME_TIME = 0.166f;
    public Animation<TextureRegion> cookingAnimation;
    public AnimatedActor cookingActor;
    private Image brownOverlay;
    private boolean isToggled = false;
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
    }

    private void renderTable() {
        this.table = new Table();
        this.table.setFillParent(true);
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

    public void toggleAnimation() {
        if (isToggled) {
            this.cookingActor.addAction(fadeOut(0.5f));
            this.brownOverlay.addAction(sequence(fadeOut(0.5f), run(() -> {
                this.cookingActor.remove();
                this.brownOverlay.remove();
                this.table.remove();
            })));

            isToggled = false;
        } else {
            this.stage.addActor(this.brownOverlay);
            this.stage.addActor(this.table);
            this.table.add(this.cookingActor).center();

            this.cookingActor.addAction(fadeIn(0.5f));
            this.brownOverlay.addAction(fadeIn(0.5f));

            isToggled = true;
        }
    }
}