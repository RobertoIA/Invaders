package screen;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import engine.Cooldown;
import engine.Core;
import engine.GameSettings;
import engine.GameState;
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
public class GameScreen extends Screen {

	/** Milliseconds until the screen accepts user input. */
	private static final int INPUT_DELAY = 6000;
	/** Bonus score for each life remaining at the end of the level. */
	private static final int LIFE_SCORE = 100;
	/** Minimum time between bonus ship's appearances. */
	private static final int BONUS_SHIP_INTERVAL = 20000;
	/** Maximum variance in the time between bonus ship's appearances. */
	private static final int BONUS_SHIP_VARIANCE = 10000;
	/** Time until bonus ship explosion disappears. */
	private static final int BONUS_SHIP_EXPLOSION = 500;
	/** Time from finishing the level to screen change. */
	private static final int SCREEN_CHANGE_INTERVAL = 1500;
	/** Height of the interface separation line. */
	public static final int SEPARATION_LINE_HEIGHT = 40;

	/** Current game difficulty settings. */
	private GameSettings gameSettings;
	/** Current difficulty level number. */
	private int level;
	/** Formation of enemy ships. */
	private EnemyShipFormation enemyShipFormation;
	/** Player's ship. */
	private Ship ship_1;
	/** Player's ship. */
	private Ship ship_2;
	/** Bonus enemy ship that appears sometimes. */
	private EnemyShip enemyShipSpecial;
	/** Minimum time between bonus ship appearances. */
	private Cooldown enemyShipSpecialCooldown;
	/** Time until bonus ship explosion disappears. */
	private Cooldown enemyShipSpecialExplosionCooldown;
	/** Time from finishing the level to screen change. */
	private Cooldown screenFinishedCooldown;
	/** Set of all bullets fired by on screen ships. */
	private Set<Bullet> bullets_en;
	/** Set of all bullets fired by on screen ships. */
	private Set<Bullet> bullets_p1;
	/** Set of all bullets fired by on screen ships. */
	private Set<Bullet> bullets_p2;
	/** Current score. */
	private int score_p1;
	/** Player lives left. */
	private int lives_p1;
	/** Total bullets shot by the player. */
	private int bulletsShot_p1;
	/** Total ships destroyed by the player. */
	private int shipsDestroyed_p1;
	/** Current score. */
	private int score_p2;
	/** Player lives left. */
	private int lives_p2;
	/** Total bullets shot by the player. */
	private int bulletsShot_p2;
	/** Total ships destroyed by the player. */
	private int shipsDestroyed_p2;
	/** Moment the game starts. */
	private long gameStartTime;
	/** Checks if the level is finished. */
	private boolean levelFinished;
	/** Checks if a bonus life is received. */
	private boolean bonusLife;
	
	private boolean isPaused = false;

	private int difficulty;
	
	private static final int RESUME = 1;
	private static final int QUIT = 2;
	private int menuNum;

