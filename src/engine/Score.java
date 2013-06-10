package engine;

/**
 * Implements a high score record.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Score implements Comparable<Score> {

	private String name;
	private int score;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            Player name, three letters.
	 * @param score
	 *            Player score.
	 */
	public Score(String name, int score) {
		this.name = name;
		this.score = score;
	}

	/**
	 * Getter for the player's name.
	 * 
	 * @return Name of the player-
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Getter for the player's score.
	 * 
	 * @return High score.
	 */
	public int getScore() {
		return this.score;
	}

	/**
	 * Orders the scores descending by score.
	 */
	@Override
	public int compareTo(Score score) {
		return this.score < score.getScore() ? 1 : this.score > score
				.getScore() ? -1 : 0;
	}

}
