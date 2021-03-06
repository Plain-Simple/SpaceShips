package com.plainsimple.spaceships.sprite;

import android.content.Context;

import com.plainsimple.spaceships.helper.BitmapCache;
import com.plainsimple.spaceships.helper.BitmapID;
import com.plainsimple.spaceships.helper.DrawImage;
import com.plainsimple.spaceships.helper.DrawParams;
import com.plainsimple.spaceships.helper.HealthBarAnimation;
import com.plainsimple.spaceships.helper.FloatRect;
import com.plainsimple.spaceships.helper.LoseHealthAnimation;
import com.plainsimple.spaceships.stats.GameStats;
import com.plainsimple.spaceships.view.GameView;

import java.util.LinkedList;
import java.util.List;

/**
 * The Asteroid is a fairly slow-moving sprite that rotates. It bounces
 * off the edges of the screen and has relatively high hp. Rotation rate
 * as well as speedY are randomized to give each Asteroid an element of
 * uniqueness.
 */

public class Asteroid extends Sprite {

    private static final BitmapID BITMAP_ID = BitmapID.ASTEROID;
    // current rotation, in degrees, of asteroid
    private float currentRotation;
    // degrees rotated per frame (positive or negative)
    private float rotationRate;

    // draws animated healthbar above Asteroid if Asteroid takes damage
    private HealthBarAnimation healthBarAnimation;
    // stores any running animations showing Asteroid taking damage
    private List<LoseHealthAnimation> loseHealthAnimations = new LinkedList<>();

    public Asteroid(float x, float y, float scrollSpeed, int difficulty, Context context) {
        super(x, y, BitmapCache.getData(BITMAP_ID, context));
        // speedX: slower than scrollspeed: give the player a chance to destroy it
        speedX = scrollSpeed * 0.6f;
        // speedY: randomized positive/negative and up to |0.03| or so
        speedY = (random.nextBoolean() ? -1 : +1) * random.nextFloat() / 120;
        // hp: high
        hp = 40 + difficulty / 100;
        // make hitbox 20% smaller than sprite
        hitBox = new FloatRect(x + getWidth() * 0.1f, y + getHeight() * 0.1f, x + getWidth() * 0.9f, y + getHeight() * 0.9f);
        // set the current rotation to a random angle
        currentRotation = random.nextInt(360);
        // random rotation rate. function of speedY (faster speed = faster rotation)
        rotationRate = speedY * 200;
        // init HealthBarAnimation for use if Asteroid takes damage
        healthBarAnimation = new HealthBarAnimation(getWidth(), getHeight(), hp);

    }

    @Override
    public void updateActions() {
        if (!isInBounds() || hp <= 0) {
            terminate = true;
        }
    }

    @Override
    public void updateSpeeds() {
        // reverse speedY if it is nearly headed off a screen edge
        if ((y <= 0 && speedY < 0) || (y >= GameView.playScreenH - getHeight() && speedY > 0)) {
            speedY *= -1;
        }
    }

    @Override
    public void updateAnimations() {
        // increment currentRotation to create the rotating animation
        currentRotation += rotationRate;
    }

    @Override
    public void handleCollision(Sprite s, int damage) {
        takeDamage(damage);
        // increment score and start HealthBarAnimation and LoseHealthAnimations
        // if Asteroid took damage and isn't dead.
        if (!dead && damage > 0) {
            GameView.incrementScore(damage);
            healthBarAnimation.start();
            loseHealthAnimations.add(new LoseHealthAnimation(getWidth(), getHeight(),
                    s.getX() - x, s.getY() - y, damage));
        }
        // if hp has hit zero and dead is false, set it to true.
        // This means hp has hit zero for the first time, and
        // Asteroid was "killed" by the collision.
        if (hp == 0 && !dead) {
            dead = true;
            // update current GameStats to reflect an Asteroid kill
            GameView.currentStats.addTo(GameStats.ASTEROIDS_KILLED, 1);
        }
    }

    private DrawImage DRAW_ASTEROID = new DrawImage(BITMAP_ID);

    @Override
    public List<DrawParams> getDrawParams() {
        drawParams.clear();

        // update DRAW_ASTEROID params with new coordinates and rotation
        DRAW_ASTEROID.setCanvasX0(x);
        DRAW_ASTEROID.setCanvasY0(y);
        DRAW_ASTEROID.setRotation((int) currentRotation);
        drawParams.add(DRAW_ASTEROID);

        // draw loseHealthAnimations
        for (int i = 0; i < loseHealthAnimations.size(); i++) {
            if (!loseHealthAnimations.get(i).isFinished()) {
                loseHealthAnimations.get(i).updateAndDraw(x, y, drawParams);
            }
        }

        // update and draw healthBarAnimation if showing
        if (healthBarAnimation.isShowing()) {
            healthBarAnimation.updateAndDraw(x, y, hp, drawParams);
        }
        return drawParams;
    }
}