	private static final int SELECTION_TIME = 200;
	private Cooldown selectionCooldown;
	/**
	 * Constructor, establishes the properties of the screen.
	 * 
	 * @param gameState
	 *            Current game state.
	 * @param gameSettings
	 *            Current game settings.
	 * @param bonnusLife
	 *            Checks if a bonus life is awarded this level.
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public GameScreen(final GameState gameState,
			final GameSettings gameSettings, final boolean bonusLife,
			final int width, final int height, final int fps) {
		super(width, height, fps);

		this.logger.info("Difficulty: "+gameState.getDifficulty());
		int w = gameSettings.getFormationWidth();
		int h = gameSettings.getFormationHeight();
		int s = gameSettings.getBaseSpeed();
		int f = gameSettings.getShootingFrecuency();
		
		w += (gameState.getDifficulty()-Core.EASY)-2;
		h += (gameState.getDifficulty()-Core.EASY)/2-2;
		s = s*10*Core.HARD/9*gameState.getDifficulty();
		f = f*10*Core.HARD/9*gameState.getDifficulty();
		
		this.gameSettings = new GameSettings(w, h, s, f);
		
//		if (gameState.getDifficulty() == Core.HARD && gameState.getLevel() == 7) {
//			this.gameSettings = new GameSettings(1, 1, 30, 100);
//		}
		
		
		this.bonusLife = bonusLife;
		this.level = gameState.getLevel();
		this.score_p1 = gameState.getScoreP1();
		this.lives_p1 = gameState.getLivesRemainingP1();
		this.bulletsShot_p1 = gameState.getBulletsShotP1();
		this.shipsDestroyed_p1 = gameState.getShipsDestroyedP1();
		
		this.score_p2 = gameState.getScoreP2();
		this.lives_p2 = gameState.getLivesRemainingP2();
		this.bulletsShot_p2 = gameState.getBulletsShotP2();
		this.shipsDestroyed_p2 = gameState.getShipsDestroyedP2();
		
		
		this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
		this.selectionCooldown.reset();
	}

	/**
	 * Initializes basic screen properties, and adds necessary elements.
	 */
	public final void initialize() {
		super.initialize();

		enemyShipFormation = new EnemyShipFormation(this.gameSettings);
		enemyShipFormation.attach(this);
		this.ship_1 = new Ship(this.width / 2, this.height - 30, true);
		this.ship_2 = new Ship(this.width / 2, this.height - 30, false);
		// Appears each 10-30 seconds.
		this.enemyShipSpecialCooldown = Core.getVariableCooldown(
				BONUS_SHIP_INTERVAL, BONUS_SHIP_VARIANCE);
		this.enemyShipSpecialCooldown.reset();
		this.enemyShipSpecialExplosionCooldown = Core
				.getCooldown(BONUS_SHIP_EXPLOSION);
		this.screenFinishedCooldown = Core.getCooldown(SCREEN_CHANGE_INTERVAL);
		this.bullets_p1 = new HashSet<Bullet>();
		this.bullets_p2 = new HashSet<Bullet>();
		this.bullets_en = new HashSet<Bullet>();

		// Special input delay / countdown.
		this.gameStartTime = System.currentTimeMillis();
		this.inputDelay = Core.getCooldown(INPUT_DELAY);
		this.inputDelay.reset();
	}

	public static int getQuit(){
		return QUIT;
	}

	public static int getResume(){
		return RESUME;
	}

