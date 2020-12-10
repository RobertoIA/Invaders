package screen;

import engine.Cooldown;
import engine.Core;
import engine.Score;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Implements the high scores screen, it shows player records.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class HighScoreScreen extends Screen {

	/** List of past high scores. */
	private List<Score> highScores;
	private int gamemode;
	private int select;
	private int playermode;
	private int difficulty;
	private Cooldown selectionCooldown;
	private final int cooldown = 200;
	/**
	 * Constructor, establishes the properties of the screen.
	 *
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public HighScoreScreen(final int width, final int height, final int fps,final int playermode,final int difficulty) {
		super(width, height, fps);

		this.returnCode = 1;
		this.gamemode = (playermode*4) + difficulty;
		this.playermode = playermode;
		this.difficulty = difficulty;
		this.select = 0;
		this.selectionCooldown = Core.getCooldown(200);
		selectionCooldown.reset();
		try {
			this.highScores = Core.getFileManager().loadHighScores(gamemode);
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
	 */
	protected final void update() {
		super.update();
		draw();

		if ((inputManager.isKeyDown(KeyEvent.VK_SPACE) || inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) && this.selectionCooldown.checkFinished())
			this.isRunning = false;
		// 점수 기록 관리 - R버튼 누를시 reset
		if(inputManager.isKeyDown(KeyEvent.VK_R) && this.resetDelay.checkFinished()){
			File file = new File("out/production/scores");
			this.highScores = null;
			drawManager.drawHighScores(this, highScores);
			file.delete();
		}
		if((inputManager.isKeyDown(KeyEvent.VK_UP) || inputManager.isKeyDown(KeyEvent.VK_DOWN) || inputManager.isKeyDown(KeyEvent.VK_W) || inputManager.isKeyDown(KeyEvent.VK_S)) && this.selectionCooldown.checkFinished()){
			if(select == 0) select = 1;
			else if(select == 1) select = 0;
			this.selectionCooldown.reset();
		}
		if((inputManager.isKeyDown(KeyEvent.VK_RIGHT) || inputManager.isKeyDown(KeyEvent.VK_D))&& this.selectionCooldown.checkFinished()){
			if(select == 0){
				if(playermode == 0) playermode = 1;
				else if(playermode == 1) playermode = 0;
			}
			if(select == 1){
				if(difficulty == 3) difficulty = 0;
				else difficulty++;
			}
			this.gamemode = (playermode*4) + difficulty;
			try {
				this.highScores = Core.getFileManager().loadHighScores(gamemode);
			} catch (NumberFormatException | IOException e) {
				logger.warning("Couldn't load high scores!");
			}
			this.selectionCooldown.reset();
		}
		if((inputManager.isKeyDown(KeyEvent.VK_LEFT) || inputManager.isKeyDown(KeyEvent.VK_A))&& this.selectionCooldown.checkFinished()){
			if(select == 0){
				if(playermode == 0) playermode = 1;
				else if(playermode == 1) playermode = 0;
			}
			if(select == 1){
				if(difficulty == 0) difficulty = 3;
				else difficulty--;
			}
			this.gamemode = (playermode*4) + difficulty;
			try {
				this.highScores = Core.getFileManager().loadHighScores(gamemode);
			} catch (NumberFormatException | IOException e) {
				logger.warning("Couldn't load high scores!");
			}
			this.selectionCooldown.reset();
		}
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawHighScoreMenu(this,select,playermode,difficulty);
		drawManager.drawHighScores(this, this.highScores);

		drawManager.completeDrawing(this);
	}
}