import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Stefan on 8/14/2015.
 */
public class Background {

    // available tiles. Element index is Tile ID
    private Image[] tiles;

    // grid of tile ID's instructing how to display background
    private int[][] background;

    private BufferedImage currentImage;

    // dimensions of screen display
    private final int SCREEN_WIDTH = 400;
    private final int SCREEN_HEIGHT = 300;

    // coordinates of upper-left of "window" being shown
    private int x;
    private int y;

    // dimensions of tiles
    private int tileWidth;
    private int tileHeight;

    private final Random random;

    // construct tiles using names of tile files
    public Background(String[] tiles) {
        this.tiles = new Image[tiles.length];
        for(int i = 0; i < tiles.length; i++) {
            this.tiles[i] = new ImageIcon(tiles[i]).getImage();
        }

        this.x = 0;
        this.y = 0;

        tileWidth = this.tiles[0].getWidth(null);
        tileHeight = this.tiles[0].getHeight(null);

        currentImage = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);

        random = new Random();

        background = new int[6][8];
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 8; j++) {
                background[i][j] = random.nextInt(4);
            }
        };
    }

    // returns current part of background being displayed
    public Image getCurrentImage() {
        Graphics2D g = currentImage.createGraphics();
        for(int i = 0; i < 6; i++) { // rows
            for(int j = 0; j < 8; j++) { // columns
                int loc_x = j * tileWidth;
                int loc_y = i * tileHeight;
                g.drawImage(tiles[background[i][j]], loc_x, loc_y, null);
            }
        }
        return currentImage;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // shifts window and updates currentImage
    // will shift as far as possible without going past edge of defaultImage
    // if window is at the edge of defaultImage, does nothing
    /*public Image scroll(int x) {
        this.x += x;


        currentImage = currentImage.getSubimage(this.x, this.y, SCREEN_WIDTH, SCREEN_HEIGHT);
        return currentImage;
    }

    // sends window to (x,y)
    public Image goTo(int x, int y) {
        this.x = x;
        this.y = y;
        return scroll(0, 0);
    }*/
}
