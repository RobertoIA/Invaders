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
	private int sizeX;
	private int sizeY;
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
	public EnemyShipFormation(int sizeX, int sizeY) {
		this.drawManager = Core.getDrawManager();
		this.logger = Core.getLogger();
		this.enemyShips = new ArrayList<List<EnemyShip>>();
		this.currentDirection = Direction.RIGHT;
		this.movementInterval = 0;
		this.shootingCooldown = Core.getVariableCooldown(2500, 1500);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.positionX = 40;
		this.positionY = 40;
		this.shooters = new ArrayList<EnemyShip>();
		SpriteType spriteType;

		this.logger.info("Initializing " + sizeX + "x" + sizeY
				+ " ship formation in (" + positionX + "," + positionY + ")");

		// Each sub-list is a column on the formation.
		for (int i = 0; i < this.sizeX; i++)
			this.enemyShips.add(new ArrayList<EnemyShip>());

		for (List<EnemyShip> column : this.enemyShips) {
			for (int i = 0; i < this.sizeY; i++) {
				if (i / (float) this.sizeY < 0.2)
					spriteType = SpriteType.EnemyShipC1;
				else if (i / (float) this.sizeY < 0.6)
					spriteType = SpriteType.EnemyShipB1;
				else
					spriteType = SpriteType.EnemyShipA1;

				column.add(new EnemyShip(screen, positionX
						* (this.enemyShips.indexOf(column) + 1), positionY
						* (i + 1), spriteType));
				this.shipCount++;
			}
		}

		this.shipHeight = this.enemyShips.get(0).get(0).getHeight();
		this.shipWidth = this.enemyShips.get(0).get(0).getWidth();

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
				// TODO temporary solution
				if (enemyShip != null)
					drawManager.drawEntity(enemyShip, enemyShip.getPositionX(),
							enemyShip.getPositionY());
	}

	/**
	 * Updates the position of the ships.
	 */
	private void move() {
		// cleanRows();
		// cleanColumns();

		int movementX = 0;
		int movementY = 0;
		movementInterval++;
		if (movementInterval >= this.shipCount * 2) {
			movementInterval = 0;

			boolean isAtBottom = positionY + 40 * (this.sizeY - 1)
					+ this.shipHeight > screen.getHeight() - 80;
			boolean isAtRightSide = positionX + 40 * (this.sizeX - 1)
					+ this.shipWidth >= screen.getWidth() - 40;
			boolean isAtLeftSide = positionX <= 40;
			boolean isAtLeftToRightAltitude = positionY % 40 == 0;
			boolean isAtRightToLeftAltitude = positionY % 20 == 0;

			if ((currentDirection == Direction.RIGHT && !isAtBottom && isAtRightSide)
					|| (currentDirection == Direction.LEFT && !isAtBottom && isAtLeftSide))
				currentDirection = Direction.DOWN;
			else if (isAtLeftToRightAltitude && positionX <= 40)
				currentDirection = Direction.RIGHT;
			else if (isAtRightToLeftAltitude && isAtRightSide)
				currentDirection = Direction.LEFT;

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
					if (column.get(i) != null && column.get(i).isDestroyed())
						column.set(i, null);

			for (List<EnemyShip> column : this.enemyShips)
				for (EnemyShip enemyShip : column) {
					// TODO temporary solution
					if (enemyShip != null) {
						enemyShip.move(movementX, movementY);
						enemyShip.update();
					}
				}
		}
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
				// TODO temporary solution
				if (column.get(i) != null
						&& column.get(i).equals(destroyedShip)) {
					// row[i] = null;
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
