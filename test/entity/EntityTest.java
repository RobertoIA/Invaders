package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    Entity testEntity;

    @BeforeEach
    void setUp() {
        // positionX, positionY, width and height are just placeholders
        // they are not necessary for our tests
        testEntity = new Entity(0,0,0,0, Color.WHITE);
    }

    @Test
    void getColor() {
        assertEquals(Color.WHITE, testEntity.getColor());
    }

    @Test
    void setColor() {
        testEntity.setColor(Color.GREEN);
        assertEquals(Color.GREEN, testEntity.getColor());
    }
}