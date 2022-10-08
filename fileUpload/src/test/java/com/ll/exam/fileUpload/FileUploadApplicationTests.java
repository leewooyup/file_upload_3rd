package com.ll.exam.fileUpload;

import com.ll.exam.fileUpload.app.home.controller.HomeController;

import com.ll.exam.fileUpload.app.member.controller.MemberController;
import com.ll.exam.fileUpload.app.member.entity.Member;
import com.ll.exam.fileUpload.app.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;


import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles({"base-addi", "test"})
class FileUploadApplicationTests {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private MemberService memberService;

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("메인화면에 안녕이 나와야한다")
	void t1() throws Exception {
		ResultActions resultActions = mvc
				.perform(get("/"))
				.andDo(print());

		resultActions
				.andExpect(status().is2xxSuccessful())
				.andExpect(handler().handlerType(HomeController.class))
				.andExpect(handler().methodName("main"))
				.andExpect(content().string(containsString("안녕")));
	}

	@Test
	@DisplayName("회원의 수")
	void t2() {
		long count = memberService.count();
		assertThat(count).isGreaterThan(0);
	}

	@Test
	@DisplayName("user1으로 로그인 후 프로필 페이지에 접속하면 user1의 이메일이 보여야한다")
	@Rollback(false)
	void t3() throws Exception {
		ResultActions resultActions = mvc
				.perform(
						get("/member/profile")
						.with(user("user1").password("1234").roles("user"))
				)
				.andDo(print());

		resultActions
				.andExpect(status().is2xxSuccessful())
				.andExpect(handler().handlerType(MemberController.class))
				.andExpect(handler().methodName("showProfile"))
				.andExpect(content().string(containsString("user1@test.com")));
	}

	@Test
	@DisplayName("user4으로 로그인 후 프로필 페이지에 접속하면 user4의 이메일이 보여야한다")
	@Rollback(false)
	void t4() throws Exception {
		ResultActions resultActions = mvc
				.perform(
						get("/member/profile")
							.with(user("user4").password("1234").roles("user"))
				)
				.andDo(print());

		resultActions
				.andExpect(status().is2xxSuccessful())
				.andExpect(handler().handlerType(MemberController.class))
				.andExpect(handler().methodName("showProfile"))
				.andExpect(content().string(containsString("user4@test.com")));
	}

	@Test
	@DisplayName("회원가입")
	void t5() throws Exception {
		String testUploadFileUrl = "https://picsum.photos/200/300";
		String originalFileName = "test.png";

		RestTemplate restTemplate = new RestTemplate(); // Rest방식으로 API를 호출할 수 있는 스프링 내장 클래스
		ResponseEntity<Resource> response = restTemplate.getForEntity(testUploadFileUrl, Resource.class);
		InputStream inputStream = response.getBody().getInputStream(); // 읽기

		MockMultipartFile profileImg = new MockMultipartFile(
				"profileImg",
				originalFileName,
				"image/png",
				inputStream
		);

		ResultActions resultActions = mvc
				.perform(
						multipart("/member/join")
								.file(profileImg)
								.param("username", "user5")
								.param("password", "1234")
								.param("email", "user5@test.com")
								.characterEncoding("UTF-8")
				)
				.andDo(print());

		resultActions
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/member/profile"))
				.andExpect(handler().handlerType(MemberController.class))
				.andExpect(handler().methodName("join"));

		Member member = memberService.getMemberById(5L);
		assertThat(member).isNotNull();

		memberService.removeProfileImg(member);
	}
}
