package io.github.tapleap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    private Main game;
    private Player player;
    private List<Obstacle> obstacles;
    private CollisionChecker collisionChecker;
    private boolean isGameOver;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private int worldWidth;  // Szerokość planszy
    private int worldHeight; // Wysokość planszy
    private ShapeRenderer shapeRenderer;

    public GameScreen(Main game, int worldWidth) {
        this.game = game;

        // Długość i wysokość planszy
        this.worldWidth = worldWidth;
        this.worldHeight = 1000; // Stała wysokość planszy

        player = new Player(50, 50);

        obstacles = new ArrayList<>();
        obstacles.add(new Obstacle(1200, 300, 200, 200));
        obstacles.add(new Obstacle(1500, 700, 200, 200));

        collisionChecker = new CollisionChecker(worldWidth, worldHeight);

        camera = new OrthographicCamera();
        float aspectRatio = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        viewport = new FitViewport(worldHeight * aspectRatio, worldHeight, camera);

        camera.position.set((float) viewport.getWorldWidth() / 2f, worldHeight / 2f, 0);
        camera.update();

        isGameOver = false;

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        if (isGameOver) {
            Gdx.gl.glClearColor(1, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            game.batch.begin();
            game.batch.end();
            return;
        }

        updateCamera();

        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        player.update(delta);
        player.render(game.batch);

        for (Obstacle obstacle : obstacles) {
            if (obstacle.isVisible(camera)) { // Renderuj tylko widoczne przeszkody
                obstacle.render(game.batch);
            }
        }

        game.batch.end();

        drawWorldBounds();

        checkCollision();
    }

    private void updateCamera() {
        // Kamera podąża za graczem w poziomie, ograniczenie do granic mapy
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

    private void checkCollision() {
        if (collisionChecker.isOutOfScreen(player)) {
            endGame();
        }

        if (collisionChecker.isCollidingWithObstacles(player, obstacles)) {
            endGame();
        }
    }

    private void endGame() {
        Gdx.app.log("GameScreen", "Game Over!");
        isGameOver = true;
        // Logika końca gry
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
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen", "Game screen is shown");
    }
}
