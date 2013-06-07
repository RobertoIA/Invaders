package engine;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import screen.GameScreen;
import screen.ScoreScreen;
import screen.Screen;
import screen.TitleScreen;

/**
 * Implements core game logic.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Core {

	private static Screen currentScreen;
	private static final int width = 448;
	private static final int height = 520;
	private static int fps = 60;

	private static final Logger logger = Logger.getLogger(Core.class
			.getSimpleName());
	private static Handler fileHandler;

	/**
	 * Test implementation.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			fileHandler = new FileHandler("log");
			fileHandler.setFormatter(new SimpleFormatter());

			// Removes console handler.
			// logger.setUseParentHandlers(false);

			logger.addHandler(fileHandler);
			logger.setLevel(Level.ALL);

		} catch (Exception e) {
			// TODO handle exception
			e.printStackTrace();
		}

		int score;
		int livesRemaining;
		int bulletsShot;
		int shipsDestroyed;

		int returnCode = 1;
		do {
			if (currentScreen != null)
				currentScreen.dispose();
			
			switch (returnCode) {
			case 1:
				// Main menu.
				currentScreen = new TitleScreen(width, height, fps);
				logger.info("Starting " + width + "x" + height + " title screen at "
						+ fps + " fps.");
				currentScreen.initialize();
				returnCode = currentScreen.run();
				logger.info("Closing title screen.");
				break;
			case 2:
				// Game & score.
				currentScreen = new GameScreen(width, height, fps);
				logger.info("Starting " + width + "x" + height + " game screen at "
						+ fps + " fps.");
				currentScreen.initialize();
				currentScreen.run();
				logger.info("Closing game screen.");
				
				score = ((GameScreen) currentScreen).getScore();
				livesRemaining = ((GameScreen) currentScreen).getLives();
				bulletsShot = ((GameScreen) currentScreen).getBulletsShot();
				shipsDestroyed = ((GameScreen) currentScreen).getShipsDestroyed();
				currentScreen.dispose();

				logger.info("Starting " + width + "x" + height
						+ " score screen at " + fps + " fps, with a score of "
						+ score + ", " + livesRemaining + " lives remaining, "
						+ bulletsShot + " bullets shot and " + shipsDestroyed
						+ " ships destroyed.");
				currentScreen = new ScoreScreen(width, height, fps, score,
						livesRemaining, bulletsShot, shipsDestroyed);
				currentScreen.initialize();
				returnCode = currentScreen.run();
				logger.info("Closing score screen.");
				break;
			case 3:
				// High scores.
				//TODO high scores screen.
				break;
			default:
				break;
			}

		} while (returnCode != 0);

		fileHandler.flush();
		fileHandler.close();
		System.exit(0);
	}

	/**
	 * Controls access to the logger.
	 * 
	 * @return Application logger.
	 */
	public static Logger getLogger() {
		return logger;
	}

	/**
	 * Controls access to the drawing manager.
	 * 
	 * @return Application draw manager.
	 */
	public static DrawManager getDrawManager() {
		return DrawManager.getInstance();
	}

	/**
	 * Controls access to the input manager.
	 * 
	 * @return Application input manager.
	 */
	public static InputManager getInputManager() {
		return InputManager.getInstance();
	}

	/**
	 * Controls creation of new cooldowns.
	 * 
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @return A new cooldown.
	 */
	public static Cooldown getCooldown(int milliseconds) {
		return new Cooldown(milliseconds);
	}

	/**
	 * Controls creation of new cooldowns with variance.
	 * 
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @param variance
	 *            Variation in the cooldown duration.
	 * @return A new cooldown with variance.
	 */
	public static Cooldown getVariableCooldown(int milliseconds, int variance) {
		return new Cooldown(milliseconds, variance);
	}
}