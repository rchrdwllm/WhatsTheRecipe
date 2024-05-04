package com.whatstherecipe.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.whatstherecipe.game.classes.Cursors;
import com.whatstherecipe.game.classes.Ingredients;
import com.whatstherecipe.game.classes.Meals;
import com.whatstherecipe.game.classes.Sounds;
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
	public Meals meals;
	public CustomSkin skin;
	public Sounds sounds;
	public Cursors cursors;
	public int currentHighScore = 0;

	@Override
	public void create() {
		this.batch = new SpriteBatch();
		this.camera = new OrthographicCamera();
		this.assets = new AssetManager();
		this.skin = new CustomSkin();
		this.loadingScreen = new LoadingScreen(this);
		this.mainMenuScreen = new MainMenuScreen(this);
		this.meals = new Meals();
		this.sounds = new Sounds();
		this.cursors = new Cursors();

		queueLoadingAssets();
		setupCursor();

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
		this.skin.dispose();
	}

	private void queueLoadingAssets() {
		this.assets.load("logo.png", Texture.class);
		this.assets.load("kitchen.jpg", Texture.class);
		this.assets.load("main-menu-screen-text.png", Texture.class);
		this.assets.load("recipe-ref.png", Texture.class);
		this.assets.load("paper.png", Texture.class);
		this.assets.load("open-cabinet-1.jpg", Texture.class);
		this.assets.load("open-cabinet-2.jpg", Texture.class);
		this.assets.load("open-cabinet-3.jpg", Texture.class);
		this.assets.load("open-cabinet-4.jpg", Texture.class);
		this.assets.load("open-cabinet-4.jpg", Texture.class);
		this.assets.load("basket.png", Texture.class);
		this.assets.load("how-to-play-book.png", Texture.class);
		this.assets.load("popup-bg.png", Texture.class);
		this.assets.load("wood-bg.png", Texture.class);
		this.assets.load("abaca-bg.png", Texture.class);
		this.assets.load("meals/adobo.png", Texture.class);
		this.assets.load("meals/pancit-palabok.png", Texture.class);
		this.assets.load("meals/papaitan.png", Texture.class);
		this.assets.load("meals/sinigang.png", Texture.class);
		this.assets.load("meals/sisig.png", Texture.class);
		this.assets.load("audio/background.mp3", Music.class);

		for (int i = 0; i < Ingredients.ingredientsList.length; i++) {
			for (int j = 0; j < Ingredients.ingredientsList[i].length; j++) {
				this.assets.load("ingredients/" + Ingredients.ingredientsList[i][j] + ".png", Texture.class);
			}
		}

		for (int i = 0; i < Ingredients.ingredientsList.length; i++) {
			for (int j = 0; j < Ingredients.ingredientsList[i].length; j++) {
				this.assets.load("ingredients/basket-view/" + Ingredients.ingredientsList[i][j] + "-basket.png",
						Texture.class);
			}
		}

		this.assets.finishLoading();
	}

	private void setupCursor() {
		Gdx.graphics.setCursor(this.cursors.spatulaCursor);
	}
}
