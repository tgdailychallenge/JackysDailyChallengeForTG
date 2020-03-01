package com.tg.jackysdailychallenge;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class JackysdailychallengeApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void test_GivenMainController_WhenGetAppStatus_ThenReturnAppIsRunning() {
		JackysdailychallengeController jackysdailychallengeController = new JackysdailychallengeController();
		String appStatus = jackysdailychallengeController.getAppStatus();
		String expectedStatus = "App is running!";

		assertEquals(appStatus, expectedStatus);
	}

}
