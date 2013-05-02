package entity;

import java.util.HashSet;
import java.util.Set;

import screen.Screen;

/**
 * Implements a pool of recyclable bullets.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class BulletPool {

	private static Set<Bullet> pool = new HashSet<Bullet>();

	/**
	 * Returns a bullet from the pool if one is available, a new one if there
	 * isn't.
	 * 
	 * @param screen
	 *            Screen where the bullet will be drawn.
	 * @param positionX
	 *            Requested position of the bullet in the X axis.
	 * @param positionY
	 *            Requested position of the bullet in the Y axis.
	 * @param speed
	 *            Requested speed of the bullet, positive or negative depending
	 *            on direction - positive is down.
	 * @return Requested bullet.
	 */
	public static Bullet getBullet(Screen screen, int positionX, int positionY,
			int speed) {
		Bullet bullet;
		if (!pool.isEmpty()) {
			bullet = pool.iterator().next();
			pool.remove(bullet);
			bullet.setPositionX(positionX);
			bullet.setPositionY(positionY);
			bullet.setSpeed(speed);
		} else
			bullet = new Bullet(screen, positionX, positionY, speed);
		return bullet;
	}

	/**
	 * Adds one or more bullets to the list of availables.
	 * 
	 * @param bullet
	 *            Bullets to recycle.
	 */
	public static void recycle(Set<Bullet> bullet) {
		pool.addAll(bullet);
	}
}
