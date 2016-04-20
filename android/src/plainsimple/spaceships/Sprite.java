package plainsimple.spaceships;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Stefan on 8/12/2015.
 */
public abstract class Sprite {

    // coordinates of sprite
    protected float x;
    protected float y;

    // intended movement in x and y directions each frame
    protected double speedX;
    protected double speedY;

    // sprite width and height
    protected int width;
    protected int height;

    // damage sprite can withstand and damage it inflicts on collision
    protected int hp;
    protected int damage;

    // whether or not sprite is visible on screen
    protected boolean vis;
    // whether or not sprite is in screen bounds
    protected boolean inBounds;
    // whether or not sprite can collide with other sprites
    protected boolean collides; // todo: flags all in one bitwise operator?
    // whether or not sprite has had a collision
    protected boolean collision;
    // whether or not sprite is currently moving
    protected boolean moving; // todo: remove?

    // hitbox for collision detection todo: complex shapes
    protected Hitbox hitBox;

    // R.drawable ID that specifies the sprite's default texture
    protected int defaultImageID;

    // random number generator
    protected Random random;

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public double getSpeedX() {
        return speedX;
    }
    public double getSpeedY() {
        return speedY;
    }
    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }
    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }
    public boolean isVisible() {
        return vis;
    }
    public void setVisible(Boolean visible) {
        vis = visible;
    }
    public boolean isInBounds() { return inBounds; }
    public boolean getCollides() {
        return collides;
    }
    public void setCollides(boolean collides) {
        this.collides = collides;
    }
    public boolean getCollision() {
        return collision;
    }
    public void setCollision(boolean collision) {
        this.collision = collision;
    }
    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    public int getHP() {
        return hp;
    }
    public void setHP(int hp) {
        this.hp = hp;
    }

    public Sprite(int defaultImageID, int spriteWidth, int spriteHeight, float x, float y) {
        this.defaultImageID = defaultImageID;
        this.width = spriteWidth;
        this.height = spriteHeight;
        this.x = x;
        this.y = y;
        initSprite();
    }

    // initializes with sprite at (0,0)
    public Sprite(int defaultImageID, int spriteWidth, int spriteHeight) {
        this(defaultImageID, spriteWidth, spriteHeight, 0, 0);
        initSprite();
    }

    private void initSprite() {
        vis = true;
        moving = true;
        collision = false;
        collides = true;
        speedX = 0.0f;
        speedY = 0.0f;
        hitBox = new Hitbox();
        random = new Random();
        damage = 0;
        hp = 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
        hitBox.updateCoordinates((int) x, hitBox.getY());
    }

    public void setY(float y) {
        this.y = y;
        hitBox.updateCoordinates(hitBox.getX(), (int) y);
    }

    // update/handle any actions sprite takes
    abstract void updateActions();

    abstract void updateSpeeds();

    abstract void updateAnimations();

    // handles collision with s
    abstract void handleCollision(Sprite s);

    // returns an int[][] specifying what Bitmaps to draw for the sprite
    // each row defines the properties of a single Bitmap to be drawn for the sprite // todo: explain better
    // columns are organized in the following way:
    // element 0: Bitmap ID
    // element 1: x-coordinate to draw Bitmap onto screen
    // element 2: y-coordinate to draw Bitmap onto screen
    // element 3: starting x-coordinate of the Bitmap to start drawing
    // element 4: starting y-coordinate of the Bitmap to start drawing
    // element 5: ending x-coordinate of the Bitmap
    // element 6: ending y-coordinate of the Bitmap
    abstract ArrayList<float[]> getDrawParams();

    // moves sprite using speedX and speedY, updates hitbox,
    // and checks if sprite is still visible
    protected void move() {
        x += GameView.screenW * speedX;
        y += GameView.screenH * speedY;
        // keep in mind sprites are generated past the screen
        if (x > GameView.screenW + width || x < -width) {
            inBounds = false;
        } else if (y > GameView.screenH + height || y < -height) { // todo: bounce off edge of screen
            inBounds = false;
        } else {
            inBounds = true;
        }
        hitBox.updateCoordinates((int) x, (int) y);
    }

    // returns whether hitbox of this sprite intersects hitbox of specified sprite
    public boolean collidesWith(Sprite s) {
        if (!collides || !s.collides)
            return false;
        return hitBox.intersects(s.hitBox);
    }

    // returns distance between centers of sprite hitboxes, as portion of screen width
    public double distanceTo(Sprite s) {
        return Math.sqrt(Math.pow((s.getHitboxCenter().getX() - getHitboxCenter().getX()), 2)
                + Math.pow((s.getHitboxCenter().getY() - getHitboxCenter().getY()), 2)) / GameView.screenW;
    }

    // returns coordinates of center of sprite's hitbox
    // as a Point2D object
    public Point2D getHitboxCenter() {
        return new Point2D(hitBox.getX() + hitBox.getOffsetX() + hitBox.getWidth() / 2,
                hitBox.getY() + hitBox.getOffsetY() + hitBox.getHeight() / 2);
    }

    public boolean getP(double probability) {
        return random.nextInt(1_000_000) + 1 <= probability * 1_000_000;
    }
}
