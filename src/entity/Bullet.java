package entity;

import screen.Screen;
import engine.DrawManager.SpriteType;

/**
 * Implements a bullet that moves vertically up or down.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Bullet extends Entity {

	private int speed;

	/**
	 * Constructor, establishes the bullet's properties.
	 * 
	 * @param screen
	 *            Screen where the bullet will be drawn.
	 * @param positionX
	 *            Initial position of the bullet in the X axis.
	 * @param positionY
	 *            Initial position of the bullet in the Y axis.
	 * @param speed
	 *            Speed of the bullet, positive or negative depending on
	 *            direction - positive is down.
	 */
	public Bullet(Screen screen, int positionX, int positionY, int speed) {
		super(screen, positionX, positionY, 3 * 2, 5 * 2);

		this.speed = speed;
		setSprite();
	}
	
	/**
	 * Sets correct sprite for the bullet, based on speed.
	 */
	public void setSprite() {
		if (speed < 0)
			this.spriteType = SpriteType.Bullet;
		else
			this.spriteType = SpriteType.EnemyBullet;
	}

	/**
	 * Updates the bullet's position.
	 */
	public void update() {
		this.positionY += this.speed;
	}

	/**
	 * Setter of the speed of the bullet.
	 * 
	 * @param speed
	 *            New speed of the bullet.
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * Getter for the Y axis position of the bullet.
	 */
	public int getPositionY() {
		return this.positionY;
	}

	/**
	 * Getter for the speed of the bullet.
	 * 
	 * @return Speed of the bullet.
	 */
	public int getSpeed() {
		return this.speed;
	}
}