	/**
	 * Starts the action.
	 * 
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		this.logger.info("Screen cleared with a score of " + this.score_p1);
		this.logger.info("Screen cleared with a score of " + this.score_p2);

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() {
		super.update();

		if (this.inputDelay.checkFinished() && !this.levelFinished && !this.isPaused) {
			
			if (!this.ship_1.isDestroyed()) {
				boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_D);
				boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_A);

				boolean isRightBorder = this.ship_1.getPositionX()
						+ this.ship_1.getWidth() + this.ship_1.getSpeed() > this.width - 1;
				boolean isLeftBorder = this.ship_1.getPositionX()
						- this.ship_1.getSpeed() < 1;

				if (moveRight && !isRightBorder) {
					this.ship_1.moveRight();
				}
				if (moveLeft && !isLeftBorder) {
					this.ship_1.moveLeft();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_G))
					if (this.ship_1.shoot(this.bullets_p1))
						this.bulletsShot_p1++;
			}
			
			if (!this.ship_2.isDestroyed()) {
				boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_RIGHT);
				boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_LEFT);

				boolean isRightBorder = this.ship_2.getPositionX()
						+ this.ship_2.getWidth() + this.ship_2.getSpeed() > this.width - 1;
				boolean isLeftBorder = this.ship_2.getPositionX()
						- this.ship_2.getSpeed() < 1;

				if (moveRight && !isRightBorder) {
					this.ship_2.moveRight();
				}
				if (moveLeft && !isLeftBorder) {
					this.ship_2.moveLeft();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_SLASH))
					if (this.ship_2.shoot(this.bullets_p2))
						this.bulletsShot_p2++;
			}

			if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)
						&& this.inputDelay.checkFinished()
						&& this.selectionCooldown.checkFinished()) {
				// Escape!
				this.isPaused = true;
				this.menuNum = 1;
				this.selectionCooldown.reset();
			}

			if (this.enemyShipSpecial != null) {
				if (!this.enemyShipSpecial.isDestroyed())
					this.enemyShipSpecial.move(2, 0);
				else if (this.enemyShipSpecialExplosionCooldown.checkFinished())
					this.enemyShipSpecial = null;

			}
			if (this.enemyShipSpecial == null
					&& this.enemyShipSpecialCooldown.checkFinished()) {
				this.enemyShipSpecial = new EnemyShip();
				this.enemyShipSpecialCooldown.reset();
				this.logger.info("A special ship appears");
			}
			if (this.enemyShipSpecial != null
					&& this.enemyShipSpecial.getPositionX() > this.width) {
				this.enemyShipSpecial = null;
				this.logger.info("The special ship has escaped");
			}

			this.ship_1.update();
			this.ship_2.update();
			this.enemyShipFormation.update();
			this.enemyShipFormation.shoot(this.bullets_en);
			manageCollisions();
			cleanBullets();
		}

		if (this.isPaused) {
			if (this.selectionCooldown.checkFinished()
					&& this.inputDelay.checkFinished()) {
				draw();
				if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
					isPaused = false;
					this.selectionCooldown.reset();
				} else if (inputManager.isKeyDown(KeyEvent.VK_UP) || inputManager.isKeyDown(KeyEvent.VK_DOWN)) {
					menuNum = menuNum%2+1;
					this.logger.info("Selected menu: "+menuNum);
					this.selectionCooldown.reset();
				} else if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
					if (menuNum == 2) {
						this.level = 7;
						isPaused = false;
						this.isRunning = false;
						this.selectionCooldown.reset();
					}
				}
//				for (int i = '1'; i <= '7'; i++) {
//					if (inputManager.isKeyDown(i)) {
//						this.level = i-'1';
//						isPaused = false;
//						this.isRunning = false;
//						this.selectionCooldown.reset();
//					}
//				}
			}
		}

		draw();

		if ((this.enemyShipFormation.isEmpty() || this.lives_p1 == 0) // will change
				&& !this.levelFinished) {
			this.levelFinished = true;
			this.screenFinishedCooldown.reset();
		}

		if (this.levelFinished && this.screenFinishedCooldown.checkFinished())
			this.isRunning = false;
		
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		if (this.isPaused) {
			drawManager.initDrawing(this);

			drawManager.drawHighScoreMenu(this);

			drawManager.completeDrawing(this);

			// Replace above with this code.
//			drawManager.drawPause(this);
//			drawManager.drawPauseMenu(this, this.menuNum);

			return;
		}
		drawManager.initDrawing(this);

		drawManager.drawEntity(this.ship_1, this.ship_1.getPositionX(),
				this.ship_1.getPositionY());
		drawManager.drawEntity(this.ship_2, this.ship_2.getPositionX(),
				this.ship_2.getPositionY());
		if (this.enemyShipSpecial != null)
			drawManager.drawEntity(this.enemyShipSpecial,
					this.enemyShipSpecial.getPositionX(),
					this.enemyShipSpecial.getPositionY());

		enemyShipFormation.draw();

		for (Bullet bullet : this.bullets_p1)
			drawManager.drawEntity(bullet, bullet.getPositionX(),
					bullet.getPositionY());
		for (Bullet bullet : this.bullets_p2)
			drawManager.drawEntity(bullet, bullet.getPositionX(),
					bullet.getPositionY());
		for (Bullet bullet : this.bullets_en)
			drawManager.drawEntity(bullet, bullet.getPositionX(),
					bullet.getPositionY());
		

		// Interface.
		drawManager.drawScore(this, this.score_p1, this.score_p2);
		drawManager.drawLives(this, this.lives_p1, this.lives_p2);
		drawManager.drawHorizontalLine(this, 2*SEPARATION_LINE_HEIGHT - 1);

		// Countdown to game start.
		if (!this.inputDelay.checkFinished()) {
			int countdown = (int) ((INPUT_DELAY
					- (System.currentTimeMillis()
							- this.gameStartTime)) / 1000);
			drawManager.drawCountDown(this, this.level, countdown,
					this.bonusLife);
			drawManager.drawHorizontalLine(this, this.height / 2 - this.height
					/ 12);
			drawManager.drawHorizontalLine(this, this.height / 2 + this.height
					/ 12);
		}

		drawManager.completeDrawing(this);
	}

	/**
	 * Cleans bullets that go off screen.
	 */
	private void cleanBullets() {
		Set<Bullet> recyclable = new HashSet<Bullet>();
		for (Bullet bullet : this.bullets_p1) {
			bullet.update();
			if (bullet.getPositionY() < 2*SEPARATION_LINE_HEIGHT
					|| bullet.getPositionY() > this.height)
				recyclable.add(bullet);
		}

		this.bullets_p1.removeAll(recyclable);
		BulletPool.recycle(recyclable);
		recyclable = new HashSet<Bullet>();
		
		for (Bullet bullet : this.bullets_p2) {
			bullet.update();
			if (bullet.getPositionY() < 2*SEPARATION_LINE_HEIGHT
					|| bullet.getPositionY() > this.height)
				recyclable.add(bullet);
		}

		this.bullets_p2.removeAll(recyclable);
		BulletPool.recycle(recyclable);
		recyclable = new HashSet<Bullet>();
		
		for (Bullet bullet : this.bullets_en) {
			bullet.update();
			if (bullet.getPositionY() < 2*SEPARATION_LINE_HEIGHT
					|| bullet.getPositionY() > this.height)
				recyclable.add(bullet);
		}
		
		this.bullets_en.removeAll(recyclable);
		BulletPool.recycle(recyclable);
		recyclable = new HashSet<Bullet>();
	}

