package entity;

import screen.Screen;
import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;

public class EnemyShip extends Entity {

	private Cooldown animationCooldown;
	private boolean isDestroyed;
	private int pointValue;

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
	public EnemyShip(Screen screen, int positionX, int positionY,
			SpriteType spriteType) {
		super(screen, positionX, positionY, 12 * 2, 8 * 2);

		this.spriteType = spriteType;
		this.animationCooldown = Core.getCooldown(500);
		this.isDestroyed = false;

		switch (this.spriteType) {
		case EnemyShipA1:
		case EnemyShipA2:
			this.pointValue = 10;
			break;
		case EnemyShipB1:
		case EnemyShipB2:
			this.pointValue = 20;
			break;
		case EnemyShipC1:
		case EnemyShipC2:
			this.pointValue = 30;
			break;
		default:
			this.pointValue = 0;
			break;
		}
	}

	/**
	 * Getter for the score bonus if this ship is destroyed.
	 * 
	 * @return Value of the ship.
	 */
	public int getPointValue() {
		return this.pointValue;
	}

	/**
	 * Moves the ship the specified distance.
	 * 
	 * @param distanceX
	 *            Distance to move in the X axis.
	 * @param distanceY
	 *            Distance to move in the Y axis.
	 */
	public void move(int distanceX, int distanceY) {
		this.positionX += distanceX;
		this.positionY += distanceY;
	}

	/**
	 * Updates attributes, mainly used for animation purposes.
	 */
	public void update() {
		if (this.animationCooldown.checkFinished()) {
			this.animationCooldown.reset();

			switch (this.spriteType) {
			case EnemyShipA1:
				this.spriteType = SpriteType.EnemyShipA2;
				break;
			case EnemyShipA2:
				this.spriteType = SpriteType.EnemyShipA1;
				break;
			case EnemyShipB1:
				this.spriteType = SpriteType.EnemyShipB2;
				break;
			case EnemyShipB2:
				this.spriteType = SpriteType.EnemyShipB1;
				break;
			case EnemyShipC1:
				this.spriteType = SpriteType.EnemyShipC2;
				break;
			case EnemyShipC2:
				this.spriteType = SpriteType.EnemyShipC1;
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Destroys the ship, causing an explosion.
	 */
	public void destroy() {
		this.isDestroyed = true;
		this.spriteType = SpriteType.Explosion;
	}

	/**
	 * Checks if the ship has been destroyed.
	 * 
	 * @return True if the ship has been destroyed.
	 */
	public boolean isDestroyed() {
		return this.isDestroyed;
	}
}
