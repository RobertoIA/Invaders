package screen;

import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

import engine.DrawManager;
import engine.InputManager;
import entity.Bullet;
import entity.EnemyShipFormation;
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
	private DrawManager drawManager;
	private EnemyShipFormation enemyShipFormation;

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
		this.drawManager = DrawManager.getInstance();
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

		addKeyListener(InputManager.getInstance());

		this.ship = new Ship(this, this.width / 2, this.height - 30, 8);
		enemyShipFormation = new EnemyShipFormation(7, 5);
		enemyShipFormation.attach(this);
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
		drawManager.initDrawing(this);

		drawManager.drawEntity(this.ship, this.ship.getPositionX(),
				this.ship.getPositionY());

		enemyShipFormation.draw();

		for (Bullet bullet : this.ship.getBullets())
			drawManager.drawEntity(bullet, bullet.getPositionX(),
					bullet.getPositionY());

		drawManager.completeDrawing(this);
	}
}