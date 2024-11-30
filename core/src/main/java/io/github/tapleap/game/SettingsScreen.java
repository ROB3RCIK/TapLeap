package io.github.tapleap.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import io.github.tapleap.Main;

public class SettingsScreen implements Screen, InputProcessor {
    private Main game;
    private Screen previousScreen;
    private Texture backButtonTexture;
    private Rectangle backButtonBounds;

    public SettingsScreen(Main game, Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;

        // Przyciski
        backButtonTexture = new Texture(Gdx.files.internal("triangle.png"));
        backButtonBounds = new Rectangle(10, Gdx.graphics.getHeight() - 100, 100, 50);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(backButtonTexture, backButtonBounds.x, backButtonBounds.y, backButtonBounds.width, backButtonBounds.height);
        game.batch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        touchPos.y = Gdx.graphics.getHeight() - touchPos.y;

        if (backButtonBounds.contains(touchPos.x, touchPos.y)) {
            game.setScreen(previousScreen);
            return true;
        }
        return false;
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
        backButtonTexture.dispose();
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
