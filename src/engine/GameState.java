package engine;

/**
 * Implements an object that stores the state of the game between levels.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class GameState {

	/** Current game level. */
	private int level;
	/** Current score by p1. */
	private int score_p1;
	/** Lives currently remaining by p1. */
	private int livesRemaining_p1;
	/** Bullets shot until now by p1. */
	private int bulletsShot_p1;
	/** Ships destroyed until now by p1. */
	private int shipsDestroyed_p1;
	/** Current score by p2. */
	private int score_p2;
	/** Lives currently remaining by p2. */
	private int livesRemaining_p2;
	/** Bullets shot until now by p2. */
	private int bulletsShot_p2;
	/** Ships destroyed until now by p2. */
	private int shipsDestroyed_p2;
	private int difficulty;
	
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
	public GameState(final int level, final int score_p1,
			final int livesRemaining_p1, final int bulletsShot_p1,
			final int shipsDestroyed_p1,final int score_p2,
			final int livesRemaining_p2, final int bulletsShot_p2,
			final int shipsDestroyed_p2, final int difficulty) {
		this.level = level;
		this.score_p1 = score_p1;
		this.livesRemaining_p1 = livesRemaining_p1;
		this.bulletsShot_p1 = bulletsShot_p1;
		this.shipsDestroyed_p1 = shipsDestroyed_p1;
		this.score_p2 = score_p2;
		this.livesRemaining_p2 = livesRemaining_p2;
		this.bulletsShot_p2 = bulletsShot_p2;
		this.shipsDestroyed_p2 = shipsDestroyed_p2;
		this.difficulty = difficulty;
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
	public final int getScoreP1() {
		return score_p1;
	}

	/**
	 * @return the livesRemaining
	 */
	public final int getLivesRemainingP1() {
		return livesRemaining_p1;
	}

	/**
	 * @return the bulletsShot
	 */
	public final int getBulletsShotP1() {
		return bulletsShot_p1;
	}

	/**
	 * @return the shipsDestroyed
	 */
	public final int getShipsDestroyedP1() {
		return shipsDestroyed_p1;
	}
	
	/**
	 * @return the score
	 */
	public final int getScoreP2() {
		return score_p2;
	}

	/**
	 * @return the livesRemaining
	 */
	public final int getLivesRemainingP2() {
		return livesRemaining_p2;
	}

	/**
	 * @return the bulletsShot
	 */
	public final int getBulletsShotP2() {
		return bulletsShot_p2;
	}

	/**
	 * @return the shipsDestroyed
	 */
	public final int getShipsDestroyedP2() {
		return shipsDestroyed_p2;
	}
	
	public final int getDifficulty() {
		return difficulty;
	}
}