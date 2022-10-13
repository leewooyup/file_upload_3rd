package com.ll.exam.fileUpload.app.member.service;

import com.ll.exam.fileUpload.app.member.entity.Member;
import com.ll.exam.fileUpload.app.member.repository.MemberRepository;
import com.ll.exam.fileUpload.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    @Value("${custom.genFileDirPath}")
    private String genFileDirPath;

    private final MemberRepository memberRepository;


    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }

    public Member join(String username, String password, String email, MultipartFile profileImg) {
        String profileImgRelPath = saveProfileImg(profileImg);

        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .profileImg(profileImgRelPath)
                .build();
        memberRepository.save(member);

        return member;
    }

    private String saveProfileImg(MultipartFile profileImg) {
        if(profileImg == null || profileImg.isEmpty()) {
            return null;
        }
        String profileImgDirName = getCurrentProfileImgDirName();

        String ext = Util.file.getExt(profileImg.getOriginalFilename());
        String fileName = UUID.randomUUID().toString() + "." + ext;

        String profileImgDirPath = genFileDirPath + "/" + profileImgDirName;
        String profileImgFilePath = profileImgDirPath + "/" + fileName;

        new File(profileImgDirPath).mkdirs();

        try {
            profileImg.transferTo(new File(profileImgFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return profileImgDirName + "/" + fileName;
    }

    public Member join(String username, String password, String email) {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
        memberRepository.save(member);

        return member;
    }
    public Member getMemberById(Long loginedMemberId) {
        return memberRepository.findById(loginedMemberId).orElse(null);
    }

    public long count() {
        return memberRepository.count();
    }

    public void removeProfileImg(Member member) {
        member.removeProfileImgOnStorage();
        member.setProfileImg(null);

        memberRepository.save(member);
    }

    public void setProfileImgByUrl(Member member, String url) {
        String filePath = Util.file.downloading(url, genFileDirPath + "/" + getCurrentProfileImgDirName() + "/" + UUID.randomUUID());
        member.setProfileImg(getCurrentProfileImgDirName() + "/" + new File(filePath).getName());
        memberRepository.save(member);
    }

    private String getCurrentProfileImgDirName() {
        return "member/" + Util.date.getCurrentDateFormatted("yyyy_MM_dd");
    }

    public void modify(Member member, String email, MultipartFile profileImg) {
        removeProfileImg(member);
        String profileImgRelPath = saveProfileImg(profileImg);
        member.setEmail(email);
        member.setProfileImg(profileImgRelPath);
        memberRepository.save(member);
    }
}
