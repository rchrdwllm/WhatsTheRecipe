package com.whatstherecipe.game.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Sounds {
    public Sound clickSound;
    public Sound cookingSound;
    public Sound paperSound;
    public Sound paper2Sound;
    public Sound openCabinetSound;
    public Sound closeCabinetSound;
    public Sound dropSound;
    public Sound bookSound;
    public Sound successSound;
    public Sound failSound;
    public Sound popSound;
    public Sound finishSound;

    public Sounds() {
        initSounds();
    }

    private void initSounds() {
        this.clickSound = Gdx.audio.newSound(Gdx.files.internal("audio/click.mp3"));
        this.cookingSound = Gdx.audio.newSound(Gdx.files.internal("audio/cooking.mp3"));
        this.paperSound = Gdx.audio.newSound(Gdx.files.internal("audio/paper.mp3"));
        this.paper2Sound = Gdx.audio.newSound(Gdx.files.internal("audio/paper2.mp3"));
        this.openCabinetSound = Gdx.audio.newSound(Gdx.files.internal("audio/open-cabinet.mp3"));
        this.closeCabinetSound = Gdx.audio.newSound(Gdx.files.internal("audio/close-cabinet.mp3"));
        this.dropSound = Gdx.audio.newSound(Gdx.files.internal("audio/drop.mp3"));
        this.bookSound = Gdx.audio.newSound(Gdx.files.internal("audio/book.mp3"));
        this.successSound = Gdx.audio.newSound(Gdx.files.internal("audio/success.mp3"));
        this.failSound = Gdx.audio.newSound(Gdx.files.internal("audio/fail.mp3"));
        this.popSound = Gdx.audio.newSound(Gdx.files.internal("audio/pop.mp3"));
        this.finishSound = Gdx.audio.newSound(Gdx.files.internal("audio/finish-2.mp3"));
    }
}
