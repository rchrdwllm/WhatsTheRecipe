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
    private BitmapFont heading16, heading24, heading48, heading208;

    public CustomSkin() {
        this.lilitaOneGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/LilitaOne-Regular.ttf"));

        generateLilitaOneFonts();
        generateTextButtonStyles();
        generateTextButtonAltStyles();
    }

    private void generateLilitaOneFonts() {
        FreeTypeFontParameter parameter16 = new FreeTypeFontParameter();
        LabelStyle labelStyle16 = new LabelStyle();
        parameter16.size = 16;
        parameter16.color = Colors.brown;
        this.heading16 = lilitaOneGenerator.generateFont(parameter16);
        labelStyle16.font = heading16;
        this.add("heading-16", labelStyle16);

        FreeTypeFontParameter parameter24 = new FreeTypeFontParameter();
        LabelStyle labelStyle24 = new LabelStyle();
        parameter24.size = 24;
        parameter24.color = Colors.brown;
        this.heading24 = lilitaOneGenerator.generateFont(parameter24);
        labelStyle24.font = heading24;
        this.add("heading-24", labelStyle24);

        FreeTypeFontParameter parameter48 = new FreeTypeFontParameter();
        LabelStyle labelStyle48 = new LabelStyle();
        parameter48.size = 48;
        parameter48.color = Colors.brown;
        this.heading48 = lilitaOneGenerator.generateFont(parameter48);
        labelStyle48.font = heading48;
        this.add("heading-48", labelStyle48);

        FreeTypeFontParameter parameter208 = new FreeTypeFontParameter();
        LabelStyle labelStyle208 = new LabelStyle();
        parameter208.size = 208;
        parameter208.color = Colors.brown;
        this.heading208 = lilitaOneGenerator.generateFont(parameter208);
        labelStyle208.font = heading208;
        this.add("heading-208", labelStyle208);
    }

    private void generateTextButtonStyles() {
        FreeTypeFontParameter params = new FreeTypeFontParameter();
        Drawable buttonPatch = new NinePatchDrawable(
                new NinePatch(new Texture(Gdx.files.internal("patches/text_button.9.png")), 32, 32, 0,
                        0));
        Drawable buttonDown = new NinePatchDrawable(
                new NinePatch(new Texture(Gdx.files.internal("patches/text_button_down.9.png")), 32, 32,
                        0, 0));
        Drawable buttonOver = new NinePatchDrawable(
                new NinePatch(new Texture(Gdx.files.internal("patches/text_button_over.9.png")), 32, 32,
                        0, 0));
        TextButtonStyle textButtonStyle = new TextButtonStyle();

        params.size = 48;

        BitmapFont font = lilitaOneGenerator.generateFont(params);

        textButtonStyle.up = buttonPatch;
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Colors.brown;
        textButtonStyle.down = buttonDown;
        textButtonStyle.downFontColor = Colors.lightBrown;
        textButtonStyle.over = buttonOver;
        textButtonStyle.overFontColor = Colors.lightBrown;

        this.add("text-button-default", textButtonStyle);
    }

    private void generateTextButtonAltStyles() {
        FreeTypeFontParameter params = new FreeTypeFontParameter();
        Drawable buttonPatch = new NinePatchDrawable(
                new NinePatch(new Texture(Gdx.files.internal("patches/text_button_alt.9.png")), 32, 32,
                        0, 0));
        Drawable buttonDown = new NinePatchDrawable(
                new NinePatch(new Texture(Gdx.files.internal("patches/text_button_down_alt.9.png")), 32,
                        32, 0, 0));
        Drawable buttonOver = new NinePatchDrawable(
                new NinePatch(new Texture(Gdx.files.internal("patches/text_button_over_alt.9.png")), 32,
                        32, 0, 0));
        TextButtonStyle textButtonStyle = new TextButtonStyle();

        params.size = 48;

        BitmapFont font = lilitaOneGenerator.generateFont(params);

        textButtonStyle.up = buttonPatch;
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Colors.lightBrown;
        textButtonStyle.down = buttonDown;
        textButtonStyle.downFontColor = Colors.lightBrown;
        textButtonStyle.over = buttonOver;
        textButtonStyle.overFontColor = Colors.lightBrown;

        this.add("text-button-alt", textButtonStyle);
    }

    public static LabelStyle generateCustomLilitaOneFont(Color color, int size) {
        FreeTypeFontGenerator lilitaOneGenerator = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/LilitaOne-Regular.ttf"));
        FreeTypeFontParameter customParameter = new FreeTypeFontParameter();
        LabelStyle customLabelStyle = new LabelStyle();

        customParameter.size = size;
        customParameter.color = color;

        BitmapFont customFont = lilitaOneGenerator.generateFont(customParameter);
        customLabelStyle.font = customFont;

        return customLabelStyle;
    }

    public static LabelStyle generateCustomLilitaOneBackground(Color color, int size) {
        FreeTypeFontGenerator lilitaOneGenerator = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/LilitaOne-Regular.ttf"));
        FreeTypeFontParameter customParameter = new FreeTypeFontParameter();
        LabelStyle customLabelStyle = new LabelStyle();
        Drawable labelPatch = new NinePatchDrawable(
                new NinePatch(new Texture(Gdx.files.internal("patches/label.9.png")), 32, 32, 0, 0));

        customParameter.size = size;
        customParameter.color = color;

        BitmapFont customFont = lilitaOneGenerator.generateFont(customParameter);
        customLabelStyle.font = customFont;
        customLabelStyle.background = labelPatch;

        return customLabelStyle;
    }
}
