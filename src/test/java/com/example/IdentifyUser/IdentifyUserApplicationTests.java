package com.example.IdentifyUser;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IdentifyUserApplicationTests {

	@Test
	void contextLoads() {
		String password = "123456";
		String encodedPassword = org.springframework.security.crypto.bcrypt.BCrypt
				.hashpw(password, org.springframework.security.crypto.bcrypt.BCrypt.gensalt());
		System.out.println("Encoded password: " + encodedPassword);
	}

}
