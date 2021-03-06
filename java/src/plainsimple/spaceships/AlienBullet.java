package plainsimple.spaceships;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Created by Stefan on 8/29/2015.
 */
public class AlienBullet extends Sprite {

    public AlienBullet(double x, double y, Board board) {
        super(x, y, board);
        initAlienBullet();
    }

    private void initAlienBullet() {
        loadDefaultImage("sprites/alien/alien_bullet.png");

        hitBox.setDimensions(10, 10);

        damage = 20;
    }

    @Override
    public void updateActions() {

    }

    @Override
    public void updateSpeeds() {

    }

    @Override
    public void handleCollision(Sprite s) {

    }

    @Override
    void render(Graphics2D g, ImageObserver o) {
        g.drawImage(defaultImage, (int) x, (int) y, o);
    }
}
