package screen;

import java.awt.*;
import java.awt.event.KeyEvent;
import engine.Cooldown;
import engine.Core;

/**
 * Implements the Settings screen, it shows the options to change the settings of the game.
 */

public class SettingsScreen extends Screen {
    /** Number of options in the settings menu. */
    private static final int NO_OF_OPTIONS = 4;
    /** Current settings option. */
    private static int settingsOption;
    /** Change settings. */
    private static int change;
    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;
    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;
    /** Get screen size using the Toolkit class */
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

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
    public SettingsScreen(final int width, final int height, final int fps) {
        super(width, height, fps);

        this.returnCode = 1;
        this.settingsOption = 1;
        this.change = 1;

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
                this.change = 1;
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) {
                nextMenuItem();
                this.change = 1;
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_LEFT)
                    || inputManager.isKeyDown(KeyEvent.VK_A)) {
                previousMenuChange();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                    || inputManager.isKeyDown(KeyEvent.VK_D)) {
                nextMenuChange();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE))
                this.isRunning = false;

            // Change screen size
            if (settingsOption == 1) {
                changeScreenSize();
            }
        }
    }

    /**
     * Changes the screen size
     */
    private void changeScreenSize() {
        if (this.change == 1)
            Core.setSize(448, 520);
        else if (this.change == 2)
            Core.setSize(1020,520);
        else if (this.change == 3)
            Core.setSize(screenSize.width, screenSize.height);
    }

    /**
     * Shifts the focus to the next menu item.
     */
    private void nextMenuItem() {
        if (this.settingsOption == NO_OF_OPTIONS)
            this.settingsOption = 1;
        else if (this.settingsOption == 1)
            this.settingsOption = 2;
        else
            this.settingsOption++;
    }

    /**
     * Shifts the focus to the previous menu item.
     */
    private void previousMenuItem() {
        if (this.settingsOption == 1)
            this.settingsOption = NO_OF_OPTIONS;
        else if (this.settingsOption == 2)
            this.settingsOption = 1;
        else
            this.settingsOption--;
    }

    /**
     * Shifts the focus to the next change in the settings option.
     */
    private void nextMenuChange() {
        int no_of_changes = 3;

        if(this.settingsOption == 4)
            no_of_changes = 4;

        if (this.change == no_of_changes)
            this.change = 1;
        else if (this.change == 1)
            this.change = 2;
        else
            this.change++;
    }

    /**
     * Shifts the focus to the previous change in the settings option.
     */
    private void previousMenuChange() {
        int no_of_changes;

        if(this.settingsOption == 4)
            no_of_changes = 4;
        else
            no_of_changes = 3;

        if (this.change == 1)
            this.change = no_of_changes;
        else if (this.change == 2)
            this.change = 1;
        else
            this.change--;
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);
        drawManager.drawSettings(this);
        drawManager.drawSettingsChange(this, settingsOption, change);
        drawManager.drawSettingsOptions(this, settingsOption);
        drawManager.completeDrawing(this);
    }
}