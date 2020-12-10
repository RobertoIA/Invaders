package engine;

import screen.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.*;

/**
 * Implements core game logic. // 핵심 게임 논리 구현
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>

 */
public final class Core {

	/** Width of current screen. // 현재 스크린 폭 */
	private static final int WIDTH = 448;
	/** Height of current screen. // 현재 스크린 높이 */
	private static final int HEIGHT = 520;
	/** Max fps of current screen.  // 최대 fps 설정 */
	private static final int FPS = 60;

	/** DIFFICULT */
	private static int DIFFICULTY = 0; // 0: easy  1: normal  2: hard 3: extra hard
	/** PLAYERMODE */
	private static int PLAYERMODE = 0; // 0:single 1:double

	/** Max lives. //최대 생명 개수 */
	private static final int MAX_LIVES = 3; // (1인용) 기본 생명 개수 설정
	/** Levels between extra life. 추가로 주어지는 생명 개수 // */
	private static final int EXTRA_LIFE_FRECUENCY = 3;
	/** Total number of levels. // 총 레벨 7까지 */
	private static final int NUM_LEVELS = 7;

	/** Difficulty settings for level 1.  // 레벨 1에 대한 난이도 설정 */
	private static final GameSettings[] SETTINGS_LEVEL_1 =
			{new GameSettings(5, 4, 60, 3000),
					new GameSettings(5, 4, 60, 2500),
					new GameSettings(5, 4, 60, 1500),
					new GameSettings(5, 4, 80,80)};
	/** Difficulty settings for level 2. // 레벨 2에 대한 난이도 설정 */
	private static final GameSettings[] SETTINGS_LEVEL_2 =
			{new GameSettings(5, 5, 50, 2500),
					new GameSettings(5, 5, 50, 2000),
					new GameSettings(5, 5, 50, 1200),
					new GameSettings(5, 5, 70, 80)};
	/** Difficulty settings for level 3. // 레벨 3에 대한 난이도 설정 */
	private static final GameSettings[] SETTINGS_LEVEL_3 =
			{new GameSettings(6, 5, 40, 1500),
					new GameSettings(6, 5, 40, 1000),
					new GameSettings(6, 5, 40, 900),
					new GameSettings(6, 5, 60, 80)};
	/** Difficulty settings for level 4. // 레벨 4에 대한 난이도 설정 */
	private static final GameSettings[] SETTINGS_LEVEL_4 =
			{new GameSettings(6, 6, 30, 1500),
					new GameSettings(6, 6, 30, 1000),
					new GameSettings(6, 6, 30, 700),
					new GameSettings(6, 6, 50, 80)};
	/** Difficulty settings for level 5. // 레벨 5에 대한 난이도 설정 */
	private static final GameSettings[] SETTINGS_LEVEL_5 =
			{new GameSettings(7, 6, 20, 1000),
					new GameSettings(7, 6, 20, 700),
					new GameSettings(7, 6, 20, 500),
					new GameSettings(7, 6, 40, 80)};
	/** Difficulty settings for level 6. // 레벨 6에 대한 난이도 설정 */
	private static final GameSettings[] SETTINGS_LEVEL_6 =
			{new GameSettings(7, 7, 10, 1000),
					new GameSettings(7, 7, 10, 500),
					new GameSettings(7, 7, 10, 300),
					new GameSettings(7, 7, 30, 80)};
	/** Difficulty settings for level 7. // 레벨 7에 대한 난이도 설정 */
	private static final GameSettings[] SETTINGS_LEVEL_7 =
			{new GameSettings(8, 7, 2, 500),
					new GameSettings(8, 7, 2, 400),
					new GameSettings(8, 7, 2, 300),
					new GameSettings(8, 7, 20, 80)};

	/** Frame to draw the screen on. // 화면을 그려주는 프레임 */
	private static Frame frame;
	/** Screen currently shown. // 현재 화면이 표시됨 */
	private static Screen currentScreen;
	/** Difficulty settings list. // 난이도 설정 리스트 */
	private static List<GameSettings> gameSettings;
	/** Application logger. // 응용 프로그램 로거 -> 로그 띄어주는 프로그램 */
	private static final Logger LOGGER = Logger.getLogger(Core.class
			.getSimpleName());
	/** Logger handler for printing to disk. // 디스크에 작성하기 위한 로거 핸들러 */
	private static Handler fileHandler;
	/** Logger handler for printing to console. // 콘솔로 인쇄하기 위한 로거 핸들러  */
	private static ConsoleHandler consoleHandler;


