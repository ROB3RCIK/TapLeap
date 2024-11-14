package io.github.tapleap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class CollisionChecker {

    //Kolizja z granica ekranu
    public boolean isOutOfScreen(Player player) {
        Rectangle hitbox = player.getHitbox();
        return hitbox.y<0 || hitbox.y + hitbox.height > Gdx.graphics.getHeight();
    }

    //kolizja z przeszkoda
    public boolean isCollidingWithObstacles(Player player, List<Obstacle> obstacles) {
        Rectangle playerHitbox = player.getHitbox();

        for (Obstacle obstacle : obstacles) {
            if(playerHitbox.overlaps(obstacle.getHitbox())) {
                return true;
            }
        }
        return false;
    }

}
