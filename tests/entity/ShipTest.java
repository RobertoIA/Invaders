package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    @Test
    void moveRight() {

        Ship ship = new Ship(1,3);
        assertEquals(ship.getPositionX() + ship.getSpeed(), ship);

    }

    @Test
    void moveLeft() {
    }

    @Test
    void shoot() {
    }

    @Test
    void update() {
    }

    @Test
    void destroy() {
    }

    @Test
    void isDestroyed() {
    }

    @Test
    void getSpeed() {
    }
}