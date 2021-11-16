package screen;


import java.awt.event.KeyEvent;


public class RuleScreen extends Screen {

    /** List of rule. */
    String ruleList[] = new String[]{
            "The initial number of lives is 3.",
            "The player : ",
            "controls the cannon horizontally.",
            "If an alien is shot by the cannon,",
            "it is destroyed",
            "Each eliminated alien is worth 10 pts."
    };


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
    public RuleScreen(final int width, final int height, final int fps) {
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
        drawManager.ruleMenu(this);
        drawManager.drawRule(this, this.ruleList);
        drawManager.completeDrawing(this);
    }
}
