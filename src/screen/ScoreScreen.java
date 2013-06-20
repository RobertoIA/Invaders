package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import engine.Core;
import engine.Score;

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
	private boolean isNewRecord;

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
		this.isNewRecord = false;

		try {
			List<Score> highScores = Core.getFileManager().loadHighScores();
			if (highScores.size() < 7) {
				this.isNewRecord = true;
				// TODO placeholder player name.
				highScores.add(new Score("AAA", score));
				Collections.sort(highScores);

				Core.getFileManager().saveHighScores(highScores);
			} else if (highScores.get(highScores.size() - 1).getScore() < this.score) {
				this.isNewRecord = true;
				// TODO placeholder player name.
				highScores.add(new Score("AAA", score));
				Collections.sort(highScores);
				highScores.remove(highScores.size() - 1);

				Core.getFileManager().saveHighScores(highScores);
			}
		} catch (NumberFormatException | IOException e) {
			logger.warning("Couldn't load high scores!");
		}
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
		if (this.inputDelay.checkFinished()) {
			// Return to main menu.
			if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
				this.returnCode = 1;
				this.isRunning = false;
			}
			// Play again.
			else if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
				this.returnCode = 2;
				this.isRunning = false;
			}
		}

	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawScoreScreen(this, this.score, this.livesRemaining,
				this.shipsDestroyed, (float) this.shipsDestroyed
						/ this.bulletsShot, this.inputDelay.checkFinished(),
				this.isNewRecord);

		drawManager.completeDrawing(this);
	}
}
