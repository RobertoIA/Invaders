package screen;

import engine.Cooldown;
import engine.Core;
import engine.Score;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Implements the high scores screen, it shows player records.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class SettingScreen extends Screen {

    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;

    /** List of past high scores. */
    protected int select;
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
    public SettingScreen(final int width, final int height, final int fps, Map<String,Integer> dic) {
        super(width, height, fps);
        this.select = 0;
        this.returnCode = 1;
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

        if (inputManager.isKeyDown(KeyEvent.VK_SPACE) && this.inputDelay.checkFinished()) {
            this.isRunning = false;
        }
        if ((inputManager.isKeyDown(KeyEvent.VK_UP) || inputManager.isKeyDown(KeyEvent.VK_W) || inputManager.isKeyDown(KeyEvent.VK_S) || inputManager.isKeyDown(KeyEvent.VK_DOWN)) && this.selectionCooldown.checkFinished()) {
            changeItem();
            this.selectionCooldown.reset();
        }
    }

    private void changeItem() {
        if (this.select == 0)
            this.select = 1;
        else{
            this.select = 0;
        }
    }
    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);
        drawManager.drawSetting(this);
        drawManager.drawSettingMenu(this,select);

        drawManager.completeDrawing(this);
    }
}