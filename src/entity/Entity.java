package entity;

import java.awt.Color;

import engine.DrawManager.SpriteType;

/**
 * Implements a generic game entity.
 * 일반 게임 엔터티를 구현합니다.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class Entity {

	/** Position in the x-axis of the upper left corner of the entity.
	 * 엔터티 왼쪽 위 모서리의 x축 위치입니다. */
	protected int positionX;
	/** Position in the y-axis of the upper left corner of the entity.
	 * 엔티티의 왼쪽 위 모서리의 y축 위치입니다. */
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
	 * 생성자, 엔터티의 일반 속성을 설정합니다.
	 *
	 * @param positionX
	 *            Initial position of the entity in the X axis.
	 *            X축에서 엔티티의 초기 위치입니다.
	 * @param positionY
	 *            Initial position of the entity in the Y axis.
	 *            Y축에서 엔티티의 초기 위치입니다.
	 * @param width
	 *            Width of the entity.
	 *            엔터티의 너비입니다.
	 * @param height
	 *            Height of the entity.
	 *            엔티티의 높이입니다.
	 * @param color
	 *            Color of the entity.
	 *            엔티티의 색상입니다.
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
	 * 엔티티의 색상에 대한 Getter입니다.
	 *
	 * @return Color of the entity, used when drawing it.
	 */
	public final Color getColor() {
		return color;
	}

	/**
	 * Getter for the X axis position of the entity.
	 * 엔터티의 X축 위치에 대한 Getter입니다.
	 *
	 * @return Position of the entity in the X axis.
	 */
	public final int getPositionX() {
		return this.positionX;
	}

	/**
	 * Getter for the Y axis position of the entity.
	 * 엔터티의 Y축 위치에 대한 Getter입니다.
	 *
	 * @return Position of the entity in the Y axis.
	 */
	public final int getPositionY() {
		return this.positionY;
	}

	/**
	 * Setter for the X axis position of the entity.
	 * 엔터티의 X축 위치에 대한 Setter입니다.
	 *
	 * @param positionX
	 *            New position of the entity in the X axis.
	 */
	public final void setPositionX(final int positionX) {
		this.positionX = positionX;
	}

	/**
	 * Setter for the Y axis position of the entity.
	 * 엔터티의 Y축 위치에 대한 Setter입니다.
	 *
	 * @param positionY
	 *            New position of the entity in the Y axis.
	 */
	public final void setPositionY(final int positionY) {
		this.positionY = positionY;
	}

	/**
	 * Getter for the sprite that the entity will be drawn as.
	 * 엔티티가 그려질 스프라이트의 Getter입니다.
	 *
	 * @return Sprite corresponding to the entity.
	 */
	public final SpriteType getSpriteType() {
		return this.spriteType;
	}

	/**
	 * Getter for the width of the image associated to the entity.
	 * 엔터티와 연결된 이미지의 너비에 대한 Getter입니다.
	 *
	 * @return Width of the entity.
	 */
	public final int getWidth() {
		return this.width;
	}

	/**
	 * Getter for the height of the image associated to the entity.
	 * 엔터티와 연결된 이미지의 높이에 대한 Getter입니다.
	 *
	 * @return Height of the entity.
	 */
	public final int getHeight() {
		return this.height;
	}
}
