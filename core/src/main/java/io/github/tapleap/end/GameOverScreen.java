package io.github.tapleap.end;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import io.github.tapleap.Main;

public class GameOverScreen implements Screen {
    private Main game;

    public GameOverScreen(Main game) {
        this.game=game;
    }

    @Override
    public void show() {
        Gdx.app.log("GameOverScreen", "Game over screen is shown");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

    }
}
