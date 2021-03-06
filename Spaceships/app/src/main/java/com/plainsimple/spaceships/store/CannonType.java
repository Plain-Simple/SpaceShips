package com.plainsimple.spaceships.store;

import com.plainsimple.spaceships.helper.BitmapID;

/**
 * Created by Stefan on 8/27/2016.
 */
public enum CannonType {

    CANNON_0(BitmapID.CANNONS_0, BitmapID.BULLET_0, 5, 12, 0.01f),
    CANNON_1(BitmapID.CANNONS_1, BitmapID.BULLET_1, 8, 10, 0.011f),
    CANNON_2(BitmapID.CANNONS_2, BitmapID.BULLET_2, 12, 8, 0.012f), // todo: graphics
    CANNON_3(BitmapID.CANNONS_3, BitmapID.BULLET_3, 16, 10, 0.013f);

    // R.drawable of the cannon, which is used to draw the spaceship modularly
    private BitmapID spaceshipOverlayId;
    // R.drawable of fired bullet when it is shown on screen
    private BitmapID drawableId;
    // damage bullet does on contact with another sprite
    private int damage;
    // the number of frames that must pass before the Spaceship can fire again
    private int delay;
    // speed the bullet travels when fired
    private float speedX;

    CannonType(BitmapID spaceshipOverlayId, BitmapID drawableId, int damage, int delay, float speedX) {
        this.spaceshipOverlayId = spaceshipOverlayId;
        this.drawableId = drawableId;
        this.damage = damage;
        this.delay = delay;
        this.speedX = speedX;
    }

    public BitmapID getDrawableId() {
        return drawableId;
    }

    public BitmapID getSpaceshipOverlayId() {
        return spaceshipOverlayId;
    }

    public int getDamage() {
        return damage;
    }

    public int getDelay() {
        return delay;
    }

    public float getSpeedX() {
        return speedX;
    }
}
