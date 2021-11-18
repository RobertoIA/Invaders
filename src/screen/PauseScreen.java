package screen;

import engine.Cooldown;
import engine.Core;
import engine.GameState;

import java.awt.event.KeyEvent;

public class PauseScreen<selectionCooldown> extends Screen {

    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;
    private final Cooldown selectionCooldown;

    public PauseScreen(final int width, final int height, final int fps) {
        super(width, height, fps);

        // Defaults to pause.
        this.returnCode = 2; // pause -> main menu 대체
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
        

        if (this.selectionCooldown.checkFinished()
                && this.inputDelay.checkFinished()) {
            if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) {
                nextCheckOption();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_UP)
                    || inputManager.isKeyDown(KeyEvent.VK_W)){
                previousCheckOption();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE))
                this.isRunning = false;

            if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
                this.returnCode = 2;
                this.isRunning = false;
            }
        }



    }

    /**다음 옵션*/
    private void nextCheckOption() {
        if(this.returnCode == 2)
            this.returnCode = 6;
    }

    /**그전 선택지*/
    private void previousCheckOption() {
        if (this.returnCode == 6)
            this.returnCode = 2;
    }



}
