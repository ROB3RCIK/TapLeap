package io.github.tapleap.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.tapleap.LangScreen;
import io.github.tapleap.end.GameOverScreen;
import io.github.tapleap.Main;

public class GameScreen implements Screen, InputProcessor, LangScreen {
    private Main game;
    private String lang;
    private Player player;
    private List<Obstacle> obstacles;
    private CollisionChecker collisionChecker;
    private boolean isGameOver;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private int worldWidth;
    private int worldHeight;
    private ShapeRenderer shapeRenderer;
    private Rectangle pauseButtonBounds;
    private Texture pauseButtonTexture;
    private float progress;
    private float playerSpeed; // Prędkość gracza

    public GameScreen(Main game, int worldWidth, int[][] obstaclesArray, float playerSpeed, String lang) {
        this.game = game;
        this.lang = lang;

        // Długość i wysokość planszy
        this.worldWidth = worldWidth;
        this.worldHeight = 1000; // Stała wysokość planszy

        // Prędkość gracza
        this.playerSpeed = playerSpeed;

        // Tworzenie gracza
        player = new Player(50, 50, playerSpeed);

        // Przeszkody
        obstacles = new ArrayList<>();
        for (int[] obstacleData : obstaclesArray) {
            obstacles.add(new Obstacle(obstacleData[0], obstacleData[1], obstacleData[2], obstacleData[3]));
        }

        // Obramowanie
        shapeRenderer = new ShapeRenderer();

        // Ustawienie kamery
        camera = new OrthographicCamera();
        float aspectRatio = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        viewport = new FitViewport(worldHeight * aspectRatio, worldHeight, camera);
        camera.position.set(viewport.getWorldWidth() / 2f, worldHeight / 2f, 0);
        camera.update();

        // Przyciski
        pauseButtonTexture = new Texture(Gdx.files.internal("pause.png"));
        pauseButtonBounds = new Rectangle(10, viewport.getWorldHeight() - 110, 100, 100);

        Gdx.input.setInputProcessor(this);

        // Kolizje
        collisionChecker = new CollisionChecker(worldWidth, worldHeight);

        // Koniec gry
        isGameOver = false;
    }

    @Override
    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public void render(float delta) {
        updateCamera();

        // Aktualizacja postępu
        updateProgress();

        // Sprawdzenie, czy gracz osiągnął 100%
        if (progress >= 100) {
            Gdx.app.log("GameScreen", "Player reached 100% of the map!");
            game.setScreen(new GameOverScreen(game,progress,lang)); // Zmień na odpowiedni ekran
            return;
        }

        // Tło
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // Gracz
        player.update(delta);
        player.render(game.batch);

        // Przeszkody
        for (Obstacle obstacle : obstacles) {
            if (obstacle.isVisible(camera)) {
                obstacle.render(game.batch);
            }
        }

        // Przyciski
        drawPauseButton();

        // Wyświetlenie postępu
        drawProgress();

        game.batch.end();

        // Obramowanie
        // drawWorldBounds();

        // Kolizje
        checkCollision();
    }

    private void updateProgress() {
        float playerPosition = player.getPosition().x;
        float threshold = worldWidth * 0.95f;

        if (playerPosition >= threshold) {
            progress = 100;
            endGame();
        } else {
            progress = Math.min(95, (playerPosition / threshold) * 95);
        }
    }

    private void drawProgress() {
        String progressText;
        if(Objects.equals(lang, "PL")) {
            progressText = String.format("Postep: %.1f%%", progress);
        } else {
            progressText = String.format("Progress: %.1f%%", progress);
        }

        // Ustawienie skali tekstu
        game.font.getData().setScale(4.0f); // Zwiększenie skali czcionki (wartość 2x większa)

        // Obliczanie pozycji
        float textWidth = game.font.getRegion().getRegionWidth();
        float x = camera.position.x - textWidth / 2 - 100;
        float y = camera.position.y + viewport.getWorldHeight() / 2 - 100; // Bliżej górnej krawędzi (odległość 100 jednostek)

        // Rysowanie tekstu
        game.font.draw(
            game.batch,
            progressText,
            x,
            y
        );

        // Przywrócenie domyślnej skali, jeśli potrzeba
        game.font.getData().setScale(1.0f);
    }

    private void updateCamera() {
        // Kamera podąża za graczem, ograniczona do granic świata
        float cameraLeftBoundary = viewport.getWorldWidth() / 2f;
        float cameraRightBoundary = worldWidth - viewport.getWorldWidth() / 2f;

        camera.position.x = Math.max(cameraLeftBoundary,
            Math.min(player.getPosition().x + player.getWidth() / 2f, cameraRightBoundary));
        camera.position.y = worldHeight / 2f; // Kamera jest statyczna w osi Y
        camera.update();
    }

    private void drawPauseButton() {
        game.batch.draw(
            pauseButtonTexture,
            camera.position.x - viewport.getWorldWidth() / 2 + pauseButtonBounds.x,
            camera.position.y + viewport.getWorldHeight() / 2 - pauseButtonBounds.height - 10,
            pauseButtonBounds.width,
            pauseButtonBounds.height
        );
    }

    private void checkCollision() {
        if (collisionChecker.isOutOfScreen(player)) {
            Gdx.app.log("GameScreen", "Collision - OutOfScreen");
            endGame();
        }

        if (collisionChecker.isCollidingWithObstacles(player, obstacles)) {
            Gdx.app.log("GameScreen", "Collision - with Obstacles");
            endGame();
        }
    }

    private void endGame() {
        Gdx.app.log("GameScreen", "Game Over");
        isGameOver = true;

        game.setScreen(new GameOverScreen(game,progress,lang));
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = camera.unproject(new Vector3(screenX, screenY, 0));

        if (pauseButtonBounds.contains(
            touchPos.x - (camera.position.x - viewport.getWorldWidth() / 2),
            touchPos.y - (camera.position.y - viewport.getWorldHeight() / 2)))
        {
            Gdx.app.log("GameScreen", "Pause button clicked!");
            game.setScreen(new SettingsScreen(game,this, lang)); // Przejście na ekran ustawień
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        player.dispose();
        for (Obstacle obstacle : obstacles) {
            obstacle.dispose();
        }

        shapeRenderer.dispose();
        pauseButtonTexture.dispose();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        Gdx.app.log("GameScreen", "Game screen is shown");
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
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
}
