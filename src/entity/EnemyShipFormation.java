package entity;

import screen.Screen;
import engine.DrawManager;
import engine.DrawManager.SpriteType;

public class EnemyShipFormation {

	private static EnemyShipFormation instance = new EnemyShipFormation();
	private static Screen screen;
	private static EnemyShip[][] enemyShips = new EnemyShip[7][5];

	private static enum Direction {
		RIGHT, LEFT, DOWN
	};

	private static Direction currentDirection;
	private static int movementInterval;

	public static EnemyShipFormation getInstance() {
		return instance;
	}

	public static void attach(Screen newScreen) {
		screen = newScreen;
	}

	public static void reset() {
		currentDirection = Direction.RIGHT;
		movementInterval = 0;
		SpriteType spriteType;
		for (int i = 0; i < enemyShips.length; i++) {
			spriteType = SpriteType.EnemyShipC1;
			for (int j = 0; j < enemyShips[i].length; j++) {
				if (j == (enemyShips[i].length / 2) - 1)
					spriteType = SpriteType.EnemyShipB1;
				else if (j == enemyShips[i].length - 2)
					spriteType = SpriteType.EnemyShipA1;
				enemyShips[i][j] = new EnemyShip(screen, 40 * (i + 1),
						40 * (j + 1), spriteType);
			}
		}
	}

	public static void draw() {
		move();
		for (int i = 0; i < enemyShips.length; i++)
			for (int j = 0; j < enemyShips[i].length; j++)
				DrawManager.drawEntity(enemyShips[i][j],
						enemyShips[i][j].getPositionX(),
						enemyShips[i][j].getPositionY());
	}

	private static void move() {
		int movementX = 0;
		int movementY = 0;
		movementInterval++;
		if (movementInterval >= 60) {
			movementInterval = 0;
			// TODO Simplify.
			if (currentDirection == Direction.RIGHT
					&& enemyShips[enemyShips.length - 1][0].getPositionX() >= screen
							.getWidth() - 24 - 40) // TODO without constants if
													// possible.
				currentDirection = Direction.DOWN;
			else if (currentDirection == Direction.LEFT
					&& enemyShips[0][0].getPositionX() <= 40)
				currentDirection = Direction.DOWN;
			else if (currentDirection == Direction.DOWN
					&& enemyShips[0][0].getPositionY() % 20 == 0
					&& enemyShips[enemyShips.length - 1][0].getPositionX() >= screen
							.getWidth() - 24 - 40)
				currentDirection = Direction.LEFT;
			else if (currentDirection == Direction.DOWN
					&& enemyShips[0][0].getPositionY() % 40 == 0
					&& enemyShips[0][0].getPositionX() <= 40)
				currentDirection = Direction.RIGHT;

			if (currentDirection == Direction.RIGHT)
				movementX = 8;
			else if (currentDirection == Direction.LEFT)
				movementX = -8;
			else
				movementY = 4;
			
			for (int i = 0; i < enemyShips.length; i++)
				for (int j = 0; j < enemyShips[i].length; j++) {
					enemyShips[i][j].move(movementX,
							movementY);
					enemyShips[i][j].update();
				}
			
			if(enemyShips[0][enemyShips[0].length - 1].getPositionY() > screen.getHeight() - 80)
				reset();
		}
	}
}
