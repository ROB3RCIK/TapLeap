package io.github.tapleap.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.tapleap.Main;

public class SettingsScreen implements Screen, InputProcessor {
    private Main game;
    private String lang;
    private Screen previousScreen;
    private Texture backButtonTexture;
    private Rectangle backButtonBounds;

    private OrthographicCamera camera;
    private ScreenViewport viewport;

    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 600;
    private static final float BUTTON_SIZE = 100;
    private static final float MARGIN = 10; // Taki sam margines jak w GameScreen

    public SettingsScreen(Main game, Screen previousScreen, String lang) {
        this.game = game;
        this.lang = lang;
        this.previousScreen = previousScreen;

        camera = new OrthographicCamera();
        camera.setToOrtho(true, WORLD_WIDTH, WORLD_HEIGHT);

        viewport = new ScreenViewport(camera);
        viewport.setUnitsPerPixel(1);

        backButtonTexture = new Texture(Gdx.files.internal("pause.png"));

        // Ustawienie przycisku z takimi samymi marginesami jak w GameScreen
        backButtonBounds = new Rectangle(
            MARGIN, // 10 jednostek od lewej krawędzi
            MARGIN, // 10 jednostek od górnej krawędzi (przy odwróconej osi Y)
            BUTTON_SIZE,
            BUTTON_SIZE
        );
    }

    private void updateButtonPosition() {
        backButtonBounds.x = MARGIN;
        backButtonBounds.y = MARGIN;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(backButtonTexture,
            backButtonBounds.x,
            backButtonBounds.y,
            BUTTON_SIZE,
            BUTTON_SIZE);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(width/2f, height/2f, 0);
        camera.update();
        updateButtonPosition();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3();
        touchPos.set(screenX, screenY, 0);
        viewport.unproject(touchPos);

        if (backButtonBounds.contains(touchPos.x, touchPos.y)) {
            game.setScreen(previousScreen);
            return true;
        }
        return false;
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        backButtonTexture.dispose();
    }

    @Override
    public boolean keyDown(int keycode) { return false; }

    @Override
    public boolean keyUp(int keycode) { return false; }

    @Override
    public boolean keyTyped(char character) { return false; }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

    @Override
    public boolean mouseMoved(int screenX, int screenY) { return false; }

    @Override
    public boolean scrolled(float amountX, float amountY) { return false; }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }
}
