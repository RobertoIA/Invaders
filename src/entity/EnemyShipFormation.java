package entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import screen.Screen;
import engine.Cooldown;
import engine.Core;
import engine.DrawManager;
import engine.DrawManager.SpriteType;
import engine.GameSettings;

/**
 * Groups enemy ships into a formation that moves together.
 * 적 함선을 함께 이동하는 대형으로 그룹화합니다.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class EnemyShipFormation implements Iterable<EnemyShip> {

	/** Initial position in the x-axis.
	 * x축의 초기 위치입니다. */
	private static final int INIT_POS_X = 20;
	/** Initial position in the y-axis.
	 * y축의 초기 위치입니다. */
	private static final int INIT_POS_Y = 100;
	/** Distance between ships.
	 * 선박 사이의 거리. */
	private static final int SEPARATION_DISTANCE = 40;
	/** Proportion of C-type ships.
	 * C형 함선의 비율. */
	private static final double PROPORTION_C = 0.2;
	/** Proportion of B-type ships.
	 * B형 함선의 비율. */
	private static final double PROPORTION_B = 0.4;
	/** Lateral speed of the formation.
	 * 포메이션의 측면 속도. */
	private static final int X_SPEED = 8;
	/** Downwards speed of the formation.
	 * 포메이션의 하향 속도. */
	private static final int Y_SPEED = 4;
	/** Speed of the bullets shot by the members.
	 * 멤버들이 쏘는 총알의 속도. */
	private static final int BULLET_SPEED = 4;
	/** Proportion of differences between shooting times.
	 * 쏘는 시간의 사이의 비율. */
	private static final double SHOOTING_VARIANCE = .2;
	/** Margin on the sides of the screen.
	 * 화면 측면의 Margin. */
	private static final int SIDE_MARGIN = 20;
	/** Margin on the bottom of the screen.
	 * 화면 하단의 Margin. */
	private static final int BOTTOM_MARGIN = 80;
	/** Distance to go down each pass.
	 * 각 패스를 내려가는 거리입니다. */
	private static final int DESCENT_DISTANCE = 20;
	/** Minimum speed allowed.
	 * 허용되는 최소 속도. */
	private static final int MINIMUM_SPEED = 10;

	/** DrawManager instance. */
	private DrawManager drawManager;
	/** Application logger. */
	private Logger logger;
	/** Screen to draw ships on. */
	private Screen screen;

	/** List of enemy ships forming the formation.
	 * 포메이션을 형성하는 적 함선의 List. */
	private List<List<EnemyShip>> enemyShips;
	/** Minimum time between shots.
	 * 총 쏘기 사이의 최소 시간 */
	private Cooldown shootingCooldown;
	/** Number of ships in the formation - horizontally.
	 * 대형 선박의 수 - 수평. */
	private int nShipsWide;
	/** Number of ships in the formation - vertically.
	 * 대형 선박의 수 - 수직. */
	private int nShipsHigh;
	/** Time between shots.
	 * 총 쏘기 사이의 시간입니다. */
	private int shootingInterval;
	/** Variance in the time between shots.
	 * 총 쏘기 사이의 시간 Variance. */
	private int shootingVariance;
	/** Initial ship speed.
	 * 초기 함선 속도. */
	private int baseSpeed;
	/** Speed of the ships.
	 * 함선의 속도. */
	private int movementSpeed;
	/** Current direction the formation is moving on.
	 * 포메이션이 진행되고 있는 현재 방향. */
	private Direction currentDirection;
	/** Direction the formation was moving previously.
	 * 포메이션이 이전에 움직이는 방향. */
	private Direction previousDirection;
	/** Interval between movements, in frames.
	 * 프레임 단위의 움직임 사이의 간격입니다. */
	private int movementInterval;
	/** Total width of the formation.
	 * 포메이션의 총 너비. */
	private int width;
	/** Total height of the formation.
	 * 포메이션의 총 높이. */
	private int height;
	/** Position in the x-axis of the upper left corner of the formation.
	 * 포메이션의 왼쪽 상단 모서리의 x축 위치입니다. */
	private int positionX;
	/** Position in the y-axis of the upper left corner of the formation.
	 * 포메이션의 왼쪽 상단 모서리의 y축 위치입니다. */
	private int positionY;
	/** Width of one ship.
	 * 한 선박의 너비입니다. */
	private int shipWidth;
	/** Height of one ship.
	 * 한 선박의 높이입니다. */
	private int shipHeight;
	/** List of ships that are able to shoot.
	 * 쏠 수 있는 함선 목록입니다. */
	private List<EnemyShip> shooters;
	/** Number of not destroyed ships.
	 * 파괴되지 않은 선박의 수. */
	private int shipCount;

	/** Directions the formation can move.
	 * 포메이션이 움직일 수 있는 방향. */
	private enum Direction {
		/** Movement to the right side of the screen.
		 * 화면 오른쪽으로 이동합니다. */
		RIGHT,
		/** Movement to the left side of the screen.
		 * 화면 왼쪽으로 이동합니다. */
		LEFT,
		/** Movement to the bottom of the screen.
		 * 화면 하단으로 이동합니다. */
		DOWN
	};

	/**
	 * Constructor, sets the initial conditions.
	 * 생성자, 초기 조건을 설정합니다.
	 *
	 * @param gameSettings
	 *            Current game settings.
	 *            현재 게임 설정.
	 */
	public EnemyShipFormation(final GameSettings gameSettings) {
		this.drawManager = Core.getDrawManager();
		this.logger = Core.getLogger();
		this.enemyShips = new ArrayList<List<EnemyShip>>();
		this.currentDirection = Direction.RIGHT;
		this.movementInterval = 0;
		this.nShipsWide = gameSettings.getFormationWidth();
		this.nShipsHigh = gameSettings.getFormationHeight();
		this.shootingInterval = gameSettings.getShootingFrecuency();
		this.shootingVariance = (int) (gameSettings.getShootingFrecuency()
				* SHOOTING_VARIANCE);
		this.baseSpeed = gameSettings.getBaseSpeed();
		this.movementSpeed = this.baseSpeed;
		this.positionX = INIT_POS_X;
		this.positionY = INIT_POS_Y;
		this.shooters = new ArrayList<EnemyShip>();
		SpriteType spriteType;

		this.logger.info("Initializing " + nShipsWide + "x" + nShipsHigh
				+ " ship formation in (" + positionX + "," + positionY + ")");

		// Each sub-list is a column on the formation.
		for (int i = 0; i < this.nShipsWide; i++)
			this.enemyShips.add(new ArrayList<EnemyShip>());

		for (List<EnemyShip> column : this.enemyShips) {
			for (int i = 0; i < this.nShipsHigh; i++) {
				if (i / (float) this.nShipsHigh < PROPORTION_C)
					spriteType = SpriteType.EnemyShipC1;
				else if (i / (float) this.nShipsHigh < PROPORTION_B
						+ PROPORTION_C)
					spriteType = SpriteType.EnemyShipB1;
				else
					spriteType = SpriteType.EnemyShipA1;

				column.add(new EnemyShip((SEPARATION_DISTANCE
						* this.enemyShips.indexOf(column))
						+ positionX, (SEPARATION_DISTANCE * i)
						+ positionY, spriteType));
				this.shipCount++;
			}
		}

		this.shipWidth = this.enemyShips.get(0).get(0).getWidth();
		this.shipHeight = this.enemyShips.get(0).get(0).getHeight();

		this.width = (this.nShipsWide - 1) * SEPARATION_DISTANCE
				+ this.shipWidth;
		this.height = (this.nShipsHigh - 1) * SEPARATION_DISTANCE
				+ this.shipHeight;

		for (List<EnemyShip> column : this.enemyShips)
			this.shooters.add(column.get(column.size() - 1));
	}

	/**
	 * Associates the formation to a given screen.
	 * 포메이션을 주어진 화면에 연결합니다.
	 *
	 * @param newScreen
	 *            Screen to attach.
	 *            첨부할 화면입니다.
	 */
	public final void attach(final Screen newScreen) {
		screen = newScreen;
	}

	/**
	 * Draws every individual component of the formation.
	 * 포메이션의 모든 개별 구성 요소를 그립니다.
	 */
	public final void draw() {
		for (List<EnemyShip> column : this.enemyShips)
			for (EnemyShip enemyShip : column)
				drawManager.drawEntity(enemyShip, enemyShip.getPositionX(),
						enemyShip.getPositionY());
	}

	/**
	 * Updates the position of the ships.
	 * 선박의 위치를 업데이트합니다.
	 */
	public final void update() {
		if(this.shootingCooldown == null) {
			this.shootingCooldown = Core.getVariableCooldown(shootingInterval,
					shootingVariance);
			this.shootingCooldown.reset();
		}

		cleanUp();

		int movementX = 0;
		int movementY = 0;
		double remainingProportion = (double) this.shipCount
				/ (this.nShipsHigh * this.nShipsWide);
		this.movementSpeed = (int) (Math.pow(remainingProportion, 2)
				* this.baseSpeed);
		this.movementSpeed += MINIMUM_SPEED;

		movementInterval++;
		if (movementInterval >= this.movementSpeed) {
			movementInterval = 0;

			boolean isAtBottom = positionY
					+ this.height > screen.getHeight() - BOTTOM_MARGIN;
			boolean isAtRightSide = positionX
					+ this.width >= screen.getWidth() - SIDE_MARGIN;
			boolean isAtLeftSide = positionX <= SIDE_MARGIN;
			boolean isAtHorizontalAltitude = positionY % DESCENT_DISTANCE == 0;

			if (currentDirection == Direction.DOWN) {
				if (isAtHorizontalAltitude)
					if (previousDirection == Direction.RIGHT) {
						currentDirection = Direction.LEFT;
						this.logger.info("Formation now moving left 1");
					} else {
						currentDirection = Direction.RIGHT;
						this.logger.info("Formation now moving right 2");
					}
			} else if (currentDirection == Direction.LEFT) {
				if (isAtLeftSide)
					if (!isAtBottom) {
						previousDirection = currentDirection;
						currentDirection = Direction.DOWN;
						this.logger.info("Formation now moving down 3");
					} else {
						currentDirection = Direction.RIGHT;
						this.logger.info("Formation now moving right 4");
					}
			} else {
				if (isAtRightSide)
					if (!isAtBottom) {
						previousDirection = currentDirection;
						currentDirection = Direction.DOWN;
						this.logger.info("Formation now moving down 5");
					} else {
						currentDirection = Direction.LEFT;
						this.logger.info("Formation now moving left 6");
					}
			}

			if (currentDirection == Direction.RIGHT)
				movementX = X_SPEED;
			else if (currentDirection == Direction.LEFT)
				movementX = -X_SPEED;
			else
				movementY = Y_SPEED;

			positionX += movementX;
			positionY += movementY;

			// Cleans explosions.
			List<EnemyShip> destroyed;
			for (List<EnemyShip> column : this.enemyShips) {
				destroyed = new ArrayList<EnemyShip>();
				for (EnemyShip ship : column) {
					if (ship != null && ship.isDestroyed()) {
						destroyed.add(ship);
						this.logger.info("Removed enemy "
								+ column.indexOf(ship) + " from column "
								+ this.enemyShips.indexOf(column));
					}
				}
				column.removeAll(destroyed);
			}

			for (List<EnemyShip> column : this.enemyShips)
				for (EnemyShip enemyShip : column) {
					enemyShip.move(movementX, movementY);
					enemyShip.update();
				}
		}
	}

	/**
	 * Cleans empty columns, adjusts the width and height of the formation.
	 * 빈 열들을 정리하고 포메이션의 너비와 높이를 조정합니다.
	 */
	private void cleanUp() {
		Set<Integer> emptyColumns = new HashSet<Integer>();
		int maxColumn = 0;
		int minPositionY = Integer.MAX_VALUE;
		for (List<EnemyShip> column : this.enemyShips) {
			if (!column.isEmpty()) {
				// Height of this column
				int columnSize = column.get(column.size() - 1).positionY
						- this.positionY + this.shipHeight;
				maxColumn = Math.max(maxColumn, columnSize);
				minPositionY = Math.min(minPositionY, column.get(0)
						.getPositionY());
			} else {
				// Empty column, we remove it.
				emptyColumns.add(this.enemyShips.indexOf(column));
			}
		}
		for (int index : emptyColumns) {
			this.enemyShips.remove(index);
			logger.info("Removed column " + index);
		}

		int leftMostPoint = 0;
		int rightMostPoint = 0;

		for (List<EnemyShip> column : this.enemyShips) {
			if (!column.isEmpty()) {
				if (leftMostPoint == 0)
					leftMostPoint = column.get(0).getPositionX();
				rightMostPoint = column.get(0).getPositionX();
			}
		}

		this.width = rightMostPoint - leftMostPoint + this.shipWidth;
		this.height = maxColumn;

		this.positionX = leftMostPoint;
		this.positionY = minPositionY;
	}

	/**
	 * Shoots a bullet downwards.
	 * 총알을 아래로 쏩니다.
	 *
	 * @param bullets
	 *            Bullets set to add the bullet being shot.
	 */
	public final void shoot(final Set<Bullet> bullets) {
		// For now, only ships in the bottom row are able to shoot.
		int index = (int) (Math.random() * this.shooters.size());
		EnemyShip shooter = this.shooters.get(index);

		if (this.shootingCooldown.checkFinished()) {
			this.shootingCooldown.reset();
			bullets.add(BulletPool.getBullet(shooter.getPositionX()
					+ shooter.width / 2, shooter.getPositionY(), BULLET_SPEED));
		}
	}

	/**
	 * Destroys a ship.
	 * 선박을 파괴합니다.
	 *
	 * @param destroyedShip
	 *            Ship to be destroyed.
	 */
	public final void destroy(final EnemyShip destroyedShip) {
		for (List<EnemyShip> column : this.enemyShips)
			for (int i = 0; i < column.size(); i++)
				if (column.get(i).equals(destroyedShip)) {
					column.get(i).destroy();
					this.logger.info("Destroyed ship in ("
							+ this.enemyShips.indexOf(column) + "," + i + ")");
				}

		// Updates the list of ships that can shoot the player.
		if (this.shooters.contains(destroyedShip)) {
			int destroyedShipIndex = this.shooters.indexOf(destroyedShip);
			int destroyedShipColumnIndex = -1;

			for (List<EnemyShip> column : this.enemyShips)
				if (column.contains(destroyedShip)) {
					destroyedShipColumnIndex = this.enemyShips.indexOf(column);
					break;
				}

			EnemyShip nextShooter = getNextShooter(this.enemyShips
					.get(destroyedShipColumnIndex));

			if (nextShooter != null)
				this.shooters.set(destroyedShipIndex, nextShooter);
			else {
				this.shooters.remove(destroyedShipIndex);
				this.logger.info("Shooters list reduced to "
						+ this.shooters.size() + " members.");
			}
		}

		this.shipCount--;
	}

	/**
	 * Gets the ship on a given column that will be in charge of shooting.
	 * 사격을 담당할 지정된 열에 배를 가져옵니다.
	 *
	 * @param column
	 *            Column to search.
	 *            검색할 열입니다.
	 * @return New shooter ship.
	 */
	public final EnemyShip getNextShooter(final List<EnemyShip> column) {
		Iterator<EnemyShip> iterator = column.iterator();
		EnemyShip nextShooter = null;
		while (iterator.hasNext()) {
			EnemyShip checkShip = iterator.next();
			if (checkShip != null && !checkShip.isDestroyed())
				nextShooter = checkShip;
		}

		return nextShooter;
	}

	/**
	 * Returns an iterator over the ships in the formation.
	 * 포메이션의 함선에 대한 iterator를 반환합니다.
	 *
	 * @return Iterator over the enemy ships.
	 */
	@Override
	public final Iterator<EnemyShip> iterator() {
		Set<EnemyShip> enemyShipsList = new HashSet<EnemyShip>();

		for (List<EnemyShip> column : this.enemyShips)
			for (EnemyShip enemyShip : column)
				enemyShipsList.add(enemyShip);

		return enemyShipsList.iterator();
	}

	/**
	 * Checks if there are any ships remaining.
	 * 남은 함선이 있는지 확인합니다.
	 *
	 * @return True when all ships have been destroyed.
	 * 			모든 함선이 파괴되었을 때 참입니다.
	 */
	public final boolean isEmpty() {
		return this.shipCount <= 0;
	}
}
