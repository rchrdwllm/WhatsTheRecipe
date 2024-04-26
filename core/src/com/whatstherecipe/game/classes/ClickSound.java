package com.whatstherecipe.game.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class ClickSound {
    private Sound clickSound;

    public ClickSound() {
        this.clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/click.mp3"));
    }

    public void play() {
        this.clickSound.play();
    }

    public void dispose() {
        this.clickSound.dispose();
    }
}
