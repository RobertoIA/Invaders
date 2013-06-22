package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import engine.Cooldown;
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
	private List<Score> highScores;
	private boolean isNewRecord;
	private char[] name;
	private int nameCharSelected;
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
		this.name = "AAA".toCharArray();
		this.nameCharSelected = 0;
		this.selectionCooldown = Core.getCooldown(200);
		this.selectionCooldown.reset();

		try {
			this.highScores = Core.getFileManager().loadHighScores();
			if (highScores.size() < 7
					|| highScores.get(highScores.size() - 1).getScore() < this.score)
				this.isNewRecord = true;

		} catch (IOException e) {
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
				if (this.isNewRecord)
					saveScore();
			}
			// Play again.
			else if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
				this.returnCode = 2;
				this.isRunning = false;
				if (this.isNewRecord)
					saveScore();
			}

			if (this.isNewRecord && this.selectionCooldown.checkFinished()) {
				if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
					this.nameCharSelected = this.nameCharSelected == 2 ? 0
							: this.nameCharSelected + 1;
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
					this.nameCharSelected = this.nameCharSelected == 0 ? 2
							: this.nameCharSelected - 1;
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_UP)) {
					this.name[this.nameCharSelected] = (char) (this.name[this.nameCharSelected] == 90 ? 65
							: this.name[this.nameCharSelected] + 1);
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_DOWN)) {
					this.name[this.nameCharSelected] = (char) (this.name[this.nameCharSelected] == 65 ? 90
							: this.name[this.nameCharSelected] - 1);
					this.selectionCooldown.reset();
				}
			}
		}

	}

	/**
	 * Saves the score as a high score.
	 */
	private void saveScore() {
		highScores.add(new Score(new String(this.name), score));
		Collections.sort(highScores);
		if (highScores.size() > 7)
			highScores.remove(highScores.size() - 1);

		try {
			Core.getFileManager().saveHighScores(highScores);
		} catch (IOException e) {
			logger.warning("Couldn't load high scores!");
		}
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		if (this.isNewRecord)
			drawManager.drawScoreScreen(this, this.score, this.livesRemaining,
					this.shipsDestroyed, (float) this.shipsDestroyed
							/ this.bulletsShot,
					this.inputDelay.checkFinished(), this.name,
					this.nameCharSelected);
		else
			drawManager
					.drawScoreScreen(this, this.score, this.livesRemaining,
							this.shipsDestroyed, (float) this.shipsDestroyed
									/ this.bulletsShot,
							this.inputDelay.checkFinished());
		drawManager.completeDrawing(this);
	}
}
