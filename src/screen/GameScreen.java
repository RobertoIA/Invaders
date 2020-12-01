package screen;

import java.awt.*;
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
import entity.Pair;

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
	private static final int SEPARATION_LINE_HEIGHT = 40;

	/** Current game difficulty settings. */
	private GameSettings gameSettings;
	/** Current difficulty level number. */
	private int level;
	/** Formation of enemy ships. */
	private EnemyShipFormation enemyShipFormation;
	/** Player1's ship. */
	private Ship ship1;
	/** Player2's ship. */
	private Ship ship2;
	/** Bonus enemy ship that appears sometimes. */
	private EnemyShip enemyShipSpecial;
	/** Minimum time between bonus ship appearances. */
	private Cooldown enemyShipSpecialCooldown;
	/** Time until bonus ship explosion disappears. */
	private Cooldown enemyShipSpecialExplosionCooldown;
	/** Time from finishing the level to screen change. */
	private Cooldown screenFinishedCooldown;
	/** Set of all bullets fired by on screen ships. */
	private Set<Bullet> bullets;
	/** Current score. */
	private Pair score;
	/** Player lives left. */
	private Pair lives;
	/** Total bullets shot by the player. */
	private Pair bulletsShot;
	/** Total ships destroyed by the player. */
	private Pair shipsDestroyed;
	/** Current Players' numbers.*/
	private int playerCode;
	/** Moment the game starts. */
	private long gameStartTime;
	/** Checks if the level is finished. */
	private boolean levelFinished;
	/** Checks if a bonus life is received. */
	private boolean bonusLife;

	/** Default color for player1's ship.*/
	private Color SHIP1_COLOR = Color.GREEN;
	/** Default color for player2's ship.*/
	private Color SHIP2_COLOR = Color.RED;


	/**
	 * Constructor, establishes the properties of the screen.
	 * 
	 * @param gameState
	 *            Current game state.
	 * @param gameSettings
	 *            Current game settings.
	 * @param bonusLife
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

		this.gameSettings = gameSettings;
		this.bonusLife = bonusLife;
		this.playerCode = gameState.getPlayerCode();
		this.level = gameState.getLevel();
		this.score = gameState.getScore();
		this.lives = gameState.getLivesRemaining();
		if (this.bonusLife) {
			if(this.lives.getPlayer1Value() > 0) this.lives.addPlayer1Value(1);
			if(this.playerCode == 2 && this.lives.getPlayer2Value() > 0) this.lives.addPlayer2Value(1);
		}
		this.bulletsShot = gameState.getBulletsShot();
		this.shipsDestroyed = gameState.getShipsDestroyed();

	}

	/**
	 * Initializes basic screen properties, and adds necessary elements.
	 */
	public final void initialize() {
		super.initialize();

		enemyShipFormation = new EnemyShipFormation(this.gameSettings);
		enemyShipFormation.attach(this);
		if(this.playerCode == 1) //player1
			this.ship1 = new Ship(this.width / 2, this.height - 30, SHIP1_COLOR);
		else{ //player2
			this.ship1 = new Ship(this.width / 3, this.height - 30, SHIP1_COLOR);
			this.ship2 = new Ship(2 * this.width / 3, this.height - 30, SHIP2_COLOR);
		}

		// Appears each 10-30 seconds.
		this.enemyShipSpecialCooldown = Core.getVariableCooldown(
				BONUS_SHIP_INTERVAL, BONUS_SHIP_VARIANCE);
		this.enemyShipSpecialCooldown.reset();
		this.enemyShipSpecialExplosionCooldown = Core
				.getCooldown(BONUS_SHIP_EXPLOSION);
		this.screenFinishedCooldown = Core.getCooldown(SCREEN_CHANGE_INTERVAL);
		this.bullets = new HashSet<Bullet>();

		// Special input delay / countdown.
		this.gameStartTime = System.currentTimeMillis();
		this.inputDelay = Core.getCooldown(INPUT_DELAY);
		this.inputDelay.reset();
	}

	/**
	 * Starts the action.
	 * 
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		this.score.addPlayer1Value(LIFE_SCORE * (this.lives.getPlayer1Value() - 1));
		this.logger.info("Screen cleared with a score of " + this.score + " for Player1");
		if(this.playerCode == 2){
			this.score.addPlayer2Value(LIFE_SCORE * (this.lives.getPlayer2Value() - 1));
			this.logger.info("Screen cleared with a score of " + this.score + "for Player2");
		}

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() {
		super.update();

		if (this.inputDelay.checkFinished() && !this.levelFinished) {
			if(playerCode == 1){ //When you are playing one player mode.
				if (!this.ship1.isDestroyed()) {
					boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_RIGHT)
							|| inputManager.isKeyDown(KeyEvent.VK_D);
					boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_LEFT)
							|| inputManager.isKeyDown(KeyEvent.VK_A);

					boolean isRightBorder = this.ship1.getPositionX()
							+ this.ship1.getWidth() + this.ship1.getSpeed() > this.width - 1;
					boolean isLeftBorder = this.ship1.getPositionX()
							- this.ship1.getSpeed() < 1;

					if (moveRight && !isRightBorder) {
						this.ship1.moveRight();
					}
					if (moveLeft && !isLeftBorder) {
						this.ship1.moveLeft();
					}
					if (inputManager.isKeyDown(KeyEvent.VK_SPACE))
						if (this.ship1.shoot(this.bullets, "ship1"))
							this.bulletsShot.addPlayer1Value(1);
				}
			} else if(playerCode == 2){ //When you are playing two players mode.
				if (!this.ship1.isDestroyed()) { //player1 status
					boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_RIGHT);
					boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_LEFT);

					boolean isRightBorder = this.ship1.getPositionX()
							+ this.ship1.getWidth() + this.ship1.getSpeed() > this.width - 1;
					boolean isLeftBorder = this.ship1.getPositionX()
							- this.ship1.getSpeed() < 1;

					if (moveRight && !isRightBorder) {
						this.ship1.moveRight();
					}
					if (moveLeft && !isLeftBorder) {
						this.ship1.moveLeft();
					}
					if (inputManager.isKeyDown(KeyEvent.VK_SPACE))
						if (this.ship1.shoot(this.bullets, "ship1"))
							this.bulletsShot.addPlayer1Value(1);
				}

				if (!this.ship2.isDestroyed()){ //player2 status
					boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_D);
					boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_A);

					boolean isRightBorder = this.ship2.getPositionX()
							+ this.ship2.getWidth() + this.ship2.getSpeed() > this.width - 1;
					boolean isLeftBorder = this.ship2.getPositionX()
							- this.ship2.getSpeed() < 1;

					if (moveRight && !isRightBorder) {
						this.ship2.moveRight();
					}
					if (moveLeft && !isLeftBorder) {
						this.ship2.moveLeft();
					}
					if (inputManager.isKeyDown(KeyEvent.VK_ENTER))
						if (this.ship2.shoot(this.bullets, "ship2"))
							this.bulletsShot.addPlayer2Value(1);
				}
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


			this.ship1.update();//player1 update
			if(playerCode == 2) this.ship2.update();//player2 update
			this.enemyShipFormation.update();
			this.enemyShipFormation.shoot(this.bullets);
		}

		manageCollisions();
		cleanBullets();
		draw();

		switch(playerCode){
			case 1 : //one-player mode check
				if ((this.enemyShipFormation.isEmpty() || this.lives.getPlayer1Value() == 0)
						&& !this.levelFinished) {
					this.levelFinished = true;
					this.screenFinishedCooldown.reset();
				}
				break;

			case 2 : //two-players mode check
				if ((this.enemyShipFormation.isEmpty() || this.lives.getPlayer1Value() == 0 || this.lives.getPlayer2Value() == 0)
						&& !this.levelFinished) {
					this.levelFinished = true;
					this.screenFinishedCooldown.reset();
				}
				break;

			default : break;
		}

		if (this.levelFinished && this.screenFinishedCooldown.checkFinished())
			this.isRunning = false;

	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawEntity(this.ship1, this.ship1.getPositionX(),
				this.ship1.getPositionY());
		if(playerCode == 2) //two-players mode
			drawManager.drawEntity(this.ship2, this.ship2.getPositionX(),
					this.ship2.getPositionY());
		if (this.enemyShipSpecial != null)
			drawManager.drawEntity(this.enemyShipSpecial,
					this.enemyShipSpecial.getPositionX(),
					this.enemyShipSpecial.getPositionY());

		enemyShipFormation.draw();

		for (Bullet bullet : this.bullets)
			drawManager.drawEntity(bullet, bullet.getPositionX(),
					bullet.getPositionY());

		// Interface.
		if(playerCode == 1) { //one-player mode
			drawManager.drawScore(this, this.score.getPlayer1Value());
			drawManager.drawLives(this, this.lives.getPlayer1Value());
		}else if(playerCode == 2){ //two-players mode
			drawManager.drawScoreFor2(this, this.score.getPlayer1Value(), this.score.getPlayer2Value());
			drawManager.drawLivesFor2(this, this.lives.getPlayer1Value(), this.lives.getPlayer2Value());
		}
		drawManager.drawHorizontalLine(this, SEPARATION_LINE_HEIGHT - 1);

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
		for (Bullet bullet : this.bullets) {
			bullet.update();
			if (bullet.getPositionY() < SEPARATION_LINE_HEIGHT
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
				if (checkCollision(bullet, this.ship1) && !this.levelFinished) { //player1 being collided
					recyclable.add(bullet);
					if (!this.ship1.isDestroyed()) {
						this.ship1.destroy();
						this.lives.addPlayer1Value(-1);
						this.logger.info("Hit on player ship, " + this.lives.getPlayer1Value()
								+ " lives remaining.");
					}
				}

				if (playerCode == 2 && checkCollision(bullet, this.ship2) && !this.levelFinished){ //player2 being collided
					recyclable.add(bullet);
					if (!this.ship2.isDestroyed()) {
						this.ship2.destroy();
						this.lives.addPlayer2Value(-1);
						this.logger.info("Hit on player ship, " + this.lives.getPlayer2Value()
								+ " lives remaining.");
					}
				}
			} else {
				for (EnemyShip enemyShip : this.enemyShipFormation)
					if (!enemyShip.isDestroyed()
							&& checkCollision(bullet, enemyShip)) {
						if(bullet.getName().equals("ship1")){ //if player1's bullet collide with enemy ship
							this.score.addPlayer1Value(enemyShip.getPointValue());
							this.shipsDestroyed.addPlayer1Value(1);
						} else if(bullet.getName().equals("ship2")){ //if player2's bullet collide with enemy ship
							this.score.addPlayer2Value(enemyShip.getPointValue());
							this.shipsDestroyed.addPlayer2Value(1);
						}

						this.enemyShipFormation.destroy(enemyShip);
						recyclable.add(bullet);
					}
				if (this.enemyShipSpecial != null
						&& !this.enemyShipSpecial.isDestroyed()
						&& checkCollision(bullet, this.enemyShipSpecial)) {
					if(bullet.getName().equals("ship1")){
						this.score.addPlayer1Value(this.enemyShipSpecial.getPointValue());
						this.shipsDestroyed.addPlayer1Value(1);
					}if(bullet.getName().equals("ship2")) {
						this.score.addPlayer2Value(this.enemyShipSpecial.getPointValue());
						this.shipsDestroyed.addPlayer2Value(1);
					}
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
		return new GameState(this.level, this.score, this.lives,
				this.bulletsShot, this.shipsDestroyed, this.playerCode);
	}
}