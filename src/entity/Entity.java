package entity;

import java.awt.Color;
import java.awt.Graphics;

import screen.Screen;

/**
 * Implements a generic game entity.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Entity {

	protected Screen screen;
	protected int positionX;
	protected int positionY;
	protected int width;
	protected int height;

	/**
	 * Constructor, establishes the entity's generic properties.
	 * 
	 * @param screen
	 *            Screen where the entity will be drawn.
	 * @param positionX
	 *            Initial position of the entity in the X axis.
	 * @param positionY
	 *            Initial position of the entity in the Y axis.
	 * @param width
	 *            Width of the entity.
	 * @param height
	 *            Height of the entity.
	 */
	public Entity(Screen screen, int positionX, int positionY, int width,
			int height) {
		this.screen = screen;
		this.positionX = positionX;
		this.positionY = positionY;
		this.width = width;
		this.height = height;
	}

	/**
	 * Draws the entity in a given buffer. All entities are green.
	 * 
	 * @param backBufferGraphics
	 *            Buffer to draw on.
	 */
	public void draw(Graphics backBufferGraphics) {
		backBufferGraphics.setColor(Color.GREEN);
	}

	/**
	 * Getter for the X axis position of the entity.
	 * 
	 * @return Position of the entity in the X axis.
	 */
	public int getPositionX() {
		return this.positionX;
	}

	/**
	 * Getter for the Y axis position of the entity.
	 * 
	 * @return Position of the entity in the Y axis.
	 */
	public int getPositionY() {
		return this.positionY;
	}

	/**
	 * Setter for the X axis position of the entity.
	 * 
	 * @param positionX
	 *            New position of the entity in the X axis.
	 */
	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	/**
	 * Setter for the Y axis position of the entity.
	 * 
	 * @param positionY
	 *            New position of the entity in the Y axis.
	 */
	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
}
