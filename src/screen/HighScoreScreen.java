package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import engine.Core;
import engine.Score;

/**
 * Implements the high scores screen, it shows player records.
 * 고득점 화면을 구현하여 플레이어의 기록을 보여줍니다.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class HighScoreScreen extends Screen {

	/** List of past high scores.
	 * 과거 최고 점수 List입니다. */
	private List<Score> highScores;

	/**
	 * Constructor, establishes the properties of the screen.
	 * 생성자, 화면의 속성을 설정합니다.
	 *
	 * @param width
	 *            Screen width.
	 *            화면 너비.
	 * @param height
	 *            Screen height.
	 *            화면 높이.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 *            초당 프레임 수, 게임이 실행되는 프레임 속도입니다.
	 */
	public HighScoreScreen(final int width, final int height, final int fps) {
		super(width, height, fps);

		this.returnCode = 1;

		try {
			this.highScores = Core.getFileManager().loadHighScores();
		} catch (NumberFormatException | IOException e) {
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
		if (inputManager.isKeyDown(KeyEvent.VK_SPACE)
				&& this.inputDelay.checkFinished())
			this.isRunning = false;
	}

	/**
	 * Draws the elements associated with the screen.
	 * 화면과 관련된 요소를 그립니다.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawHighScoreMenu(this);
		drawManager.drawHighScores(this, this.highScores);

		drawManager.completeDrawing(this);
	}
}
