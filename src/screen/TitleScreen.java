package screen;

import engine.DrawManager;

/**
 * Implements the title screen.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
@SuppressWarnings("serial")
public class TitleScreen extends Screen {

	private DrawManager drawManager;

	public TitleScreen(int width, int height, int fps) {
		super(width, height, fps);

		this.fps = fps;
	}

	/**
	 * Starts the action.
	 */
	public void run() {
		super.run();
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected void update() {
		super.update();
		
		draw();
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.completeDrawing(this);
	}
}
