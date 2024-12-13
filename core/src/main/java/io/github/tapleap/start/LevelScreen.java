package io.github.tapleap.start;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.tapleap.Main;
import io.github.tapleap.game.GameScreen;

public class LevelScreen implements Screen {
    private Main game;
    private OrthographicCamera camera;
    private FitViewport viewport;

    // Poziomy gry
    private static final int LEVEL_1_WIDTH = 2000;
    private static final int LEVEL_2_WIDTH = 4000;
    private static final int LEVEL_3_WIDTH = 6000;

    private static final int[][] LEVEL_1_OBSTACLES = {{400, 300, 100, 100}, {800, 500, 100, 100}, {1200, 700, 100, 100}};
    private static final int[][] LEVEL_2_OBSTACLES = {{500, 200, 150, 150}, {1000, 400, 150, 150}, {2000, 600, 150, 150}, {3000, 800, 150, 150}};
    private static final int[][] LEVEL_3_OBSTACLES = {{600, 300, 200, 200}, {1200, 500, 200, 200}, {2400, 700, 200, 200}, {3600, 400, 200, 200}, {5000, 600, 200, 200}};

    private static final float LEVEL_1_SPEED = 200f;
    private static final float LEVEL_2_SPEED = 300f;
    private static final float LEVEL_3_SPEED = 400f;

    private Texture level1ButtonTexture;
    private Texture level2ButtonTexture;
    private Texture level3ButtonTexture;

    private Rectangle level1ButtonBounds;
    private Rectangle level2ButtonBounds;
    private Rectangle level3ButtonBounds;

    public LevelScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();

        // Wczytanie tekstur przycisków
        level1ButtonTexture = new Texture(Gdx.files.internal("triangle.png"));
        level2ButtonTexture = new Texture(Gdx.files.internal("triangle.png"));
        level3ButtonTexture = new Texture(Gdx.files.internal("triangle.png"));

        // Definicja prostokątów dla przycisków
        level1ButtonBounds = new Rectangle(300, 400, 200, 50);
        level2ButtonBounds = new Rectangle(300, 300, 200, 50);
        level3ButtonBounds = new Rectangle(300, 200, 200, 50);
    }

    @Override
    public void show() {
        Gdx.app.log("LevelScreen", "Level selection screen is shown");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // Rysowanie przycisków
        game.batch.draw(level1ButtonTexture, level1ButtonBounds.x, level1ButtonBounds.y, level1ButtonBounds.width, level1ButtonBounds.height);
        game.batch.draw(level2ButtonTexture, level2ButtonBounds.x, level2ButtonBounds.y, level2ButtonBounds.width, level2ButtonBounds.height);
        game.batch.draw(level3ButtonTexture, level3ButtonBounds.x, level3ButtonBounds.y, level3ButtonBounds.width, level3ButtonBounds.height);

        game.batch.end();

        // Obsługa kliknięć w przyciski
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (level1ButtonBounds.contains(touchX, touchY)) {
                startGame(LEVEL_1_WIDTH, LEVEL_1_OBSTACLES, LEVEL_1_SPEED);
            } else if (level2ButtonBounds.contains(touchX, touchY)) {
                startGame(LEVEL_2_WIDTH, LEVEL_2_OBSTACLES, LEVEL_2_SPEED);
            } else if (level3ButtonBounds.contains(touchX, touchY)) {
                startGame(LEVEL_3_WIDTH, LEVEL_3_OBSTACLES, LEVEL_3_SPEED);
            }
        }
    }

    private void startGame(int worldWidth, int[][] obstacles, float playerSpeed) {
        Gdx.app.log("LevelScreen", "Starting game with worldWidth: " + worldWidth + ", obstacles: " + obstacles.length + ", speed: " + playerSpeed);
        game.setScreen(new GameScreen(game, worldWidth, obstacles, playerSpeed));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        level1ButtonTexture.dispose();
        level2ButtonTexture.dispose();
        level3ButtonTexture.dispose();
    }
}
