package screen;

import java.awt.event.KeyEvent;

import engine.Cooldown;
import engine.Core;
import engine.InputManager;

@SuppressWarnings("serial")
public class HighScoreScreen extends Screen {

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
	public HighScoreScreen(int width, int height, int fps) {
		super(width, height, fps);

		this.returnCode = 1;
		this.selectionCooldown = Core.getCooldown(200);
		this.selectionCooldown.reset();
	}

	/**
	 * Starts the action.
	 */
	public int run() {
		super.run();

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected void update() {
		super.update();

		draw();
		if (this.selectionCooldown.checkFinished()
				&& InputManager.isKeyDown(KeyEvent.VK_SPACE))
			this.isRunning = false;
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);
		
		drawManager.drawHighScoreScreen(this, Core.getHighScores());

		drawManager.completeDrawing(this);
	}
}
