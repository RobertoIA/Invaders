package screen;

import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager;
import engine.InputManager;

/**
 * Implements the score screen.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
@SuppressWarnings("serial")
public class ScoreScreen extends Screen {

	private DrawManager drawManager;
	private int fps;

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
		super(width, height);
		this.fps = fps;
		this.score = score;
		this.livesRemaining = livesRemaining;
		this.bulletsShot = bulletsShot;
		this.shipsDestroyed = shipsDestroyed;
		this.drawManager = Core.getDrawManager();
		// Waits for a second before accepting input.
		this.inputCooldown = Core.getCooldown(1000);
		this.inputCooldown.reset();
	}

	/**
	 * Initializes basic screen properties, and adds necessary elements.
	 */
	public void initialize() {
		super.initialize();

		this.insets = getInsets();
		this.width -= this.insets.left + this.insets.right;
		this.height -= this.insets.top + this.insets.bottom;
		setTitle("Invaders");

		addKeyListener(Core.getInputManager());
	}

	/**
	 * Starts the action.
	 */
	public void run() {
		super.run();

		while (this.isRunning) {
			long time = System.currentTimeMillis();

			update();

			time = (1000 / this.fps) - (System.currentTimeMillis() - time);
			if (time > 0) {
				try {
					TimeUnit.MILLISECONDS.sleep(time);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	private void update() {
		draw();
		if (this.inputCooldown.checkFinished())
			if (InputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
				this.playAgain = false;
				this.isRunning = false;
			} else if (InputManager.isKeyDown(KeyEvent.VK_SPACE)) {
				this.playAgain = true;
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
