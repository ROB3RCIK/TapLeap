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
import io.github.tapleap.Main;

public class SettingsScreen implements Screen, InputProcessor {
    private Main game;
    private Screen previousScreen;
    private Texture backButtonTexture;
    private Rectangle backButtonBounds;

    private OrthographicCamera camera;
    private FitViewport viewport;

    public SettingsScreen(Main game, Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;

        // Ustawienia kamery i widoku
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();

        // Wczytanie tekstury przycisku
        backButtonTexture = new Texture(Gdx.files.internal("triangle.png"));

        // Wstępne utworzenie prostokąta dla przycisku (później dynamicznie aktualizowany)
        backButtonBounds = new Rectangle(0, 0, 50, 50); // Szerokość i wysokość ustawione na 50x50
        updateButtonPosition(); // Aktualizacja pozycji przycisku
    }

    private void updateButtonPosition() {
        // Ustawienie przycisku w lewym górnym rogu świata kamery
        backButtonBounds.x = 10; // 10 jednostek od lewej krawędzi
        backButtonBounds.y = viewport.getWorldHeight() - backButtonBounds.height - 10; // 10 jednostek od górnej krawędzi
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        // Czyszczenie ekranu
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Ustawienie kamery i rysowanie przycisków
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        // Rysowanie tekstury przycisku w tym samym miejscu, co hitbox
        game.batch.draw(backButtonTexture, backButtonBounds.x, backButtonBounds.y, backButtonBounds.width, backButtonBounds.height);
        game.batch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Konwersja współrzędnych dotyku z układu ekranu na układ świata
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        camera.unproject(touchPos);

        // Sprawdzenie, czy kliknięto przycisk "Powrót"
        if (backButtonBounds.contains(touchPos.x, touchPos.y)) {
            game.setScreen(previousScreen);
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        // Aktualizacja widoku i pozycji kamery
        viewport.update(width, height);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();

        // Aktualizacja pozycji przycisku po zmianie rozmiaru okna
        updateButtonPosition();
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
