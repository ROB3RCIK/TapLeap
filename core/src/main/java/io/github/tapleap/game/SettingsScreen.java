package io.github.tapleap.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Objects;

import io.github.tapleap.Main;
import io.github.tapleap.LangScreen;

public class SettingsScreen implements Screen, InputProcessor {
    private Main game;
    private String lang;
    private LangScreen previousScreen;
    private Texture backButtonTexture;
    private Rectangle backButtonBounds;

    private Texture languageButtonTexture;
    private Texture languageButtonTexturePL;
    private Rectangle languageButtonBounds;

    private OrthographicCamera camera;
    private FitViewport viewport;

    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 600;
    private static final float BUTTON_SIZE = 100;
    private static final float LANG_BUTTON_WIDTH = 200;
    private static final float LANG_BUTTON_HEIGHT = 50;
    private static final float MARGIN = 10; // Taki sam margines jak w GameScreen

    public SettingsScreen(Main game, LangScreen previousScreen, String lang) {
        this.game = game;
        this.lang = lang;
        this.previousScreen = previousScreen;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);

        backButtonTexture = new Texture(Gdx.files.internal("pause.png"));

        // Wczytaj tekstury przycisku językowego
        languageButtonTexture = new Texture(Gdx.files.internal("lang.png"));
        languageButtonTexturePL = new Texture(Gdx.files.internal("lang_pl.png"));

        // Ustawienie przycisku pauzy w lewym górnym rogu
        backButtonBounds = new Rectangle(
            MARGIN, // 10 jednostek od lewej krawędzi
            WORLD_HEIGHT - BUTTON_SIZE - MARGIN, // 10 jednostek od górnej krawędzi
            BUTTON_SIZE,
            BUTTON_SIZE
        );

        // Ustawienie przycisku zmiany języka na środku ekranu
        languageButtonBounds = new Rectangle(
            (WORLD_WIDTH - LANG_BUTTON_WIDTH) / 2, // Środek ekranu w poziomie
            (WORLD_HEIGHT - LANG_BUTTON_HEIGHT) / 2, // Środek ekranu w pionie
            LANG_BUTTON_WIDTH,
            LANG_BUTTON_HEIGHT
        );
    }

    private void updateButtonPosition() {
        // Przesunięcie przycisku pauzy
        backButtonBounds.x = MARGIN;
        backButtonBounds.y = WORLD_HEIGHT - BUTTON_SIZE - MARGIN;

        // Przesunięcie przycisku zmiany języka
        languageButtonBounds.x = (WORLD_WIDTH - LANG_BUTTON_WIDTH) / 2;
        languageButtonBounds.y = (WORLD_HEIGHT - LANG_BUTTON_HEIGHT) / 2;
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

        // Rysowanie przycisku zmiany języka
        Texture currentLangTexture;
        if(Objects.equals(lang, "PL")) {
            currentLangTexture = languageButtonTexturePL;
        } else {
            currentLangTexture = languageButtonTexture;
        }
        game.batch.draw(currentLangTexture,
            languageButtonBounds.x,
            languageButtonBounds.y,
            LANG_BUTTON_WIDTH,
            LANG_BUTTON_HEIGHT);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();
        updateButtonPosition();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3();
        touchPos.set(screenX, screenY, 0);
        viewport.unproject(touchPos);

        if (backButtonBounds.contains(touchPos.x, touchPos.y)) {
            previousScreen.setLang(lang); // Przekazanie zmiennej lang do poprzedniego ekranu
            game.setScreen(previousScreen);
            return true;
        }

        if (languageButtonBounds.contains(touchPos.x, touchPos.y)) {
            toggleLanguage();
            return true;
        }

        return false;
    }

    private void toggleLanguage() {
        if(Objects.equals(lang, "PL")) {
            lang = "EN";
        } else {
            lang = "PL";
        }
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
        languageButtonTexture.dispose();
        languageButtonTexturePL.dispose();
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
