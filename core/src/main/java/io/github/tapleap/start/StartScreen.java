package io.github.tapleap.start;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import io.github.tapleap.Main;
import io.github.tapleap.game.GameScreen;
import io.github.tapleap.game.SettingsScreen;

public class StartScreen implements Screen {
    private final Main game;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private BitmapFont titleFont;
    private BitmapFont buttonFont;

    private Rectangle startButtonBounds;
    private Rectangle settingsButtonBounds;
    private Rectangle exitButtonBounds;

    private Texture startButtonTexture;
    private Texture settingsButtonTexture;
    private Texture exitButtonTexture;

    public StartScreen(Main game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        batch = new SpriteBatch();

        titleFont = new BitmapFont();
        titleFont.getData().setScale(3);

        buttonFont = new BitmapFont();
        buttonFont.getData().setScale(2);

        startButtonTexture = new Texture(Gdx.files.internal("start.png"));
        settingsButtonTexture = new Texture(Gdx.files.internal("settings.png"));
        exitButtonTexture = new Texture(Gdx.files.internal("exit.png"));

        startButtonBounds = new Rectangle(300, 300, 200, 50);
        settingsButtonBounds = new Rectangle(300, 200, 200, 50);
        exitButtonBounds = new Rectangle(300, 100, 200, 50);
    }

    @Override
    public void show() {
        Gdx.app.log("StartScreen", "Start screen is shown");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // Draw title
        titleFont.draw(batch, "TapLeap", 250, 550);

        // Draw buttons
        batch.draw(startButtonTexture, startButtonBounds.x, startButtonBounds.y, startButtonBounds.width, startButtonBounds.height);
        buttonFont.draw(batch, "", startButtonBounds.x + 50, startButtonBounds.y + 35);

        batch.draw(settingsButtonTexture, settingsButtonBounds.x, settingsButtonBounds.y, settingsButtonBounds.width, settingsButtonBounds.height);
        buttonFont.draw(batch, "", settingsButtonBounds.x + 50, settingsButtonBounds.y + 35);

        batch.draw(exitButtonTexture, exitButtonBounds.x, exitButtonBounds.y, exitButtonBounds.width, exitButtonBounds.height);
        buttonFont.draw(batch, "", exitButtonBounds.x + 40, exitButtonBounds.y + 35);

        batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (startButtonBounds.contains(touchPos.x, touchPos.y)) {
                Gdx.app.log("StartScreen", "Level button clicked!");
                game.setScreen(new LevelScreen(game));
            } else if (settingsButtonBounds.contains(touchPos.x, touchPos.y)) {
                Gdx.app.log("StartScreen", "Settings button clicked!");
                game.setScreen(new SettingsScreen(game,this));
            } else if (exitButtonBounds.contains(touchPos.x, touchPos.y)) {
                Gdx.app.log("StartScreen", "Exit button clicked!");
                Gdx.app.exit();
            }
        }
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
        batch.dispose();
        titleFont.dispose();
        buttonFont.dispose();
        startButtonTexture.dispose();
        exitButtonTexture.dispose();
        settingsButtonTexture.dispose();
    }
}
