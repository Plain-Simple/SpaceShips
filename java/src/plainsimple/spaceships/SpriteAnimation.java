package plainsimple.spaceships;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Stefan on 8/13/2015.
 */
public class SpriteAnimation {

    // whether or not to loop animation
    private boolean loop;

    // whether or not this animation is playing
    private boolean isPlaying;

    // wether or not animation has already played
    private boolean hasPlayed;

    // frames of the animation to play in order
    private BufferedImage[] frames;

    // current position in array of frames
    private int frameCounter;

    // number of frames to display each sprite
    private int frameSpeed;

    // counts number of frames current sprite has been shown
    private int frameSpeedCounter;

    // reads in spritesheet consisting of one row of sprites
    // initializes all frames now so as to cut down on processing time later
    public SpriteAnimation(String spriteSheetPath, int frameWidth,
                           int frameHeight, int frameSpeed, boolean loop) throws IOException {

        BufferedImage sheet = ImageIO.read(new File(spriteSheetPath));

        int frames_w = sheet.getWidth(null) / frameWidth;
        int frames_h = sheet.getHeight(null) / frameHeight;

        frames = new BufferedImage[frames_w * frames_h];

        for (int i = 0; i < frames_w; i++) {
            for (int j = 0; j < frames_h; j++) {
                frames[i] = sheet.getSubimage(i * frameWidth, j * frameHeight, frameWidth, frameHeight);
            }
        }

        this.frameSpeed = frameSpeed;
        this.frameSpeedCounter = 0;

        frameCounter = 0;
        hasPlayed = false;
        this.loop = loop;
        isPlaying = false;
    }

    // resets fields so that nextFrame() can play animation
    public void start() {
        isPlaying = true;
        frameCounter = 0;
        frameSpeedCounter = 0;
    }

    // whether animation has finished or not
    public boolean isPlaying() {
        return isPlaying;
    }

    // returns next image in animation
    // starts animation if it is not playing already
    public BufferedImage nextFrame() {
        if (!isPlaying) // todo: good practice is to start animation before calling nextFrame()
            start();

        if (frameSpeedCounter == frameSpeed) {
            frameCounter++;
            frameSpeedCounter = 0;
        } else {
            frameSpeedCounter++;
        }

        if (loop) { // reached end of loop, start from beginning
            if (frameCounter == frames.length) {
                frameCounter = 0;
                hasPlayed = true;
            }
        } else { // reached end of loop
            if (frameCounter == frames.length - 1) {
                isPlaying = false;
                hasPlayed = true;
            }
        }
        return frames[frameCounter];
    }

    // stops animation
    public void stop() {
        isPlaying = false;
        frameCounter = 0;
    }

}
