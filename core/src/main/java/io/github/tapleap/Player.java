package io.github.tapleap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle = 30f;

    public Player() {
        texture = new Texture("triangle.png");
        position = new Vector2(50, 50);
        velocity = new Vector2(800, 0);
        velocity.rotateDeg(30);
    }


    public void update(float delta) {
        position.add(velocity.cpy().scl(delta));

        if(Gdx.input.justTouched()) {
            velocity.y *= -1;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void dispose() {
        texture.dispose();
    }
}
