package screen;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import engine.Cooldown;
import engine.Core;
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

	private EnemyShipFormation enemyShipFormation;
	private Logger logger;

	private Ship ship;
	private EnemyShip enemyShipSpecial;
	private Cooldown enemyShipSpecialCooldown;
	private Cooldown enemyShipSpecialExplosionCooldown;
	private Set<Bullet> bullets;
	private int formationSizeX;
	private int formationSizeY;
	private int score;
	private int lives;
	private int bulletsShot;
	private int shipsDestroyed;

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
		super(width, height, fps);

		this.logger = Core.getLogger();
	}

	/**
	 * Initializes basic screen properties, and adds necessary elements.
	 */
	public void initialize() {
		super.initialize();

		this.formationSizeX = 7;
		this.formationSizeY = 5;
		enemyShipFormation = new EnemyShipFormation(formationSizeX,
				formationSizeY);
		enemyShipFormation.attach(this);
		this.ship = new Ship(this, this.width / 2, this.height - 30, 8);
		// Appears each 10-30 seconds.
		this.enemyShipSpecialCooldown = Core.getVariableCooldown(20000, 10000);
		this.enemyShipSpecialCooldown.reset();
		this.enemyShipSpecialExplosionCooldown = Core.getCooldown(500);
		this.bullets = new HashSet<Bullet>();
		this.score = 0;
		this.lives = 3;
		this.bulletsShot = 0;
		this.shipsDestroyed = 0;
	}

	/**
	 * Starts the action.
	 */
	public int run() {
		super.run();

		this.score += 100 * (this.lives - 1);
		this.logger.info("Screen cleared with a score of " + this.score);

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected void update() {
		super.update();

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

		if (this.enemyShipSpecial != null) {
			if (!this.enemyShipSpecial.isDestroyed())
				this.enemyShipSpecial.move(2, 0);
			else if (this.enemyShipSpecialExplosionCooldown.checkFinished())
				this.enemyShipSpecial = null;

		}
		if (this.enemyShipSpecial == null
				&& this.enemyShipSpecialCooldown.checkFinished()) {
			this.enemyShipSpecial = new EnemyShip(this);
			this.enemyShipSpecialCooldown.reset();
			this.logger.info("A special ship appears");
		}
		if (this.enemyShipSpecial != null
				&& this.enemyShipSpecial.getPositionX() > this.width) {
			this.enemyShipSpecial = null;
			this.logger.info("The special ship has escaped");
		}

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
		if (this.enemyShipSpecial != null)
			drawManager.drawEntity(this.enemyShipSpecial,
					this.enemyShipSpecial.getPositionX(),
					this.enemyShipSpecial.getPositionY());

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
						this.shipsDestroyed++;
						this.enemyShipFormation.destroy(enemyShip);
						recyclable.add(bullet);
					}
				if (this.enemyShipSpecial != null
						&& !this.enemyShipSpecial.isDestroyed()
						&& checkCollision(bullet, this.enemyShipSpecial)) {
					this.score += this.enemyShipSpecial.getPointValue();
					this.shipsDestroyed++;
					this.enemyShipSpecial.destroy();
					this.enemyShipSpecialExplosionCooldown.reset();
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
	 * Returns bullets shot in the course of the game.
	 * 
	 * @return Number of bullets shot by the player.
	 */
	public int getBulletsShot() {
		return this.bulletsShot;
	}

	/**
	 * Returns ships destroyed in the course of the game.
	 * 
	 * @return Number of enemies shot down by the player.
	 */
	public int getShipsDestroyed() {
		return this.shipsDestroyed;
	}
}