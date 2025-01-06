package io.github.tapleap.end;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Objects;

import io.github.tapleap.Main;
import io.github.tapleap.game.GameScreen;
import io.github.tapleap.start.StartScreen;

public class GameOverScreen implements Screen {
    private Main game;
    private String lang;
    private float score;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 600;
    private static final float RESTART_BUTTON_SIZE = 100;
    private static final float MENU_BUTTON_WIDTH = 200;
    private static final float MENU_BUTTON_HEIGHT = 50;
    private static final float BUTTON_SPACING = 20;

    private BitmapFont scoreFont;
    private GlyphLayout scoreLayout;

    private Texture restartButtonTexture;
    private Texture exitButtonTexture;
    private Rectangle restartButtonBounds;
    private Rectangle exitButtonBounds;

    public GameOverScreen(Main game, float score, String lang) {
        this.game = game;
        this.score = score;
        this.lang = lang;

        // Ustawienia kamery
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);

        // Inicjalizacja czcionki dla wyniku
        scoreFont = new BitmapFont();
        scoreFont.getData().setScale(3);
        scoreFont.setColor(Color.GOLD);
        scoreLayout = new GlyphLayout();
        if(Objects.equals(lang, "PL")) {
            scoreLayout.setText(scoreFont, String.format("Wynik: %.1f%%", score));
        } else {
            scoreLayout.setText(scoreFont, String.format("Score: %.1f%%", score));
        }

        // Wczytanie tekstur
        restartButtonTexture = new Texture(Gdx.files.internal("retry.png"));
        exitButtonTexture = new Texture(Gdx.files.internal("menu.png"));

        // Inicjalizacja przyciskow
        initializeButtons();
    }

    private void initializeButtons() {
        float centerX = (WORLD_WIDTH / 2f) - 10;
        float centerY = WORLD_HEIGHT / 2f - 50;

        // Przycisk restart
        restartButtonBounds = new Rectangle(
            centerX - (RESTART_BUTTON_SIZE / 2),
            centerY + BUTTON_SPACING,
            RESTART_BUTTON_SIZE,
            RESTART_BUTTON_SIZE
        );

        // Przycisk menu
        exitButtonBounds = new Rectangle(
            centerX - (MENU_BUTTON_WIDTH / 2),
            centerY - MENU_BUTTON_HEIGHT,
            MENU_BUTTON_WIDTH,
            MENU_BUTTON_HEIGHT
        );
    }

    @Override
    public void show() {
        Gdx.app.log("GameOverScreen", "Game over screen is shown");
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply(true);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        // Rysowanie wyniku
        scoreFont.draw(game.batch, scoreLayout,
            (WORLD_WIDTH - scoreLayout.width) / 2,
            WORLD_HEIGHT - 150); //

        // Rysowanie przycisku restart (100x100)
        game.batch.draw(restartButtonTexture,
            restartButtonBounds.x,
            restartButtonBounds.y,
            RESTART_BUTTON_SIZE,
            RESTART_BUTTON_SIZE);

        // Rysowanie przycisku menu (200x50)
        game.batch.draw(exitButtonTexture,
            exitButtonBounds.x,
            exitButtonBounds.y,
            MENU_BUTTON_WIDTH,
            MENU_BUTTON_HEIGHT);

        game.batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (restartButtonBounds.contains(touchPos.x, touchPos.y)) {
                // Restart gry z domyślnymi parametrami
                game.setScreen(new GameScreen(game, 6000, new int[][]{{600, 300, 200, 200},
                    {1200, 500, 200, 200}, {2400, 700, 200, 200},
                    {3600, 400, 200, 200}, {5000, 600, 200, 200}}, 200f, lang));
            } else if (exitButtonBounds.contains(touchPos.x, touchPos.y)) {
                // Powrót do menu głównego
                game.setScreen(new StartScreen(game, lang));
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
        scoreFont.dispose();
        restartButtonTexture.dispose();
        exitButtonTexture.dispose();
    }
}
