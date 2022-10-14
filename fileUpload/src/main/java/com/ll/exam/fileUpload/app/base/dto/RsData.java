package com.ll.exam.fileUpload.app.base.dto;

import com.ll.exam.fileUpload.app.base.entity.GenFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@AllArgsConstructor
@Getter
@ToString
public class RsData<T> {
    private final String resultCode;
    private final String msg;
    private final T body;

    public boolean isSuccess() {
        return resultCode.startsWith("S-");
    }
    public boolean isFail() {
        return isSuccess() == false;
    }
}
