package io.github.tapleap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class GameScreen implements Screen {
    private Main game;
    private Player player;
    private Texture testTexture;

    public GameScreen(Main game) {
        this.game=game;
        player = new Player();
        testTexture = new Texture("libgdx.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        player.update(delta);
        player.render(game.batch);
        game.batch.end();
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
        testTexture.dispose(); // Usuwanie tekstury, aby uniknąć wycieków pamięci
        player.dispose();
    }
    @Override
    public void show() {
        Gdx.app.log("GameScreen", "Game screen is shown");
    }
}
