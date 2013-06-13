package entity;

import java.awt.Color;
import java.util.Set;

import screen.Screen;
import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;

/**
 * Implements a ship, to be controlled by the player.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Ship extends Entity {

	private int speed;
	private Cooldown shootingCooldown;
	private Cooldown destructionCooldown;

	/**
	 * Constructor, establishes the ship's properties.
	 * 
	 * @param screen
	 *            Screen where the ship will be drawn.
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 * @param speed
	 *            Absolute speed of the ship, when ordered to move.
	 */
	public Ship(Screen screen, int positionX, int positionY, int speed) {
		super(screen, positionX, positionY, 13 * 2, 8 * 2, Color.GREEN);

		this.spriteType = SpriteType.Ship;
		this.speed = speed;
		this.shootingCooldown = Core.getCooldown(350);
		this.destructionCooldown = Core.getCooldown(1000);
	}

	/**
	 * Moves the ship speed units right, or until the right screen border is
	 * reached.
	 */
	public void moveRight() {
		this.positionX += this.speed;
		if (this.positionX > this.screen.getWidth() - this.width - 1)
			this.positionX = this.screen.getWidth() - this.width - 1;
	}

	/**
	 * Moves the ship speed units left, or until the left screen border is
	 * reached.
	 */
	public void moveLeft() {
		this.positionX -= this.speed;
		if (this.positionX < 1)
			this.positionX = 1;
	}

	/**
	 * Shoots a bullet upwards.
	 * 
	 * @param bullets
	 *            List of bullets on screen, to add the new bullet.
	 * @return Checks if the bullet was shot correctly.
	 */
	public boolean shoot(Set<Bullet> bullets) {
		if (this.shootingCooldown.checkFinished()) {
			this.shootingCooldown.reset();
			bullets.add(BulletPool.getBullet(this.screen, positionX
					+ this.width / 2, positionY, -4));
			return true;
		}
		return false;
	}

	/**
	 * Updates status of the ship.
	 */
	public void update() {
		if (!this.destructionCooldown.checkFinished())
			this.spriteType = SpriteType.ShipDestroyed;
		else
			this.spriteType = SpriteType.Ship;
	}

	/**
	 * Switches the ship to its destroyed state.
	 */
	public void destroy() {
		this.destructionCooldown.reset();
	}

	/**
	 * Checks if the ship is destroyed.
	 * 
	 * @return True if the ship is currently destroyed.
	 */
	public boolean isDestroyed() {
		return !this.destructionCooldown.checkFinished();
	}
}
