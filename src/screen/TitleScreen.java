package screen;

import java.awt.event.KeyEvent;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager;
import engine.InputManager;

/**
 * Implements the title screen.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
@SuppressWarnings("serial")
public class TitleScreen extends Screen {

	private DrawManager drawManager;
	private Cooldown selectionCooldown;

	public TitleScreen(int width, int height, int fps) {
		super(width, height, fps);

		this.drawManager = Core.getDrawManager();
		this.selectionCooldown = Core.getCooldown(200);
		this.selectionCooldown.reset();
	}

	/**
	 * Starts the action.
	 */
	public int run() {
		super.run();

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected void update() {
		super.update();

		draw();
		if (this.selectionCooldown.checkFinished()) {
			if (InputManager.isKeyDown(KeyEvent.VK_UP)
					|| InputManager.isKeyDown(KeyEvent.VK_W)) {
				previousMenuItem();
				this.selectionCooldown.reset();
			}
			if (InputManager.isKeyDown(KeyEvent.VK_DOWN)
					|| InputManager.isKeyDown(KeyEvent.VK_S)) {
				nextMenuItem();
				this.selectionCooldown.reset();
			}
		}
		if (InputManager.isKeyDown(KeyEvent.VK_SPACE))
			// TODO remporary until high score screen is built.
			if (this.returnCode != 3)
				this.isRunning = false;
	}

	/**
	 * Shifts the focus to the next menu item.
	 */
	private void nextMenuItem() {
		if (this.returnCode == 3)
			this.returnCode = 0;
		else if (this.returnCode == 0)
			this.returnCode = 2;
		else
			this.returnCode++;
	}

	/**
	 * Shifts the focus to the previous menu item.
	 */
	private void previousMenuItem() {
		if (this.returnCode == 0)
			this.returnCode = 3;
		else if (this.returnCode == 2)
			this.returnCode = 0;
		else
			this.returnCode--;
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawTitleScreen(this, this.returnCode);

		drawManager.completeDrawing(this);
	}
}
