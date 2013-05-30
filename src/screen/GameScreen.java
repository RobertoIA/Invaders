package screen;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import engine.Core;
import engine.DrawManager;
import engine.InputManager;
import entity.Bullet;
import entity.BulletPool;
import entity.EnemyShip;
import entity.EnemyShipFormation;
import entity.Entity;
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
	private Logger logger;

	private Ship ship;
	private Set<Bullet> bullets;
	private int formationSizeX;
	private int formationSizeY;
	private int score;
	private int lives;
	private int bulletsShot;

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
		this.drawManager = Core.getDrawManager();
		this.logger = Core.getLogger();
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

		this.formationSizeX = 7;
		this.formationSizeY = 5;
		enemyShipFormation = new EnemyShipFormation(formationSizeX,
				formationSizeY);
		enemyShipFormation.attach(this);
		this.ship = new Ship(this, this.width / 2, this.height - 30, 8);
		this.bullets = new HashSet<Bullet>();
		this.score = 0;
		this.lives = 3;
		this.bulletsShot = 0;
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

		this.score += 100 * (this.lives - 1);
		this.logger.info("Screen cleared with a score of " + this.score);
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
			if (this.ship.shoot(this.bullets))
				this.bulletsShot++;

		this.enemyShipFormation.shoot(this.bullets);

		manageCollisions();
		cleanBullets();
		draw();

		if (this.enemyShipFormation.isEmpty() || this.lives == 0)
			this.isRunning = false;
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawEntity(this.ship, this.ship.getPositionX(),
				this.ship.getPositionY());

		enemyShipFormation.draw();

		for (Bullet bullet : this.bullets)
			drawManager.drawEntity(bullet, bullet.getPositionX(),
					bullet.getPositionY());

		// Interface.
		drawManager.drawScore(this, this.score);
		drawManager.drawLives(this, this.lives);
		drawManager.drawSeparatingLine(this);

		drawManager.completeDrawing(this);
	}

	/**
	 * Cleans bullets that go off screen.
	 */
	private void cleanBullets() {
		Set<Bullet> recyclable = new HashSet<Bullet>();
		for (Bullet bullet : this.bullets) {
			bullet.update();
			if (bullet.getPositionY() < 40
					|| bullet.getPositionY() > this.height)
				recyclable.add(bullet);
		}
		this.bullets.removeAll(recyclable);
		BulletPool.recycle(recyclable);
	}

	/**
	 * Manages collisions between bullets and ships.
	 */
	private void manageCollisions() {
		Set<Bullet> recyclable = new HashSet<Bullet>();
		for (Bullet bullet : this.bullets)
			if (bullet.getSpeed() > 0) {
				if (checkCollision(bullet, this.ship)) {
					recyclable.add(bullet);
					this.lives--;
					this.logger.info("Hit on player ship, " + this.lives
							+ " lives remaining.");
				}
			} else {
				for (EnemyShip enemyShip : this.enemyShipFormation)
					if (!enemyShip.isDestroyed()
							&& checkCollision(bullet, enemyShip)) {
						this.score += enemyShip.getPointValue();
						this.enemyShipFormation.destroy(enemyShip);
						recyclable.add(bullet);
					}
			}
		this.bullets.removeAll(recyclable);
		BulletPool.recycle(recyclable);
	}

	/**
	 * Checks if two entities are colliding.
	 * 
	 * @param a
	 *            First entity, the bullet.
	 * @param b
	 *            Second entity, the ship.
	 * @return Result of the collision test.
	 */
	private boolean checkCollision(Entity a, Entity b) {
		// Calculate center point of the entities in both axis.
		int centerAX = a.getPositionX() + a.getWidth() / 2;
		int centerAY = a.getPositionY() + a.getHeight() / 2;
		int centerBX = b.getPositionX() + b.getWidth() / 2;
		int centerBY = b.getPositionY() + b.getHeight() / 2;
		// Calculate maximum distance without collision.
		int maxDistanceX = a.getWidth() / 2 + b.getWidth() / 2;
		int maxDistanceY = a.getHeight() / 2 + b.getHeight() / 2;
		// Calculates distance.
		int distanceX = Math.abs(centerAX - centerBX);
		int distanceY = Math.abs(centerAY - centerBY);

		return distanceX < maxDistanceX && distanceY < maxDistanceY;
	}

	/**
	 * Returns current score.
	 * 
	 * @return Score in points.
	 */
	public int getScore() {
		return this.score;
	}

	/**
	 * Returns current number of lives.
	 * 
	 * @return Lives remaining.
	 */
	public int getLives() {
		return this.lives;
	}

	/**
	 * Returns percentage of bullets that hit the target.
	 * 
	 * @return Accuracy percentage.
	 */
	public float getAccuracy() {
		int shipsDestroyed = this.formationSizeX * this.formationSizeY
				- this.enemyShipFormation.getShipCount();
		return (float) shipsDestroyed / this.bulletsShot;
	}
}