package io.github.tapleap.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.List;

import io.github.tapleap.end.GameOverScreen;
import io.github.tapleap.Main;

public class GameScreen implements Screen, InputProcessor {
    private Main game;
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

    public GameScreen(Main game, int worldWidth) {
        this.game = game;

        // Długość i wysokość planszy
        this.worldWidth = worldWidth;
        this.worldHeight = 1000; // Stała wysokość planszy

        // Tworzenie gracza
        player = new Player(50, 50);

        // Przeszkody
        obstacles = new ArrayList<>();
        obstacles.add(new Obstacle(1200, 300, 200, 200));
        obstacles.add(new Obstacle(1500, 700, 200, 200));

        // Obramowanie
        shapeRenderer = new ShapeRenderer();

        // Ustawienia kamery
        camera = new OrthographicCamera();
        float aspectRatio = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        viewport = new FitViewport(worldHeight * aspectRatio, worldHeight, camera);
        camera.position.set( viewport.getWorldWidth() / 2f, worldHeight / 2f, 0);
        camera.update();

        // Przyciski
        pauseButtonTexture = new Texture(Gdx.files.internal("triangle.png"));
        pauseButtonBounds = new Rectangle(10, viewport.getWorldHeight() - 110, 100, 100);

        Gdx.input.setInputProcessor(this);

        // Kolizje
        collisionChecker = new CollisionChecker(worldWidth, worldHeight);

        // Koniec gry
        isGameOver = false;
    }

    @Override
    public void render(float delta) {
        updateCamera();

        // Tło
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
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

        game.batch.end();

        // Obramowanie
        drawWorldBounds();

        // Kolizje
        checkCollision();
    }

    private void updateCamera() {
        // Kamera podąża za graczem w poziomie
        camera.position.x = Math.max(viewport.getWorldWidth() / 2f,
            Math.min(player.getPosition().x + player.getWidth() / 2f, worldWidth - viewport.getWorldWidth() / 2f));
        // Stała pozycja kamery w pionie
        camera.position.y = worldHeight / 2f;

        camera.update();
    }

    private void drawWorldBounds() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 1);

        float borderWidth = 10;
        shapeRenderer.rect(0, 0, worldWidth, borderWidth);
        shapeRenderer.rect(0, 0, borderWidth, worldHeight);
        shapeRenderer.rect(0, worldHeight - borderWidth, worldWidth, borderWidth);
        shapeRenderer.rect(worldWidth - borderWidth, 0, borderWidth, worldHeight);

        shapeRenderer.end();
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

        game.setScreen(new GameOverScreen(game));
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = camera.unproject(new Vector3(screenX, screenY, 0));

        if (pauseButtonBounds.contains(
            touchPos.x - (camera.position.x - viewport.getWorldWidth() / 2),
            touchPos.y - (camera.position.y - viewport.getWorldHeight() / 2)))
        {
            Gdx.app.log("GameScreen", "Pause button clicked!");
            game.setScreen(new SettingsScreen(game,this)); // Przejście na ekran ustawień
            return true;
        }
        return false;
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
