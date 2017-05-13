package com.plainsimple.spaceships.sprite;

import android.content.Context;

import com.plainsimple.spaceships.helper.BitmapCache;
import com.plainsimple.spaceships.helper.BitmapID;
import com.plainsimple.spaceships.store.CannonType;
import com.plainsimple.spaceships.helper.DrawImage;
import com.plainsimple.spaceships.helper.DrawParams;
import com.plainsimple.spaceships.helper.Hitbox;

import java.util.List;

/**
 * Created by Stefan on 8/17/2015.
 */
public class Bullet extends Sprite {

    private BitmapID bitmapID;

    public Bullet(float x, float y, Context context, CannonType cannonType) {
        super(x, y, BitmapCache.getData(cannonType.getDrawableId(), context));
        hitBox = new Hitbox(x + getWidth() * 0.7f, y - getHeight() * 0.2f, x + getWidth() * 1.5f, y + getHeight() * 1.2f);
        hp = cannonType.getDamage();
        speedX = cannonType.getSpeedX();
        bitmapID = cannonType.getDrawableId();
    }

    @Override
    public void updateActions() {
        if (!isInBounds()) {
            terminate = true;
        }
    }

    @Override
    public void updateSpeeds() {

    }

    @Override
    public void updateAnimations() {

    }

    @Override
    public void handleCollision(Sprite s, int damage) {
        collides = false;
        terminate = true;
    }

    @Override
    public List<DrawParams> getDrawParams() {
        drawParams.clear();
        drawParams.add(new DrawImage(bitmapID, x, y));
        return drawParams;
    }
}
