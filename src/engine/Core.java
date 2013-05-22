package engine;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import screen.GameScreen;
import screen.Screen;

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
			logger.setLevel(Level.INFO);

		} catch (Exception e) {
			// TODO handle exception
			e.printStackTrace();
		}

		logger.info("Starting " + width + "x" + height + " game screen at "
				+ fps + " fps.");
		currentScreen = new GameScreen(width, height, fps);
		currentScreen.initialize();
		currentScreen.run();
		logger.info("Closing game screen.");

		fileHandler.flush();
		fileHandler.close();
		System.exit(0);
	}

	public static Logger getLogger() {
		return logger;
	}
}