	/**
	 * Manages collisions between bullets and ships.
	 */
	private void manageCollisions() {
		Set<Bullet> recyclable = new HashSet<Bullet>();
		for (Bullet bullet : this.bullets_en) {
			if (checkCollision(bullet, this.ship_1) && !this.levelFinished) {
				recyclable.add(bullet);
				if (!this.ship_1.isDestroyed()) {
					this.ship_1.destroy();
					this.score_p1 -= Math.min(50,score_p1);
					this.logger.info("Hit on player1 ship, " + this.lives_p1
							+ " lives remaining.");
				}
			}
			if (checkCollision(bullet, this.ship_2) && !this.levelFinished) {
				recyclable.add(bullet);
				if (!this.ship_2.isDestroyed()) {
					this.ship_2.destroy();
					this.score_p2 -= Math.min(50,score_p2);
					this.logger.info("Hit on player2 ship, " + this.lives_p2
							+ " lives remaining.");
				}
			}
		}

		this.bullets_en.removeAll(recyclable);
		BulletPool.recycle(recyclable);
		recyclable = new HashSet<Bullet>();
		
		for (Bullet bullet : this.bullets_p1) {
			for (EnemyShip enemyShip : this.enemyShipFormation)
				if (!enemyShip.isDestroyed()
						&& checkCollision(bullet, enemyShip)) {
					this.score_p1 += enemyShip.getPointValue();
					this.shipsDestroyed_p1++;
					this.enemyShipFormation.destroy(enemyShip);
					recyclable.add(bullet);
				}
			if (this.enemyShipSpecial != null
					&& !this.enemyShipSpecial.isDestroyed()
					&& checkCollision(bullet, this.enemyShipSpecial)) {
				this.score_p1 += this.enemyShipSpecial.getPointValue();
				this.shipsDestroyed_p1++;
				this.enemyShipSpecial.destroy();
				this.enemyShipSpecialExplosionCooldown.reset();
				recyclable.add(bullet);
			}
		}
		
		this.bullets_p1.removeAll(recyclable);
		BulletPool.recycle(recyclable);
		recyclable = new HashSet<Bullet>();
		
		for (Bullet bullet : this.bullets_p2) {
			for (EnemyShip enemyShip : this.enemyShipFormation)
				if (!enemyShip.isDestroyed()
						&& checkCollision(bullet, enemyShip)) {
					this.score_p2 += enemyShip.getPointValue();
					this.shipsDestroyed_p2++;
					this.enemyShipFormation.destroy(enemyShip);
					recyclable.add(bullet);
				}
			if (this.enemyShipSpecial != null
					&& !this.enemyShipSpecial.isDestroyed()
					&& checkCollision(bullet, this.enemyShipSpecial)) {
				this.score_p2 += this.enemyShipSpecial.getPointValue();
				this.shipsDestroyed_p2++;
				this.enemyShipSpecial.destroy();
				this.enemyShipSpecialExplosionCooldown.reset();
				recyclable.add(bullet);
			}
		}
		
		this.bullets_p2.removeAll(recyclable);
		BulletPool.recycle(recyclable);
		recyclable = new HashSet<Bullet>();
		
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
	private boolean checkCollision(final Entity a, final Entity b) {
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
	 * Returns a GameState object representing the status of the game.
	 * 
	 * @return Current game state.
	 */
	public final GameState getGameState() {
		return new GameState(this.level, this.score_p1, this.lives_p1,
				this.bulletsShot_p1, this.shipsDestroyed_p1,
				this.score_p2, this.lives_p2,
				this.bulletsShot_p2, this.shipsDestroyed_p2, 
				this.difficulty);
	}
}