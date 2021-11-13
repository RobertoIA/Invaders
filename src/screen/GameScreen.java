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
 * 액션이 발생하는 게임 화면을 구현합니다.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class GameScreen extends Screen {

	/** Milliseconds until the screen accepts user input.
	 * 화면이 사용자 입력을 수락할 때까지의 시간(밀리초)입니다. */
	private static final int INPUT_DELAY = 6000;
	/** Bonus score for each life remaining at the end of the level.
	 * 레벨이 끝날 때 남은 각 목숨에 대한 보너스 점수. */
	private static final int LIFE_SCORE = 100;
	/** Minimum time between bonus ship's appearances.
	 * 보너스 함선의 출현 사이의 최소 시간. */
	private static final int BONUS_SHIP_INTERVAL = 20000;
	/** Maximum variance in the time between bonus ship's appearances.
	 * 보너스 함선의 출현 사이의 최대 variance. */
	private static final int BONUS_SHIP_VARIANCE = 10000;
	/** Time until bonus ship explosion disappears.
	 * 보너스 함선 폭발이 사라질 때까지의 시간. */
	private static final int BONUS_SHIP_EXPLOSION = 500;
	/** Time from finishing the level to screen change.
	 * 레벨 완료 후 화면 변경까지의 시간입니다. */
	private static final int SCREEN_CHANGE_INTERVAL = 1500;
	/** Height of the interface separation line.
	 * 인터페이스 분리선의 높이입니다. */
	private static final int SEPARATION_LINE_HEIGHT = 40;

	/** Current game difficulty settings.
	 * 현재 게임 난이도 설정. */
	private GameSettings gameSettings;
	/** Current difficulty level number.
	 * 현재 난이도 번호입니다. */
	private int level;
	/** Formation of enemy ships.
	 * 적 함선의 포메이션. */
	private EnemyShipFormation enemyShipFormation;
	/** Player's ship.
	 * 플레이어의 함선. */
	private Ship ship;
	/** Bonus enemy ship that appears sometimes.
	 * 가끔 등장하는 보너스 적함. */
	private EnemyShip enemyShipSpecial;
	/** Minimum time between bonus ship appearances.
	 * 보너스 함선 출현 사이의 최소 시간. */
	private Cooldown enemyShipSpecialCooldown;
	/** Time until bonus ship explosion disappears.
	 * 보너스 함선 폭발이 사라질 때까지의 시간. */
	private Cooldown enemyShipSpecialExplosionCooldown;
	/** Time from finishing the level to screen change.
	 * 레벨 완료 후 화면 변경까지의 시간입니다. */
	private Cooldown screenFinishedCooldown;
	/** Set of all bullets fired by on screen ships.
	 * 화면에서 발사한 모든 총알에 대한 설정입니다. */
	private Set<Bullet> bullets;
	/** Current score.
	 * 현재 점수. */
	private int score;
	/** Player lives left.
	 * 플레이어의 남은 목숨. */
	private int lives;
	/** Total bullets shot by the player.
	 * 플레이어가 쏜 전체 총알들입니다. */
	private int bulletsShot;
	/** Total ships destroyed by the player.
	 * 플레이어가 파괴한 총 선박입니다. */
	private int shipsDestroyed;
	/** Moment the game starts.
	 * 게임이 시작되는 순간. */
	private long gameStartTime;
	/** Checks if the level is finished.
	 * 레벨이 완료되었는지 확인합니다. */
	private boolean levelFinished;
	/** Checks if a bonus life is received.
	 * 보너스 목숨을 받았는지 확인합니다. */
	private boolean bonusLife;

	/**
	 * Constructor, establishes the properties of the screen.
	 * 생성자, 화면의 속성을 설정합니다.
	 *
	 * @param gameState
	 *            Current game state.
	 *            현재 게임 상태입니다.
	 * @param gameSettings
	 *            Current game settings.
	 *            현재 게임 설정.
	 * @param bonnusLife
	 *            Checks if a bonus life is awarded this level.
	 *            이 레벨에서 보너스 생명이 주어지는지 확인합니다.
	 * @param width
	 *            Screen width.
	 *            화면 너비.
	 * @param height
	 *            Screen height.
	 *            화면 높이.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 *            초당 프레임 수, 게임이 실행되는 프레임 속도입니다.
	 */
	public GameScreen(final GameState gameState,
					  final GameSettings gameSettings, final boolean bonusLife,
					  final int width, final int height, final int fps) {
		super(width, height, fps);

		this.gameSettings = gameSettings;
		this.bonusLife = bonusLife;
		this.level = gameState.getLevel();
		this.score = gameState.getScore();
		this.lives = gameState.getLivesRemaining();
		if (this.bonusLife)
			this.lives++;
		this.bulletsShot = gameState.getBulletsShot();
		this.shipsDestroyed = gameState.getShipsDestroyed();
	}

	/**
	 * Initializes basic screen properties, and adds necessary elements.
	 * 기본 화면 속성을 초기화하고 필요한 요소를 추가합니다.
	 */
	public final void initialize() {
		super.initialize();

		enemyShipFormation = new EnemyShipFormation(this.gameSettings);
		enemyShipFormation.attach(this);
		this.ship = new Ship(this.width / 2, this.height - 30);
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
	 * 작업을 시작합니다.
	 *
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		this.score += LIFE_SCORE * (this.lives - 1);
		this.logger.info("Screen cleared with a score of " + this.score);

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 * 화면의 요소를 업데이트하고 이벤트를 확인합니다.
	 */
	protected final void update() {
		super.update();

		if (this.inputDelay.checkFinished() && !this.levelFinished) {

			if (!this.ship.isDestroyed()) {
				boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_RIGHT)
						|| inputManager.isKeyDown(KeyEvent.VK_D);
				boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_LEFT)
						|| inputManager.isKeyDown(KeyEvent.VK_A);

				boolean isRightBorder = this.ship.getPositionX()
						+ this.ship.getWidth() + this.ship.getSpeed() > this.width - 1;
				boolean isLeftBorder = this.ship.getPositionX()
						- this.ship.getSpeed() < 1;

				if (moveRight && !isRightBorder) {
					this.ship.moveRight();
				}
				if (moveLeft && !isLeftBorder) {
					this.ship.moveLeft();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_SPACE))
					if (this.ship.shoot(this.bullets))
						this.bulletsShot++;
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

			this.ship.update();
			this.enemyShipFormation.update();
			this.enemyShipFormation.shoot(this.bullets);
		}

		manageCollisions();
		cleanBullets();
		draw();

		if ((this.enemyShipFormation.isEmpty() || this.lives == 0)
				&& !this.levelFinished) {
			this.levelFinished = true;
			this.screenFinishedCooldown.reset();
		}

		if (this.levelFinished && this.screenFinishedCooldown.checkFinished())
			this.isRunning = false;

	}

	/**
	 * Draws the elements associated with the screen.
	 * 화면과 관련된 요소를 그립니다.
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
	 * 화면 밖으로 나가는 총알을 정리합니다.
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
	 * 총알과 선박 간의 충돌을 관리합니다.
	 */
	private void manageCollisions() {
		Set<Bullet> recyclable = new HashSet<Bullet>();
		for (Bullet bullet : this.bullets)
			if (bullet.getSpeed() > 0) {
				if (checkCollision(bullet, this.ship) && !this.levelFinished) {
					recyclable.add(bullet);
					if (!this.ship.isDestroyed()) {
						this.ship.destroy();
						this.lives--;
						this.logger.info("Hit on player ship, " + this.lives
								+ " lives remaining.");
					}
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
	 * 두 엔터티가 충돌하는지 확인합니다.
	 *
	 * @param a
	 *            First entity, the bullet.
	 *            첫 번째 엔티티, 총알.
	 * @param b
	 *            Second entity, the ship.
	 *            두 번째 엔티티, 배.
	 * @return Result of the collision test.
	 * 			충돌 테스트 결과를 반환.
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
	 * 게임의 상태를 나타내는 GameState 오브젝트를 반환합니다.
	 *
	 * @return Current game state.
	 */
	public final GameState getGameState() {
		return new GameState(this.level, this.score, this.lives,
				this.bulletsShot, this.shipsDestroyed);
	}
}