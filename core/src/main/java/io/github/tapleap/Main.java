package io.github.tapleap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.tapleap.game.GameScreen;


public class Main extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        //setScreen(new StartScreen(this));
        setScreen(new GameScreen(this,5000));
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}

