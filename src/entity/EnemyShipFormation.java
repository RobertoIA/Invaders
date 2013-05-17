package entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import screen.Screen;
import engine.Cooldown;
import engine.DrawManager;
import engine.DrawManager.SpriteType;

/**
 * Groups enemy ships into a formation that moves together.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class EnemyShipFormation implements Iterable<EnemyShip> {

	private DrawManager drawManager = DrawManager.getInstance();
	private Screen screen;
	private List<EnemyShip[]> enemyShips;
	private Cooldown shootingCooldown;
	private int sizeX;
	private int sizeY;
	private int positionX;
	private int positionY;
	private EnemyShip[] shooters;

	private enum Direction {
		RIGHT, LEFT, DOWN
	};

	private Direction currentDirection;
	private int movementInterval;

	/**
	 * Constructor, sets the initial conditions.
	 */
	public EnemyShipFormation(int sizeX, int sizeY) {
		this.enemyShips = new ArrayList<EnemyShip[]>();
		this.currentDirection = Direction.RIGHT;
		this.movementInterval = 0;
		this.shootingCooldown = new Cooldown(2500);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.positionX = 40;
		this.positionY = 40;
		this.shooters = new EnemyShip[this.sizeX];
		SpriteType spriteType;

		for (int i = 0; i < this.sizeY; i++) {

			if (i / (float) this.sizeY < 0.2)
				spriteType = SpriteType.EnemyShipC1;
			else if (i / (float) this.sizeY < 0.6)
				spriteType = SpriteType.EnemyShipB1;
			else
				spriteType = SpriteType.EnemyShipA1;

			EnemyShip[] row = new EnemyShip[this.sizeX];

			for (int j = 0; j < this.sizeX; j++) {
				row[j] = new EnemyShip(screen, positionX * (j + 1), positionY
						* (i + 1), spriteType);
			}
			this.enemyShips.add(row);
		}

		// TODO does this contain the same ships, or copies?
		this.shooters = this.enemyShips.get(this.enemyShips.size() - 1).clone();

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

		for (EnemyShip[] row : this.enemyShips)
			for (EnemyShip enemyShip : row)
				// TODO temporary solution
				if (enemyShip != null)
					drawManager.drawEntity(enemyShip, enemyShip.getPositionX(),
							enemyShip.getPositionY());
	}

	/**
	 * Updates the position of the ships.
	 */
	private void move() {
		int movementX = 0;
		int movementY = 0;
		movementInterval++;
		if (movementInterval >= 60) {
			movementInterval = 0;
			// TODO cleanup
			// TODO BUG : nullPointerException if ship 0,0 is destroyed.
			int shipHeight = this.enemyShips.get(0)[0].getHeight();
			int shipWidth = this.enemyShips.get(0)[0].getHeight();
			boolean isAtBottom = positionY + 40 * (this.sizeY - 1) + shipHeight > screen
					.getHeight() - 80;

			if (currentDirection == Direction.RIGHT
					&& !isAtBottom
					&& positionX + 40 * (this.sizeX - 1) + shipWidth >= screen
							.getWidth() - 40)
				currentDirection = Direction.DOWN;
			else if (currentDirection == Direction.LEFT && !isAtBottom
					&& positionX <= 40)
				currentDirection = Direction.DOWN;
			else if (positionY % 40 == 0 && positionX <= 40)
				currentDirection = Direction.RIGHT;
			else if (positionY % 20 == 0
					&& positionX + 40 * (this.sizeX - 1) + shipWidth >= screen
							.getWidth() - 40)
				currentDirection = Direction.LEFT;

			if (currentDirection == Direction.RIGHT)
				movementX = 8;
			else if (currentDirection == Direction.LEFT)
				movementX = -8;
			else
				movementY = 4;

			positionX += movementX;
			positionY += movementY;

			for (EnemyShip[] row : this.enemyShips)
				for (EnemyShip enemyShip : row) {
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
		int index = (int) (Math.random() * this.shooters.length);
		EnemyShip shooter = this.shooters[index];

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
		EnemyShip shipAbove;
		for (int i = 0; i < this.shooters.length; i++)
			if (this.shooters[i].equals(destroyedShip)) {
				shipAbove = getShipAbove(destroyedShip);
				if (shipAbove != null)
					this.shooters[i] = shipAbove;
				else {
					// No more ships in this column, we trim the shooters array.
					EnemyShip[] newShooters = new EnemyShip[this.shooters.length - 1];
					int index = 0;
					for (int j = 0; j < this.shooters.length; j++)
						if (!this.shooters[j].equals(destroyedShip)) {
							newShooters[index] = this.shooters[j];
							index++;
						}
					this.shooters = newShooters;
				}
			}

		for (EnemyShip[] row : this.enemyShips)
			for (int i = 0; i < row.length; i++)
				// TODO temporary solution
				if (row[i] != null && row[i].equals(destroyedShip))
					row[i] = null;
		cleanRows();
	}

	/**
	 * Finds the next ship up.
	 * 
	 * @param ship
	 *            The ship from where we start the search.
	 * @return A ship directly above the one provided, or null if there is none.
	 */
	private EnemyShip getShipAbove(EnemyShip ship) {
		int row = -1;
		int index = -1;

		for (int i = 0; i < this.enemyShips.size(); i++)
			for (int j = 0; j < this.enemyShips.get(i).length; j++)
				if (this.enemyShips.get(i)[j] != null
						&& this.enemyShips.get(i)[j].equals(ship)) {
					row = i;
					index = j;
				}

		// TODO debug
		if (row == -1 || index == -1)
			System.out.println("ERROR: SHIP NOT FOUND!");

		for (int i = row - 1; i >= 0; i--)
			if (this.enemyShips.get(i)[index] != null)
				return this.enemyShips.get(i)[index];

		return null;
	}

	/**
	 * Deletes empty rows.
	 */
	private void cleanRows() {
		boolean isEmpty;
		List<EnemyShip[]> emptyRows = new ArrayList<EnemyShip[]>();

		for (EnemyShip[] row : this.enemyShips) {
			isEmpty = true;
			for (int i = 0; i < row.length; i++)
				if (row[i] != null)
					isEmpty = false;
			if (isEmpty)
				emptyRows.add(row);
		}

		this.enemyShips.removeAll(emptyRows);
	}

	/**
	 * Returns an iterator over the ships in the formation.
	 * 
	 * @return Iterator over the enemy ships.
	 */
	@Override
	public Iterator<EnemyShip> iterator() {
		Set<EnemyShip> enemyShips = new HashSet<EnemyShip>();

		for (EnemyShip[] row : this.enemyShips)
			for (EnemyShip enemyShip : row)
				enemyShips.add(enemyShip);

		return enemyShips.iterator();
	}
}
