package com.whatstherecipe.game.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;

public class Cursors {
    public Cursor spatulaCursor;
    public Cursor spatulaWhiskCursor;

    public Cursors() {
        initCursors();
    }

    private void initCursors() {
        int xHotspot = 15, yHotspot = 15;

        Pixmap spatulaPixmap = new Pixmap(Gdx.files.internal("cursors/spatula.png"));
        Cursor spatulaCursor = Gdx.graphics.newCursor(spatulaPixmap, xHotspot, yHotspot);

        this.spatulaCursor = spatulaCursor;
        spatulaPixmap.dispose();

        Pixmap spatulaWhiskPixmap = new Pixmap(Gdx.files.internal("cursors/spatula-whisk.png"));
        Cursor spatulaWhiskCursor = Gdx.graphics.newCursor(spatulaWhiskPixmap, xHotspot, yHotspot);

        this.spatulaWhiskCursor = spatulaWhiskCursor;
        spatulaWhiskPixmap.dispose();
    }
}
