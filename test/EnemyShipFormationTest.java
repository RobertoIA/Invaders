import org.junit.jupiter.api.Test;

import engine.Core;
import engine.GameSettings;
import entity.EnemyShipFormation;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnemyShipFormationTest {

	@Test
	void convertminimumspeed() {
		final GameSettings SETTINGS_LEVEL_1 =
			new GameSettings(5, 4, 60, 2000);
		Core.setspeedCode(3);
		final EnemyShipFormation enemyShipFormation = new EnemyShipFormation(SETTINGS_LEVEL_1);

		//when
		final int minimum_speed = enemyShipFormation.convertminimumspeed();

		//then
		assertEquals(5, minimum_speed, "It must be 5");
	}

	@Test
	void convertbulletspeed() {
		//given
		final GameSettings SETTINGS_LEVEL_1 =
			new GameSettings(5, 4, 60, 2000);
		Core.setspeedCode(3);
		final EnemyShipFormation enemyShipFormation = new EnemyShipFormation(SETTINGS_LEVEL_1);

		//when
		final int bullet_speed = enemyShipFormation.convertbulletspeed();

		//then
		assertEquals(6, bullet_speed, "It must be 6");
	}
}