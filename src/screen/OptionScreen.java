package screen;

import engine.Cooldown;
import engine.Core;

import java.awt.event.KeyEvent;

public class OptionScreen extends Screen {

	/** Milliseconds between changes in user selection. */
	private static final int SELECTION_TIME = 200;

	/** Time between changes in user selection. */
	private Cooldown selectionCooldown;

	/**
	 * Constructor, establishes the properties of the screen.
	 *
	 * @param width  Screen width.
	 *
	 * @param height Screen height.
	 *
	 * @param fps Frames per second, frame rate at which the game is run.
	 */
	public OptionScreen(final int width, final int height, final int fps) {
		super(width, height, fps);
		this.reframe = 1;
		this.returnCode = 1;
		this.chk_setting = false;
		this.selResolution = 1;
		this.selSpeed = 1;
		this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
		this.selectionCooldown.reset();
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
	 */
	protected final void update() {
		super.update();

		draw();
		if (this.selectionCooldown.checkFinished()
			&& this.inputDelay.checkFinished()) {
			if (inputManager.isKeyDown(KeyEvent.VK_UP)
				|| inputManager.isKeyDown(KeyEvent.VK_W)) {
				upSolItem();
				this.chk_setting = false;
				this.selectionCooldown.reset();
			}
			if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
				|| inputManager.isKeyDown(KeyEvent.VK_S)) {
				downSpeedItem();
				this.chk_setting = true;
				this.selectionCooldown.reset();
			}
			if (inputManager.isKeyDown(KeyEvent.VK_LEFT)
				|| inputManager.isKeyDown(KeyEvent.VK_A)) {
				if (!this.chk_setting)
					previousSolItem();
				else
					previousSpeedItem();
				this.selectionCooldown.reset();
			}
			if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
				|| inputManager.isKeyDown(KeyEvent.VK_D)) {
				if (!this.chk_setting)
					nextSolItem();
				else
					nextSpeedItem();
				this.selectionCooldown.reset();
			}

			if (inputManager.isKeyDown((KeyEvent.VK_ENTER))
				&& this.inputDelay.checkFinished()) {
				if (!this.chk_setting) {
					this.reframe = this.selResolution;
				} else {
					this.respeed = this.selSpeed;
				}
			}

			if (inputManager.isKeyDown(KeyEvent.VK_SPACE)
				&& this.inputDelay.checkFinished())
				this.isRunning = false;
		}
	}

	/**
	 * Shifts the focus on resolution setting.
	 */
	private void nextSolItem() {
		if (this.selResolution == 3)
			this.selResolution = 1;
		else
			this.selResolution++;
	}

	private void previousSolItem() {
		if (this.selResolution == 1)
			this.selResolution = 3;

		else
			this.selResolution--;
	}

	private void upSolItem() {
		this.selResolution = this.selSpeed;
	}

	/**
	 * Shifts the focus on speed setting.
	 */
	private void nextSpeedItem() {
		if (this.selSpeed == 3)
			this.selSpeed = 1;
		else
			this.selSpeed++;
	}

	private void previousSpeedItem() {
		if (this.selSpeed == 1)
			this.selSpeed = 3;

		else
			this.selSpeed--;
	}

	private void downSpeedItem() {
		this.selSpeed = this.selResolution;
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);
		drawManager.drawOptionMenu(this);
		drawManager.drawSelectSetting(this, this.chk_setting, this.selResolution,
			this.selSpeed);
		drawManager.completeDrawing(this);

	}
	//해상도 구현, 속도 조절//
    /*
    해상도 조절 인수
    width : 가로 길이
    height : 세로 길이
    속도 조절 인수
    GameSettings의 basespeed : 적 움직이는 속도
    총알 속도
    자신의 속도
    DrawManager에서 화면그리기
     */

}
