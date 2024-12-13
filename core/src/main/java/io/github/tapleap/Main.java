package io.github.tapleap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import io.github.tapleap.game.GameScreen;


public class Main extends Game {
    public SpriteBatch batch;
    public BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        //setScreen(new StartScreen(this));
        setScreen(new GameScreen(this,5000));
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}

