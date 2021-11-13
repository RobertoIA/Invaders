package entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Implements a pool of recyclable bullets.
 * 재활용 가능한 총알 풀을 구현합니다.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public final class BulletPool {

	/** Set of already created bullets.
	 * 이미 생성된 총알들의 집합입니다. */
	private static Set<Bullet> pool = new HashSet<Bullet>();

	/**
	 * Constructor, not called.
	 */
	private BulletPool() {

	}

	/**
	 * Returns a bullet from the pool if one is available, a new one if there
	 * isn't.
	 * 사용 가능한 경우 풀에서 총알을 반환하고 없는 경우 새 총알을 반환합니다.
	 *
	 * @param positionX
	 *            Requested position of the bullet in the X axis.
	 *            X축에서 총알의 요청된 위치입니다.
	 * @param positionY
	 *            Requested position of the bullet in the Y axis.
	 *            Y축에서 총알의 요청된 위치입니다.
	 * @param speed
	 *            Requested speed of the bullet, positive or negative depending
	 *            on direction - positive is down.
	 *            총알의 요청된 속도, 방향에 따라 양수 또는 음수 - 양수는 아래입니다.
	 * @return Requested bullet.
	 */
	public static Bullet getBullet(final int positionX,
								   final int positionY, final int speed) {
		Bullet bullet;
		if (!pool.isEmpty()) {
			bullet = pool.iterator().next();
			pool.remove(bullet);
			bullet.setPositionX(positionX - bullet.getWidth() / 2);
			bullet.setPositionY(positionY);
			bullet.setSpeed(speed);
			bullet.setSprite();
		} else {
			bullet = new Bullet(positionX, positionY, speed);
			bullet.setPositionX(positionX - bullet.getWidth() / 2);
		}
		return bullet;
	}

	/**
	 * Adds one or more bullets to the list of available ones.
	 * 사용 가능한 목록에 하나 이상의 총알들을 추가합니다.
	 *
	 * @param bullet
	 *            Bullets to recycle.
	 */
	public static void recycle(final Set<Bullet> bullet) {
		pool.addAll(bullet);
	}
}
