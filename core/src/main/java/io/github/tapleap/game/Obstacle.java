package io.github.tapleap.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Obstacle {
    private Texture texture;
    private Vector2 position;
    private Rectangle hitbox;

    public Obstacle(float x, float y, float width, float height) {
        texture = new Texture("triangle.png");
        position = new Vector2(x,y);
        hitbox = new Rectangle(x,y,width,height);
    }

    public boolean isVisible(OrthographicCamera camera) {
        float cameraLeft = camera.position.x - camera.viewportWidth / 2f;
        float cameraRight = camera.position.x + camera.viewportWidth / 2f;

        return hitbox.x + hitbox.width > cameraLeft && hitbox.x < cameraRight;
    }


    public void render(SpriteBatch batch) {
        batch.draw(texture,hitbox.x,hitbox.y,hitbox.width,hitbox.height);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void dispose() {
        texture.dispose();
    }

}
