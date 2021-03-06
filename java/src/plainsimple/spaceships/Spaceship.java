package plainsimple.spaceships;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Stefan on 8/13/2015.
 */
public class Spaceship extends Sprite {

    // arrowkey direction in y
    private int dy;

    private final float MAX_SPEED_Y = 3.5f;

    private SpriteAnimation movingAnimation;
    private SpriteAnimation fireRocketAnimation;
    private SpriteAnimation explodeAnimation;

    // whether user has control over spaceship
    boolean controllable;

    public final int BULLET_LASER = 1;
    public final int BULLET_ION = 2;
    public final int BULLET_PLASMA = 3;

    private boolean firesBullets;
    // ms to wait between firing bullets
    private int bulletDelay = 100;
    private int bulletType = 10;
    private long lastFiredBullet;
    private boolean firingBullets;

    private boolean firesRockets;
    // ms to wait between firing rockets
    private int rocketDelay = 420;
    private int rocketType = 40;
    private long lastFiredRocket;
    private boolean firingRockets;

    // keeps track of fired bullets and rockets
    private ArrayList<Sprite> projectiles;

    public ArrayList<Sprite> getProjectiles() {
        return projectiles;
    }

    public void setControllable(boolean controllable) {
        this.controllable = controllable;
    }

    public void setHP(int hp) {
        this.hp = hp;
    }

    public void setBullets(boolean firesBullets, int bulletType, int bulletDelay) {
        this.firesBullets = firesBullets;
        this.bulletType = bulletType;
        this.bulletDelay = bulletDelay;
    }

    public void setRockets(boolean firesRockets, int rocketType, int rocketDelay) {
        this.firesRockets = firesRockets;
        this.rocketType = rocketType;
        this.rocketType = rocketDelay;
    }

    // default constructor
    public Spaceship(String imageName, int x, int y, Board board) {
        super(imageName, x, y, board);
        initCraft();
    }

    private void initCraft() {
        try { // todo: this will not catch all errors... Resources should be initialized in main or Board.java
            movingAnimation = new SpriteAnimation("sprites/spaceship/spaceship_moving_spritesheet_diff.png", 50, 50, 5, true);
            fireRocketAnimation = new SpriteAnimation("sprites/spaceship/spaceship_firing_spritesheet_diff.png", 50, 50, 8, false);
            explodeAnimation = new SpriteAnimation("sprites/spaceship/spaceship_exploding_spritesheet_diff.png", 50, 50, 5, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        projectiles = new ArrayList<>();
        lastFiredBullet = 0;
        lastFiredRocket = 0;

        damage = 100;
        controllable = true;
        collides = true;
        hitBox.setDimensions(33, 28);
        hitBox.setOffsets(12, 11);
    }

    public void updateActions() {
        if (firingBullets && lastFiredBullet + bulletDelay <= System.currentTimeMillis()) {
            fireBullets();
            lastFiredBullet = System.currentTimeMillis();
        }

        if (firingRockets && lastFiredRocket + rocketDelay <= System.currentTimeMillis()) {
            fireRockets();
            lastFiredRocket = System.currentTimeMillis();
            fireRocketAnimation.start();
        }
    }

    // fires two rockets
    public void fireRockets() {
        projectiles.add(new Rocket(x + 43, y + 15, rocketType, board));
        projectiles.add(new Rocket(x + 43, y + 33, rocketType, board));
    }

    // fires two bullets
    public void fireBullets() {
        projectiles.add(new Bullet(x + 43, y + 15, bulletType, board));
        projectiles.add(new Bullet(x + 43, y + 33, bulletType, board));
    }

    public void updateSpeeds() {
        speedY = Math.abs(speedY);
        if (speedY < MAX_SPEED_Y) {
            speedY += 0.25;
        } else if (speedY > MAX_SPEED_Y) {
            speedY = MAX_SPEED_Y;
        }
        speedY *= dy;
    }

    public void handleCollision(Sprite s) {
        hp -= s.getDamage();
        if (hp < 0) {
            explodeAnimation.start();
            hp = 0;
            collision = true;
        }
    }

    @Override
    void render(Graphics2D g, ImageObserver o) {
        g.drawImage(defaultImage, (int) x, (int) y, o);
        if (moving) {
            g.drawImage(movingAnimation.nextFrame(), (int) x, (int) y, o);
        }
        if (fireRocketAnimation.isPlaying()) {
            g.drawImage(fireRocketAnimation.nextFrame(), (int) x, (int) y, o);
        }
        if (explodeAnimation.isPlaying()) {
            g.drawImage(explodeAnimation.nextFrame(), (int) x, (int) y, o);
        }

    }

    // Sets direction of sprite based on key pressed.
    public void keyPressed(KeyEvent e) {
        if (controllable) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_UP) {
                dy = -1;
            } else if (key == KeyEvent.VK_DOWN) {
                dy = 1;
            } else if (key == KeyEvent.VK_SPACE && firesBullets) {
                firingBullets = true;
            } else if (key == KeyEvent.VK_X && firesRockets) {
                firingRockets = true;
            }
        }
    }

    // sets movement direction to zero once key is released
    public void keyReleased(KeyEvent e) {
        if (controllable) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_UP) {
                dy = 0;
            } else if (key == KeyEvent.VK_DOWN) {
                dy = 0;
            } else if (key == KeyEvent.VK_SPACE) {
                firingBullets = false;
            } else if (key == KeyEvent.VK_X) {
                firingRockets = false;
            }
        }
    }
}
