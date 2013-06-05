package screen;

import java.awt.Insets;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import engine.Core;

/**
 * Implements a generic screen.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
@SuppressWarnings("serial")
public class Screen extends JFrame {

	protected int width;
	protected int height;
	protected int fps;
	protected Insets insets;

	protected boolean isRunning;
	protected int returnCode;

	/**
	 * Constructor, establishes the properties of the screen.
	 * 
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 */
	public Screen(int width, int height, int fps) {
		this.width = width;
		this.height = height;
		this.fps = fps;
		
		this.returnCode = 0;
	}

	/**
	 * Initializes basic screen properties.
	 */
	public void initialize() {
		setSize(this.width, this.height);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLocationRelativeTo(null);
		setVisible(true);
		
		this.insets = getInsets();
		this.width -= this.insets.left + this.insets.right;
		this.height -= this.insets.top + this.insets.bottom;
		setTitle("Invaders");

		addKeyListener(Core.getInputManager());
	}

	/**
	 * Activates the screen.
	 */
	public int run() {
		this.isRunning = true;
		
		while (this.isRunning) {
			long time = System.currentTimeMillis();

			update();

			time = (1000 / this.fps) - (System.currentTimeMillis() - time);
			if (time > 0) {
				try {
					TimeUnit.MILLISECONDS.sleep(time);
				} catch (InterruptedException e) {
					return 0;
				}
			}
		}
		
		return 0;
	}
	
	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected void update() {
		
	}

	/**
	 * Getter for screen width.
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Getter for screen height.
	 */
	public int getHeight() {
		return this.height;
	}
}