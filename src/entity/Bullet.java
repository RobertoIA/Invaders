package entity;

import java.awt.Color;

import engine.DrawManager.SpriteType;

/**
 * Implements a bullet that moves vertically up or down.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class Bullet extends Entity {

	/**
	 * Speed of the bullet, positive or negative depending on direction -
	 * positive is down.
	 * 총알의 속도, 방향에 따라 양수 또는 음수 - 양수는 아래입니다.
	 */
	private int speed;

	/**
	 * Constructor, establishes the bullet's properties.
	 * 생성자, 총알의 속성을 설정합니다.
	 *
	 * @param positionX
	 *            Initial position of the bullet in the X axis.
	 *            X축에서 총알의 초기 위치입니다.
	 * @param positionY
	 *            Initial position of the bullet in the Y axis.
	 *            Y축에서 총알의 초기 위치입니다.
	 * @param speed
	 *            Speed of the bullet, positive or negative depending on
	 *            direction - positive is down.
	 *            총알의 속도, 방향에 따라 양수 또는 음수 - 양수는 아래입니다.
	 */
	public Bullet(final int positionX, final int positionY, final int speed) {
		super(positionX, positionY, 3 * 2, 5 * 2, Color.WHITE);

		this.speed = speed;
		setSprite();
	}

	/**
	 * Sets correct sprite for the bullet, based on speed.
	 * 속도에 따라 총알에 대한 올바른 스프라이트를 설정합니다.
	 */
	public final void setSprite() {
		if (speed < 0)
			this.spriteType = SpriteType.Bullet;
		else
			this.spriteType = SpriteType.EnemyBullet;
	}

	/**
	 * Updates the bullet's position.
	 * 총알의 위치를 업데이트합니다.
	 */
	public final void update() {
		this.positionY += this.speed;
	}

	/**
	 * Setter of the speed of the bullet.
	 * 총알 속도에 대한 Setter.
	 *
	 * @param speed
	 *            New speed of the bullet.
	 */
	public final void setSpeed(final int speed) {
		this.speed = speed;
	}

	/**
	 * Getter for the speed of the bullet.
	 * 총알 속도에 대한 Getter.
	 *
	 * @return Speed of the bullet.
	 */
	public final int getSpeed() {
		return this.speed;
	}
}
