package entity;

import java.awt.Color;
import java.util.Set;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;

/**
 * Implements a ship, to be controlled by the player.
 * 플레이어가 제어할 함선을 구현합니다.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class Ship extends Entity {

	/** Time between shots.
	 * 샷 사이의 시간. */
	private static final int SHOOTING_INTERVAL = 750;
	/** Speed of the bullets shot by the ship.
	 * 함선이 발사하는 총알의 속도. */
	private static final int BULLET_SPEED = -6;
	/** Movement of the ship for each unit of time.
	 * 시간 단위별 배의 움직임. */
	private static final int SPEED = 2;

	/** Minimum time between shots.
	 * 샷 사이의 최소 시간. */
	private Cooldown shootingCooldown;
	/** Time spent inactive between hits.
	 * hits 사이에 비활성화되는 시간입니다. */
	private Cooldown destructionCooldown;

	/**
	 * Constructor, establishes the ship's properties.
	 * 생성자, 선박의 속성을 설정합니다.
	 *
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 */
	public Ship(final int positionX, final int positionY) {
		super(positionX, positionY, 13 * 2, 8 * 2, Color.GREEN);

		this.spriteType = SpriteType.Ship;
		this.shootingCooldown = Core.getCooldown(SHOOTING_INTERVAL);
		this.destructionCooldown = Core.getCooldown(1000);
	}

	/**
	 * Moves the ship speed uni ts right, or until the right screen border is
	 * reached.
	 * 선박 속도 단위를 오른쪽으로 이동하거나 오른쪽 화면 경계에 도달할 때까지 변경합니다.
	 */
	public final void moveRight() {
		this.positionX += SPEED;
	}

	/**
	 * Moves the ship speed units left, or until the left screen border is
	 * reached.
	 * 선박 속도 단위를 왼쪽으로 이동하거나 왼쪽 화면 경계에 도달할 때까지 변경합니다.
	 */
	public final void moveLeft() {
		this.positionX -= SPEED;
	}

	/**
	 * Shoots a bullet upwards.
	 * 총알을 위로 쏩니다.
	 *
	 * @param bullets
	 *            List of bullets on screen, to add the new bullet.
	 *            새 총알들을 추가하기 위한 화면의 총알들 List입니다.
	 * @return Checks if the bullet was shot correctly.
	 */
	public final boolean shoot(final Set<Bullet> bullets) {
		if (this.shootingCooldown.checkFinished()) {
			this.shootingCooldown.reset();
			bullets.add(BulletPool.getBullet(positionX + this.width / 2,
					positionY, BULLET_SPEED));
			return true;
		}
		return false;
	}

	/**
	 * Updates status of the ship.
	 * 선박의 상태를 업데이트합니다.
	 */
	public final void update() {
		if (!this.destructionCooldown.checkFinished())
			this.spriteType = SpriteType.ShipDestroyed;
		else
			this.spriteType = SpriteType.Ship;
	}

	/**
	 * Switches the ship to its destroyed state.
	 * 함선을 파괴된 상태로 전환합니다.
	 */
	public final void destroy() {
		this.destructionCooldown.reset();
	}

	/**
	 * Checks if the ship is destroyed.
	 * 함선이 파괴되었는지 확인합니다.
	 *
	 * @return True if the ship is currently destroyed.
	 */
	public final boolean isDestroyed() {
		return !this.destructionCooldown.checkFinished();
	}

	/**
	 * Getter for the ship's speed.
	 * 배의 속도에 대한 Getter.
	 *
	 * @return Speed of the ship.
	 */
	public final int getSpeed() {
		return SPEED;
	}
}
