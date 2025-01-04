package io.github.tapleap.start;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.tapleap.Main;
import io.github.tapleap.game.GameScreen;

public class LevelScreen implements Screen {
    private Main game;
    private OrthographicCamera camera;
    private FitViewport viewport;

    // Poziomy gry
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

    public LevelScreen(Main game) {
        this.game = game;

        // Ustawienia kamery i widoku
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();

        // Wczytanie tekstur przycisków
        level1ButtonTexture = new Texture(Gdx.files.internal("level1.png"));
        level2ButtonTexture = new Texture(Gdx.files.internal("level2.png"));
        level3ButtonTexture = new Texture(Gdx.files.internal("level3.png"));

        // Definicja prostokątów dla przycisków (pozycje w układzie świata)
        level1ButtonBounds = new Rectangle(300, 400, 200, 50);
        level2ButtonBounds = new Rectangle(300, 300, 200, 50);
        level3ButtonBounds = new Rectangle(300, 200, 200, 50);
    }

    @Override
    public void show() {
        // Kamera ustawiona statycznie i pokazująca cały ekran
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        // Czyszczenie ekranu
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Rysowanie przycisków
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(level1ButtonTexture, level1ButtonBounds.x, level1ButtonBounds.y, level1ButtonBounds.width, level1ButtonBounds.height);
        game.batch.draw(level2ButtonTexture, level2ButtonBounds.x, level2ButtonBounds.y, level2ButtonBounds.width, level2ButtonBounds.height);
        game.batch.draw(level3ButtonTexture, level3ButtonBounds.x, level3ButtonBounds.y, level3ButtonBounds.width, level3ButtonBounds.height);
        game.batch.end();

        // Obsługa kliknięć w przyciski
        if (Gdx.input.isTouched()) {
            // Pobranie współrzędnych kliknięcia w układzie ekranu
            float touchX = Gdx.input.getX();
            float touchY = Gdx.input.getY();

            // Konwersja współrzędnych na układ świata
            Vector3 touchPoint = new Vector3(touchX, touchY, 0);
            camera.unproject(touchPoint, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());

            // Sprawdzenie, który przycisk został kliknięty
            if (level1ButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                startGame(LEVEL_1_WIDTH, LEVEL_1_OBSTACLES, LEVEL_1_SPEED);
            } else if (level2ButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                startGame(LEVEL_2_WIDTH, LEVEL_2_OBSTACLES, LEVEL_2_SPEED);
            } else if (level3ButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                startGame(LEVEL_3_WIDTH, LEVEL_3_OBSTACLES, LEVEL_3_SPEED);
            }
        }
    }

    private void startGame(int worldWidth, int[][] obstacles, float playerSpeed) {
        // Przekazujemy do GameScreen odpowiednią szerokość świata oraz przeszkody
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
        // Zwolnienie zasobów
        level1ButtonTexture.dispose();
        level2ButtonTexture.dispose();
        level3ButtonTexture.dispose();
    }
}
