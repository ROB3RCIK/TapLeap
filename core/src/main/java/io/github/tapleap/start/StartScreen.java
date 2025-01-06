package io.github.tapleap.start;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Objects;

import io.github.tapleap.LangScreen;
import io.github.tapleap.Main;
import io.github.tapleap.game.SettingsScreen;

public class StartScreen implements Screen, LangScreen {
    private final Main game;
    private String lang;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private FitViewport viewport;
    private BitmapFont titleFont;
    private GlyphLayout titleLayout;
    private Rectangle startButtonBounds;
    private Rectangle settingsButtonBounds;
    private Rectangle exitButtonBounds;
    private Texture startButtonTexture;
    private Texture settingsButtonTexture;
    private Texture exitButtonTexture;
    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 600;
    private static final float BUTTON_WIDTH = 200;
    private static final float BUTTON_HEIGHT = 50;
    private static final float BUTTON_SPACING = 20;

    public StartScreen(Main game, String lang) {
        this.game = game;
        this.lang = lang;

        // Ustawienia kamery
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);

        batch = new SpriteBatch();

        // Napis - nazwa gry
        titleFont = new BitmapFont();
        titleFont.getData().setScale(3);
        titleFont.setColor(Color.GOLD);
        titleLayout = new GlyphLayout();
        titleLayout.setText(titleFont, "TapLeap");

        // Aktualizacja tekstur na podstawie jezyka
        updateTextures();

        // Inicjalizacja przyciskow
        initializeButtons();
    }

    private void initializeButtons() {
        float centerX = (WORLD_WIDTH / 2f) - 10;
        float centerY = WORLD_HEIGHT / 2f;

        float totalHeight = (BUTTON_HEIGHT * 3) + (BUTTON_SPACING * 2);
        float topButtonY = centerY + (totalHeight / 2);

        startButtonBounds = new Rectangle(
            centerX - (BUTTON_WIDTH / 2),
            topButtonY - BUTTON_HEIGHT,
            BUTTON_WIDTH,
            BUTTON_HEIGHT
        );

        settingsButtonBounds = new Rectangle(
            centerX - (BUTTON_WIDTH / 2),
            topButtonY - (BUTTON_HEIGHT * 2) - BUTTON_SPACING,
            BUTTON_WIDTH,
            BUTTON_HEIGHT
        );

        exitButtonBounds = new Rectangle(
            centerX - (BUTTON_WIDTH / 2),
            topButtonY - (BUTTON_HEIGHT * 3) - (BUTTON_SPACING * 2),
            BUTTON_WIDTH,
            BUTTON_HEIGHT
        );
    }

    private void updateTextures() {
        // Wczytanie nowej tekstury na podstawie wartości lang
        if (Objects.equals(lang, "PL")) {
            startButtonTexture = new Texture(Gdx.files.internal("start.png"));
            settingsButtonTexture = new Texture(Gdx.files.internal("settings_pl.png"));
            exitButtonTexture = new Texture(Gdx.files.internal("exit_pl.png"));
        } else {
            startButtonTexture = new Texture(Gdx.files.internal("start.png"));
            settingsButtonTexture = new Texture(Gdx.files.internal("settings.png"));
            exitButtonTexture = new Texture(Gdx.files.internal("exit.png"));
        }
    }

    @Override
    public void show() {
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();
        initializeButtons();
    }

    @Override
    public void setLang(String lang) {
        // Aktualizacja tekstur po zmianie języka
        this.lang = lang;
        updateTextures();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply(true);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        titleFont.draw(batch, "TapLeap",
            (WORLD_WIDTH - titleLayout.width) / 2,
            WORLD_HEIGHT - 50);

        batch.draw(startButtonTexture,
            startButtonBounds.x,
            startButtonBounds.y,
            BUTTON_WIDTH,
            BUTTON_HEIGHT);

        batch.draw(settingsButtonTexture,
            settingsButtonBounds.x,
            settingsButtonBounds.y,
            BUTTON_WIDTH,
            BUTTON_HEIGHT);

        batch.draw(exitButtonTexture,
            exitButtonBounds.x,
            exitButtonBounds.y,
            BUTTON_WIDTH,
            BUTTON_HEIGHT);

        batch.end();

        // Obsluga klikniec przyciskow
        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (startButtonBounds.contains(touchPos.x, touchPos.y)) {
                Gdx.app.log("StartScreen", "Level button clicked!");
                game.setScreen(new LevelScreen(game, lang));
            } else if (settingsButtonBounds.contains(touchPos.x, touchPos.y)) {
                Gdx.app.log("StartScreen", "Settings button clicked!");
                game.setScreen(new SettingsScreen(game, this, lang));
            } else if (exitButtonBounds.contains(touchPos.x, touchPos.y)) {
                Gdx.app.log("StartScreen", "Exit button clicked!");
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        startButtonTexture.dispose();
        settingsButtonTexture.dispose();
        exitButtonTexture.dispose();
    }
}
