package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import engine.Cooldown;
import engine.Core;
import engine.GameState;
import engine.Score;

import entity.Pair;

/**
 * Implements the score screen.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class ScoreScreen extends Screen {

	/** Milliseconds between changes in user selection. */
	private static final int SELECTION_TIME = 200;
	/** Maximum number of high scores. */
	private static final int MAX_HIGH_SCORE_NUM = 7;
	/** Code of first mayus character. */
	private static final int FIRST_CHAR = 65;
	/** Code of last mayus character. */
	private static final int LAST_CHAR = 90;

	/** Current score. */
	private Pair score;
	/** Player lives left. */
	private Pair livesRemaining;
	/** Total bullets shot by the player. */
	private Pair bulletsShot;
	/** Total ships destroyed by the player. */
	private Pair shipsDestroyed;
	/** Current Players' numbers.*/
	private int playerCode;
	/** List of past high scores. */
	private List<Score> highScores;
	/** Checks if current score is a new high score. */
	private boolean isNewRecord;
	/** Player name for record input. */
	private char[] p1name;
	/** Character of players name selected for change. */
	private int nameCharSelected;
	/** Time between changes in user selection. */
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
	 * @param gameState
	 *            Current game state.
	 */
	public ScoreScreen(final int width, final int height, final int fps,
			final GameState gameState) {
		super(width, height, fps);

		this.score = gameState.getScore();
		this.livesRemaining = gameState.getLivesRemaining();
		this.bulletsShot = gameState.getBulletsShot();
		this.shipsDestroyed = gameState.getShipsDestroyed();
		this.playerCode = gameState.getPlayerCode(); // 1 or 2
		this.isNewRecord = false;
		this.p1name = "AAA".toCharArray();
		this.nameCharSelected = 0;
		this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
		this.selectionCooldown.reset();

		try {
			this.highScores = Core.getFileManager().loadHighScores();
			if (highScores.size() < MAX_HIGH_SCORE_NUM
					|| highScores.get(highScores.size() - 1).getScore()
					< this.score.getPlayer1Value() || highScores.get(highScores.size() - 1).getScore()
					< this.score.getPlayer2Value())
				this.isNewRecord = true;

		} catch (IOException e) {
			logger.warning("Couldn't load high scores!");
		}
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
		if (this.inputDelay.checkFinished()) {
			if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
				// Return to main menu.
				this.returnCode = 1;
				this.isRunning = false;
				if (this.isNewRecord)
					saveScore();
			} else if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
				// Play again.
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
					this.p1name[this.nameCharSelected] =
							(char) (this.p1name[this.nameCharSelected]
									== LAST_CHAR ? FIRST_CHAR
							: this.p1name[this.nameCharSelected] + 1);
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_DOWN)) {
					this.p1name[this.nameCharSelected] =
							(char) (this.p1name[this.nameCharSelected]
									== FIRST_CHAR ? LAST_CHAR
							: this.p1name[this.nameCharSelected] - 1);
					this.selectionCooldown.reset();
				}
			}
		}

	}

	/**
	 * Saves the score as a high score.
	 */
	private void saveScore() {
		highScores.add(new Score(new String(this.p1name), Math.max(score.getPlayer1Value(), score.getPlayer2Value())));

		Collections.sort(highScores);
		if (highScores.size() > MAX_HIGH_SCORE_NUM)
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

		drawManager.drawGameOver(this, this.inputDelay.checkFinished(),
				this.isNewRecord);

		if (playerCode == 1) {
			drawManager.drawResults(this, this.score.getPlayer1Value(), this.livesRemaining.getPlayer1Value(),
					this.shipsDestroyed.getPlayer1Value(), (float) this.shipsDestroyed.getPlayer1Value()
							/ this.bulletsShot.getPlayer1Value(), this.isNewRecord);
		}
		else if (playerCode == 2) {
			drawManager.draw2PResults(this, this.score.getPlayer1Value(), this.score.getPlayer2Value(),
										this.livesRemaining.getPlayer1Value(), this.livesRemaining.getPlayer2Value(),
										this.shipsDestroyed.getPlayer1Value(), this.shipsDestroyed.getPlayer2Value(),
								(float) this.shipsDestroyed.getPlayer1Value() / this.bulletsShot.getPlayer1Value(),
								(float) this.shipsDestroyed.getPlayer2Value() / this.bulletsShot.getPlayer2Value(),
										this.isNewRecord);
		}
		else {
			logger.warning("Player Code Error on ScoreScreen");
		}

		if (this.isNewRecord)
			drawManager.drawNameInput(this, this.p1name, this.nameCharSelected);

		drawManager.completeDrawing(this);
	}
}
