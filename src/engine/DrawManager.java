package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import screen.Screen;
import entity.Entity;

public class DrawManager {

	private static DrawManager instance = new DrawManager();
	private static Graphics graphics;
	private static Graphics backBufferGraphics;
	private static BufferedImage backBuffer;

	private static boolean[][] ship, shipDestroyed;
	private static boolean[][] bullet, enemyBullet;
	private static boolean[][] enemyShipTypeA1, enemyShipTypeA2;
//	private static boolean[][] enemyShipTypeB1, enemyShipTypeB2;
//	private static boolean[][] enemyShipTypeC1, enemyShipTypeC2;
//	private static boolean[][] enemyShipSpecial;
	//private static boolean[][] explosion;

	public static enum SpriteType {
		Ship, ShipDestroyed, Bullet, EnemyBullet, EnemyShipA1, EnemyShipA2, EnemyShipB1, EnemyShipB2, EnemyShipC1, EnemyShipC2, EnemyShipSpecial, Explosion
	};
	
	/**
	 * Private constructor;
	 */
	private DrawManager() {
		try {
			load();
		} catch (IOException e) {
			// TODO handle exception
			e.printStackTrace();
		}
	}

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

		switch (entity.getSpriteType()) {
		case Ship:
			image = ship;
			break;
		case EnemyShipA1:
			image = enemyShipTypeA1;
			break;
		case EnemyShipA2:
			image = enemyShipTypeA2;
			break;
//		case EnemyShipB1:
//			image = enemyShipTypeB1;
//			break;
//		case EnemyShipB2:
//			image = enemyShipTypeB2;
//			break;
//		case EnemyShipC1:
//			image = enemyShipTypeC1;
//			break;
//		case EnemyShipC2:
//			image = enemyShipTypeC1;
//			break;
		case Bullet:
			image = bullet;
			break;
		case EnemyBullet:
			image = enemyBullet;
			break;
		default:
			image = enemyShipTypeA2; // can be used to test new designs.
			break;
		}

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
	 * Loads the images from disk.
	 */
	private static void load() throws IOException{
		FileInputStream inputStream = null;
		
		try {
			inputStream = new FileInputStream("res/graphics");
			
			int c;
			
			// TODO graphics for:
			// enemyShipTypeB1, enemyShipTypeB2
			// enemyShipTypeC1, enemyShipTypeC2
			// enemyShipSpecial
			// explosion
			
			ship = new boolean[13][8];
			for(int i = 0; i < ship.length; i++)
				for(int j = 0; j < ship[i].length; j++)
					if(((c = inputStream.read()) != -1) && (char) c == '1')
						ship[i][j] = true;
					else
						ship[i][j] = false;
			inputStream.read(); // line break.
			inputStream.read();
			
			shipDestroyed = new boolean[13][8];
			for(int i = 0; i < shipDestroyed.length; i++)
				for(int j = 0; j < shipDestroyed[i].length; j++)
					if(((c = inputStream.read()) != -1) && (char) c == '1')
						shipDestroyed[i][j] = true;
					else
						shipDestroyed[i][j] = false;
			inputStream.read(); // line break.
			inputStream.read();
			
			bullet = new boolean[3][5];
			for(int i = 0; i < bullet.length; i++)
				for(int j = 0; j < bullet[i].length; j++)
					if(((c = inputStream.read()) != -1) && (char) c == '1')
						bullet[i][j] = true;
					else
						bullet[i][j] = false;
			inputStream.read(); // line break.
			inputStream.read();
			
			enemyBullet = new boolean[3][5];
			for(int i = 0; i < enemyBullet.length; i++)
				for(int j = 0; j < enemyBullet[i].length; j++)
					if(((c = inputStream.read()) != -1) && (char) c == '1')
						enemyBullet[i][j] = true;
					else
						enemyBullet[i][j] = false;
			inputStream.read(); // line break.
			inputStream.read();
			
			enemyShipTypeA1 = new boolean[12][8];
			for(int i = 0; i < enemyShipTypeA1.length; i++)
				for(int j = 0; j < enemyShipTypeA1[i].length; j++)
					if(((c = inputStream.read()) != -1) && (char) c == '1')
						enemyShipTypeA1[i][j] = true;
					else
						enemyShipTypeA1[i][j] = false;
			inputStream.read(); // line break.
			inputStream.read();
			
			enemyShipTypeA2 = new boolean[12][8];
			for(int i = 0; i < enemyShipTypeA2.length; i++)
				for(int j = 0; j < enemyShipTypeA2[i].length; j++)
					if(((c = inputStream.read()) != -1) && (char) c == '1')
						enemyShipTypeA2[i][j] = true;
					else
						enemyShipTypeA2[i][j] = false;
			inputStream.read(); // line break.
			inputStream.read();
			
		} finally {
			if(inputStream != null)
				inputStream.close();
		}
	}
}
