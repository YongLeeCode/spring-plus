package org.example.expert;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.expert.config.JwtUtil;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserPerformanceTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserRepository userRepository;

	// @BeforeAll
	// void setUp() {
	// 	int batchSize = 1000;
	// 	List<User> users = new ArrayList<>(batchSize);
	// 	for (int i = 0; i < 1000000; i++) {
	// 		String unique = UUID.randomUUID().toString();
	// 		users.add(new User(unique+"@user", "1234", "user" + unique, UserRole.USER));
	// 		if (users.size() == batchSize) {
	// 			userRepository.saveAll(users);
	// 			users.clear();
	// 		}
	// 	}
	// 	users.add(new User("user1@user.com", "1234", "user1", UserRole.USER));
	// 	// 남은 데이터 저장
	// 	if (!users.isEmpty()) {
	// 		userRepository.saveAll(users);
	// 	}
	// }

	@Test
	void test1() throws Exception {
		String nick = "user1";
		String token = jwtUtil.createToken(
			123L,
			"test@example.com",
			nick,
			UserRole.USER
		);
		long start = System.currentTimeMillis();

		try {
			var result = mockMvc
				.perform(get("/users/search")
					.header("Authorization", token)
					.param("nick", nick))
					.andDo(print());

			// System.out.println("응답 상태 코드: " + result.getResponse().getStatus());
			// System.out.println("응답 바디: " + result.getResponse().getContentAsString());

			long end = System.currentTimeMillis();
			System.out.println("조회 시간: " + (end - start));

			User user = userRepository.findByNickname(nick);
			System.out.println(result);
			if (user != null) {
				System.out.printf("%s, %s, %s\n", user.getId(), user.getNickname(), user.getEmail());
			} else {
				System.out.println("User not found in DB.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
