package engine;

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

	/**
	 * Test implementation.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		currentScreen = new GameScreen(width, height, fps);
		currentScreen.initialize();
		currentScreen.run();

		System.exit(0);
	}

}