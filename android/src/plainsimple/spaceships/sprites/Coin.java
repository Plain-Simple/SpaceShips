package plainsimple.spaceships.sprites;

import android.graphics.Rect;
import android.util.Log;
import plainsimple.spaceships.util.BitmapData;
import plainsimple.spaceships.util.DrawParams;
import plainsimple.spaceships.activity.GameActivity;
import plainsimple.spaceships.util.SpriteAnimation;

import java.util.ArrayList;

/**
 * Created by Stefan on 8/28/2015.
 */
public class Coin extends Sprite {

    private SpriteAnimation spin;
    private SpriteAnimation disappear;

    public Coin(BitmapData bitmapData, SpriteAnimation spinAnimation, SpriteAnimation disappearAnimation, int x, int y) {
        super(bitmapData, x, y);
        //spin = new SpriteAnimation(spinAnimation, width, height, 5, true);
        spin = spinAnimation;
        spin.start();
        //disappear = new SpriteAnimation(disappearAnimation, width, height, 1, false);
        disappear = disappearAnimation;
        initObstacle();
    }

    private void initObstacle() {
        hitBox = new Rect(x + (int) (getWidth() * 0.3), y + (int) (getHeight() * 0.1), x + (int) (getWidth() * 0.7), y + (int) (getHeight() * 0.9));
    }

    @Override
    public void updateActions() {
        if (disappear.hasPlayed()) {
            terminate = true;
        }
        if (!isInBounds()) {
            terminate = true;
            Log.d("Termination", "Removing Coin at x = " + x);
        }
    }

    @Override
    public void updateSpeeds() {

    }

    @Override
    public void updateAnimations() {
        if (disappear.isPlaying()) {
            disappear.incrementFrame();
        } else {
            spin.incrementFrame();
        }
    }

    @Override
    public void handleCollision(Sprite s) {
        if (s instanceof Spaceship) {
            //disappear.start();
            collides = false;
            //GameActivity.incrementScore(GameActivity.COIN_VALUE);
        }
    }

    @Override
    public ArrayList<DrawParams> getDrawParams() {
        ArrayList<DrawParams> params = new ArrayList<>(); // todo: store one list that gets reset?
        /*if (disappear.isPlaying()) {
            Rect source = disappear.getCurrentFrameSrc();
            params.add(new DrawParams(disappear.getBitmapID(), x, y, source.left, source.top, source.right, source.bottom));
        } else {*/
            Rect source = spin.getCurrentFrameSrc();
            params.add(new DrawParams(spin.getBitmapID(), x, y, source.left, source.top, source.right, source.bottom));
        //}
        return params;
    }
}
