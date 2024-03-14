package com.whatstherecipe.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.whatstherecipe.game.screens.KitchenScreen;
import com.whatstherecipe.game.screens.LoadingScreen;
import com.whatstherecipe.game.screens.MainMenuScreen;
import com.whatstherecipe.game.ui.CustomSkin;

public class WhatsTheRecipe extends Game {
	public int V_WIDTH = 1920;
	public int V_HEIGHT = 1080;

	public SpriteBatch batch;
	public OrthographicCamera camera;
	public AssetManager assets;
	public LoadingScreen loadingScreen;
	public MainMenuScreen mainMenuScreen;
	public KitchenScreen kitchenScreen;
	public CustomSkin skin;

	@Override
	public void create() {
		this.batch = new SpriteBatch();
		this.camera = new OrthographicCamera();
		this.assets = new AssetManager();
		this.skin = new CustomSkin();
		this.loadingScreen = new LoadingScreen(this);
		this.mainMenuScreen = new MainMenuScreen(this);
		this.kitchenScreen = new KitchenScreen(this);

		queueLoadingAssets();

		this.camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
		this.setScreen(this.loadingScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		this.batch.dispose();
		this.assets.dispose();
		this.loadingScreen.dispose();
		this.mainMenuScreen.dispose();
		this.kitchenScreen.dispose();
		this.skin.dispose();
	}

	private void queueLoadingAssets() {
		this.assets.load("badlogic.jpg", Texture.class);
		this.assets.load("logo.png", Texture.class);
		this.assets.load("kitchen.jpg", Texture.class);
		this.assets.finishLoading();
	}
}
