package entity;

import java.awt.Color;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;

/**
 * Implements a enemy ship, to be destroyed by the player.
 * 플레이어가 파괴할 적 함선을 구현합니다.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class EnemyShip extends Entity {

	/** Point value of a type A enemy. */
	private static final int A_TYPE_POINTS = 10;
	/** Point value of a type B enemy. */
	private static final int B_TYPE_POINTS = 20;
	/** Point value of a type C enemy. */
	private static final int C_TYPE_POINTS = 30;
	/** Point value of a bonus enemy. */
	private static final int BONUS_TYPE_POINTS = 100;

	/** Cooldown between sprite changes.
	 * 스프라이트 변경 사이의 쿨다운. */
	private Cooldown animationCooldown;
	/** Checks if the ship has been hit by a bullet.
	 * 함선이 총알에 맞았는지 확인합니다. */
	private boolean isDestroyed;
	/** Values of the ship, in points, when destroyed.
	 * 파괴된 선박의 가치(포인트)입니다. */
	private int pointValue;

	/**
	 * Constructor, establishes the ship's properties.
	 * 생성자, 함선의 속성을 설정합니다.
	 *
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 *            X축에서 함선의 초기 위치입니다.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 *            Y축에서 함선의 초기 위치입니다.
	 * @param spriteType
	 *            Sprite type, image corresponding to the ship.
	 *            스프라이트 타입, 함선에 대응하는 이미지.
	 */
	public EnemyShip(final int positionX, final int positionY,
			final SpriteType spriteType) {
		super(positionX, positionY, 12 * 2, 8 * 2, Color.WHITE);

		this.spriteType = spriteType;
		this.animationCooldown = Core.getCooldown(500);
		this.isDestroyed = false;

		switch (this.spriteType) {
		case EnemyShipA1:
		case EnemyShipA2:
			this.pointValue = A_TYPE_POINTS;
			break;
		case EnemyShipB1:
		case EnemyShipB2:
			this.pointValue = B_TYPE_POINTS;
			break;
		case EnemyShipC1:
		case EnemyShipC2:
			this.pointValue = C_TYPE_POINTS;
			break;
		default:
			this.pointValue = 0;
			break;
		}
	}

	/**
	 * Constructor, establishes the ship's properties for a special ship, with
	 * known starting properties.
	 * 생성자, 알려진 시작 속성을 사용하여 특수 함선에 대한 함선 속성을 설정합니다.
	 */
	public EnemyShip() {
		super(-32, 60, 16 * 2, 7 * 2, Color.RED);

		this.spriteType = SpriteType.EnemyShipSpecial;
		this.isDestroyed = false;
		this.pointValue = BONUS_TYPE_POINTS;
	}

	/**
	 * Getter for the score bonus if this ship is destroyed.
	 * 이 함선이 파괴되면 점수 보너스를 얻을 수 있습니다.
	 *
	 * @return Value of the ship.
	 */
	public final int getPointValue() {
		return this.pointValue;
	}

	/**
	 * Moves the ship the specified distance.
	 * 함선을 지정된 거리만큼 이동합니다.
	 *
	 * @param distanceX
	 *            Distance to move in the X axis.
	 *            X축에서 이동할 거리입니다.
	 * @param distanceY
	 *            Distance to move in the Y axis.
	 *            Y축에서 이동할 거리입니다.
	 */
	public final void move(final int distanceX, final int distanceY) {
		this.positionX += distanceX;
		this.positionY += distanceY;
	}

	/**
	 * Updates attributes, mainly used for animation purposes.
	 * 주로 애니메이션 목적으로 사용되는 속성을 업데이트합니다.
	 */
	public final void update() {
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
	 * 함선을 파괴하여 폭발을 일으킵니다.
	 */
	public final void destroy() {
		this.isDestroyed = true;
		this.spriteType = SpriteType.Explosion;
	}

	/**
	 * Checks if the ship has been destroyed.
	 * 함선이 파괴되었는지 확인합니다.
	 *
	 * @return True if the ship has been destroyed.
	 */
	public final boolean isDestroyed() {
		return this.isDestroyed;
	}
}
