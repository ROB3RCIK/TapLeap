package io.github.tapleap.start;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Objects;

import io.github.tapleap.Main;
import io.github.tapleap.game.GameScreen;

public class LevelScreen implements Screen {
    private Main game;
    private String lang;
    private OrthographicCamera camera;
    private FitViewport viewport;

    // Stałe wymiary (takie same jak w StartScreen)
    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 600;
    private static final float BUTTON_WIDTH = 200;
    private static final float BUTTON_HEIGHT = 50;
    private static final float BUTTON_SPACING = 20;

    // Parametry poziomów
    private static final int LEVEL_1_WIDTH = 6000;
    private static final int LEVEL_2_WIDTH = 6000;
    private static final int LEVEL_3_WIDTH = 6000;
    private static final int[][] LEVEL_1_OBSTACLES = {{600, 300, 200, 200}, {1200, 500, 200, 200}, {2400, 700, 200, 200}, {3600, 400, 200, 200}, {5000, 600, 200, 200}};
    private static final int[][] LEVEL_2_OBSTACLES = {{600, 300, 200, 200}, {1200, 500, 200, 200}, {2400, 700, 200, 200}, {3600, 400, 200, 200}, {5000, 600, 200, 200}};
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

    public LevelScreen(Main game, String lang) {
        this.game = game;
        this.lang = lang;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);

        // Wczytanie tekstur
        if(Objects.equals(lang, "PL")) {
            level1ButtonTexture = new Texture(Gdx.files.internal("level1_pl.png"));
            level2ButtonTexture = new Texture(Gdx.files.internal("level2_pl.png"));
            level3ButtonTexture = new Texture(Gdx.files.internal("level3_pl.png"));
        } else {
            level1ButtonTexture = new Texture(Gdx.files.internal("level1.png"));
            level2ButtonTexture = new Texture(Gdx.files.internal("level2.png"));
            level3ButtonTexture = new Texture(Gdx.files.internal("level3.png"));
        }


        initializeButtons();
    }

    private void initializeButtons() {
        float centerX = (WORLD_WIDTH / 2f) - 10; // Tak samo jak w StartScreen
        float centerY = WORLD_HEIGHT / 2f;

        float totalHeight = (BUTTON_HEIGHT * 3) + (BUTTON_SPACING * 2);
        float topButtonY = centerY + (totalHeight / 2);

        level1ButtonBounds = new Rectangle(
            centerX - (BUTTON_WIDTH / 2),
            topButtonY - BUTTON_HEIGHT,
            BUTTON_WIDTH,
            BUTTON_HEIGHT
        );

        level2ButtonBounds = new Rectangle(
            centerX - (BUTTON_WIDTH / 2),
            topButtonY - (BUTTON_HEIGHT * 2) - BUTTON_SPACING,
            BUTTON_WIDTH,
            BUTTON_HEIGHT
        );

        level3ButtonBounds = new Rectangle(
            centerX - (BUTTON_WIDTH / 2),
            topButtonY - (BUTTON_HEIGHT * 3) - (BUTTON_SPACING * 2),
            BUTTON_WIDTH,
            BUTTON_HEIGHT
        );
    }

    @Override
    public void show() {
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();
        initializeButtons();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply(true);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        // Rysowanie przycisków
        game.batch.draw(level1ButtonTexture,
            level1ButtonBounds.x,
            level1ButtonBounds.y,
            BUTTON_WIDTH,
            BUTTON_HEIGHT);

        game.batch.draw(level2ButtonTexture,
            level2ButtonBounds.x,
            level2ButtonBounds.y,
            BUTTON_WIDTH,
            BUTTON_HEIGHT);

        game.batch.draw(level3ButtonTexture,
            level3ButtonBounds.x,
            level3ButtonBounds.y,
            BUTTON_WIDTH,
            BUTTON_HEIGHT);

        game.batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (level1ButtonBounds.contains(touchPos.x, touchPos.y)) {
                startGame(LEVEL_1_WIDTH, LEVEL_1_OBSTACLES, LEVEL_1_SPEED);
            } else if (level2ButtonBounds.contains(touchPos.x, touchPos.y)) {
                startGame(LEVEL_2_WIDTH, LEVEL_2_OBSTACLES, LEVEL_2_SPEED);
            } else if (level3ButtonBounds.contains(touchPos.x, touchPos.y)) {
                startGame(LEVEL_3_WIDTH, LEVEL_3_OBSTACLES, LEVEL_3_SPEED);
            }
        }
    }

    private void startGame(int worldWidth, int[][] obstacles, float playerSpeed) {
        game.setScreen(new GameScreen(game, worldWidth, obstacles, playerSpeed, lang));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
