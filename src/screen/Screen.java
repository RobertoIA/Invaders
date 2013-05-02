package screen;

import java.awt.Insets;

import javax.swing.JFrame;

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
	protected Insets insets;

	protected boolean isRunning;

	/**
	 * Constructor, establishes the properties of the screen.
	 * 
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 */
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
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
	}

	/**
	 * Activates the screen.
	 */
	public void run() {
		this.isRunning = true;
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