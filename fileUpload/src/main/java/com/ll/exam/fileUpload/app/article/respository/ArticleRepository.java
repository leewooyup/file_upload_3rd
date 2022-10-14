package com.ll.exam.fileUpload.app.article.respository;

import com.ll.exam.fileUpload.app.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
