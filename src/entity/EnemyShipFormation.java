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

/**
 * Groups enemy ships into a formation that moves together.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class EnemyShipFormation implements Iterable<EnemyShip> {

	private DrawManager drawManager;
	private Logger logger;
	private Screen screen;
	private List<List<EnemyShip>> enemyShips;
	private Cooldown shootingCooldown;
	private int nShipsWide;
	private int nShipsHigh;
	private int width;
	private int height;
	private int positionX;
	private int positionY;
	private int shipWidth;
	private int shipHeight;
	private List<EnemyShip> shooters;
	private int shipCount;

	private enum Direction {
		RIGHT, LEFT, DOWN
	};

	private Direction currentDirection;
	private int movementInterval;

	/**
	 * Constructor, sets the initial conditions.
	 */
	public EnemyShipFormation(int nShipsWide, int nShipsHigh) {
		this.drawManager = Core.getDrawManager();
		this.logger = Core.getLogger();
		this.enemyShips = new ArrayList<List<EnemyShip>>();
		this.currentDirection = Direction.RIGHT;
		this.movementInterval = 0;
		this.shootingCooldown = Core.getVariableCooldown(2500, 1500);
		this.nShipsWide = nShipsWide;
		this.nShipsHigh = nShipsHigh;
		this.positionX = 40;
		this.positionY = 100;
		this.shooters = new ArrayList<EnemyShip>();
		SpriteType spriteType;

		this.logger.info("Initializing " + nShipsWide + "x" + nShipsHigh
				+ " ship formation in (" + positionX + "," + positionY + ")");

		// Each sub-list is a column on the formation.
		for (int i = 0; i < this.nShipsWide; i++)
			this.enemyShips.add(new ArrayList<EnemyShip>());

		for (List<EnemyShip> column : this.enemyShips) {
			for (int i = 0; i < this.nShipsHigh; i++) {
				if (i / (float) this.nShipsHigh < 0.2)
					spriteType = SpriteType.EnemyShipC1;
				else if (i / (float) this.nShipsHigh < 0.6)
					spriteType = SpriteType.EnemyShipB1;
				else
					spriteType = SpriteType.EnemyShipA1;

				column.add(new EnemyShip(screen, (40 * this.enemyShips
						.indexOf(column)) + positionX, (40 * i) + positionY,
						spriteType));
				this.shipCount++;
			}
		}

		this.shipWidth = this.enemyShips.get(0).get(0).getWidth();
		this.shipHeight = this.enemyShips.get(0).get(0).getHeight();

		this.width = (this.nShipsWide - 1) * 40 + this.shipWidth;
		this.height = (this.nShipsHigh - 1) * 40 + this.shipHeight;

		for (List<EnemyShip> column : this.enemyShips)
			this.shooters.add(column.get(column.size() - 1));

		shootingCooldown.reset();
	}

	/**
	 * Associates the formation to a given screen.
	 * 
	 * @param newScreen
	 *            Screen to attach.
	 */
	public void attach(Screen newScreen) {
		screen = newScreen;
	}

	/**
	 * Draws every individual component of the formation.
	 */
	public void draw() {
		move();

		for (List<EnemyShip> column : this.enemyShips)
			for (EnemyShip enemyShip : column)
				drawManager.drawEntity(enemyShip, enemyShip.getPositionX(),
						enemyShip.getPositionY());
	}

	/**
	 * Updates the position of the ships.
	 */
	private void move() {
		cleanUp();

		int movementX = 0;
		int movementY = 0;
		movementInterval++;
		if (movementInterval >= this.shipCount * 2) {
			movementInterval = 0;

			boolean isAtBottom = positionY + this.height > screen.getHeight() - 80;
			boolean isAtRightSide = positionX + this.width >= screen.getWidth() - 40;
			boolean isAtLeftSide = positionX <= 40;
			boolean isAtLeftToRightAltitude = positionY % 40 == 0;
			boolean isAtRightToLeftAltitude = positionY % 20 == 0;

			if ((currentDirection == Direction.RIGHT && !isAtBottom && isAtRightSide)
					|| (currentDirection == Direction.LEFT && !isAtBottom && isAtLeftSide)) {
				currentDirection = Direction.DOWN;
				this.logger.info("Formation now moving down");
			} else if (isAtLeftToRightAltitude && positionX <= 40) {
				currentDirection = Direction.RIGHT;
				this.logger.info("Formation now moving right");
			} else if (isAtRightToLeftAltitude && isAtRightSide) {
				currentDirection = Direction.LEFT;
				this.logger.info("Formation now moving left");
			}

			if (currentDirection == Direction.RIGHT)
				movementX = 8;
			else if (currentDirection == Direction.LEFT)
				movementX = -8;
			else
				movementY = 4;

			positionX += movementX;
			positionY += movementY;

			// Cleans explosions.
			for (List<EnemyShip> column : this.enemyShips)
				for (int i = 0; i < column.size(); i++)
					if (column.get(i) != null && column.get(i).isDestroyed()) {
						column.remove(i);
						this.logger.info("Removed enemy " + i + " from column "
								+ this.enemyShips.indexOf(column));
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

		this.width = this.enemyShips.get(this.enemyShips.size() - 1).get(0).positionX
				- this.enemyShips.get(0).get(0).positionX + this.shipWidth;
		this.height = maxColumn;

		this.positionX = this.enemyShips.get(0).get(0).positionX;
		this.positionY = minPositionY;
	}

	/**
	 * Shoots a bullet downwards.
	 */
	public void shoot(Set<Bullet> bullets) {
		// For now, only ships in the bottom row are able to shoot.
		int index = (int) (Math.random() * this.shooters.size());
		EnemyShip shooter = this.shooters.get(index);

		if (this.shootingCooldown.checkFinished()) {
			this.shootingCooldown.reset();
			bullets.add(BulletPool.getBullet(this.screen,
					shooter.getPositionX() + shooter.width / 2,
					shooter.getPositionY(), 3));
		}
	}

	/**
	 * Destroys a ship.
	 * 
	 * @param enemyShip
	 *            Ship to be destroyed.
	 */
	public void destroy(EnemyShip destroyedShip) {
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
	 * 
	 * @param column
	 *            Column to search.
	 * @return New shooter ship.
	 */
	public EnemyShip getNextShooter(List<EnemyShip> column) {
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
	 * 
	 * @return Iterator over the enemy ships.
	 */
	@Override
	public Iterator<EnemyShip> iterator() {
		Set<EnemyShip> enemyShips = new HashSet<EnemyShip>();

		for (List<EnemyShip> column : this.enemyShips)
			for (EnemyShip enemyShip : column)
				enemyShips.add(enemyShip);

		return enemyShips.iterator();
	}

	/**
	 * Checks if there are any ships remaining.
	 * 
	 * @return True when all ships have been destroyed.
	 */
	public boolean isEmpty() {
		return this.shipCount <= 0;
	}
}
