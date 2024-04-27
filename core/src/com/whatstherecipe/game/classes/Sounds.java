package com.whatstherecipe.game.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Sounds {
    public Sound clickSound;
    public Sound cookingSound;
    public Sound paperSound;
    public Sound openCabinetSound;
    public Sound closeCabinetSound;

    public Sounds() {
        initSounds();
    }

    private void initSounds() {
        this.clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/click.mp3"));
        this.cookingSound = Gdx.audio.newSound(Gdx.files.internal("audio/cooking.mp3"));
        this.paperSound = Gdx.audio.newSound(Gdx.files.internal("audio/paper.mp3"));
        this.openCabinetSound = Gdx.audio.newSound(Gdx.files.internal("audio/open-cabinet.mp3"));
        this.closeCabinetSound = Gdx.audio.newSound(Gdx.files.internal("audio/close-cabinet.mp3"));
    }
}
