package entity;

import engine.DrawManager.SpriteType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class EnemyShipTest {

    EnemyShip testShip;

    @BeforeEach
    void setUp() {
        // positionX, positionY and spriteType are just placeholders
        // they are not necessary for our tests
        testShip = new EnemyShip(0, 0, SpriteType.EnemyShipA1, 5);
    }

    @Test
    void getHealth() {
        assertEquals(5, testShip.getHealth());
    }

    @Test
    void reduceHealthWithoutColorChange() {
        testShip.reduceHealth();
        assertEquals(4, testShip.getHealth());
    }

    @Test
    void reduceHealthWithColorChange() {
        testShip.reduceHealth();
        testShip.reduceHealth();
        assertEquals(3, testShip.getHealth());
        assertEquals(Color.GREEN, testShip.getColor());
    }
}