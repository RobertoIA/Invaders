package engine;

import entity.Pair;

/**
 * Implements an object that stores the state of the game between levels.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class GameState {

	/** Current game level. */
	private int level;
	/** Current score. */
	private Pair score;
	/** Lives currently remaining. */
	private Pair livesRemaining;
	/** Bullets shot until now. */
	private Pair bulletsShot;
	/** Ships destroyed until now. */
	private Pair shipsDestroyed;
	/** The code for the number of players. */
	private int playerCode;


	/**
	 * Constructor.
	 * 
	 * @param level
	 *            Current game level.
	 * @param score
	 *            Current score.
	 * @param livesRemaining
	 *            Lives currently remaining.
	 * @param bulletsShot
	 *            Bullets shot until now.
	 * @param shipsDestroyed
	 *            Ships destroyed until now.
	 */
	public GameState(final int level, final Pair score,
			final Pair livesRemaining, final Pair bulletsShot,
			final Pair shipsDestroyed, final int playerCode) {
		this.level = level;
		this.score = score;
		this.livesRemaining = livesRemaining;
		this.bulletsShot = bulletsShot;
		this.shipsDestroyed = shipsDestroyed;
		this.playerCode = playerCode;
	}

	/**
	 * @return the level
	 */
	public final int getLevel() {
		return level;
	}

	/**
	 * @return the score
	 */
	public final Pair getScore() {
		return score;
	}

	/**
	 * @return the livesRemaining
	 */
	public final Pair getLivesRemaining() {
		return livesRemaining;
	}

	/**
	 * @return the bulletsShot
	 */
	public final Pair getBulletsShot() {
		return bulletsShot;
	}

	/**
	 * @return the shipsDestroyed
	 */
	public final Pair getShipsDestroyed() {
		return shipsDestroyed;
	}

	/**
	 * @return the playerCode
	 */
	public final int getPlayerCode() {
		return playerCode;
	}

}
