package io.github.tapleap.game;

import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class CollisionChecker {

    private int worldWidth;
    private int worldHeight;

    public CollisionChecker(int worldWidth, int worldHeight) {
        this.worldWidth=worldWidth;
        this.worldHeight=worldHeight;
    }

    //Kolizja z granica ekranu
    public boolean isOutOfScreen(Player player) {
        Rectangle hitbox = player.getHitbox();
        // Sprawdzenie kolizji z dolną i górną granicą planszy
        if (hitbox.y < 0 || hitbox.y + hitbox.height > worldHeight) {
            return true;
        }

        // Sprawdzenie kolizji z lewą i prawą granicą
        if (hitbox.x < 0 || hitbox.x + hitbox.width > worldWidth) {
            return true;
        }

        return false;
    }

    //kolizja z przeszkoda
    public boolean isCollidingWithObstacles(Player player, List<Obstacle> obstacles) {
        Rectangle playerHitbox = player.getHitbox();

        for (Obstacle obstacle : obstacles) {
            if (playerHitbox.overlaps(obstacle.getHitbox())) {
                return true;
            }
        }
        return false;
    }

}
