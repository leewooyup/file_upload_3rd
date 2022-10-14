package com.ll.exam.fileUpload.app.article.entity;

import com.ll.exam.fileUpload.app.base.AppConfig;
import com.ll.exam.fileUpload.app.base.entity.BaseEntity;
import com.ll.exam.fileUpload.app.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.File;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Article extends BaseEntity {
    @ManyToOne
    private Member author;
    @Column(unique = true)
    private String subject;
    private String content;
}

