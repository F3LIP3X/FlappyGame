package com.mygdx.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	int flapState = 0;
	float birdY = 0;
	float velocity = 0;
	int gameState = 0;
	Texture topTube, bottomTube;
	float gap = 500;
	Random randomGenerator;
	int totalTubes = 20;
	float[] tubeX = new float[totalTubes];
	float[] tubeOffset = new float[totalTubes];
	float distanceBetweenTubes;
	Circle birdCircle;
	ShapeRenderer shapeRenderer;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;
	Texture gameOver;

	@Override
	public void create() {
		batch = new SpriteBatch();

		// Bird
		birds = new Texture[2];
		birds[0] = new Texture("flappybirdup.png");
		birds[1] = new Texture("flappybirddown.png");

		// Pipes
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");

		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

		// Background
		background = new Texture("background.png");

		// Random
		randomGenerator = new Random();

		// Tubes
		distanceBetweenTubes = Gdx.graphics.getWidth() / 1;

		// Circle
		birdCircle = new Circle();
		shapeRenderer = new ShapeRenderer();

		// Rectangles
		topTubeRectangles = new Rectangle[totalTubes];
		bottomTubeRectangles = new Rectangle[totalTubes];

		// Game Over
		gameOver = new Texture("gameover.png");

		startGame();
	}

	private void startGame() {
		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

		for (int i = 0; i < totalTubes; i++) {
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 950);
			tubeX[i] = Gdx.graphics.getWidth() + i * distanceBetweenTubes;

			topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
			bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
		}
	}

	@Override
	public void render() {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Circle
		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getHeight() / 2);

		for (int i = 0; i < totalTubes; i++) {
			if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
				gameState = 2;
			}
		}

		// gameState
		if (gameState == 1) {
			if (Gdx.input.justTouched()) {
				velocity = -30;
			}
			for (int i = 0; i < totalTubes; i++) {
				if (tubeX[i] < -topTube.getWidth()) {
					tubeX[i] += totalTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap);
				} else {
					tubeX[i] -= 4 * 2;
				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangles[i].set(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i].set(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			}

			if (birdY > 0) {
				velocity += 2;
				birdY -= velocity;
			} else {
				gameState = 2;
			}
		} else if (gameState == 0) {
			if (Gdx.input.justTouched()) {
				gameState = 1;
				startGame();
			}
		} else if (gameState == 2) {
			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2);
			if (Gdx.input.justTouched()) {
				gameState = 1;
				velocity = 1;
				startGame();
			}
		}

		// Flap
		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}
		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY + 1);

		batch.end();
	}

	@Override
	public void dispose() {

	}
}

