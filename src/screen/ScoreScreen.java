package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import engine.Cooldown;
import engine.Core;
import engine.GameState;
import engine.Score;

/**
 * Implements the score screen.
 * 점수 화면을 구현합니다.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class ScoreScreen extends Screen {

	/** Milliseconds between changes in user selection.
	 * 사용자 선택 변경 사이의 밀리초입니다. */
	private static final int SELECTION_TIME = 200;
	/** Maximum number of high scores.
	 * 최고 점수의 최대 수입니다. */
	private static final int MAX_HIGH_SCORE_NUM = 7;
	/** Code of first mayus character.
	 * 첫 번째 Mayus 문자의 코드입니다. */
	private static final int FIRST_CHAR = 65;
	/** Code of last mayus character.
	 * 마지막 Mayus 문자의 코드입니다. */
	private static final int LAST_CHAR = 90;

	/** Current score.
	 * 현재 점수. */
	private int score;
	/** Player lives left.
	 * 플레이어의 남은 목숨. */
	private int livesRemaining;
	/** Total bullets shot by the player.
	 * 플레이어가 쏜 전체 총알입니다. */
	private int bulletsShot;
	/** Total ships destroyed by the player.
	 * 플레이어가 파괴한 총 선박입니다. */
	private int shipsDestroyed;
	/** List of past high scores.
	 * 과거 최고 점수 List입니다. */
	private List<Score> highScores;
	/** Checks if current score is a new high score.
	 * 현재 점수가 새로운 최고 점수인지 확인합니다. */
	private boolean isNewRecord;
	/** Player name for record input.
	 * 레코드 입력을 위한 플레이어 이름입니다. */
	private char[] name;
	/** Character of players name selected for change.
	 * 변경을 위해 선택된 플레이어 이름의 캐릭터. */
	private int nameCharSelected;
	/** Time between changes in user selection.
	 * 사용자 선택 변경 사이의 시간입니다. */
	private Cooldown selectionCooldown;

	/**
	 * Constructor, establishes the properties of the screen.
	 * 생성자, 화면의 속성을 설정합니다.
	 *
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 * @param gameState
	 *            Current game state.
	 */
	public ScoreScreen(final int width, final int height, final int fps,
					   final GameState gameState) {
		super(width, height, fps);

		this.score = gameState.getScore();
		this.livesRemaining = gameState.getLivesRemaining();
		this.bulletsShot = gameState.getBulletsShot();
		this.shipsDestroyed = gameState.getShipsDestroyed();
		this.isNewRecord = false;
		this.name = "AAA".toCharArray();
		this.nameCharSelected = 0;
		this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
		this.selectionCooldown.reset();

		try {
			this.highScores = Core.getFileManager().loadHighScores();
			if (highScores.size() < MAX_HIGH_SCORE_NUM
					|| highScores.get(highScores.size() - 1).getScore()
					< this.score)
				this.isNewRecord = true;

		} catch (IOException e) {
			logger.warning("Couldn't load high scores!");
		}
	}

	/**
	 * Starts the action.
	 *
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 * 화면의 요소를 업데이트하고 이벤트를 확인합니다.
	 */
	protected final void update() {
		super.update();

		draw();
		if (this.inputDelay.checkFinished()) {
			if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
				// Return to main menu.
				this.returnCode = 1;
				this.isRunning = false;
				if (this.isNewRecord)
					saveScore();
			} else if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
				// Play again.
				this.returnCode = 2;
				this.isRunning = false;
				if (this.isNewRecord)
					saveScore();
			}

			if (this.isNewRecord && this.selectionCooldown.checkFinished()) {
				if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
					this.nameCharSelected = this.nameCharSelected == 2 ? 0
							: this.nameCharSelected + 1;
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
					this.nameCharSelected = this.nameCharSelected == 0 ? 2
							: this.nameCharSelected - 1;
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_UP)) {
					this.name[this.nameCharSelected] =
							(char) (this.name[this.nameCharSelected]
									== LAST_CHAR ? FIRST_CHAR
									: this.name[this.nameCharSelected] + 1);
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_DOWN)) {
					this.name[this.nameCharSelected] =
							(char) (this.name[this.nameCharSelected]
									== FIRST_CHAR ? LAST_CHAR
									: this.name[this.nameCharSelected] - 1);
					this.selectionCooldown.reset();
				}
			}
		}

	}

	/**
	 * Saves the score as a high score.
	 * 점수를 높은 점수로 저장합니다.
	 */
	private void saveScore() {
		highScores.add(new Score(new String(this.name), score));
		Collections.sort(highScores);
		if (highScores.size() > MAX_HIGH_SCORE_NUM)
			highScores.remove(highScores.size() - 1);

		try {
			Core.getFileManager().saveHighScores(highScores);
		} catch (IOException e) {
			logger.warning("Couldn't load high scores!");
		}
	}

	/**
	 * Draws the elements associated with the screen.
	 * 화면과 관련된 요소를 그립니다.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawGameOver(this, this.inputDelay.checkFinished(),
				this.isNewRecord);
		drawManager.drawResults(this, this.score, this.livesRemaining,
				this.shipsDestroyed, (float) this.shipsDestroyed
						/ this.bulletsShot, this.isNewRecord);

		if (this.isNewRecord)
			drawManager.drawNameInput(this, this.name, this.nameCharSelected);

		drawManager.completeDrawing(this);
	}
}
