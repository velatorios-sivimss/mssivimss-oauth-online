package com.imss.sivimss.oauth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class OauthApplicationTests {

	@Test
	void contextLoads() {
		String result="test";
		OauthApplication.main(new String[]{});
		assertNotNull(result);
	}
	
}
