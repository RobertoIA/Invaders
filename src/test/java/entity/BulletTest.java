package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BulletTest {

    Bullet bullet;

    @BeforeEach
    void setUp() {
        bullet = new Bullet(0,0,3);
    }

    @Test
    void getSpeed() {
        assertEquals(3, bullet.getSpeed());
    }
}