	/**
	 * Test implementation. // 구현 테스트
	 *
	 * @param args
	 *            Program args, ignored.
	 */
	public static void main(final String[] args) {
		try {
			LOGGER.setUseParentHandlers(false);

			fileHandler = new FileHandler("log");
			fileHandler.setFormatter(new MinimalFormatter());

			consoleHandler = new ConsoleHandler();
			consoleHandler.setFormatter(new MinimalFormatter());

			LOGGER.addHandler(fileHandler);
			LOGGER.addHandler(consoleHandler);
			LOGGER.setLevel(Level.ALL);

		} catch (Exception e) {
			// TODO handle exception
			e.printStackTrace();
		}

		frame = new Frame(WIDTH, HEIGHT);
		DrawManager.getInstance().setFrame(frame);
		int width = frame.getWidth();
		int height = frame.getHeight();

		gameSettings = new ArrayList<GameSettings>();
		gameSettings.add(SETTINGS_LEVEL_1[DIFFICULTY]);
		gameSettings.add(SETTINGS_LEVEL_2[DIFFICULTY]);
		gameSettings.add(SETTINGS_LEVEL_3[DIFFICULTY]);
		gameSettings.add(SETTINGS_LEVEL_4[DIFFICULTY]);
		gameSettings.add(SETTINGS_LEVEL_5[DIFFICULTY]);
		gameSettings.add(SETTINGS_LEVEL_6[DIFFICULTY]);
		gameSettings.add(SETTINGS_LEVEL_7[DIFFICULTY]);

		GameState gameState;

		int returnCode = 1;
		do {

			gameState = new GameState(1, 0, MAX_LIVES, 0, 0);
			if(PLAYERMODE == 1){
				gameState = new GameState(1, 0, MAX_LIVES*2, 0, 0);
			}

			switch (returnCode) {
				case 1:
					// Main menu.
					currentScreen = new TitleScreen(width, height, FPS);
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " title screen at " + FPS + " fps.");
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing title screen.");
					break;
				case 2:
					// Game & score.
					do {
						// One extra live every few levels.
						boolean bonusLife = gameState.getLevel()
								% EXTRA_LIFE_FRECUENCY == 0
								&& gameState.getLivesRemaining() < MAX_LIVES;

						currentScreen = new GameScreen(gameState,
								gameSettings.get(gameState.getLevel() - 1),
								bonusLife, width, height, FPS, PLAYERMODE);
						LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
								+ " game screen at " + FPS + " fps.");
						frame.setScreen(currentScreen);
						LOGGER.info("Closing game screen.");

						gameState = ((GameScreen) currentScreen).getGameState();

						gameState = new GameState(gameState.getLevel() + 1,
								gameState.getScore(),
								gameState.getLivesRemaining(),
								gameState.getBulletsShot(),
								gameState.getShipsDestroyed());

					} while (gameState.getLivesRemaining() > 0
							&& gameState.getLevel() <= NUM_LEVELS);

					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " score screen at " + FPS + " fps, with a score of "
							+ gameState.getScore() + ", "
							+ gameState.getLivesRemaining() + " lives remaining, "
							+ gameState.getBulletsShot() + " bullets shot and "
							+ gameState.getShipsDestroyed() + " ships destroyed.");
					currentScreen = new ScoreScreen(width, height, FPS, gameState);
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing score screen.");
					break;
				case 3:
					// High scores.
					currentScreen = new HighScoreScreen(width, height, FPS);
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " high score screen at " + FPS + " fps.");
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing high score screen.");
					break;
				case 4:
					Map<String, Integer> setDic = new HashMap<String,Integer>();
					setDic.put("DIFFICULTY",DIFFICULTY);
					setDic.put("PLAYERMODE",PLAYERMODE);
					currentScreen = new SettingScreen(width,height,FPS,setDic);
					returnCode = frame.setScreen(currentScreen);
					DIFFICULTY = setDic.get("DIFFICULTY");
					PLAYERMODE = setDic.get("PLAYERMODE");
					gameSettings.clear();
					gameSettings.add(SETTINGS_LEVEL_1[DIFFICULTY]);
					gameSettings.add(SETTINGS_LEVEL_2[DIFFICULTY]);
					gameSettings.add(SETTINGS_LEVEL_3[DIFFICULTY]);
					gameSettings.add(SETTINGS_LEVEL_4[DIFFICULTY]);
					gameSettings.add(SETTINGS_LEVEL_5[DIFFICULTY]);
					gameSettings.add(SETTINGS_LEVEL_6[DIFFICULTY]);
					gameSettings.add(SETTINGS_LEVEL_7[DIFFICULTY]);
				default:
					break;
			}

		} while (returnCode != 0);

		fileHandler.flush();
		fileHandler.close();
		System.exit(0);
	}

	/**
	 * Constructor, not called. // 생성자, 호출되지 않음
	 */
	private Core() {

	}

	/**
	 * Controls access to the logger. // 로거에 대한 접근 제어
	 *
	 * @return Application logger.
	 */
	public static Logger getLogger() {
		return LOGGER;
	}

	/**
	 * Controls access to the drawing manager. // 화면에 띄어주는? 관리자에 대한 접근 제어
	 *
	 * @return Application draw manager.
	 */
	public static DrawManager getDrawManager() {
		return DrawManager.getInstance();
	}

	/**
	 * Controls access to the input manager. // 입력에 관여하는 관리자에 대한 접근 제어
	 *
	 * @return Application input manager.
	 */
	public static InputManager getInputManager() {
		return InputManager.getInstance();
	}

	/**
	 * Controls access to the file manager. // 파일 관리자에 대한 접근 제어
	 *
	 * @return Application file manager.
	 */
	public static FileManager getFileManager() {
		return FileManager.getInstance();
	}

	/**
	 * Controls creation of new cooldowns. // 새로운 재사용 대기열의 생성 제어
	 *
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @return A new cooldown.
	 */
	public static Cooldown getCooldown(final int milliseconds) {
		return new Cooldown(milliseconds);
	}

	/**
	 * Controls creation of new cooldowns with variance. // 분산된 재사용 대기열의 생성 제어
	 *
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @param variance
	 *            Variation in the cooldown duration.
	 * @return A new cooldown with variance.
	 */
	public static Cooldown getVariableCooldown(final int milliseconds,
											   final int variance) {
		return new Cooldown(milliseconds, variance);
	}
}