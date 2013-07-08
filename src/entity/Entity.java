package entity;

import java.awt.Color;

import engine.DrawManager.SpriteType;

/**
 * Implements a generic game entity.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Entity {

	/** Position in the x-axis of the upper left corner of the entity. */
	protected int positionX;
	/** Position in the y-axis of the upper left corner of the entity. */
	protected int positionY;
	/** Width of the entity. */
	protected int width;
	/** Height of the entity. */
	protected int height;
	/** Color of the entity. */
	private Color color;
	/** Sprite type assigned to the entity. */
	protected SpriteType spriteType;

	/**
	 * Constructor, establishes the entity's generic properties.
	 * 
	 * @param positionX
	 *            Initial position of the entity in the X axis.
	 * @param positionY
	 *            Initial position of the entity in the Y axis.
	 * @param width
	 *            Width of the entity.
	 * @param height
	 *            Height of the entity.
	 * @param color
	 *            Color of the entity.
	 */
	public Entity(final int positionX, final int positionY, final int width,
			final int height, final Color color) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	/**
	 * Getter for the color of the entity.
	 * 
	 * @return Color of the entity, used when drawing it.
	 */
	public final Color getColor() {
		return color;
	}

	/**
	 * Getter for the X axis position of the entity.
	 * 
	 * @return Position of the entity in the X axis.
	 */
	public final int getPositionX() {
		return this.positionX;
	}

	/**
	 * Getter for the Y axis position of the entity.
	 * 
	 * @return Position of the entity in the Y axis.
	 */
	public final int getPositionY() {
		return this.positionY;
	}

	/**
	 * Setter for the X axis position of the entity.
	 * 
	 * @param positionX
	 *            New position of the entity in the X axis.
	 */
	public final void setPositionX(final int positionX) {
		this.positionX = positionX;
	}

	/**
	 * Setter for the Y axis position of the entity.
	 * 
	 * @param positionY
	 *            New position of the entity in the Y axis.
	 */
	public final void setPositionY(final int positionY) {
		this.positionY = positionY;
	}

	/**
	 * Getter for the sprite that the entity will be drawn as.
	 * 
	 * @return Sprite corresponding to the entity.
	 */
	public final SpriteType getSpriteType() {
		return this.spriteType;
	}

	/**
	 * Getter for the width of the image associated to the entity.
	 * 
	 * @return Width of the entity.
	 */
	public final int getWidth() {
		return this.width;
	}

	/**
	 * Getter for the height of the image associated to the entity.
	 * 
	 * @return Height of the entity.
	 */
	public final int getHeight() {
		return this.height;
	}
}
