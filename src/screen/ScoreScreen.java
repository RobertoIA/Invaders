package screen;

import java.awt.event.KeyEvent;

import engine.Cooldown;
import engine.Core;
import engine.InputManager;

/**
 * Implements the score screen.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
@SuppressWarnings("serial")
public class ScoreScreen extends Screen {

	private int score;
	private int livesRemaining;
	private int bulletsShot;
	private int shipsDestroyed;
	private boolean playAgain;
	private Cooldown inputCooldown;

	/**
	 * Constructor, establishes the properties of the screen.
	 * 
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 * @param score
	 *            Player score at the end of the game.
	 */
	public ScoreScreen(int width, int height, int fps, int score,
			int livesRemaining, int bulletsShot, int shipsDestroyed) {
		super(width, height, fps);

		this.score = score;
		this.livesRemaining = livesRemaining;
		this.bulletsShot = bulletsShot;
		this.shipsDestroyed = shipsDestroyed;
		// Waits for a second before accepting input.
		this.inputCooldown = Core.getCooldown(1000);
		this.inputCooldown.reset();
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
		if (this.inputCooldown.checkFinished())
			// Return to main menu.
			if (InputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
				this.returnCode = 1;
				this.isRunning = false;
			}
			// Play again.
			else if (InputManager.isKeyDown(KeyEvent.VK_SPACE)) {
				this.returnCode = 2;
				this.isRunning = false;
			}

	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawScoreScreen(this, this.score, this.livesRemaining,
				this.shipsDestroyed, (float) this.shipsDestroyed
						/ this.bulletsShot, this.inputCooldown.checkFinished());

		drawManager.completeDrawing(this);
	}

	/**
	 * Checks if the play again option has been selected.
	 * 
	 * @return Start a new game or not.
	 */
	public boolean playAgain() {
		return this.playAgain;
	}
}
