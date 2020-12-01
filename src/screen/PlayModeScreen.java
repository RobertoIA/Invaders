package screen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import engine.Cooldown;
import engine.Core;

/**
 * Implements the high scores screen, it shows player records.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class PlayModeScreen extends Screen {

    private static final int SELECTION_TIME = 200;
    private Cooldown selectionCooldown;


    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param width
     *            Screen width.
     * @param height
     *            Screen height.
     * @param fps
     *            Frames per second, frame rate at which the game is run.
     */
    public PlayModeScreen(final int width, final int height, final int fps) {
        super(width, height, fps);

        this.returnCode = 2;

        this.returnCode = 2;
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();
    }

    /**
     * Starts the action.
     *
     * @return Next screen code.
     */
    public final int run() {
        super.run();

        return this.returnCode;
    }

    /**
     * Updates the elements on screen and checks for events.
     */
    protected final void update() {
        super.update();

        draw();
        if (this.selectionCooldown.checkFinished()
                && this.inputDelay.checkFinished()) {
            if (inputManager.isKeyDown(KeyEvent.VK_UP)
                    || inputManager.isKeyDown(KeyEvent.VK_W)) {
                previousMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) {
                nextMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE))
                this.isRunning = false;
        }
    }

    private void nextMenuItem() {
        if (this.returnCode == 2)
            this.returnCode = 6;
        else if (this.returnCode == 6)
            this.returnCode = 7;
        else
            this.returnCode = 2;
    }

    private void previousMenuItem() {
        if (this.returnCode == 2)
            this.returnCode = 7;
        else if (this.returnCode == 6)
            this.returnCode = 2;
        else
            this.returnCode = 6;
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawPlayModeTitle(this);
        drawManager.drawPlayModeMenu(this, this.returnCode);

        drawManager.completeDrawing(this);
    }
}
