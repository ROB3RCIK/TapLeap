package io.github.tapleap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    private Main game;
    private Player player;
    private List<Obstacle> obstacles;
    private CollisionChecker collisionChecker;
    private boolean isGameOver;

    public GameScreen(Main game) {
        this.game=game;
        player = new Player();

        //dodawanie przeszkod
        obstacles= new ArrayList<>();
        obstacles.add(new Obstacle(1200, 300, 200, 200));
        obstacles.add(new Obstacle(1500, 700, 200, 200));

        collisionChecker = new CollisionChecker();

        isGameOver = false;
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


        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        player.update(delta);
        player.render(game.batch);


        for (Obstacle obstacle:obstacles) {
            obstacle.render(game.batch);
        }

        game.batch.end();

        checkCollision();
    }

    private void checkCollision() {
        if(collisionChecker.isOutOfScreen(player)) {
            endGame();
        }

        if(collisionChecker.isCollidingWithObstacles(player,obstacles)) {
            endGame();
        }
    }

    private void endGame() {
        Gdx.app.log("GameScreen", "Game Over!");
        isGameOver = true;
        //logika konca gry
    }

    @Override
    public void resize(int width, int height) {

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
        for (Obstacle obstacle:obstacles) {
            obstacle.dispose();
        }
    }
    @Override
    public void show() {
        Gdx.app.log("GameScreen", "Game screen is shown");
    }
}
