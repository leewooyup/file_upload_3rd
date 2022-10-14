package com.ll.exam.fileUpload.app.upload.repository;

import com.ll.exam.fileUpload.app.base.entity.GenFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenFileRepsoitory extends JpaRepository<GenFile, Long> {
}
