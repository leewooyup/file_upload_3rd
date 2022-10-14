package com.ll.exam.fileUpload.app.article.controller;

import com.ll.exam.fileUpload.app.article.controller.input.ArticleForm;

import com.ll.exam.fileUpload.app.article.entity.Article;
import com.ll.exam.fileUpload.app.article.service.ArticleService;

import com.ll.exam.fileUpload.app.member.service.dto.MemberContext;
import com.ll.exam.fileUpload.app.upload.service.GenFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.util.TypeSafeEnum;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.validation.Valid;
import java.util.Map;


@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {
    private final ArticleService articleService;
    private final GenFileService genFileService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWrite(ArticleForm articleForm) {
        return "article/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    @ResponseBody
    public String write(@AuthenticationPrincipal MemberContext memberContext, @Valid ArticleForm articleForm, BindingResult bindingResult, MultipartRequest multipartRequest) {
        if(bindingResult.hasErrors()) {
            log.debug("error!!!!!!!");
            return "article/write";
        }
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        log.debug("fileMap : " + fileMap);
        Article article = articleService.write(memberContext.getId(), articleForm.getSubject(), articleForm.getContent());

        genFileService.saveFiles(article, fileMap);
        return "작업중";
    }
}
