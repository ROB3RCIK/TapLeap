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

    //Kolizja z granicami ekranu
    public boolean isOutOfScreen(Player player) {
        Rectangle hitbox = player.getHitbox(); // Pobranie Hitboxów gracza

        // Sprawdzenie kolizji z dolną i górną granicą planszy
        if (hitbox.y < 0 || hitbox.y + hitbox.height > worldHeight) {
            return true;
        }

        // Sprawdzenie kolizji z lewą i prawą granicą planszy
        if (hitbox.x < 0 || hitbox.x + hitbox.width > worldWidth) {
            return true;
        }
        return false;
    }

    // Kolizja z przeszkodami
    public boolean isCollidingWithObstacles(Player player, List<Obstacle> obstacles) {
        Rectangle playerHitbox = player.getHitbox(); // Pobranie Htiboxów gracza

        // Sprawdzenie czy żadna z przeszkód nie ma kontaktu z graczem
        for (Obstacle obstacle : obstacles) {
            if (playerHitbox.overlaps(obstacle.getHitbox())) {
                return true;
            }
        }
        return false;
    }

}
