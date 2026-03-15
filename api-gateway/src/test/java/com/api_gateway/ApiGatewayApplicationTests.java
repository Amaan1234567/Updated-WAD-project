package com.api_gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"spring.r2dbc.url=r2dbc:postgresql://localhost:5432/postgres",
		"spring.r2dbc.username=postgres",
		"spring.r2dbc.password=postgres",
		"app.jwt-secret=testsecrettestsecrettestsecrettestsecret"
})
class ApiGatewayApplicationTests {

	@Test
	void contextLoads() {
	}
}
