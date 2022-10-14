package com.ll.exam.fileUpload.app.article.controller;

import com.ll.exam.fileUpload.app.article.controller.input.ArticleForm;

import com.ll.exam.fileUpload.app.article.entity.Article;
import com.ll.exam.fileUpload.app.article.service.ArticleService;

import com.ll.exam.fileUpload.app.base.dto.RsData;
import com.ll.exam.fileUpload.app.member.service.dto.MemberContext;
import com.ll.exam.fileUpload.app.upload.service.GenFileService;
import com.ll.exam.fileUpload.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.util.TypeSafeEnum;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    public String write(@AuthenticationPrincipal MemberContext memberContext, @Valid ArticleForm articleForm, BindingResult bindingResult, MultipartRequest multipartRequest) {
        if(bindingResult.hasErrors()) {
            log.debug("error!!!!!!!");
            return "article/write";
        }
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        log.debug("fileMap : " + fileMap);
        Article article = articleService.write(memberContext.getId(), articleForm.getSubject(), articleForm.getContent());

        RsData<Map<String, Long>> saveFilesRsData = genFileService.saveFiles(article, fileMap);
        log.debug("saveFilesRsData: " + saveFilesRsData);
        String msg = "%d번 게시물이 작성되었습니다.".formatted(article.getId());
        msg = Util.url.encode(msg);
        return "redirect:/article/%d?msg=%s".formatted(article.getId(), msg);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String showDetail(Model model, @PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "article/detail";
    }
}
