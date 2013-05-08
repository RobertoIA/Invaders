package entity;

import engine.DrawManager.SpriteType;
import screen.Screen;

public class EnemyShip extends Entity {

	/**
	 * Constructor, establishes the ship's properties.
	 * 
	 * @param screen
	 *            Screen where the ship will be drawn.
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 */
	public EnemyShip(Screen screen, int positionX, int positionY) {
		super(screen, positionX, positionY, 12 * 2, 8 * 2);
		
		this.spriteType = SpriteType.EnemyShipB1;
	}
	
	public void move(int distanceX, int distanceY) {
		this.positionX += distanceX;
		this.positionY += distanceY;
	}
}
