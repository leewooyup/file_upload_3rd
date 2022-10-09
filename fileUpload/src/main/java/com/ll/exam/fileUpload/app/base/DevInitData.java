package com.ll.exam.fileUpload.app.base;

import com.ll.exam.fileUpload.app.member.entity.Member;
import com.ll.exam.fileUpload.app.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
public class DevInitData {
    @Bean
    CommandLineRunner init(MemberService memberService, PasswordEncoder passwordEncoder) {
        return args -> {
            String password = passwordEncoder.encode("1234");

            Member member1 = memberService.join("user1", password, "user1@test.com");
            memberService.setProfileImgByUrl(member1, "https://picsum.photos/200/300");

            Member member2 = memberService.join("user2", password, "user2@test.com");
            memberService.setProfileImgByUrl(member2, "https://picsum.photos/200/300");
        };
    }
}
