package io.github.tapleap.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private Texture texture;
    private Vector2 position;
    private float width;
    private float height;
    private Rectangle hitbox;
    private Vector2 velocity;
    private final float angle = 30f;

    public Player(float width, float height) {
        texture = new Texture("triangle.png");
        position = new Vector2(50, 50);
        hitbox = new Rectangle(position.x, position.y, width, height);

        velocity = new Vector2(500, 0);
        velocity.rotateDeg(angle);
    }


    public void update(float delta) {
        position.add(velocity.cpy().scl(delta));

        hitbox.setPosition(position.x, position.y);

        if(Gdx.input.justTouched()) {
            velocity.y *= -1;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public Rectangle getHitbox() {
        hitbox.setPosition(position.x, position.y);
        return hitbox;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void dispose() {
        texture.dispose();
    }
}
