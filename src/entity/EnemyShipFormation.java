package entity;

import screen.Screen;
import engine.DrawManager;
import engine.DrawManager.SpriteType;

/**
 * Groups enemy ships into a formation that moves together.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class EnemyShipFormation {

	private static EnemyShipFormation instance;
	private static DrawManager drawManager = DrawManager.getInstance();
	private static Screen screen;
	private static EnemyShip[][] enemyShips = new EnemyShip[7][5];
	private static int positionX;
	private static int positionY;

	private static enum Direction {
		RIGHT, LEFT, DOWN
	};

	private static Direction currentDirection;
	private static int movementInterval;

	/**
	 * Private constructor. Sets the initial conditions.
	 */
	private EnemyShipFormation() {
		reset();
	}

	/**
	 * Returns a shared instance of the formation.
	 * 
	 * @return instance of formation.
	 */
	public static EnemyShipFormation getInstance() {
		if (instance == null)
			instance = new EnemyShipFormation();
		return instance;
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
	 * Resets the formation to its starting point.
	 */
	public static void reset() {
		currentDirection = Direction.RIGHT;
		movementInterval = 0;
		positionX = 40;
		positionY = 40;
		SpriteType spriteType;

		for (int i = 0; i < enemyShips.length; i++) {
			spriteType = SpriteType.EnemyShipC1;

			for (int j = 0; j < enemyShips[i].length; j++) {

				if (j == (enemyShips[i].length / 2) - 1)
					spriteType = SpriteType.EnemyShipB1;
				else if (j == enemyShips[i].length - 2)
					spriteType = SpriteType.EnemyShipA1;

				enemyShips[i][j] = new EnemyShip(screen, positionX * (i + 1),
						positionY * (j + 1), spriteType);
			}
		}
	}

	/**
	 * Draws every individual component of the formation.
	 */
	public void draw() {
		move();

		for (int i = 0; i < enemyShips.length; i++)
			for (int j = 0; j < enemyShips[i].length; j++)
				drawManager.drawEntity(enemyShips[i][j],
						enemyShips[i][j].getPositionX(),
						enemyShips[i][j].getPositionY());
	}

	/**
	 * Updates the position of the ships.
	 */
	private static void move() {
		int movementX = 0;
		int movementY = 0;
		movementInterval++;
		if (movementInterval >= 60) {
			movementInterval = 0;
			// TODO what happens if enemyShips[0][0] is destroyed?
			if (currentDirection == Direction.RIGHT
					&& positionX + 40 * (enemyShips.length - 1)
							+ enemyShips[0][0].getWidht() >= screen.getWidth() - 40)
				currentDirection = Direction.DOWN;
			else if (currentDirection == Direction.LEFT && positionX <= 40)
				currentDirection = Direction.DOWN;
			else if (currentDirection == Direction.DOWN
					&& positionY % 20 == 0
					&& positionX + 40 * (enemyShips.length - 1)
							+ enemyShips[0][0].getWidht() >= screen.getWidth() - 40)
				currentDirection = Direction.LEFT;
			else if (currentDirection == Direction.DOWN && positionY % 40 == 0
					&& positionX <= 40)
				currentDirection = Direction.RIGHT;

			if (currentDirection == Direction.RIGHT)
				movementX = 8;
			else if (currentDirection == Direction.LEFT)
				movementX = -8;
			else
				movementY = 4;

			positionX += movementX;
			positionY += movementY;

			for (int i = 0; i < enemyShips.length; i++)
				for (int j = 0; j < enemyShips[i].length; j++) {
					enemyShips[i][j].move(movementX, movementY);
					enemyShips[i][j].update();
				}

			if (positionY + 40 * (enemyShips[0].length - 1)
					+ enemyShips[0][0].getHeight() > screen.getHeight() - 80)
				reset();
		}
	}
}
