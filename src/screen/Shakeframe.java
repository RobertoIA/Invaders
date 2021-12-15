package screen;

import static engine.Core.frame;

public class Shakeframe extends Screen {

    private final static int VIB_LENGTH = 20;
    private final static int VIB_VEL = 5;

    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param width  Screen width.
     * @param height Screen height.
     * @param fps
     */
    public Shakeframe(int width, int height, int fps) {
        super(width, height, fps);
    }


    public static void vibrate() {
        try {
            final int originalX = frame.getLocationOnScreen().x;
            final int originalY = frame.getLocationOnScreen().y;
            for(int i = 0; i < VIB_LENGTH; i++) {
                Thread.sleep(10);
                frame.setLocation(originalX, originalY + VIB_VEL);
                Thread.sleep(10);
                frame.setLocation(originalX, originalY - VIB_VEL);
                Thread.sleep(10);
                frame.setLocation(originalX + VIB_VEL, originalY);
                Thread.sleep(10);
                frame.setLocation(originalX, originalY);
            }
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }
}