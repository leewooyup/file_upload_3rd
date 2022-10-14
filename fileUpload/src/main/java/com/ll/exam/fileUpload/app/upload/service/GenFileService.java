package com.ll.exam.fileUpload.app.upload.service;

import com.ll.exam.fileUpload.app.article.entity.Article;
import com.ll.exam.fileUpload.app.base.entity.GenFile;
import com.ll.exam.fileUpload.app.upload.repository.GenFileRepsoitory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenFileService {
    private final GenFileRepsoitory genFileRepsoitory;

    public void saveFiles(Article article, Map<String, MultipartFile> fileMap) {
        String relTypeCode = "article";
        long relId = article.getId();

        for(String inputName : fileMap.keySet() ) {
            MultipartFile multipartFile = fileMap.get(inputName);
            String typeCode = "common"; // 용도
            String type2Code = "inBody";
            String fileExt = "jpg";
            String fileExtTypeCode = "img"; // 종류
            String fileExtType2Code = "jpg";
            int fileNo = 1;
            int fileSize = 1000;
            String fileDir = "article/2022_10_14";
            String originFileName = "??";

            GenFile genFile = GenFile.builder()
                    .relTypeCode(relTypeCode)
                    .relId(relId)
                    .typeCode(typeCode)
                    .type2Code(type2Code)
                    .fileExtTypeCode(fileExtTypeCode)
                    .fileExtType2Code(fileExtType2Code)
                    .fileNo(fileNo)
                    .fileSize(fileSize)
                    .fileDir(fileDir)
                    .fileExt(fileExt)
                    .originFileName(originFileName)
                    .build();
            genFileRepsoitory.save(genFile);
        }


    }
}
