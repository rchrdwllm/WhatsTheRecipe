package com.whatstherecipe.game.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AnimatedActor extends Actor {
    private Animation<TextureRegion> animation;
    private float stateTime;

    public AnimatedActor(Animation<TextureRegion> animation) {
        this.animation = animation;
        stateTime = 0f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(animation.getKeyFrame(stateTime, true), getX(), getY(), getOriginX(), getOriginY(), getWidth(),
                getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
