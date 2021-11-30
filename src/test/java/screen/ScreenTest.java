package screen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScreenTest {

    Screen screen;

    @BeforeEach
    void setUp() {
        screen = new Screen(100, 100, 60);
    }

    @Test
    void getWidth() {
        assertEquals(100, screen.getWidth());
    }
}