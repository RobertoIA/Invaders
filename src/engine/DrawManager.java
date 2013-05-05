package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import screen.Screen;
import entity.Entity;
import entity.Ship;

public class DrawManager {

	private static DrawManager instance = new DrawManager();
	private static Graphics graphics;
	private static Graphics backBufferGraphics;
	private static BufferedImage backBuffer;

	private static boolean[][] shipImage;
	private static boolean[][] bulletImage;

	/**
	 * Returns shared instance of DrawManager.
	 * 
	 * @return Shared instance of DrawManager.
	 */
	public static DrawManager getInstance() {
		return instance;
	}

	/**
	 * First part of the drawing process. Initialices buffers, draws the
	 * background and prepares the images.
	 * 
	 * @param screen
	 *            Screen to draw in.
	 */
	public static void initDrawing(Screen screen) {
		backBuffer = new BufferedImage(screen.getWidth(), screen.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		graphics = screen.getGraphics();
		backBufferGraphics = backBuffer.getGraphics();

		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics
				.fillRect(0, 0, screen.getWidth(), screen.getHeight());

		// drawBorders(screen);
		// drawGrid(screen);

		initImages();
	}

	public static void completeDrawing(Screen screen) {
		graphics.drawImage(backBuffer, screen.getInsets().left,
				screen.getInsets().top, screen);
	}

	/**
	 * Draws an entity, using the apropiate image.
	 * 
	 * @param entity
	 *            Entity to be drawn.
	 * @param positionX
	 *            Coordinates for the left side of the image.
	 * @param positionY
	 *            Coordinates for the upper side of the image.
	 */
	public static void drawEntity(Entity entity, int positionX, int positionY) {
		boolean[][] image;
		if (entity.getClass() == Ship.class)
			image = shipImage;
		else
			image = bulletImage;

		backBufferGraphics.setColor(Color.GREEN);
		for (int i = 0; i < image.length; i++)
			for (int j = 0; j < image[i].length; j++)
				if (image[i][j])
					backBufferGraphics.drawRect(positionX + i * 2, positionY
							+ j * 2, 1, 1);
	}

	/**
	 * For debugging purpouses, draws the canvas borders.
	 * 
	 * @param screen
	 *            Screen to draw in.
	 */
	@SuppressWarnings("unused")
	private static void drawBorders(Screen screen) {
		backBufferGraphics.setColor(Color.GREEN);
		backBufferGraphics.drawLine(0, 0, screen.getWidth() - 1, 0);
		backBufferGraphics.drawLine(0, 0, 0, screen.getHeight() - 1);
		backBufferGraphics.drawLine(screen.getWidth() - 1, 0,
				screen.getWidth() - 1, screen.getHeight() - 1);
		backBufferGraphics.drawLine(0, screen.getHeight() - 1,
				screen.getWidth() - 1, screen.getHeight() - 1);
	}

	/**
	 * For debugging purpouses, draws a grid over the canvas.
	 * 
	 * @param screen
	 *            Screen to draw in.
	 */
	@SuppressWarnings("unused")
	private static void drawGrid(Screen screen) {
		backBufferGraphics.setColor(Color.DARK_GRAY);
		for (int i = 0; i < screen.getHeight() - 1; i += 2)
			backBufferGraphics.drawLine(0, i, screen.getWidth() - 1, i);
		for (int j = 0; j < screen.getWidth() - 1; j += 2)
			backBufferGraphics.drawLine(j, 0, j, screen.getHeight() - 1);
	}

	/**
	 * Prepares the images.
	 */
	private static void initImages() {
		// TODO Reads images from a file.
		shipImage = new boolean[13][8];
		shipImage[0][4] = shipImage[0][5] = shipImage[0][6] = shipImage[0][7] = true;
		shipImage[1][3] = shipImage[1][4] = shipImage[1][5] = shipImage[1][6] = shipImage[1][7] = true;
		shipImage[2][3] = shipImage[2][4] = shipImage[2][5] = shipImage[2][6] = shipImage[2][7] = true;
		shipImage[3][3] = shipImage[3][4] = shipImage[3][5] = shipImage[3][6] = shipImage[3][7] = true;
		shipImage[4][3] = shipImage[4][4] = shipImage[4][5] = shipImage[4][6] = shipImage[4][7] = true;
		shipImage[5][1] = shipImage[5][2] = shipImage[5][3] = shipImage[5][4] = shipImage[5][5] = shipImage[5][6] = shipImage[5][7] = true;
		shipImage[6][0] = shipImage[6][1] = shipImage[6][2] = shipImage[6][3] = shipImage[6][4] = shipImage[6][5] = shipImage[6][6] = shipImage[6][7] = true;
		shipImage[7][1] = shipImage[7][2] = shipImage[7][3] = shipImage[7][4] = shipImage[7][5] = shipImage[7][6] = shipImage[7][7] = true;
		shipImage[8][3] = shipImage[8][4] = shipImage[8][5] = shipImage[8][6] = shipImage[8][7] = true;
		shipImage[9][3] = shipImage[9][4] = shipImage[9][5] = shipImage[9][6] = shipImage[9][7] = true;
		shipImage[10][3] = shipImage[10][4] = shipImage[10][5] = shipImage[10][6] = shipImage[10][7] = true;
		shipImage[11][3] = shipImage[11][4] = shipImage[11][5] = shipImage[11][6] = shipImage[11][7] = true;
		shipImage[12][4] = shipImage[12][5] = shipImage[12][6] = shipImage[12][7] = true;

		bulletImage = new boolean[3][5];
		bulletImage[0][0] = true;
		bulletImage[1][0] = bulletImage[1][1] = bulletImage[1][2] = bulletImage[1][3] = bulletImage[1][4] = true;
		bulletImage[2][0] = true;
	}
}
