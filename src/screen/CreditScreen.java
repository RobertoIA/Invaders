package screen;
import java.awt.event.KeyEvent;

public class CreditScreen extends Screen {
    //name
    private String name[] = {"Park Yura", "Cha Mi Kyo", "Kim Doo Hyun", "Chung Minho", "Kim Heesoo", "Kim Gyumin"};

    public CreditScreen(final int width, final int height, final int fps) {
        super(width, height, fps);

        this.returnCode = 1;
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
        if (inputManager.isKeyDown(KeyEvent.VK_SPACE)
                && this.inputDelay.checkFinished())
            this.isRunning = false;
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawCreditMenu(this);
        drawManager.drawText(this, name);

        drawManager.completeDrawing(this);
    }
}
