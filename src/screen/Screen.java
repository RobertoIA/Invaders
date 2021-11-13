package screen;

import java.awt.Insets;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager;
import engine.InputManager;

/**
 * Implements a generic screen.
 * 일반 화면을 구현합니다.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class Screen {

	/** Milliseconds until the screen accepts user input.
	 * 화면이 사용자 입력을 수락할 때까지의 시간(밀리초)입니다. */
	private static final int INPUT_DELAY = 1000;

	/** Draw Manager instance. */
	protected DrawManager drawManager;
	/** Input Manager instance. */
	protected InputManager inputManager;
	/** Application logger. */
	protected Logger logger;

	/** Screen width. */
	protected int width;
	/** Screen height. */
	protected int height;
	/** Frames per second shown on the screen.
	 * 화면에 표시되는 초당 프레임 수입니다. */
	protected int fps;
	/** Screen insets.
	 * 화면 삽입. */
	protected Insets insets;
	/** Time until the screen accepts user input.
	 * 화면이 사용자 입력을 수락할 때까지의 시간입니다. */
	protected Cooldown inputDelay;

	/** If the screen is running.
	 * 화면이 실행 중인 경우. */
	protected boolean isRunning;
	/** What kind of screen goes next.
	 * 다음은 어떤 종류의 화면으로 갈지. */
	protected int returnCode;

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
	 */
	public Screen(final int width, final int height, final int fps) {
		this.width = width;
		this.height = height;
		this.fps = fps;

		this.drawManager = Core.getDrawManager();
		this.inputManager = Core.getInputManager();
		this.logger = Core.getLogger();
		this.inputDelay = Core.getCooldown(INPUT_DELAY);
		this.inputDelay.reset();
		this.returnCode = 0;
	}

	/**
	 * Initializes basic screen properties.
	 * 기본 화면 속성을 초기화합니다.
	 */
	public void initialize() {

	}

	/**
	 * Activates the screen.
	 * 화면을 활성화합니다.
	 *
	 * @return Next screen code.
	 */
	public int run() {
		this.isRunning = true;

		while (this.isRunning) {
			long time = System.currentTimeMillis();

			update();

			time = (1000 / this.fps) - (System.currentTimeMillis() - time);
			if (time > 0) {
				try {
					TimeUnit.MILLISECONDS.sleep(time);
				} catch (InterruptedException e) {
					return 0;
				}
			}
		}

		return 0;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 * 화면의 요소를 업데이트하고 이벤트를 확인합니다.
	 */
	protected void update() {
	}

	/**
	 * Getter for screen width.
	 * 화면 너비에 대한 Getter.
	 *
	 * @return Screen width.
	 */
	public final int getWidth() {
		return this.width;
	}

	/**
	 * Getter for screen height.
	 * 화면 높이에 대한 Getter.
	 *
	 * @return Screen height.
	 */
	public final int getHeight() {
		return this.height;
	}
}