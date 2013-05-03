package screen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import engine.InputManager;
import entity.Ship;

/**
 * Implements the game screen, where the action happens.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
@SuppressWarnings("serial")
public class GameScreen extends Screen {

	private int fps;

	private Ship ship;

	/**
	 * Constructor, establishes the properties of the screen.
	 * 
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public GameScreen(int width, int height, int fps) {
		super(width, height);
		this.fps = fps;
	}

	/**
	 * Initializes basic screen properties, and adds necessary elements.
	 */
	public void initialize() {
		super.initialize();

		this.insets = getInsets();
		this.width -= this.insets.left + this.insets.right;
		this.height -= this.insets.top + this.insets.bottom;
		setTitle("Game Screen");

		addKeyListener(InputManager.getInstance());
		
		this.ship = new Ship(this, this.width / 2, this.height - 30, 10);
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
		if (InputManager.isKeyDown(KeyEvent.VK_RIGHT)
				|| InputManager.isKeyDown(KeyEvent.VK_D))
			this.ship.moveRight();
		if (InputManager.isKeyDown(KeyEvent.VK_LEFT)
				|| InputManager.isKeyDown(KeyEvent.VK_A))
			this.ship.moveLeft();
		if (InputManager.isKeyDown(KeyEvent.VK_SPACE))
			this.ship.shoot();
		draw();
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		BufferedImage backBuffer = new BufferedImage(this.width, this.height,
				BufferedImage.TYPE_INT_RGB);
		Graphics graphics = getGraphics();
		Graphics backBufferGraphics = backBuffer.getGraphics();

		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics.fillRect(0, 0, this.width, this.height);

		this.ship.draw(backBufferGraphics);

		graphics.drawImage(backBuffer, insets.left, insets.top, this);
	}
}