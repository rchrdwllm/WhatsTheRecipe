package com.whatstherecipe.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class CustomSkin extends Skin {
    private FreeTypeFontGenerator lilitaOneGenerator;
    private FreeTypeFontGenerator denkOneGenerator;
    private BitmapFont text16, text24, text48;
    private BitmapFont heading16, heading24, heading48, heading208;

    public Color brown = Color.valueOf("#C18F4B");

    public CustomSkin() {
        this.lilitaOneGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/LilitaOne-Regular.ttf"));
        this.denkOneGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/DenkOne-Regular.ttf"));

        generateDenkOneFonts();
        generateLilitaOneFonts();
        generateTextButtonStyles();
    }

    private void generateDenkOneFonts() {
        FreeTypeFontParameter parameter16 = new FreeTypeFontParameter();
        LabelStyle labelStyle16 = new LabelStyle();
        parameter16.size = 16;
        parameter16.color = brown;
        this.text16 = denkOneGenerator.generateFont(parameter16);
        labelStyle16.font = text16;
        this.add("text-16", labelStyle16);

        FreeTypeFontParameter parameter24 = new FreeTypeFontParameter();
        LabelStyle labelStyle24 = new LabelStyle();
        parameter24.size = 24;
        parameter24.color = brown;
        this.text24 = denkOneGenerator.generateFont(parameter24);
        labelStyle24.font = text24;
        this.add("text-24", labelStyle24);

        FreeTypeFontParameter parameter48 = new FreeTypeFontParameter();
        LabelStyle labelStyle48 = new LabelStyle();
        parameter48.size = 48;
        parameter48.color = brown;
        this.text48 = denkOneGenerator.generateFont(parameter48);
        labelStyle48.font = text48;
        this.add("text-48", labelStyle48);
    }

    private void generateLilitaOneFonts() {
        FreeTypeFontParameter parameter16 = new FreeTypeFontParameter();
        LabelStyle labelStyle16 = new LabelStyle();
        parameter16.size = 16;
        parameter16.color = brown;
        this.heading16 = lilitaOneGenerator.generateFont(parameter16);
        labelStyle16.font = heading16;
        this.add("heading-16", labelStyle16);

        FreeTypeFontParameter parameter24 = new FreeTypeFontParameter();
        LabelStyle labelStyle24 = new LabelStyle();
        parameter24.size = 24;
        parameter24.color = brown;
        this.heading24 = lilitaOneGenerator.generateFont(parameter24);
        labelStyle24.font = heading24;
        this.add("heading-24", labelStyle24);

        FreeTypeFontParameter parameter48 = new FreeTypeFontParameter();
        LabelStyle labelStyle48 = new LabelStyle();
        parameter48.size = 48;
        parameter48.color = brown;
        this.heading48 = lilitaOneGenerator.generateFont(parameter48);
        labelStyle48.font = heading48;
        this.add("heading-48", labelStyle48);

        FreeTypeFontParameter parameter208 = new FreeTypeFontParameter();
        LabelStyle labelStyle208 = new LabelStyle();
        parameter208.size = 208;
        parameter208.color = brown;
        this.heading208 = lilitaOneGenerator.generateFont(parameter208);
        labelStyle208.font = heading208;
        this.add("heading-208", labelStyle208);
    }

    private void generateTextButtonStyles() {
        Drawable buttonPatch = new NinePatchDrawable(
                new NinePatch(new Texture(Gdx.files.internal("patches/text_button.9.png")), 32, 32, 0, 0));
        TextButtonStyle textButtonStyle = new TextButtonStyle();

        textButtonStyle.up = buttonPatch;
        textButtonStyle.font = heading48;

        this.add("text-button-default", textButtonStyle);
    }
}
