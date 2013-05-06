package entity;

import engine.DrawManager;
import screen.Screen;

public class EnemyShipFormation {

	private static EnemyShipFormation instance = new EnemyShipFormation();
	private static Screen screen;
	private static EnemyShip[][] enemyShips = new EnemyShip[7][5];
	private static int movementInterval = 0;
	private static int currentDirection = -1;

	public static EnemyShipFormation getInstance() {
		return instance;
	}

	public static void attach(Screen newScreen) {
		screen = newScreen;
	}

	public static void reset() {
		for (int i = 0; i < enemyShips.length; i++)
			for (int j = 0; j < enemyShips[i].length; j++)
				enemyShips[i][j] = new EnemyShip(screen, 40 * (i + 1),
						40 * (j + 1));
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
		movementInterval++;
		int movementX = 8;
		int movementY = 0;
		if (movementInterval >= 60) {
			movementInterval = 0;
			if (enemyShips[0][0].getPositionX() <= 40
					|| enemyShips[enemyShips.length - 1][0].getPositionX() >= screen
							.getWidth() - 24 - 40)
				currentDirection *= -1;
			for (int i = 0; i < enemyShips.length; i++)
				for (int j = 0; j < enemyShips[i].length; j++)
					enemyShips[i][j].move(movementX * currentDirection,
							movementY);
		}
	}
}
