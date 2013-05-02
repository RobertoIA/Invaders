package entity;

import java.awt.Graphics;

import screen.Screen;

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
		super(screen, positionX, positionY, 5, 10);
		this.positionX -= this.width / 2;
		this.positionY -= this.height / 2;
		this.speed = speed;
	}

	/**
	 * Draws the bullet on the screen in its new position.
	 */
	public void draw(Graphics backBufferGraphics) {
		super.draw(backBufferGraphics);

		update();
		backBufferGraphics.fillRect(this.positionX, this.positionY, this.width,
				this.height);
	}

	/**
	 * Updates the bullet's position.
	 */
	private void update() {
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
}
